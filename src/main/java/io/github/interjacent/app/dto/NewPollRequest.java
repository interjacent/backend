package io.github.interjacent.app.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class NewPollRequest {
    private List<PollDay> days;
}
