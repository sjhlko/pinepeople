package com.lion.pinepeople.service;

import com.lion.pinepeople.repository.PartyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartyService {
    private final PartyRepository partyRepository;
}
