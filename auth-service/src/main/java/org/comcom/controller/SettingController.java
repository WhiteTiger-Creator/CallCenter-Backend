package org.comcom.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.comcom.dto.ApiResponse;
import org.comcom.dto.SettingChangeRequest;
import org.comcom.service.SettingService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.comcom.utils.ApiResponseUtils.buildResponse;

@CrossOrigin
@RestController
@RequestMapping("/v1/setting")
@AllArgsConstructor
@Slf4j(topic = "SettingController")
public class SettingController {

    private final SettingService settingService;

    @PutMapping(path = "/change/{id}",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> changeSettings(@RequestBody @Valid SettingChangeRequest settings, @PathVariable Long id) {
        return buildResponse(settingService.changeSettings(settings, id));
    }
}
