package org.comcom.model;

import lombok.*;
import org.comcom.dto.VideoCallRoomDto;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "videocallroom")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoCallRoom implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "videocallroom_gen")
    @SequenceGenerator(name = "videocallroom_gen", sequenceName = "videocallroom_gen_sequence", allocationSize = 1)
    private Long id;

    @JoinColumn(name = "appointment", referencedColumnName = "id")
    @OneToOne
    private Appointment appointment;

    @JoinColumn(name = "organizer", referencedColumnName = "id")
    @OneToOne
    private Users organizer;

    @JoinColumn(name = "company", referencedColumnName = "id")
    @OneToOne
    private Company company;

    @JoinColumn(name = "participant", referencedColumnName = "id")
    @OneToMany(fetch = FetchType.EAGER)
    private List<Users> participant;

    @Column(name = "room_status")
    private Integer room_status;

    @Column(name = "start_time")
    private LocalDateTime start_time;

    @Column(name = "end_time")
    private LocalDateTime end_time;

    public VideoCallRoomDto toDto() {
        return VideoCallRoomDto.builder()
                .id(id)
                .appointment(appointment)
                .organizer(organizer)
                .participants(participant)
                .room_status(room_status)
                .start_time(start_time)
                .end_time(end_time)
                .build();
    }

}
