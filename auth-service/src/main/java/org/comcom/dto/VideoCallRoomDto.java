package org.comcom.dto;

import jdk.jshell.Snippet;
import lombok.*;
import org.comcom.model.Appointment;
import org.comcom.model.Users;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoCallRoomDto {

    private Long id;

    private Appointment appointment;

    private Users organizer;

    private List<Users> participants;

    private Integer room_status;

    private LocalDateTime start_time;

    private LocalDateTime end_time;

}