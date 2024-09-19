package org.comcom.dto;

import lombok.*;
import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoCallRoomRequest {

    private Long appointment_id;
    private Long organizer;
    private List<Long> participant_ids;
}
