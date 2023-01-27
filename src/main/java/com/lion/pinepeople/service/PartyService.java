package com.lion.pinepeople.service;

import com.lion.pinepeople.domain.dto.party.*;
import com.lion.pinepeople.domain.entity.*;
import com.lion.pinepeople.enums.ParticipantRole;
import com.lion.pinepeople.enums.UserRole;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.customException.AppException;
import com.lion.pinepeople.repository.CategoryRepository;
import com.lion.pinepeople.repository.PartyRepository;
import com.lion.pinepeople.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PartyService {
    private final PartyRepository partyRepository;
    private final UserRepository userRepository;
    private final ParticipantService participantService;
    private final CategoryRepository categoryRepository;

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
        if(!Objects.equals(party.getUser().getId(), currentUser.getId())){
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
     * 새로운 파티 등록
     * @param request 등록할 파티의 정보가 저장된 request
     * @param userId 파티를 등록할 host 의 userId
     * 파티 생성시 해당 파티와 유저의 정보가 participant 테이블에도 저장됨
     * @return 생성된 파티와 파티의 host 정보를 리턴
     */
    public PartyCreateResponse createPartyWithCategory(PartyCategoryRequest request, String userId) {
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage()));
        //카테고리 생성
        Category category = categoryRepository.findByBranchAndCode(request.getBranch(), request.getCode()).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        Party party = partyRepository.save(request.toEntity(category,user));
        Participant participant = participantService.createHostParticipant(user,party);
        return PartyCreateResponse.of(party,participant);
    }

    /**
     * 파티 id 를 통해 파티를 검색 후 리턴
     * @param partyId 상세정보를 검색할 파티의 id
     * @return 해당 파티 정보를 리턴
     */
    public PartyInfoResponse getParty(Long partyId) {
        Party party = validateParty(partyId);
        return PartyInfoResponse.of(party);
    }

    /**
     * 존재하는 모든 파티의 정보를 조회
     * @return 모든 파티의 상세 정보를 페이징 해서 리턴
     */
    public Page<PartyInfoResponse> getAllParty(Pageable pageable) {
        Page<Party> parties = partyRepository.findAll(pageable);
        return parties.map(PartyInfoResponse::of);
    }

    /**
     * 파티 정보 수정
     * @param userId 현재 로그인된 유저의 userId
     * @param partyId 수정하고자 하는 파티의 partyId
     * @param partyUpdateRequest 수정사항이 담긴 request
     * @return 수정 전 파티정보와 수정 후 파티 정보를 리턴
     * 파티 정보 수정은 해당 파티의 host 만 가능하다.
     */
    public PartyUpdateResponse updateParty(Long partyId, PartyUpdateRequest partyUpdateRequest, String userId) {
        User user = validateUser(userId);
        Party party = validateParty(partyId);
        Timestamp createdAt = party.getCreatedAt();
        validateHost(party,user);
        Party updatedParty = partyRepository.save(partyUpdateRequest.toEntity(party));
        return PartyUpdateResponse.of(createdAt,updatedParty);
    }

    /**
     * 파티 삭제
     * @param userId 현재 로그인된 유저의 userId
     * @param partyId 삭제하고자 하는 파티의 partyId
     * @return 삭제한 파티 정보를 리턴
     * 파티 삭제는 해당 파티의 host 나 관리자만 가능하다.
     */
    public PartyDeleteResponse deleteParty(Long partyId, String userId) {
        User user = validateUser(userId);
        Party party = validateParty(partyId);
        validateHostOrAdmin(party,user);
        partyRepository.delete(party);
        return PartyDeleteResponse.of(party);
    }

    /**
     * 파티 검색 (주소, 파티 이름, 파티 내용 등으로 검색이 가능하며 여러개의 조건을 포함하여 검색하는것도 가능하다
     *
     * ex)http://localhost:8787/api/partys/search?partyTitle=수정&address=서울&partyContent
     * 위와같이 요청시 파티 제목에 '수정'이 들어가고, 주소에 '서울'이 들어가며 파티 내용은 무슨 내용이어도 상관없게 설정하여 파티를 검색함
     *
     * @param address 주소로 검색 시 검색하고자 하는 주소의 내용
     * @param partyContent 파티 내용으로 검색 시 검색하고자 하는 파티 내용
     * @param partyTitle 파티 이름으로 검색 시 검색하고자 하는 파티의 이름
     * @return 검색 조건에 부합하는 파티의 상세 정보를 페이징해 리턴
     */
    public Page<PartyInfoResponse> searchParty(Pageable pageable, String address, String partyContent, String partyTitle) {
        Page<Party> parties = partyRepository.findBySearchOption(pageable,address,partyContent,partyTitle);
        return parties.map(PartyInfoResponse::of);
    }

    /**
     * 내가 속한 파티 조회(HOST/GUEST 로 나누어 조회함)
     * @param role 내가 속한 파티 중 해당 role 의 자격으로 속한 파티만 조회함
     * @param userId 현재 로그인한 사용자의 id
     * @return 조건에 부합하는 파티의 상세 정보를 페이징해 리턴
     */
    public Page<PartyInfoResponse> getMyParty(Pageable pageable, String role, String userId) {
        User user = validateUser(userId);
        if(role.equals(ParticipantRole.HOST.name())){
            Page<Party> parties = partyRepository.findAllByUser(pageable,user);
            return parties.map(PartyInfoResponse::of);
        }
        else {
            Page<Participant> participants = participantService.getMyGuestParty(pageable,user);
            return participants.map(PartyInfoResponse::of);
        }
    }
}
