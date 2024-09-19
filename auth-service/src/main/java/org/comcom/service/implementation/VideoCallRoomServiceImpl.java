package org.comcom.service.implementation;

import lombok.AllArgsConstructor;
import org.comcom.config.security.SecurityUtils;
import org.comcom.dto.VideoCallRoomDto;
import org.comcom.dto.VideoCallRoomRequest;
import org.comcom.exception.IncorrectCredentialsException;
import org.comcom.exception.NotFoundException;
import org.comcom.model.Appointment;
import org.comcom.model.Users;
import org.comcom.model.VideoCallRoom;
import org.comcom.repository.AppointmentRepository;
import org.comcom.repository.CompanyRepository;
import org.comcom.repository.UserRepository;
import org.comcom.repository.VideoCallRoomRepository;
import org.comcom.service.NtfyService;
import org.comcom.service.VideoCallRoomService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VideoCallRoomServiceImpl implements VideoCallRoomService {

    private final VideoCallRoomRepository videoCallRoomRepository;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final NtfyService ntfyService;

    @Override
    public VideoCallRoomDto createVideoCallRoom(VideoCallRoomRequest videoCallRoomRequest) {
        VideoCallRoom videoCallRoom = new VideoCallRoom();
        Appointment appointment = appointmentRepository.findById(videoCallRoomRequest.getAppointment_id()).get();
        videoCallRoom.setAppointment(appointment);
        videoCallRoom.setRoom_status(0);
        List<Users> appendUsers = new ArrayList<>();
        for (Long userId : videoCallRoomRequest.getParticipant_ids()) {
            Users user = userRepository.findById(userId).get();
            appendUsers.add(user);
        }
        videoCallRoom.setParticipant(appendUsers);
        videoCallRoom.setOrganizer(userRepository.findById(videoCallRoomRequest.getOrganizer()).get());
        return videoCallRoomRepository.save(videoCallRoom).toDto();
    }

    @Override
    public VideoCallRoomDto joinCall(Long room_id){
        System.out.println("server : "+room_id);
        VideoCallRoom videoCallRoom = videoCallRoomRepository.findById(room_id).get();
        videoCallRoom.setStart_time(LocalDateTime.now());
        List<Users> appendUsers = new ArrayList<>();
        appendUsers.add(userRepository.findByEmailIgnoreCase(SecurityUtils.getAuthentication().getPrincipal().toString()).get());
        videoCallRoom.setParticipant(appendUsers);
        videoCallRoom.setRoom_status(1);
        videoCallRoomRepository.save(videoCallRoom);
        sendNotification("comcom-videoroom-employer-joined-" + videoCallRoom.getId(), "" + videoCallRoom.getId());
        return videoCallRoomRepository.save(videoCallRoom).toDto();
    }

    @Override
    public VideoCallRoomDto createCompanyRoom(Long companyId){
        VideoCallRoom videoCallRoom = new VideoCallRoom();
        videoCallRoom.setRoom_status(0);
        videoCallRoom.setCompany(companyRepository.findById(companyId).get());
        videoCallRoom.setOrganizer(userRepository.findByEmailIgnoreCase(SecurityUtils.getAuthentication().getPrincipal().toString()).get());
        videoCallRoomRepository.save(videoCallRoom);
        userRepository.findAll()
                .stream()
                .filter(user ->  user.getCompany() != null && user.getCompany().getId() == companyId && user.getProfile().getId() == 5)
                .forEach(user -> sendNotification("comcom-videoroom-income-call-" + user.getId(), "" + videoCallRoom.getId()));
        return videoCallRoomRepository.save(videoCallRoom).toDto();
    }

    @Override
    public VideoCallRoomDto createContactRoom(Long participant_id){
        VideoCallRoom videoCallRoom = new VideoCallRoom();
        videoCallRoom.setRoom_status(0);
        videoCallRoom.setOrganizer(userRepository.findByEmailIgnoreCase(SecurityUtils.getAuthentication().getPrincipal().toString()).get());
        List<Users> appendUsers = new ArrayList<>();
        appendUsers.add(userRepository.findById(participant_id).get());
        videoCallRoom.setParticipant(appendUsers);
        return videoCallRoomRepository.save(videoCallRoom).toDto();
    }

    @Override
    public VideoCallRoomDto getVideoCallRoomById(Long id) {
        Optional<VideoCallRoom> optionalVideoCallRoom = videoCallRoomRepository.findById(id);

        if (optionalVideoCallRoom.isPresent()) {
            VideoCallRoom videoCallRoom = optionalVideoCallRoom.get();
            return videoCallRoom.toDto();
        } else {
            throw new NotFoundException("VideoCallRoom not found", "VideoCallRoom not found with ID: " + id);
        }
    }

    @Override
    public List<VideoCallRoom> getAllVideoCallRooms(){
        return videoCallRoomRepository.findAll();
    }

    @Override
    public VideoCallRoomDto updateStartTime(Long id){
        Optional<VideoCallRoom> optionalVideoCallRoom = videoCallRoomRepository.findById(id);

        if (optionalVideoCallRoom.isPresent()) {
            VideoCallRoom videoCallRoom = optionalVideoCallRoom.get();
            videoCallRoom.setStart_time(LocalDateTime.now());
            videoCallRoom.setRoom_status(1);
            return videoCallRoomRepository.save(videoCallRoom).toDto();
        } else {
            throw new NotFoundException("VideoCallRoom not found", "VideoCallRoom not found with ID: " + id);
        }
    }

    @Override
    public VideoCallRoomDto updateEndTime(Long id){
        VideoCallRoom videoCallRoom = videoCallRoomRepository.findById(id)
                .orElseThrow(IncorrectCredentialsException::new);

        videoCallRoom.setEnd_time(LocalDateTime.now());
        videoCallRoom.setRoom_status(2);
        return videoCallRoomRepository.save(videoCallRoom).toDto();
    }

    @Override
    public List<VideoCallRoom> getAllRoomByStatus(Integer status){
        return videoCallRoomRepository.findAll()
                .stream()
                .filter(videoCallRoom -> videoCallRoom.getRoom_status() == status)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteVideoCallRoom(Long id){
        if(videoCallRoomRepository.existsById(id)){
            videoCallRoomRepository.deleteById(id);
            return true;
        }
        throw new NotFoundException("VideoCallRoom not found " + id, "VideoCallRoom not found: " + id);
    }


    public void sendNotification(String topic, String message){
        System.out.println("Employer-topic : " + topic);
        System.out.println("Employer-message : " + message);
        ntfyService.sendMessage(topic, message);
    }

}
