package com.lion.pinepeople.service;

import com.lion.pinepeople.domain.entity.Participant;
import com.lion.pinepeople.domain.entity.Party;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.enums.ParticipantRole;
import com.lion.pinepeople.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParticipantService {
    private final ParticipantRepository participantRepository;
    public Participant createHostParticipant(User user, Party party){
        return participantRepository.save(Participant.of(user,party, ParticipantRole.HOST));
    }

    public Participant createGuestParticipant(User user, Party party){
        return participantRepository.save(Participant.of(user,party, ParticipantRole.GUEST));
    }
}
