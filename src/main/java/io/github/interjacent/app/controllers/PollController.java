package io.github.interjacent.app.controllers;

import io.github.interjacent.app.dto.*;
import io.github.interjacent.app.entity.Poll;
import io.github.interjacent.app.entity.PollResult;
import io.github.interjacent.app.entity.PollUser;
import io.github.interjacent.app.entity.PollUserInterval;
import io.github.interjacent.app.services.PollService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/polls")
public class PollController {
    private final PollService pollService;

    @PostMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> createPoll(@RequestBody NewPollRequest request) {
        Poll poll = pollService.createPoll(request);

        return ResponseEntity.ok(new NewPollResponse(
                poll.getUuid().toString(),
                poll.getAdminToken()
        ));
    }

    @PostMapping(
        path = "/{pollId}/join",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> joinToPoll(
        @PathVariable String pollId,
        @RequestBody PollInviteRequest request
    ) {
        pollService.joinUser(pollId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(true);
    }

    @GetMapping(
        path = "/{pollId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getPoll(
        @PathVariable String pollId
    ) {
        Poll poll = pollService.getPoll(pollId);
        if (poll == null) {
            return ResponseEntity.notFound().build();
        }

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
        response.setAvailables(pollService.calculateAvailables(pollId));

        PollResult pollResult = pollService.getPollResult(pollId);

        response.setResult(Optional.ofNullable(pollResult).map(result ->
                    new PollDay(pollResult.getStart(), pollResult.getEnd())
                ).orElse(null));

        return ResponseEntity.ok(response);
    }

    @GetMapping(
        path = "/{pollId}/users/{userId}/intervals",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getUserIntervals(
            @PathVariable String pollId,
            @PathVariable String userId
    ) {
        PollUser pollUser = pollService.getUser(pollId, userId);
        List<PollUserInterval> pollUserIntervals = pollService.getUserIntervals(pollId, userId);
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
        path = "/{pollId}/users/{userId}/intervals",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> addUserInterval(
        @PathVariable String pollId,
        @PathVariable String userId,
        @RequestBody List<UserInterval> userIntervals
    ) {
        pollService.setUserInterval(pollId, userId, userIntervals);

        return ResponseEntity.ok(true);
    }

    @PostMapping(
        path = "/{pollId}/finish",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> finish(
        @PathVariable String pollId,
        @RequestBody PollFinishRequest request
    ) {
        Poll poll = pollService.getPoll(pollId);
        if (poll == null)
            return ResponseEntity.notFound().build();
        else if (!poll.getOpen())
            return ResponseEntity.badRequest().build();
        else if (request.getAdminToken() == null || !poll.getAdminToken().equals(request.getAdminToken()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        pollService.closePollAndSaveResult(poll, request);

        return ResponseEntity.ok(new PollDay(request.getStart(), request.getEnd()));
    }
}
