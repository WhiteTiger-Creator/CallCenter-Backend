package org.comcom.controller;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.comcom.config.security.JwtSecurityFilter;
import org.comcom.config.security.SecurityConfig;
import org.comcom.config.security.SecurityUtils;
import org.comcom.dto.ApiResponse;
import org.comcom.dto.AppointmentRequest;
import org.comcom.exception.NotFoundException;
import org.comcom.service.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.validation.Valid;
import javax.validation.constraints.Null;

import static org.comcom.utils.ApiResponseUtils.buildResponse;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/v1/appointments")
@Slf4j(topic = "AppointmentController")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final JwtSecurityFilter jwtSecurityFilter;
    private final SecurityConfig securityConfig;



    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<?> getAppointmentById(@PathVariable Long id){
        return buildResponse(appointmentService.getAppointmentById(id));
    }

    @GetMapping(path = "/byUserid/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<?> getAppointmentByUserid(@PathVariable Long id){
        return buildResponse(appointmentService.getAppointmentByUserid(id));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<?> getAllAppointments(){
        return buildResponse(appointmentService.getAllAppointments());
    }

    @PostMapping
    ApiResponse<?> createAppointment(@RequestBody @Valid AppointmentRequest appointmentRequest){
        return buildResponse(appointmentService.createAppointment(appointmentRequest));
    }

    @PostMapping(path = "/accept", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<?> acceptAppointment(@RequestParam Long appointment_id, @RequestParam Long interpreter_id){
        return buildResponse(appointmentService.acceptAppointment(appointment_id, interpreter_id));
    }

    @PostMapping(path = "/decline", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<?> declineAppointment(@RequestParam Long appointment_id){
        return buildResponse(appointmentService.declineAppointment(appointment_id));
    }

    @PutMapping(path = {"/update/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<?> updateAppointment(@PathVariable Long id, @RequestBody AppointmentRequest appointmentRequest){
        return buildResponse(appointmentService.updateAppointment(id, appointmentRequest));
    }

    @DeleteMapping(path = {"/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<?> deleteAppointment(@PathVariable Long id){
        return buildResponse(appointmentService.deleteAppointment(id));
    }

}