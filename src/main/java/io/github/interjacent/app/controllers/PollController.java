package io.github.interjacent.app.controllers;

import io.github.interjacent.app.dto.*;
import io.github.interjacent.app.entity.Poll;
import io.github.interjacent.app.entity.PollUser;
import io.github.interjacent.app.entity.PollUserInterval;
import io.github.interjacent.app.math.*;
import io.github.interjacent.app.repositories.PollRepository;
import io.github.interjacent.app.services.PollService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/polls/")
public class PollController {
    private final PollService pollService;

    @PostMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> createPoll(@RequestBody NewPollRequest request) {
        Poll poll = pollService.createPoll(request);

        return ResponseEntity.ok(new NewPollResponse(
                poll.getId(),
                poll.getUuid().toString(),
                poll.getAdminToken()
        ));
    }

    @PostMapping(
        path = "{publicPollId}/join",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> joinToPoll(
        @PathVariable String publicPollId,
        @RequestBody PollInviteRequest request
    ) {
        pollService.joinUser(publicPollId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(true);
    }

    @GetMapping(
        path = "{publicPollId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getPoll(
        @PathVariable String publicPollId
    ) {
        Poll poll = pollService.getPoll(publicPollId);

        PollResponse response = new PollResponse();

        response.setActive(poll.getOpen());
        response.setDays(poll.getPollIntervals().stream().map(pollInterval ->
                new PollDay(pollInterval.getStart(), pollInterval.getEnd())).toList());
        response.setUsers(poll.getUsers().stream().map(pollUser -> {
            UserIntervalsResponse userIntervalsResponse = new UserIntervalsResponse();
            userIntervalsResponse.setUserName(pollUser.getUsername());
            userIntervalsResponse.setIntervals(pollUser.getIntervals().stream().map(pollUserInterval ->
                    new UserInterval(pollUserInterval.getStart(), pollUserInterval.getEnd(), false)).toList());

            return userIntervalsResponse;
        }).toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping(
        path = "{publicPollId}/users/{userId}/intervals",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getUserIntervals(
            @PathVariable String publicPollId,
            @PathVariable String userId
    ) {
        PollUser pollUser = pollService.getUser(publicPollId, userId);
        List<PollUserInterval> pollUserIntervals = pollService.getUserIntervals(publicPollId, userId);
        UserIntervalsResponse response = new UserIntervalsResponse();
        response.setUserName(pollUser.getUsername());
        response.setIntervals(pollUserIntervals.stream().map(pollUserInterval ->
                new UserInterval(
                    pollUserInterval.getStart(),
                    pollUserInterval.getEnd(),
                    true
                )).toList());

        return ResponseEntity.ok(response);
    }

    @PostMapping(
        path = "{publicPollId}/users/{userId}/intervals",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> addUserInterval(
        @PathVariable String publicPollId,
        @PathVariable String userId,
        @RequestBody UserInterval userInterval
    ) {
        pollService.addUserInterval(publicPollId, userId, userInterval);

        return ResponseEntity.ok(true);
    }

    @PostMapping(
        path = "{privatePollId}/finish",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> finish(
        @PathVariable String privatePollId,
        @RequestBody UserIntervalsResponse userIntervalResponse
    ) {
        Poll poll = pollService.getPoll(privatePollId);
        if (poll == null)
            return ResponseEntity.status(404).body(userIntervalResponse);
        if (!poll.getOpen())
            return ResponseEntity.status(400).body(userIntervalResponse);

        pollService.closePoll(poll);

        return ResponseEntity.ok(userIntervalResponse);
    }

    private static IntervalSet<Long> transformSet(UserIntervalsResponse response) {
        IntervalSet<Long> retval = new IntervalSet<>();
        for (UserInterval interval : response.getIntervals()) {
            retval.add(new Interval<Long>(interval.getStart(), interval.getEnd()));
        }
        return retval;
    }
}
