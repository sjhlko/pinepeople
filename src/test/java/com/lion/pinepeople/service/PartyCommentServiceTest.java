//package com.lion.pinepeople.service;
//
//import com.lion.pinepeople.domain.dto.partyComment.PartyCommentResponse;
//import com.lion.pinepeople.domain.entity.Party;
//import com.lion.pinepeople.domain.entity.PartyComment;
//import com.lion.pinepeople.domain.entity.User;
//import com.lion.pinepeople.exception.ErrorCode;
//import com.lion.pinepeople.exception.customException.AppException;
//import com.lion.pinepeople.fixture.AllFixture;
//import com.lion.pinepeople.fixture.PartyCommentFixture;
//import com.lion.pinepeople.fixture.PartyFixture;
//import com.lion.pinepeople.fixture.UserFixture;
//import com.lion.pinepeople.repository.CategoryRepository;
//import com.lion.pinepeople.repository.PartyCommentRepository;
//import com.lion.pinepeople.repository.PartyRepository;
//import com.lion.pinepeople.repository.UserRepository;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.mockito.junit.jupiter.MockitoSettings;
//import org.mockito.quality.Strictness;
//import org.springframework.security.test.context.support.WithMockUser;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//@MockitoSettings(strictness = Strictness.LENIENT)
//class PartyCommentServiceTest {
//    @InjectMocks
//    PartyCommentService PartyCommentService;
//    @Mock
//    UserRepository userRepository;
//    @Mock
//    PartyRepository partyRepository;
//    @Mock
//    PartyCommentRepository PartyCommentRepository;
//
//
//    @Test
//    @DisplayName("답변 등록 성공")
//    void 답변_등록_성공() throws Exception {
//
//        AllFixture all = AllFixture.getDto();
//        User user = UserFixture.get(all.getEmail(), all.getPassword());
//        Party party = PartyFixture.get(user);
//        PartyComment comment = PartyCommentFixture.get(user, party);
//
//
//        when(userRepository.findById(any())).thenReturn(Optional.of(user));
//        when(partyRepository.findById(any())).thenReturn(Optional.of(party));
//        when(PartyCommentRepository.save(any())).thenReturn(comment);
//        /*서비스 로직*/
//        PartyCommentResponse response
//                = PartyCommentService.addPartyComment(all.getPartyId(), String.valueOf(all.getUserId()), all.getBody());
//
//        assertThat(response.getPartyId()).isEqualTo(party.getId());
//        assertThat(response.getBody()).isEqualTo(comment.getBody());
//        assertThat(response.getPartyCommentId()).isEqualTo(comment.getId());
//
//        assertDoesNotThrow(()->response);
//
//    }
//
//    @Test
//    @WithMockUser
//    @DisplayName("댓글 등록 실패 : 유저 존재 하지 않음")
//    void 댓글_등록_실패1() throws Exception {
//
//        AllFixture all = AllFixture.getDto();
//        User user = UserFixture.get(all.getEmail(), all.getPassword());
//        Party party = PartyFixture.get(user);
//        PartyComment comment = PartyCommentFixture.get(user, party);
//
//
//        /**회원이 존재하지 않는 상황 가정**/
//        when(userRepository.findById(any())).thenReturn(Optional.empty());
//        when(partyRepository.findById(any())).thenReturn(Optional.of(party));
//        when(PartyCommentRepository.save(any())).thenReturn(comment);
//        /*서비스 로직*/
//        AppException appException = assertThrows(AppException.class,
//                () -> PartyCommentService.addPartyComment(all.getPartyId(), String.valueOf(all.getUserId()), all.getBody()));
//
//        assertThat(appException.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
//
//    }
//
//
//    @Test
//    @WithMockUser
//    @DisplayName("댓글 등록 실패 : 파티 존재 하지 않음")
//    void 댓글_등록_실패2() throws Exception {
//
//        AllFixture all = AllFixture.getDto();
//        User user = UserFixture.get(all.getEmail(), all.getPassword());
//        Party party = PartyFixture.get(user);
//        PartyComment comment = PartyCommentFixture.get(user, party);
//
//
//        /**회원이 존재하지 않는 상황 가정**/
//        when(userRepository.findById(any())).thenReturn(Optional.of(user));
//        when(partyRepository.findById(any())).thenReturn(Optional.empty());
//        when(PartyCommentRepository.save(any())).thenReturn(comment);
//        /*서비스 로직*/
//        AppException appException = assertThrows(AppException.class,
//                () -> PartyCommentService.addPartyComment(all.getPartyId(), String.valueOf(all.getUserId()), all.getBody()));
//
//        assertThat(appException.getErrorCode()).isEqualTo(ErrorCode.PARTY_NOT_FOUND);
//
//    }
//}