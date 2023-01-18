package com.lion.pinepeople.service;

import com.lion.pinepeople.domain.entity.Party;
import com.lion.pinepeople.domain.entity.PartyComment;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.domain.response.PartyCommentListResponse;
import com.lion.pinepeople.domain.response.PartyCommentResponse;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.customException.AppException;
import com.lion.pinepeople.repository.PartyCommentRepository;
import com.lion.pinepeople.repository.PartyRepository;
import com.lion.pinepeople.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PartyCommentService {

    private final PartyCommentRepository partyCommentRepository;
    private final UserRepository userRepository;
    private final PartyRepository partyRepository;



    public PartyCommentResponse addPartyComment(Long partyId, Long userId, String body) {
        //회원
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        //파티
        Party party = partyRepository.findById(partyId).orElseThrow(() -> new IllegalStateException());
        //파티 댓글
        PartyComment partyComment = PartyComment.builder()
                .body(body)
                .user(user)
                .party(party)
                .build();
        //파티 댓글 저장
        PartyComment savedPartyComment = partyCommentRepository.save(partyComment);
        return PartyCommentResponse.of(savedPartyComment);
    }

    public Page<PartyCommentListResponse> getComments(Long partyId, Pageable pageable) {
        Party party =
                partyRepository.findById(partyId).orElseThrow(() -> new AppException(ErrorCode.BRIX_NOT_FOUND));
        Page<PartyComment> partyComments = partyCommentRepository.findAllByParty(party, pageable);
        return PartyCommentListResponse.toResponse(partyComments);
    }



}
