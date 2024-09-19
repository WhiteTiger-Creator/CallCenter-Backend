package org.comcom.service;

import org.comcom.dto.*;
import org.comcom.model.Appointment;
import java.util.List;

public interface AppointmentService {

    AppointmentDto createAppointment(AppointmentRequest appointmentRequest);
    AppointmentDto getAppointmentById(Long id);
    List<AppointmentDto> getAppointmentByUserid(Long id);
    List<Appointment> getAllAppointments();
    AppointmentDto updateAppointment(Long id, AppointmentRequest appointmentRequest);

    AppointmentDto acceptAppointment(Long appointment_id, Long interpreter_id);

    AppointmentDto declineAppointment(Long id);
    boolean deleteAppointment(Long id);
}
