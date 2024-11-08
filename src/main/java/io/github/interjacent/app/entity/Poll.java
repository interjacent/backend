package io.github.interjacent.app.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "polls")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Poll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID uuid;
    private Long startedAt;
    private Long updatedAt;
    private Boolean open;
    private String adminToken;

    @OneToMany(mappedBy = "poll", fetch = FetchType.LAZY)
    private List<PollInterval> pollIntervals = new ArrayList<>();

    @OneToOne(mappedBy = "poll", fetch = FetchType.LAZY)
    private PollResult result;

    @OneToMany(mappedBy = "poll", fetch = FetchType.LAZY)
    private List<PollUser> users = new ArrayList<>();
}
