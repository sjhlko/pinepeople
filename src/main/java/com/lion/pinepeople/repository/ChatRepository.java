package com.lion.pinepeople.repository;

import com.lion.pinepeople.domain.entity.Chat;
import com.lion.pinepeople.domain.entity.ChattingRoom;
import com.lion.pinepeople.domain.entity.Participant;
import com.lion.pinepeople.domain.entity.Party;
import com.lion.pinepeople.enums.ApprovalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findAllByChattingRoom(ChattingRoom chattingRoom);
}
