package org.comcom.service;

import org.comcom.dto.*;
import org.comcom.model.VideoCallRoom;

import java.util.List;

public interface VideoCallRoomService {

    VideoCallRoomDto createVideoCallRoom(VideoCallRoomRequest videoCallRoomRequest);

    VideoCallRoomDto createCompanyRoom(Long companyId);

    VideoCallRoomDto joinCall(Long room_id);
    VideoCallRoomDto createContactRoom(Long participant_id);
    VideoCallRoomDto getVideoCallRoomById(Long id);
    List<VideoCallRoom> getAllVideoCallRooms();
    VideoCallRoomDto updateStartTime(Long id);
    VideoCallRoomDto updateEndTime(Long id);

    List<VideoCallRoom> getAllRoomByStatus(Integer status);
    boolean deleteVideoCallRoom(Long id);
}
