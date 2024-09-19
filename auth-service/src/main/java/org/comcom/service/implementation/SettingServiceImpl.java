package org.comcom.service.implementation;

import lombok.AllArgsConstructor;
import org.comcom.dto.SettingChangeRequest;
import org.comcom.model.Setting;
import org.comcom.repository.SettingRepository;
import org.comcom.service.SettingService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SettingServiceImpl implements SettingService {

    private final SettingRepository settingRepository;

    @Override
    public Setting changeSettings(SettingChangeRequest settings, Long id) {
        System.out.println("settings: " + settings);
        System.out.println(("Id: " + id));
        Setting oldSetting = settingRepository.findById(id).get();
        BeanUtils.copyProperties(settings, oldSetting);
        return settingRepository.save(oldSetting);
    }
}
