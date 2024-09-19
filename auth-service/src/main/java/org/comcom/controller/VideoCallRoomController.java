package org.comcom.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.comcom.dto.ApiResponse;
import org.comcom.dto.CreateRoomRequest;
import org.comcom.dto.VideoCallRoomRequest;
import org.comcom.service.VideoCallRoomService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static org.comcom.utils.ApiResponseUtils.buildResponse;

@CrossOrigin
@RestController
@RequestMapping("/v1/videocallroom")
@AllArgsConstructor
@Slf4j(topic = "VideoCallRoomController")
public class VideoCallRoomController {

    private final VideoCallRoomService videoCallRoomService;

    @PostMapping(path = "/createAppointmentRoom",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> createAppointmentRoom(@RequestBody VideoCallRoomRequest videoCallRoomRequest) {
        return buildResponse(videoCallRoomService.createVideoCallRoom(videoCallRoomRequest));
    }

    @PostMapping(path = "/createCompanyRoom",produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> createCompanyRoom(@RequestBody CreateRoomRequest createRoomRequest){
        return buildResponse(videoCallRoomService.createCompanyRoom(createRoomRequest.getId()));
    }

    @PostMapping(path = "/createContactRoom",produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> createContactRoom(@RequestBody CreateRoomRequest createRoomRequest){
        return buildResponse(videoCallRoomService.createContactRoom(createRoomRequest.getId()));
    }

    @PostMapping(path = "/joinCall",produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> joinCall(@RequestBody CreateRoomRequest createRoomRequest){
        System.out.println(createRoomRequest.getId());
        return buildResponse(videoCallRoomService.joinCall(createRoomRequest.getId()));
    }

    @PutMapping(path = "/updateStartTime/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> updateStartTime(@PathVariable Long id) {
        return buildResponse(videoCallRoomService.updateStartTime(id));
    }

    @PutMapping(path = "/updateEndTime/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> updateEndTime(@PathVariable Long id) {
        return buildResponse(videoCallRoomService.updateEndTime(id));
    }

    @GetMapping(path = "/getById/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> getVideoCallRoomById(@PathVariable Long id) {
        return buildResponse(videoCallRoomService.getVideoCallRoomById(id));
    }

    @GetMapping(path = "/getAll")
    public ApiResponse<?> getAllVideoCallRooms() {
        return buildResponse(videoCallRoomService.getAllVideoCallRooms());
    }

    @GetMapping(path = "/getAllRoomByStatus",produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> getAllRoomByStatus(@RequestParam("status") Integer status){
        return buildResponse(videoCallRoomService.getAllRoomByStatus(status));
    }

    @DeleteMapping(path = "/deleteById/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> deleteVideoCallRoomById(@PathVariable Long id) {
        return buildResponse(videoCallRoomService.deleteVideoCallRoom(id));
    }

}
