package org.comcom.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.comcom.dto.ApiResponse;
import org.comcom.model.Notification;
import org.comcom.model.Users;
import org.comcom.repository.NotificationRepository;
import org.comcom.repository.UserRepository;
import org.comcom.service.NtfyService;
import org.comcom.service.VideoCallRoomService;
import org.comcom.service.implementation.VideoCallRoomServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import static org.comcom.utils.ApiResponseUtils.buildResponse;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/v1/janus/notifications")
@Slf4j(topic = "NotificationController")
public class NotificationController {

    private final NtfyService ntfyService;

    private final NotificationRepository notificationRepository;

    private final UserRepository userRepository;

    @PostMapping(path = "/sendNtfyNotification", produces = MediaType.APPLICATION_JSON_VALUE)
    public void sendNtfyNotification(@RequestParam("topic") String topic, @RequestParam("message") String message){
        ntfyService.sendMessage(topic, message);
    }

    @PostMapping(path = "/send", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> sendNotification(@RequestParam("call_id") Long call_id, @RequestParam("receiver_id") Long receiver_id, @RequestParam("interpreter_id") Long interpreter_id, @RequestParam("room_id") String room_id, @RequestParam("status_receiver") String status_receiver, @RequestParam("status_interpreter") String status_interpreter) {

        Notification ntfy = new Notification();

        ntfy.setCall_id(call_id);
        ntfy.setReceiver_id(receiver_id);
        ntfy.setStatus_receiver(status_receiver);
        ntfy.setRoom_id(room_id);
        Users callUser = userRepository.findById(call_id).get();

        if ("missed".equals(status_receiver) || "incoming".equals(status_receiver)) {
            String topic = "comcom-call-" + receiver_id;
            String message = "";
            if ("incoming".equals(status_receiver)) {
                message = "Incoming call from " + callUser.getFirstName();
            }
            if ("missed".equals(status_receiver)) {
                message = "Missed call from " + callUser.getFirstName() + " at " + LocalDateTime.now();
            }
            ntfy.setTopic_receiver(topic);
            ntfyService.sendMessage(topic, message);
        }

        if(interpreter_id != null){
            ntfy.setInterpreter_id(interpreter_id);
            ntfy.setStatus_interpreter(status_interpreter);

            if ("missed".equals(status_interpreter) || "incoming".equals(status_interpreter)) {
                String topic = "comcom-call-" + interpreter_id                                                                                                                             ;
                String message = "";
                if ("incoming".equals(status_interpreter)) {
                    message = "Incoming call from " + callUser.getFirstName();
                }

                if ("missed".equals(status_interpreter)) {
                    message = "Missed call from " + callUser.getFirstName() + " at " + LocalDateTime.now();
                }

                ntfy.setTopic_interpreter(topic);
                ntfyService.sendMessage(topic, message);
            }
        }

        ntfy.setCreatedOn(LocalDateTime.now());

        notificationRepository.save(ntfy);


        return buildResponse(ntfy);
    }
}
