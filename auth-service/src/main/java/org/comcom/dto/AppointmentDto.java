package org.comcom.dto;

import lombok.*;
import org.comcom.model.Users;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDto {

    private Long id;

    private String address;

    private String city;

    private Boolean confirmation;

    private Boolean agreement;

    private String country;

    private LocalDateTime endDate;

    private String endTime;

    private Boolean interpreterType;

    private String note;

    private String purpose;

    private Long room_id;

    private LocalDateTime startDate;

    private String startTime;

    private String street;

    private String topic;

    private String zip;

    private String user_email;

    private Users interpreter;

}