package io.github.interjacent.app.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserIntervalsResponse {
    private String userName;
    private List<UserInterval> intervals;
}
