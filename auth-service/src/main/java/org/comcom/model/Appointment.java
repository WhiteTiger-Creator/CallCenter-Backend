package org.comcom.model;

import lombok.*;
import org.comcom.dto.AppointmentDto;

import javax.persistence.*;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Appointment implements Serializable{

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appoint_gen")
    @SequenceGenerator(name="appoint_gen", sequenceName = "appoint_sequence", initialValue = 1, allocationSize = 1)
    private Long id;


    @Column (name = "address")
    private String address;

    @Column (name = "city")
    private String city;

    @Column (name = "confirmation")
    private Boolean confirmation;

    @Column (name = "agreement")
    private Boolean agreement;

    @Column (name = "country")
    private String country;

    @NotNull
    @Column (name = "endDate")
    private LocalDateTime endDate;

    @NotNull
    @Column (name = "endTime")
    private String endTime;

    @Column (name = "interpreterType")
    private Boolean interpreterType;

    @Column (name = "note")
    private String note;

    @Column (name = "purpose")
    private String purpose;

    @NotNull
    @Column (name = "startDate")
    private LocalDateTime startDate;

    @NotNull
    @Column (name = "startTime")
    private String startTime;

    @Column (name = "street")
    private String street;

    @Column (name = "room_id")
    private Long room_id;

    @Column (name = "topic")
    private String topic;

    @Column (name = "zip")
    private String zip;

    @NotNull
    @Column (name = "user_email")
    private String user_email;

    @JoinColumn(name = "users", referencedColumnName = "id")
    @OneToOne
    private Users interpreter;

    public Appointment(String address, String city, Boolean confirmation, Boolean agreement,  String country, LocalDateTime endDate, String endTime, Boolean interpretertype, String note, String purpose, LocalDateTime startDate, String startTime, String street, String topic, String zip, String user_email, Users interpreter){
        this.address = address;
        this.city = city;
        this.confirmation = confirmation;
        this.agreement = agreement;
        this.country = country;
        this.endDate = endDate;
        this.endTime = endTime;
        this.interpreterType = interpretertype;
        this.note = note;
        this.purpose = purpose;
        this.startDate = startDate;
        this.startTime = startTime;
        this.street = street;
        this.topic = topic;
        this.zip = zip;
        this.user_email = user_email;
        this.interpreter = interpreter;
    }
    public AppointmentDto toDto(){
        return AppointmentDto.builder()
                .id(id)
                .address(address)
                .city(city)
                .confirmation(confirmation)
                .agreement(agreement)
                .country(country)
                .endDate(endDate)
                .endTime(endTime)
                .interpreterType(interpreterType)
                .note(note)
                .purpose(purpose)
                .room_id(room_id)
                .startDate(startDate)
                .startTime(startTime)
                .street(street)
                .topic(topic)
                .zip(zip)
                .user_email(user_email)
                .interpreter(interpreter)
                .build();
    }

}