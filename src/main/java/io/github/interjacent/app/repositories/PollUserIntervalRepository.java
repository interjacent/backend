package io.github.interjacent.app.repositories;

import io.github.interjacent.app.entity.PollUserInterval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PollUserIntervalRepository extends JpaRepository<PollUserInterval, Long> {

}
