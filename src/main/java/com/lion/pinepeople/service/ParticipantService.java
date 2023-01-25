package com.lion.pinepeople.service;

import com.lion.pinepeople.domain.entity.Participant;
import com.lion.pinepeople.domain.entity.Party;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.enums.ParticipantRole;
import com.lion.pinepeople.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParticipantService {
    private final ParticipantRepository participantRepository;

    /**
     * 특정 User 를 host 로 하는 파티가 생성될때 participant table에 해당 유저와 파티정보를 저장함.
     * @param user 파티의 host
     * @param party host 에 의해 생성된 파티
     * @return 해당 파티와 해당 유저의 정보를 participant 테이블에 저장한 뒤 리턴함
     * host 의 자격으로 저장되었으므로 approvalStatus 는 APPROVED 로 저장됨
     */
    public Participant createHostParticipant(User user, Party party){
        return participantRepository.save(Participant.of(user,party, ParticipantRole.HOST));
    }

    /**
     * 특정 User 특정 파티의 guest 로 저장함
     * @param user guest 로 저장될 유저
     * @param party user 가 guest 로 들어가게 될 파티
     * @return 해당 파티와 해당 유저의 정보를 participant 테이블에 저장한 뒤 리턴함
     * guest 의 자격으로 저장되었으므로 approvalStatus 는 WAITING 로 저장됨
     */
    public Participant createGuestParticipant(User user, Party party){
        return participantRepository.save(Participant.of(user,party, ParticipantRole.GUEST));
    }

    /**
     * participant 테이블에서 특정 유저가 guest 자격인 경우에 해당하는 row 를 검색함
     * @param user 현재 로그인된 유저
     * @return participant 테이블에서 user 가 guest 자격인 경우에 해당하는 row 를 리턴함
     */
    public Page<Participant> getMyGuestParty(Pageable pageable, User user){
        return participantRepository.findAllByUserAndParticipantRole(pageable,user,ParticipantRole.GUEST);
    }
}
