package io.github.interjacent.app.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PollResponse {
    private boolean active;
    private List<PollDay> days;
    private List<UserIntervalsResponse> users;
    private List<PollDay> availables;
}
