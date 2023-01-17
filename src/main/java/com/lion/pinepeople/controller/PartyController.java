package com.lion.pinepeople.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Party API")
@RestController
@RequestMapping("/api/partys")
@RequiredArgsConstructor
public class PartyController {
}
