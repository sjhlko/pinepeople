package com.lion.pinepeople.service;

import com.lion.pinepeople.domain.dto.partyComment.PartyCommentResponse;
import com.lion.pinepeople.domain.entity.Party;
import com.lion.pinepeople.domain.entity.PartyComment;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.fixture.AllFixture;
import com.lion.pinepeople.fixture.PartyCommentFixture;
import com.lion.pinepeople.fixture.PartyFixture;
import com.lion.pinepeople.fixture.UserFixture;
import com.lion.pinepeople.repository.CategoryRepository;
import com.lion.pinepeople.repository.PartyCommentRepository;
import com.lion.pinepeople.repository.PartyRepository;
import com.lion.pinepeople.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PartyCommentServiceTest {
    @InjectMocks
    PartyCommentService PartyCommentService;
    @Mock
    UserRepository userRepository;
    @Mock
    PartyRepository partyRepository;
    @Mock
    PartyCommentRepository PartyCommentRepository;


    @Test
    @DisplayName("답변 등록 성공")
    void 답변_등록_성공() throws Exception {

        AllFixture all = AllFixture.getDto();
        User user = UserFixture.get(all.getEmail(), all.getPassword());
        Party party = PartyFixture.get(user);
        PartyComment comment = PartyCommentFixture.get(user, party);


        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(partyRepository.findById(any())).thenReturn(Optional.of(party));
        when(PartyCommentRepository.save(any())).thenReturn(comment);
        /*서비스 로직*/
        PartyCommentResponse response
                = PartyCommentService.addPartyComment(all.getPartyId(), String.valueOf(all.getUserId()), all.getBody());
        Assertions.assertThat(response.getPartyId()).isEqualTo(party.getId());

    }

}