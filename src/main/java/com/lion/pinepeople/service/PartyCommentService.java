package com.lion.pinepeople.service;

import com.lion.pinepeople.domain.entity.Party;
import com.lion.pinepeople.domain.entity.PartyComment;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.domain.dto.partyComment.PartyCommentDeleteResponse;
import com.lion.pinepeople.domain.dto.partyComment.PartyCommentListResponse;
import com.lion.pinepeople.domain.dto.partyComment.PartyCommentResponse;
import com.lion.pinepeople.domain.dto.partyComment.PartyCommentUpdateResponse;
import com.lion.pinepeople.enums.NotificationType;
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

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PartyCommentService {

    private final PartyCommentRepository partyCommentRepository;
    private final UserRepository userRepository;
    private final PartyRepository partyRepository;
    private final NotificationService notificationService;


    /**
     * 파티모집글에 댓글 쓰기
     *
     * @param partyId 파티 조회 목적
     * @param userId 로그인 유저 id
     * @param body 댓글 내용
     *
     * @return PartyCommentResponse 응답
     */
    public PartyCommentResponse addPartyComment(Long partyId, String userId, String body) {
        log.info("파티 댓글 들어옴");
        //회원
        User user = getUser(userId);
        //파티
        Party party = partyRepository.findById(partyId).orElseThrow(() -> new AppException(ErrorCode.PARTY_NOT_FOUND));
        //파티 댓글
        PartyComment partyComment = PartyComment.builder()
                .body(body)
                .user(user)
                .party(party)
                .build();
        //파티 댓글 저장
        log.info(partyComment.toString());
        PartyComment savedPartyComment = partyCommentRepository.save(partyComment);
        if (party.getUser().getId() != user.getId()) {
            String url ="/pinepeople/party/show-comment/" + party.getId();
            notificationService.send(party.getUser(), url, NotificationType.COMMENT_ON_PARTY,
                    user.getName() + "님이 회원님의 \"" + party.getPartyTitle()+"\" "+ NotificationType.COMMENT_ON_PARTY.getMessage());
        }
        return PartyCommentResponse.of(savedPartyComment);
    }

    private User getUser(String userId) {
        return userRepository.findById(Long.parseLong(userId)).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    /**
     * 파티모집글에 댓글 페이징 조회
     *
     * @param partyId 파티 조회 목적
     * @param pageable 페이징
     * @return Page<PartyCommentListResponse> 응답
     */
    public Page<PartyCommentListResponse> getComments(Long partyId, Pageable pageable) {
        Party party = getParty(partyId, ErrorCode.BRIX_NOT_FOUND);
        Page<PartyComment> partyComments = partyCommentRepository.findAllByParty(party, pageable);
        return PartyCommentListResponse.toResponse(partyComments);
    }

    public List<PartyComment> getCommentList(Long partyId) {
        Party party = getParty(partyId, ErrorCode.BRIX_NOT_FOUND);
        List<PartyComment> partyComments = partyCommentRepository.findListByParty(party);
        return partyComments;
    }


    /**
     * 파티모집글에 댓글 수정
     *
     * @param partyId 파티 조회 목적
     * @param commentId 파티 댓글 조회 목적
     * @param body 파티 댓글 수정 request
     * @return PartyCommentUpdateResponse 응답
     */
    public PartyCommentUpdateResponse updateComment(Long partyId, Long commentId,String body ,String userId) {
        //회원
        User user = getUser(userId);
        //파티
        Party party = getParty(partyId, ErrorCode.PARTY_NOT_FOUND);
        //comment
        PartyComment partyComment = getPartyComment(commentId);
        if (user.getId() != partyComment.getUser().getId()) {
            throw new AppException(ErrorCode.INVALID_PERMISSION);
        }
        //comment 수정
        partyComment.update(body);
        return PartyCommentUpdateResponse.of(commentId);
    }

    private Party getParty(Long partyId, ErrorCode partyNotFound) {
        return partyRepository.findById(partyId).orElseThrow(() -> new AppException(partyNotFound));
    }

    /**
     * 파티모집글에 댓글 삭제
     *
     * @param partyId 파티 조회 목적
     * @param commentId 파티 댓글 조회 목적
     * @return PartyCommentDeleteResponse 응답
     */
    public PartyCommentDeleteResponse deleteComment(Long partyId, Long commentId, String userId) {
        //회원
        User user = getUser(userId);
        //파티
        Party party = getParty(partyId, ErrorCode.PARTY_NOT_FOUND);
        //comment
        PartyComment partyComment = getPartyComment(commentId);
        if (user.getId() != partyComment.getUser().getId()) {
            throw new AppException(ErrorCode.INVALID_PERMISSION);
        }
        //삭제 진행
        partyCommentRepository.deleteById(partyComment.getId());
        return PartyCommentDeleteResponse.of(commentId);
    }

    private PartyComment getPartyComment(Long commentId) {
        return partyCommentRepository.findById(commentId).orElseThrow(() -> new AppException(ErrorCode.PARTY_COMMENT_NOT_FOUND));
    }
}
