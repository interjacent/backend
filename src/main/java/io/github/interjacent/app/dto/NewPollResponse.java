package io.github.interjacent.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewPollResponse {
    private long id;
    private String publicId;
    private String privateId;
}
