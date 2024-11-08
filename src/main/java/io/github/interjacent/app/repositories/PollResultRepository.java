package io.github.interjacent.app.repositories;

import io.github.interjacent.app.entity.PollResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PollResultRepository extends JpaRepository<PollResult, Long> {
    PollResult findByPoll_Uuid(UUID uuid);
}
