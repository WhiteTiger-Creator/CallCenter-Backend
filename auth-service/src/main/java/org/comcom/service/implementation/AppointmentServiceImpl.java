package org.comcom.service.implementation;

import lombok.AllArgsConstructor;
import org.comcom.config.security.SecurityUtils;
import org.comcom.dto.AppointmentRequest;
import org.comcom.exception.BadInputException;
import org.comcom.exception.IncorrectCredentialsException;
import org.comcom.exception.LessMoneyException;
import org.comcom.exception.NotFoundException;
import org.comcom.model.OnlineStatus;
import org.comcom.model.Users;
import org.comcom.model.VideoCallRoom;
import org.comcom.repository.UserRepository;
import org.comcom.repository.VideoCallRoomRepository;
import org.comcom.service.AppointmentService;
import org.comcom.repository.AppointmentRepository;
import org.comcom.dto.AppointmentDto;
import org.comcom.model.Appointment;
import org.comcom.service.NtfyService;
import org.springframework.beans.BeanUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final VideoCallRoomRepository videoCallRoomRepository;
    private JavaMailSender javaMailSender;
    private final NtfyService ntfyService;

    @Override
    @Transactional
    public AppointmentDto createAppointment(AppointmentRequest appointmentRequest) {
        Users user1 = userRepository.findByEmailIgnoreCase(SecurityUtils.getAuthentication().getPrincipal().toString()).get();
        Users interpreter = userRepository.findById(appointmentRequest.getInterpreter_id()).get();
        if(user1.getWallet() < interpreter.getPrice()){
            throw new BadInputException("There is insufficient balance.", "There is insufficient balance.");
        }
        user1.setWallet(user1.getWallet() - interpreter.getPrice());
        userRepository.save(user1);
        Appointment appointment = new Appointment();
        appointment.setAddress(appointmentRequest.getAddress());
        appointment.setCity(appointmentRequest.getCity());
        appointment.setNote(appointmentRequest.getNote());
        appointment.setCountry(appointmentRequest.getCountry());
        appointment.setEndDate(appointmentRequest.getEndDate());
        appointment.setConfirmation(false);
        appointment.setAgreement(appointmentRequest.getAgreement());
        appointment.setPurpose(appointmentRequest.getPurpose());
        appointment.setEndTime(appointmentRequest.getEndTime());
        appointment.setInterpreterType(appointmentRequest.getInterpreterType());
        appointment.setStartDate(appointmentRequest.getStartDate());
        appointment.setStartTime(appointmentRequest.getStartTime());
        appointment.setStreet(appointmentRequest.getStreet());
        appointment.setTopic(appointmentRequest.getTopic());
        appointment.setZip(appointmentRequest.getZip());
        appointment.setUser_email(appointmentRequest.getUser_email());
        System.out.println("interpreter_data: " + appointmentRequest.getInterpreter_id());
        if(appointmentRequest.getInterpreter_id() != -1) {

            Optional<Users> userOptional = userRepository.findById(appointmentRequest.getInterpreter_id());
            if (userOptional.isPresent()) {
                Users user = userOptional.get();
                appointment.setInterpreter(new Users());
                BeanUtils.copyProperties(user, appointment.getInterpreter());
            } else {
                throw new BadInputException("Interpreter not found for id: " + appointmentRequest.getInterpreter_id(),"Interpreter not found for id: " + appointmentRequest.getInterpreter_id());
            }
        }
        appointmentRepository.save(appointment);
//        if (appointmentRequest.getInterpreter_id() == -1){
//            userRepository.findAll()
//                    .stream()
//                    .filter(user -> user.getProfile().getId() == 3 && user.getStatus() == OnlineStatus.ONLINE)
//                    .forEach(user -> sendconfirmAppointmentUrl(user.getEmail(),appointment.getId()));
//        }
//        else {
//            sendconfirmAppointmentUrl(userRepository.findById(appointmentRequest.getInterpreter_id()).get().getEmail(), appointment.getId());
//        }
        return appointmentRepository.save(appointment).toDto();
    }

    @Override
    public AppointmentDto getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(IncorrectCredentialsException::new)
                .toDto();
    }

    @Override
    public List<AppointmentDto> getAppointmentByUserid(Long Userid) {
        return appointmentRepository.findAll()
                .stream()
                .filter(appointment -> Userid.equals(appointment.getInterpreter().getId()) || userRepository.findByEmailIgnoreCase(appointment.getUser_email()).map(user-> Userid.equals(user.getId())).orElse(false))
                .map(Appointment::toDto)
                .collect(Collectors.toList());
    }


    @Override
    public List<Appointment> getAllAppointments(){
        return appointmentRepository.findAll();
    }

    @Override
    public AppointmentDto acceptAppointment(Long appointment_id, Long interpreter_id){

        Optional<Appointment> appointment = appointmentRepository.findById(appointment_id);
        if (appointment.isPresent()) {
            Appointment app = appointment.get();
            if (!app.getConfirmation()){
                app.setConfirmation(true);
                Users interpreter = userRepository.findById(interpreter_id).get();
                app.setInterpreter(interpreter);
                VideoCallRoom videocallRoom = new VideoCallRoom();
                Users user= userRepository.findByEmailIgnoreCase(app.getUser_email()).get();
                videocallRoom.setAppointment(appointmentRepository.findById(app.getId()).get());
                List<Users> appendUsers = new ArrayList<>();
                Users appendUser = userRepository.findById(app.getInterpreter().getId()).get();
                appendUsers.add(appendUser);
                videocallRoom.setParticipant(appendUsers);
                videocallRoom.setOrganizer(userRepository.findById(user.getId()).get());
                videocallRoom.setRoom_status(0);
                videoCallRoomRepository.save(videocallRoom);
                app.setRoom_id(videocallRoom.getId());
            }
            return appointmentRepository.save(app).toDto();
        } else {
            throw new BadInputException("User not found for id: " + appointment_id,"User not found for id: " + appointment_id);
        }
    }

    @Override
    public AppointmentDto declineAppointment(Long id){

        Optional<Appointment> appointment = appointmentRepository.findById(id);
        if (appointment.isPresent()) {
            Appointment app = appointment.get();
            System.out.println(app.getUser_email() + "--------------------");
            app.setConfirmation(false);
 //           sendDeclineMessage(app.getUser_email(), id);
            return appointmentRepository.save(app).toDto();
        } else {
            throw new BadInputException("User not found for id: " + id,"User not found for id: " + id);
        }
    }

    @Override
    @Transactional
    public AppointmentDto updateAppointment(Long id, AppointmentRequest appointmentRequest) {
        System.out.println("id: " + id );
        Appointment existingAppointment = appointmentRepository.findById(id).get();
//        if(existingAppointment.getConfirmation() == true) {
//            sendCancelMessage(userRepository.findById(existingAppointment.getInterpreter().getId()).get().getEmail(), existingAppointment.getId());
//        }
        existingAppointment.setAddress(appointmentRequest.getAddress());
        existingAppointment.setCity(appointmentRequest.getCity());
        existingAppointment.setNote(appointmentRequest.getNote());
        existingAppointment.setAgreement(appointmentRequest.getAgreement());
        existingAppointment.setConfirmation(false);
        existingAppointment.setCountry(appointmentRequest.getCountry());
        existingAppointment.setEndDate(appointmentRequest.getEndDate());
        existingAppointment.setPurpose(appointmentRequest.getPurpose());
        existingAppointment.setEndTime(appointmentRequest.getEndTime());
        existingAppointment.setInterpreterType(appointmentRequest.getInterpreterType());
        existingAppointment.setStartDate(appointmentRequest.getStartDate());
        existingAppointment.setStartTime(appointmentRequest.getStartTime());
        existingAppointment.setStreet(appointmentRequest.getStreet());
        existingAppointment.setTopic(appointmentRequest.getTopic());
        existingAppointment.setZip(appointmentRequest.getZip());
        existingAppointment.setUser_email(appointmentRequest.getUser_email());
        if(appointmentRequest.getInterpreter_id() != -1) {
            System.out.println("Interpreter_id: " + appointmentRequest.getInterpreter_id());
            Optional<Users> userOptional = userRepository.findById(appointmentRequest.getInterpreter_id());
            if (userOptional.isPresent()) {
                Users user = userOptional.get();
                existingAppointment.setInterpreter(new Users());
                BeanUtils.copyProperties(user, existingAppointment.getInterpreter());
            } else {
                throw new BadInputException("Interpreter not found for id: " + appointmentRequest.getInterpreter_id(),"Interpreter not found for id: " + appointmentRequest.getInterpreter_id());
            }
        }
//        if (appointmentRequest.getInterpreter_id() == -1){
//            userRepository.findAll()
//                    .stream()
//                    .filter(user -> user.getPrice() == 0 && user.getProfile().getId() == 3 && user.getStatus() == OnlineStatus.ONLINE)
//                    .forEach(user -> sendconfirmAppointmentUrl(user.getEmail(),existingAppointment.getId()));
//        }
//        else {
//            sendconfirmAppointmentUrl(userRepository.findById(appointmentRequest.getInterpreter_id()).get().getEmail(), existingAppointment.getId());
//        }
        return appointmentRepository.save(existingAppointment).toDto();
    }

    @Override
    @Transactional
    public boolean deleteAppointment(Long id){
        if(appointmentRepository.existsById(id)){
            appointmentRepository.deleteById(id);
            return true;
        }
        throw new NotFoundException("Appointment not found", "Appointment not found: "+id);
    }

