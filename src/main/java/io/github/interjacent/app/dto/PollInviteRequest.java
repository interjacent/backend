package io.github.interjacent.app.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PollInviteRequest {
    private String userId;
    private String userName;
}
