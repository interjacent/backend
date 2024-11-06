package io.github.interjacent.app.repositories;

import io.github.interjacent.app.entity.PollInterval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PollIntervalRepository extends JpaRepository<PollInterval, Long> {
}
