package org.comcom.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ntfy_gen")
    @SequenceGenerator(name = "ntfy_gen", sequenceName = "ntfy_gen_sequence", allocationSize = 1)
    private Long id;

    @Column (name = "call_id")
    private Long call_id;

    @Column (name = "receiver_id")
    private Long receiver_id;

    @Column (name = "interpreter_id")
    private Long interpreter_id;

    @Column (name = "room_id")
    private String room_id;

    @Column (name = "status_receiver")
    private String status_receiver;

    @Column (name = "status_interpreter")
    private String status_interpreter;

    @Column (name = "topic_receiver")
    private String topic_receiver;

    @Column (name = "topic_interpreter")
    private String topic_interpreter;

    @Column(name = "created_on")
    private LocalDateTime createdOn;


    public Notification(Long call_id, Long receiver_id, Long interpreter_id, String room_id, String status_receiver, String status_interpreter){
        this.call_id = call_id;
        this.receiver_id = receiver_id;
        this.status_receiver = status_receiver;
        this.status_interpreter = status_interpreter;
        this.interpreter_id = interpreter_id;
        this.room_id = room_id;
    }

    @Override
    public String toString() {
        return "Notifications{" +
                "id=" + id +
                ", call_id='" + call_id + '\'' +
                ", receiver_id='" + receiver_id + '\'' +
                ", interpreter_id='" + interpreter_id + '\'' +
                ", room_id='" + room_id + '\'' +
                ", status_receiver='" + status_receiver + '\'' +
                ", status_interpreter='" + status_interpreter + '\'' +
                ", topic_receiver='" + topic_receiver + '\'' +
                ", topic_interpreter='" + topic_interpreter + '\'' +
                '}';
    }
}
