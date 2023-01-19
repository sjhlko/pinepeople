package com.lion.pinepeople.service;

import com.lion.pinepeople.domain.dto.party.*;
import com.lion.pinepeople.domain.entity.*;
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
import org.springframework.web.bind.annotation.PathVariable;

@Service
@RequiredArgsConstructor
public class PartyService {
    private final PartyRepository partyRepository;
    private final UserRepository userRepository;
    private final ParticipantService participantService;
    private final CategoryRepository categoryRepository;

    public User validateUser(String userId){
        return userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage()));
    }

    public Party validateParty(Long partyId){
        return partyRepository.findById(partyId)
                .orElseThrow(() -> new AppException(ErrorCode.PARTY_NOT_FOUND, ErrorCode.PARTY_NOT_FOUND.getMessage()));
    }

    public void validateAuthor(Party party, User currentUser){
        if(party.getUser().equals(currentUser)){
            throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        }
    }

    public void validateAuthorOrAdmin(Party party, User currentUser){
        if(!(party.getUser().equals(currentUser)||currentUser.getRole().equals(UserRole.ADMIN))){
            throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        }
    }


    /**파티 생성시 카테고리 선택 추가**/
    public PartyCreateResponse createPartyWithCategory(PartyCategoryRequest request, String userId) {
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage()));
        //카테고리 생성
        Category category = categoryRepository.findByBranchAndCode(request.getBranch(), request.getCode()).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        Party party = partyRepository.save(request.toEntity(category,user));
        Participant participant = participantService.createHostParticipant(user,party);
        return PartyCreateResponse.of(party,participant);
    }


    public PartyInfoResponse getParty(Long partyId) {
        Party party = validateParty(partyId);
        return PartyInfoResponse.of(party);
    }

    public Page<PartyInfoResponse> getAllParty(Pageable pageable) {
        Page<Party> parties = partyRepository.findAll(pageable);
        return parties.map(PartyInfoResponse::of);
    }

    public PartyUpdateResponse updateParty(Long partyId, PartyUpdateRequest partyUpdateRequest, String userId) {
        User user = validateUser(userId);
        Party party = validateParty(partyId);
        validateAuthor(party,user);
        Party updatedParty = partyRepository.save(partyUpdateRequest.toEntity(party));
        return PartyUpdateResponse.of(party,updatedParty);
    }

    public PartyDeleteResponse deleteParty(Long partyId, String userId) {
        User user = validateUser(userId);
        Party party = validateParty(partyId);
        validateAuthorOrAdmin(party,user);
        partyRepository.delete(party);
        return PartyDeleteResponse.of(party);
    }

    public Page<PartyInfoResponse> searchParty(Pageable pageable, String address, String partyContent, String partyTitle) {
        Page<Party> parties = partyRepository.findBySearchOption(pageable,address,partyContent,partyTitle);
        return parties.map(PartyInfoResponse::of);
    }
}
