package org.comcom.dto;

import lombok.*;
import org.comcom.model.Users;
import org.comcom.model.VideoCallRoom;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyCallRespondDto {

    private VideoCallRoom room;

}
