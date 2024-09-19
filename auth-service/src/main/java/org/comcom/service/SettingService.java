package org.comcom.service;

import org.comcom.dto.SettingChangeRequest;
import org.comcom.model.*;


public interface SettingService {

     Setting changeSettings(SettingChangeRequest settings, Long id);
}
