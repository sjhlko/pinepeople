package com.lion.pinepeople.service;

import com.lion.pinepeople.domain.dto.party.*;
import com.lion.pinepeople.domain.entity.Participant;
import com.lion.pinepeople.domain.entity.Party;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.customException.AppException;
import com.lion.pinepeople.repository.PartyRepository;
import com.lion.pinepeople.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartyService {
    private final PartyRepository partyRepository;
    private final UserRepository userRepository;
    private final ParticipantService participantService;
    public User validateUser(String userId){
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage()));
        return user;
    }

    public Party validateParty(Long partyId){
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new AppException(ErrorCode.PARTY_NOT_FOUND, ErrorCode.PARTY_NOT_FOUND.getMessage()));
        return party;
    }

    public PartyCreateResponse createParty(PartyCreateRequest partyCreateRequest, String userId) {
        User user = validateUser(userId);
        Party party = partyRepository.save(partyCreateRequest.toEntity(user));
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
}
