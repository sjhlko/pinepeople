package com.lion.pinepeople.controller;

import com.lion.pinepeople.domain.dto.party.*;
import com.lion.pinepeople.domain.dto.user.role.UserRoleResponse;
import com.lion.pinepeople.domain.response.Response;
import com.lion.pinepeople.service.PartyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Party API")
@RestController
@RequestMapping("/api/partys")
@RequiredArgsConstructor
public class PartyController {
    private final PartyService partyService;
    @PostMapping("")
    @ApiOperation(value = "파티 생성")
    public Response<PartyCreateResponse> createParty(@RequestBody PartyCreateRequest partyCreateRequest, Authentication authentication) {
        PartyCreateResponse partyCreateResponse = partyService.createParty(partyCreateRequest, authentication.getName());
        return Response.success(partyCreateResponse);
    }

    /**
     * 기존의 파티생성 시 카테고리 선택추가
     *
     * @param branch 대분류 카테고리 이름
     * @param code 소분류 카테고리 코드
     * **/
    @PostMapping("/{branch}/{code}")
    @ApiOperation(value = "파티 생성 카테고리 선택 추가")
    public Response<PartyCreateResponse> createPartyWithCategory(@PathVariable String branch, @PathVariable String code, @RequestBody PartyCategoryRequest partyCreateRequest, Authentication authentication) {
        PartyCreateResponse partyCreateResponse = partyService.createPartyWithCategory(branch,code, partyCreateRequest, authentication.getName());
        return Response.success(partyCreateResponse);
    }


    @GetMapping ("/{id}")
    @ApiOperation(value = "파티 상세 조회")
    public Response<PartyInfoResponse> getParty(@PathVariable Long id){
        PartyInfoResponse partyInfoResponse = partyService.getParty(id);
        return Response.success(partyInfoResponse);
    }

    @GetMapping("")
    @ApiOperation(value = "파티 전체 조회")
    public Response<Page<PartyInfoResponse>> getAllParty(@PageableDefault(size = 20, sort ="createdAt",
            direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PartyInfoResponse> partyInfoResponses = partyService.getAllParty(pageable);
        return Response.success(partyInfoResponses);
    }

    @PatchMapping("/{id}")
    @ApiOperation(value = "파티 수정")
    public Response<PartyUpdateResponse> modifyPost(@PathVariable Long id, @RequestBody PartyUpdateRequest partyUpdateRequest, Authentication authentication){
        PartyUpdateResponse partyUpdateResponse = partyService.updateParty(id, partyUpdateRequest, authentication.getName());
        return Response.success(partyUpdateResponse);

    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "파티 삭제")
    public Response<PartyDeleteResponse> deleteParty(@PathVariable Long id, Authentication authentication){
        PartyDeleteResponse partyDeleteResponse = partyService.deleteParty(id,authentication.getName());
        return Response.success(partyDeleteResponse);
    }

}
