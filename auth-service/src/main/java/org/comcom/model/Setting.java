package org.comcom.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "settings")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Setting implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "setting_gen")
    @SequenceGenerator(name = "setting_gen", sequenceName = "setting_gen_sequence", allocationSize = 1)
    private Long id;

    @NotNull
    @Column(name = "createProfile")
    private boolean createProfile;

    @NotBlank
    @Column(name = "statusVisibility")
    private String statusVisibility;

    @NotBlank
    @Column(name = "profileVisibility")
    private String profileVisibility;

    @NotBlank
    @Column(name = "userName")
    private String userName;

    @NotNull
    @Column(name = "germanyLanguage")
    private boolean germanyLanguage;

    @NotNull
    @Column(name = "austriaLanguage")
    private boolean austriaLanguage;

    @NotNull
    @Column(name = "switzerlandLanguage")
    private boolean switzerlandLanguage;

    @NotNull
    @Column(name = "datamCheck")
    private boolean datancheck;

    @NotNull
    @Column(name = "dataTransfer")
    private boolean dataTransfer;

    @NotNull
    @Column(name = "dataDeletion")
    private boolean dataDeletion;

    @NotNull
    @Column(name = "consentReview")
    private boolean consentReview;

    @NotNull
    @Column(name = "receiveAppointment")
    private boolean receiveAppointment;

    @NotNull
    @Column(name = "availabilityAppointment")
    private boolean availabilityAppointment;

    @NotNull
    @Column(name = "durationAppointment")
    private boolean durationAppointment;

    @NotNull
    @Column(name = "publicStatus")
    private boolean publicStatus;

    @NotNull
    @Column(name = "contactCall")
    private boolean contactCall;

    @NotNull
    @Column(name = "checkData")
    private boolean checkData;

    @NotNull
    @Column(name = "seeAvailability")
    private boolean seeAvailability;

    @NotNull
    @Column(name = "anonymousCall")
    private boolean anonymousCall;

    @NotNull
    @Column(name = "sendAppointment")
    private boolean sendAppointment;

    @NotNull
    @Column(name = "communication")
    private boolean communication;

    @NotNull
    @Column(name = "security")
    private boolean security;

    @NotNull
    @Column(name = "meet")
    private boolean meet;

    @NotNull
    @Column(name = "notify_videocall")
    private boolean notify_videocall;

    @NotNull
    @Column(name = "notify_login")
    private boolean notify_login;

    @NotNull
    @Column(name = "notify_appointment")
    private boolean notify_appointment;

    @NotNull
    @Column(name = "notify_call")
    private boolean notify_call;

    @NotNull
    @Column(name = "Ringtone")
    private String ringtone;

    @NotNull
    @Column(name = "notification_sound")
    private String notification_sound;

    @NotNull
    @Column(name = "pop_up")
    private boolean pop_up;

    @NotNull
    @Column(name = "mute")
    private boolean mute;

    @NotNull
    @Column(name = "notify_led")
    private boolean notify_led;

    @NotNull
    @Column(name = "during_call")
    private boolean during_call;

    @NotNull
    @Column(name = "notification")
    private boolean notification;

    @NotNull
    @Column(name = "who_call")
    private String who_call;

    @NotNull
    @Column(name = "forward_call")
    private boolean forward_call;

    @NotNull
    @Column(name = "call_max_len")
    private String call_max_len;

    @NotNull
    @Column(name = "allow_forward")
    private boolean allow_forward;

    @NotNull
    @Column(name = "auto_forward")
    private boolean auto_forward;

    @NotNull
    @Column(name = "notify_status")
    private String notify_status;

    public Setting(){
        this.createProfile = true;
        this.statusVisibility = "all";
        this.profileVisibility = "all";
        this.userName = "Daniel";
        this.germanyLanguage = true;
        this.austriaLanguage = true;
        this.switzerlandLanguage = true;
        this.datancheck = true;
        this.dataTransfer = true;
        this.dataDeletion = true;
        this.consentReview = true;
        this.receiveAppointment = true;
        this.availabilityAppointment = true;
        this.durationAppointment = true;
        this.publicStatus = true;
        this.contactCall = true;
        this.checkData = true;
        this.seeAvailability = true;
        this.anonymousCall = true;
        this.sendAppointment = true;
        this.communication = true;
        this.security = true;
        this.meet = true;
        this.notify_videocall = true;
        this.notify_login = true;
        this.notify_appointment = true;
        this.notify_call = true;
        this.ringtone = "ringing 1";
        this.notification_sound = "ringing 1";
        this.pop_up = true;
        this.mute = true;
        this.notify_led = true;
        this.during_call = true;
        this.notification = true;
        this.who_call = "all";
        this.forward_call = true;
        this.call_max_len = "fiveToTenMin";
        this.allow_forward = false;
        this.auto_forward = false;
        this.notify_status = "Ringtone";
    }

    public Setting(boolean createProfile, String statusVisibility, String profileVisibility, String userName, boolean germanyLanguage,
                   boolean austriaLanguage, boolean switzerlandLanguage, boolean datancheck, boolean dataTransfer, boolean dataDeletion,
                   boolean consentReview, boolean receiveAppointment, boolean availabilityAppointment, boolean durationAppointment,
                   boolean publicStatus, boolean contactCall, boolean checkData, boolean seeAvailability, boolean anonymousCall, boolean sendAppointment,
                   boolean communication, boolean security, boolean meet, boolean notify_videocall, boolean notify_login, boolean notify_appointment,
                   boolean notify_call, String ringtone, String notification_sound, boolean pop_up, boolean mute, boolean notify_led, boolean during_call,
                   boolean notification, String who_call, boolean forward_call, String call_max_len, boolean allow_forward, boolean auto_forward, String notify_status){
        this.createProfile = createProfile;
        this.statusVisibility = statusVisibility;
        this.profileVisibility = profileVisibility;
        this.userName = userName;
        this.germanyLanguage = germanyLanguage;
        this.austriaLanguage = austriaLanguage;
        this.switzerlandLanguage = switzerlandLanguage;
        this.datancheck = datancheck;
        this.dataTransfer = dataTransfer;
        this.dataDeletion = dataDeletion;
        this.consentReview = consentReview;
        this.receiveAppointment = receiveAppointment;
        this.availabilityAppointment = availabilityAppointment;
        this.durationAppointment = durationAppointment;
        this.publicStatus = publicStatus;
        this.contactCall = contactCall;
        this.checkData = checkData;
        this.seeAvailability = seeAvailability;
        this.anonymousCall = anonymousCall;
        this.sendAppointment = sendAppointment;
        this.communication = communication;
        this.security = security;
        this.meet = meet;
        this.notify_videocall = notify_videocall;
        this.notify_login = notify_login;
        this.notify_appointment = notify_appointment;
        this.notify_call = notify_call;
        this.ringtone = ringtone;
        this.notification_sound = notification_sound;
        this.pop_up = pop_up;
        this.mute = mute;
        this.notify_led = notify_led;
        this.during_call = during_call;
        this.notification = notification;
        this.who_call = who_call;
        this.forward_call = forward_call;
        this.call_max_len = call_max_len;
        this.allow_forward = allow_forward;
        this.auto_forward = auto_forward;
        this.notify_status = notify_status;
    }


}
