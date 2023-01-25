package com.lion.pinepeople.service;

import com.lion.pinepeople.domain.dto.participant.ParticipantCreateResponse;
import com.lion.pinepeople.domain.entity.Participant;
import com.lion.pinepeople.domain.entity.Party;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.enums.ParticipantRole;
import com.lion.pinepeople.enums.UserRole;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.customException.AppException;
import com.lion.pinepeople.repository.ParticipantRepository;
import com.lion.pinepeople.repository.PartyRepository;
import com.lion.pinepeople.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParticipantService {
    private final ParticipantRepository participantRepository;
    private final PartyRepository partyRepository;
    private final UserRepository userRepository;

    /**
     * 특정 유저가 존재하는지를 확인함
     * @param userId 존재하는 유저인지 확인하고픈 유저의 id
     * @return 존재할 경우 해당 userId를 가지는 user 리턴, 존재하지 않을 경우 USER_NOT_FOUND 에러 발생
     */
    public User validateUser(String userId){
        return userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage()));
    }

    /**
     * 특정 파티가 존재하는지를 확인함
     * @param partyId 존재하는 파티인지 확인하고픈 파티의 id
     * @return 존재할 경우 해당 partyId를 가지는 party 리턴, 존재하지 않을 경우 PARTY_NOT_FOUND 에러 발생
     */
    public Party validateParty(Long partyId){
        return partyRepository.findById(partyId)
                .orElseThrow(() -> new AppException(ErrorCode.PARTY_NOT_FOUND, ErrorCode.PARTY_NOT_FOUND.getMessage()));
    }

    /**
     * 특정 유저가 특정 파티의 host인지 확인
     * @param currentUser 현재 로그인된 회원
     * @param party 현재 로그인된 회원이 host 인지 확인할 파티
     * 유저가 해당 파티의 host 가 아닐 경우 INVALID_PERMISSION 에러 발생
     */
    public void validateHost(Party party, User currentUser){
        if(party.getUser().equals(currentUser)){
            throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        }
    }

    /**
     * 특정 유저가 특정 파티의 host 이거나 관리자인지 확인
     * @param currentUser 현재 로그인된 회원
     * @param party 현재 로그인된 회원이 host 인지 확인할 파티
     * 유저가 해당 파티의 host 도 아니고 관리자도 아닐 경우 INVALID_PERMISSION 에러 발생
     */
    public void validateHostOrAdmin(Party party, User currentUser){
        if(!(party.getUser().getId().equals(currentUser.getId())||currentUser.getRole().equals(UserRole.ADMIN))){
            throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        }
    }

    /**
     * 특정 User 를 host 로 하는 파티가 생성될때 participant table에 해당 유저와 파티정보를 저장함.
     * @param user 파티의 host
     * @param party host 에 의해 생성된 파티
     * @return 해당 파티와 해당 유저의 정보를 participant 테이블에 저장한 뒤 리턴함
     * host 의 자격으로 저장되었으므로 approvalStatus 는 APPROVED 로 저장됨
     */
    public Participant createHostParticipant(User user, Party party){
        validateUser(user.getId().toString());
        validateParty(party.getId());
        return participantRepository.save(Participant.of(user,party, ParticipantRole.HOST));
    }

    /**
     * 특정 User 특정 파티의 guest 로 저장함
     * @param userId guest 로 저장될 유저의 id
     * @param partyId user 가 guest 로 들어가게 될 파티의 id
     * @return 해당 파티와 해당 유저의 정보를 participant 테이블에 저장한 뒤 해당 정보를 ParticipantCreateResponse로 리턴함
     * guest 의 자격으로 저장되었으므로 approvalStatus 는 WAITING 로 저장됨
     */
    public ParticipantCreateResponse createGuestParticipant(Long partyId, String userId){
        User user = validateUser(userId);
        Party party = validateParty(partyId);
        Participant participant = participantRepository.save(Participant.of(user,party, ParticipantRole.GUEST));
        return ParticipantCreateResponse.of(participant);
    }

    /**
     * participant 테이블에서 특정 유저가 guest 자격인 경우에 해당하는 row 를 검색함
     * @param user 현재 로그인된 유저
     * @return participant 테이블에서 user 가 guest 자격인 경우에 해당하는 row 를 리턴함
     */
    public Page<Participant> getMyGuestParty(Pageable pageable, User user){
        validateUser(user.getId().toString());
        return participantRepository.findAllByUserAndParticipantRole(pageable,user,ParticipantRole.GUEST);
    }
}
