package com.lion.pinepeople.service;

import com.lion.pinepeople.domain.dto.participant.*;
import com.lion.pinepeople.domain.entity.Participant;
import com.lion.pinepeople.domain.entity.Party;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.enums.*;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.customException.AppException;
import com.lion.pinepeople.repository.ParticipantRepository;
import com.lion.pinepeople.repository.PartyRepository;
import com.lion.pinepeople.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class ParticipantService {
    private final ParticipantRepository participantRepository;
    private final PartyRepository partyRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

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
     * 특정 파티원이 존재하는지를 확인함
     * @param participantId 존재하는 파티원인지 확인하고픈 파티원의 id
     * @return 존재할 경우 해당 participantId를 가지는 participant 리턴, 존재하지 않을 경우 PARTICIPANT_NOT_FOUND 에러 발생
     */
    public Participant validateParticipant(Long participantId){
        return participantRepository.findById(participantId)
                .orElseThrow(() -> new AppException(ErrorCode.PARTICIPANT_NOT_FOUND, ErrorCode.PARTICIPANT_NOT_FOUND.getMessage()));
    }

    /**
     * 특정 파티원이 특정 파티에 존재하는지 확인함
     * @param participant 존재하는 파티원인지 확인하고픈 파티원
     * @param party 존재하는지 확인하고픈 파티
     */
    public void checkParticipantAndParty(User participant, Party party){
        participantRepository.findParticipantByUserAndParty(participant, party)
                .orElseThrow(() -> new AppException(ErrorCode.PARTICIPANT_NOT_FOUND, ErrorCode.PARTICIPANT_NOT_FOUND.getMessage()));
    }

    /**
     * 특정 유저가 특정 파티의 host인지 확인
     * @param currentUser 현재 로그인된 회원
     * @param party 현재 로그인된 회원이 host 인지 확인할 파티
     * 유저가 해당 파티의 host 가 아닐 경우 INVALID_PERMISSION 에러 발생
     */
    public void validateHost(Party party, User currentUser){
        if(!(Objects.equals(party.getUser().getId(), currentUser.getId()))){
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
     * 특정 파티에 파티원으로서 존재하는지를 확인함
     * @param user 존재하는 유저인지 확인하고픈 유저
     * @param party 확인하고 싶은 파티
     * 존재할 경우 DUPLICATED_PARTICIPANT 에러 발생
     */
    public void checkParticipantExist(Party party, User user){
        if(participantRepository.findParticipantByUserAndParty(user,party).isPresent()){
            throw new AppException(ErrorCode.DUPLICATED_PARTICIPANT,ErrorCode.DUPLICATED_PARTICIPANT.getMessage());
        }
    }
    public Participant validateParticipant(Party party, User user){
        return participantRepository.findParticipantByUserAndParty(user,party)
                .orElseThrow(() -> new AppException(ErrorCode.PARTICIPANT_NOT_FOUND, ErrorCode.PARTICIPANT_NOT_FOUND.getMessage()));
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
        checkParticipantExist(party,user);
        return participantRepository.save(Participant.of(user,party, ParticipantRole.HOST));
    }

    /**
     * 특정 User 를 host 로 하는 파티가 생성될때 participant table에 해당 유저와 파티정보를 저장함.
     * @param userId 가입한 회원의 아이디
     * @param partyId 가입한 파티 번호
     * @return 파티원 정보를 리턴함
     */
    public ParticipantInfoResponse getParticipant(Long userId, Long partyId){
        Participant participant = participantRepository.findByUserIdAndPartyId(userId, partyId);
        return ParticipantInfoResponse.of(participant);
    }


    /**
     * 특정 User 특정 파티의 guest 로 저장함
     * @param userId guest 로 저장될 유저의 id
     * @param partyId user 가 guest 로 들어가게 될 파티의 id
     * @return 해당 파티와 해당 유저의 정보를 participant 테이블에 저장한 뒤 해당 정보를 ParticipantCreateResponse로 리턴함
     * guest 의 자격으로 저장되었으므로 approvalStatus 는 WAITING 로 저장됨
     */
    @Transactional
    public ParticipantCreateResponse createGuestParticipant(Long partyId, String userId){
        User user = validateUser(userId);
        Party party = validateParty(partyId);
        checkParticipantExist(party,user);
        if(party.getPartyStatus().toString().equals("CLOSED")){
            throw new AppException(ErrorCode.PARTY_CLOSED,ErrorCode.PARTY_CLOSED.getMessage());
        }
        Participant participant = participantRepository.save(Participant.of(user,party, ParticipantRole.GUEST));
        // 파티 가입 신청
        String url = "/pinepeople/party/detail/"+partyId;
        notificationService.send(party.getUser(), url, NotificationType.APPLY_JOIN_PARTY,
                    participant.getUser().getName()+"님이 "+"\""+party.getPartyTitle() + "\" "+ NotificationType.APPLY_JOIN_PARTY.getMessage());
        return ParticipantCreateResponse.of(participant);
    }

    /**
     * participant 테이블에서 특정 유저가 guest 자격인 경우에 해당하는 row 를 검색함
     * @param user 현재 로그인된 유저
     * @return participant 테이블에서 user 가 guest 자격인 경우에 해당하는 row 를 리턴함
     */
    public Page<Participant> getMyGuestParty(Pageable pageable, User user){
        validateUser(user.getId().toString());
        return participantRepository.findAllByUserAndParticipantRoleAndApprovalStatus(pageable,user,ParticipantRole.GUEST,ApprovalStatus.APPROVED);
    }

    /**
     * participant 테이블에서 특정 유저가 승인 대기중인 경우에 해당하는 row 를 검색함
     * @param user 현재 로그인된 유저
     * @return participant 테이블에서 user 가 guest 자격인 경우에 해당하는 row 를 리턴함
     */
    public Page<Participant> getMyWaitingParty(Pageable pageable, User user){
        validateUser(user.getId().toString());
        return participantRepository.findAllByUserAndParticipantRoleAndApprovalStatus(pageable,user,ParticipantRole.GUEST,ApprovalStatus.WAITING);
    }

    /**
     * 특정 파티의 승인된 모든 파티원의 목록을 조회함
     * @param partyId 조회하고자 하는 파티
     * @return participant 테이블에서 해당 파티의 approved 상태의 파티원의 목록을 리턴함
     */
    public Page<ParticipantInfoResponse> getApprovedParticipant(Pageable pageable, Long partyId) {
        Party party = validateParty(partyId);
        Page<Participant> participants = participantRepository.findAllByPartyAndApprovalStatus(pageable,party,ApprovalStatus.APPROVED);
        return participants.map(ParticipantInfoResponse::of);
    }

    /**
     * 특정 파티의 대기중인 모든 파티원의 목록을 조회함
     * @param partyId 조회하고자 하는 파티
     * @param userId 해당 파티의 파티장
     * @return participant 테이블에서 해당 파티의 waiting 상태의 파티원의 목록을 리턴함
     */

    public Page<ParticipantInfoResponse> getWaitingParticipant(Pageable pageable, Long partyId, String userId) {
        Party party = validateParty(partyId);
        User user = validateUser(userId);
        validateHost(party,user);
        Page<Participant> participants = participantRepository.findAllByPartyAndApprovalStatus(pageable,party,ApprovalStatus.WAITING);
        return participants.map(ParticipantInfoResponse::of);
    }

    /**
     * 파티원 정보 수정
     * @param userId 현재 로그인된 유저의 userId
     * @param partyId 수정하고자 하는 파티원이 속한 파티의 partyId
     * @param participantUpdateRequest approvalStatus에 관련한 수정사항이 담긴 request
     * @return 수정 후 파티원 정보를 리턴
     * 파티원 정보 수정은 해당 파티의 host 만 가능하다.
     */
    public ParticipantUpdateResponse updateParticipant(Long partyId, Long id, ParticipantUpdateRequest participantUpdateRequest, String userId) {
        User user = validateUser(userId);
        Party party = validateParty(partyId);
        Participant participant = validateParticipant(id);
        checkParticipantAndParty(participant.getUser(),party);
        Timestamp createdAt = participant.getCreatedAt();
        validateHost(party,user);
        if(party.getPartyStatus().toString().equals("CLOSED")&& participantUpdateRequest.getApprovalStatus().equals("APPROVED")){
            throw new AppException(ErrorCode.PARTY_CLOSED,ErrorCode.PARTY_CLOSED.getMessage());
        }
        Participant updatedParticipant = participantRepository.save(participantUpdateRequest.toEntity(participant));

        String url = "/pinepeople/party/detail/"+partyId;

        if (participant.getApprovalStatus() == ApprovalStatus.APPROVED) { // 파티 승인
            notificationService.send(participant.getUser(),url, NotificationType.APPROVE_JOIN_PARTY,
                    "\""+party.getPartyTitle() + "\" "+ NotificationType.APPROVE_JOIN_PARTY.getMessage());
        } else if (participant.getApprovalStatus() == ApprovalStatus.REJECTED) { // 파티 거절
            notificationService.send(participant.getUser(),url,NotificationType.REJECT_JOIN_PARTY,
                    "\""+party.getPartyTitle() + "\" "+ NotificationType.REJECT_JOIN_PARTY.getMessage());
        }
        //파티 상태 변경(파티원 모집중인지, 마감되었는지)
        party.updateStatus(checkPartyStatus(partyId));
        partyRepository.save(party);
        return ParticipantUpdateResponse.of(createdAt,updatedParticipant);
    }

    /**
     * 파티 탈퇴
     * @param partyId 탈퇴하고자 하는 파티원이 속한 파티의 partyId
     * @return 삭제 후 해당 파티원 정보를 리턴
     */
    public ParticipantDeleteResponse deleteParticipant(Long partyId, String userId) {
        User user = validateUser(userId);
        Party party = validateParty(partyId);
        Participant participant = validateParticipant(party,user);
        participantRepository.delete(participant);

        //파티 상태 변경(파티원 모집중인지, 마감되었는지)
        party.updateStatus(checkPartyStatus(partyId));
        partyRepository.save(party);
        return ParticipantDeleteResponse.of(participant);

    }

    /**
     * 승인 완료된 파티원 인원수 조회
     * @param partyId 조회하고자 하는 파티의 id
     * @return approval status 가 approved인 파티원의 인원수 리턴
     */
    public PartyStatus checkPartyStatus(Long partyId){
        Party party = validateParty(partyId);
        Long count = participantRepository.countByApprovalStatus(ApprovalStatus.APPROVED.toString(),partyId);
        if(count.intValue()<party.getPartySize()){
            return PartyStatus.RECRUITING;
        }
        return PartyStatus.CLOSED;
    }
}
