package io.github.interjacent.app.repositories;

import io.github.interjacent.app.entity.Poll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PollRepository extends JpaRepository<Poll, Long> {
    Poll findByUuid(UUID uuid);
}
