package io.github.interjacent.app.services;

import io.github.interjacent.app.dto.NewPollRequest;
import io.github.interjacent.app.dto.PollDay;
import io.github.interjacent.app.dto.PollInviteRequest;
import io.github.interjacent.app.dto.UserInterval;
import io.github.interjacent.app.entity.*;
import io.github.interjacent.app.math.*;
import io.github.interjacent.app.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PollService {
    private PollRepository pollRepository;
    private PollIntervalRepository pollIntervalRepository;
    private PollUserRepository pollUserRepository;
    private PollUserIntervalRepository pollUserIntervalRepository;
    private PollResultRepository pollResultRepository;

    public Poll createPoll(NewPollRequest request) {
        String pollId = UUID.randomUUID().toString();
        String adminToken = UUID.randomUUID().toString();

        Poll entity = new Poll();
        entity.setUuid(UUID.fromString(pollId));
        entity.setAdminToken(adminToken);
        entity.setOpen(true);

        long currentTimestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(-3));

        entity.setStartedAt(currentTimestamp);
        entity.setUpdatedAt(currentTimestamp);

        Poll savedEntity = pollRepository.save(entity);

        request.getDays().forEach(pollDay -> {
            PollInterval interval = new PollInterval();
            interval.setPoll(savedEntity);
            interval.setStart(pollDay.getStart());
            interval.setEnd(pollDay.getEnd());

            savedEntity.getPollIntervals().add(pollIntervalRepository.save(interval));
        });

        return savedEntity;
    }

    public void closePollAndSaveResult(Poll poll, PollDay pollResult) {
        PollResult result = new PollResult();
        result.setPoll(poll);
        result.setStart(pollResult.getStart());
        result.setEnd(pollResult.getEnd());

        pollResultRepository.save(result);

        poll.setOpen(false);
        poll.setResult(result);
        pollRepository.save(poll);
    }

    public void joinUser(String pollId, PollInviteRequest request) {
        Poll poll = pollRepository.findByUuid(UUID.fromString(pollId));

        PollUser user = new PollUser();
        user.setPoll(poll);
        user.setUserId(UUID.fromString(request.getUserId()));
        user.setUsername(request.getUserName());

        pollUserRepository.save(user);
    }

    public Poll getPoll(String pollId) {
        return pollRepository.findByUuid(UUID.fromString(pollId));
    }

    public Poll getPollByAdminToken(String adminToken) {
        return pollRepository.findByAdminToken(adminToken);
    }

    public PollUser getUser(String pollId, String userId) {
        return pollUserRepository.findByPoll_UuidAndUserId(
                UUID.fromString(pollId),
                UUID.fromString(userId)
        );
    }

    public List<PollUserInterval> getUserIntervals(String pollId, String userId) {
        return pollUserRepository.findByPoll_UuidAndUserId(
                UUID.fromString(pollId),
                UUID.fromString(userId)
        ).getIntervals();
    }

    public void addUserInterval(String pollId, String userId, UserInterval userInterval) {
        PollUser pollUser = pollUserRepository.findByPoll_UuidAndUserId(
                    UUID.fromString(pollId),
                    UUID.fromString(userId)
                );

        PollUserInterval pollUserInterval = new PollUserInterval();
        pollUserInterval.setUser(pollUser);
        pollUserInterval.setStart(userInterval.getStart());
        pollUserInterval.setEnd(userInterval.getEnd());

        pollUser.getIntervals().add(pollUserInterval);

        pollUserIntervalRepository.save(pollUserInterval);
    }

    public List<PollDay> calculateAvailables(String pollId) {
        Map<UUID, IntervalSet<Long>> intervals = new TreeMap<>();

        pollUserRepository.findByPoll_Uuid(
                UUID.fromString(pollId)
        ).forEach(pollUser -> {
            for (PollUserInterval interval : pollUser.getIntervals()) {
                UUID uuid = interval.getUser().getUserId();
                if (!intervals.containsKey(uuid)) {
                    intervals.put(uuid, new IntervalSet<>());
                }
                intervals.get(uuid).add(new Interval<Long>(interval.getStart(), interval.getEnd()));
            }
        });

        return intervals
                .values()
                .stream()
                .reduce(IntervalSet::intersection)
                .orElse(new IntervalSet<>())
                .getIntervals()
                .stream()
                .map((interval) -> new PollDay(interval.begin(), interval.end()))
                .toList();
    }

    public PollResult getPollResult(String pollId) {
        return pollResultRepository.findByPoll_Uuid(UUID.fromString(pollId));
    }
}
