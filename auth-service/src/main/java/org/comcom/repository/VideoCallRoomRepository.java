package org.comcom.repository;

import org.comcom.model.VideoCallRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoCallRoomRepository extends JpaRepository<VideoCallRoom, Long> {
}