//    private void sendconfirmAppointmentUrl(String email, Long appointment_id){
//        System.out.println("Email " + email);
//        System.out.println("id " + userRepository.findByEmailIgnoreCase(email).get().getId());
//
//        ntfyService.sendMessage("comcom-appointment-" + userRepository.findByEmailIgnoreCase(email).get().getId(), "Click Next to set up your appointment. " + "https://app.comcom.at/confirm_appointment");
//
//        System.out.println("Email1 " + email);
//        String subject = "Appointment request: " + appointment_id;
//        String message = "Click Next to set up your appointment. " + "https://app.comcom.at/confirm-appointment/" + appointment_id;
//
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        mailMessage.setFrom("earl0425@outlook.com");
//        mailMessage.setTo(email);
//        mailMessage.setSubject(subject);
//        mailMessage.setText(message);
//        javaMailSender.send(mailMessage);
//        System.out.println("confirm Appointment your email: " + email);
//        System.out.println("confirm Appointment Email sent: " + message);
//    }

//    private void sendDeclineMessage(String email, Long appointment_id){
//        System.out.println();
//        System.out.println(email + "sendDeclineMessage");
//        ntfyService.sendMessage("comcom-appointment-" + userRepository.findByEmailIgnoreCase(email).get().getId(), "The appointment has been declined.");
//        String subject = "Appointment Result: " + appointment_id;
//        String message = "The appointment has been declined.";
//
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        mailMessage.setFrom("earl0425@outlook.com");
//        mailMessage.setTo(email);
//        mailMessage.setSubject(subject);
//        mailMessage.setText(message);
//        javaMailSender.send(mailMessage);
//        System.out.println("Decline Appointment your email: " + email);
//        System.out.println("Decline Appointment Email sent: " + message);
//    }

//    private void sendCancelMessage(String email, Long appointment_id){
//        System.out.println();
//        System.out.println(email + " sendCancelMessage");
//        ntfyService.sendMessage("comcom-appointment-" + userRepository.findByEmailIgnoreCase(email).get().getId(), "The appointment has been cancelled.");
//
//        String subject = "Appointment Result: " + appointment_id;
//        String message = "The appointment has been cancelled.";
//
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        mailMessage.setFrom("earl0425@outlook.com");
//        mailMessage.setTo(email);
//        mailMessage.setSubject(subject);
//        mailMessage.setText(message);
//        javaMailSender.send(mailMessage);
//        System.out.println("Cancel Appointment your email: " + email);
//        System.out.println("Cancel Appointment Email sent: " + message);
//    }
}
