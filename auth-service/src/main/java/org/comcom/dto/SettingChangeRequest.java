package org.comcom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SettingChangeRequest {

    private boolean createProfile;
    private String statusVisibility;
    private String profileVisibility;
    private String userName;
    private boolean germanyLanguage;
    private boolean austriaLanguage;
    private boolean switzerlandLanguage;
    private boolean datancheck;
    private boolean dataTransfer;
    private boolean dataDeletion;
    private boolean consentReview;
    private boolean receiveAppointment;
    private boolean availabilityAppointment;
    private boolean durationAppointment;
    private boolean publicStatus;
    private boolean contactCall;
    private boolean checkData;
    private boolean seeAvailability;
    private boolean anonymousCall;
    private boolean sendAppointment;
    private boolean communication;
    private boolean security;
    private boolean meet;
    private boolean notify_videocall;
    private boolean notify_login;
    private boolean notify_appointment;
    private boolean notify_call;
    private String ringtone;
    private String notification_sound;
    private boolean pop_up;
    private boolean mute;
    private boolean notify_led;
    private boolean during_call;
    private boolean notification;
    private String who_call;
    private boolean forward_call;
    private String call_max_len;
    private boolean allow_forward;
    private boolean auto_forward;
    private String notify_status;
}