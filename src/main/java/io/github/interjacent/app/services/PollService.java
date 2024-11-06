package io.github.interjacent.app.services;

import io.github.interjacent.app.dto.NewPollRequest;
import io.github.interjacent.app.dto.PollDay;
import io.github.interjacent.app.dto.PollInviteRequest;
import io.github.interjacent.app.dto.UserInterval;
import io.github.interjacent.app.entity.Poll;
import io.github.interjacent.app.entity.PollInterval;
import io.github.interjacent.app.entity.PollUser;
import io.github.interjacent.app.entity.PollUserInterval;
import io.github.interjacent.app.math.*;
import io.github.interjacent.app.repositories.PollIntervalRepository;
import io.github.interjacent.app.repositories.PollRepository;
import io.github.interjacent.app.repositories.PollUserIntervalRepository;
import io.github.interjacent.app.repositories.PollUserRepository;
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

    public Poll createPoll(NewPollRequest request) {
        String publicId = UUID.randomUUID().toString();
        String privateId = UUID.randomUUID().toString();

        Poll entity = new Poll();
        entity.setUuid(UUID.fromString(publicId));
        entity.setAdminToken(privateId);
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

    public void closePoll(Poll poll) {
        poll.setOpen(false);
        pollRepository.save(poll);
    }

    public void joinUser(String publicPollId, PollInviteRequest request) {
        Poll poll = pollRepository.findByUuid(UUID.fromString(publicPollId));

        PollUser user = new PollUser();
        user.setPoll(poll);
        user.setUserId(UUID.fromString(request.getUserId()));
        user.setUsername(request.getUserName());

        pollUserRepository.save(user);
    }

    public Poll getPoll(String publicPollId) {
        return pollRepository.findByUuid(UUID.fromString(publicPollId));
    }

    public PollUser getUser(String publicPollId, String userId) {
        return pollUserRepository.findByPoll_UuidAndUserId(
                UUID.fromString(publicPollId),
                UUID.fromString(userId)
        );
    }

    public List<PollUserInterval> getUserIntervals(String publicPollId, String userId) {
        return pollUserRepository.findByPoll_UuidAndUserId(
                UUID.fromString(publicPollId),
                UUID.fromString(userId)
        ).getIntervals();
    }

    public void addUserInterval(String publicPollId, String userId, UserInterval userInterval) {
        PollUser pollUser = pollUserRepository.findByPoll_UuidAndUserId(
                    UUID.fromString(publicPollId),
                    UUID.fromString(userId)
                );

        PollUserInterval pollUserInterval = new PollUserInterval();
        pollUserInterval.setUser(pollUser);
        pollUserInterval.setStart(userInterval.getStart());
        pollUserInterval.setEnd(userInterval.getEnd());

        pollUser.getIntervals().add(pollUserInterval);

        pollUserIntervalRepository.save(pollUserInterval);
    }
    
    public List<PollDay> available(String publicPollId) {
        List<PollUserInterval> pollIntervals = pollUserRepository.findByPoll_Uuid(
                UUID.fromString(publicPollId)
        ).getIntervals();

        Map<UUID, IntervalSet<Long>> intervals = new TreeMap<>();

        for (PollUserInterval interval : pollIntervals) {
            UUID uuid = interval.getUser().getUserId();
            if (!intervals.containsKey(uuid)) {
                intervals.put(uuid, new IntervalSet<>());
            }
            intervals.get(uuid).add(new Interval<Long>(interval.getStart(), interval.getEnd()));
        }

        return intervals
                .values()
                .stream()
                .reduce((a, b) -> a.intersection(b))
                .orElse(new IntervalSet<>())
                .getIntervals()
                .stream()
                .map((interval) -> new PollDay(interval.begin(), interval.end()))
                .toList();
    }
}
