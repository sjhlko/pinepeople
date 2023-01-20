package com.lion.pinepeople.service;

import com.lion.pinepeople.repository.CategoryRepository;
import com.lion.pinepeople.repository.PartyRepository;
import com.lion.pinepeople.repository.UserRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PartyCommentServiceTest {
    @InjectMocks
    PartyCommentService PartyCommentService;
    @Mock
    UserRepository userRepository;
    @Mock
    PartyRepository partyRepository;
}