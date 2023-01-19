package com.lion.pinepeople.service;

import com.lion.pinepeople.domain.dto.party.PartyCategoryRequest;
import com.lion.pinepeople.domain.dto.party.PartyCreateRequest;
import com.lion.pinepeople.domain.dto.party.PartyCreateResponse;
import com.lion.pinepeople.domain.entity.Category;
import com.lion.pinepeople.domain.entity.Participant;
import com.lion.pinepeople.domain.entity.Party;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.customException.AppException;
import com.lion.pinepeople.repository.CategoryRepository;
import com.lion.pinepeople.repository.PartyRepository;
import com.lion.pinepeople.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@RequiredArgsConstructor
public class PartyService {
    private final PartyRepository partyRepository;
    private final UserRepository userRepository;
    private final ParticipantService participantService;
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;


    /**origin 파티 생성**/
    public PartyCreateResponse createParty(PartyCreateRequest partyCreateRequest, String userId) {
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage()));
        Party party = partyRepository.save(partyCreateRequest.toEntity());
        Participant participant = participantService.createHostParticipant(user,party);
        return PartyCreateResponse.of(party,participant);
    }

    /**파티 생성시 카테고리 선택 추가**/
    public PartyCreateResponse createPartyWithCategory(String branch, String code, PartyCategoryRequest request, String userId) {
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage()));
        //카테고리 생성
        Category category = categoryRepository.findByBranchAndCode(branch, code).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        Party party = partyRepository.save(request.toEntity(category));
        Participant participant = participantService.createHostParticipant(user,party);
        return PartyCreateResponse.of(party,participant);
    }
}
