package org.comcom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AnonymousDto {

    private String token;

    private String RefreshToken;

    private String User;
}
