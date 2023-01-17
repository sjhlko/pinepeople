package com.lion.pinepeople.service;

import com.lion.pinepeople.repository.PartyCommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PartyCommentService {

    private final PartyCommentRepository partyCommentRepository;


}
