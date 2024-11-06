package io.github.interjacent.app.repositories;

import io.github.interjacent.app.entity.PollUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PollUserRepository extends JpaRepository<PollUser, Long> {
    PollUser findByPoll_UuidAndUserId(UUID uuid, UUID userId);
}
