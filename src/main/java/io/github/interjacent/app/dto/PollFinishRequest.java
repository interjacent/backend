package io.github.interjacent.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PollFinishRequest {
    private String adminToken;
    private long start;
    private long end;
}
