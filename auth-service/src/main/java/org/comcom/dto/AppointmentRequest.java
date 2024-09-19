package org.comcom.dto;

import lombok.*;
import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequest {

    private String address;
    private String city;
    private String country;
    private LocalDateTime endDate;
    private String endTime;
    private Boolean interpreterType;
    private Boolean agreement;
    private String note;
    private String purpose;
    private LocalDateTime startDate;
    private String startTime;
    private String street;
    private String topic;
    private String zip;
    private String user_email;
    private Long interpreter_id;
}
