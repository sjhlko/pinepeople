package com.lion.pinepeople.controller;

import com.lion.pinepeople.domain.dto.party.*;
import com.lion.pinepeople.domain.dto.user.role.UserRoleResponse;
import com.lion.pinepeople.domain.response.Response;
import com.lion.pinepeople.service.PartyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class PartyController {
    private final PartyService partyService;

    /**
     * 파티 생성
     * 파티 생성시 category 와 participant 에도 자동적으로 해당하는 내용이 생성된다.
     * @return 생성된 파티의 상세 정보와 생성한 host 의 상세정보
     * **/
    @PostMapping
    @ApiOperation(value = "파티 생성")
    public Response<PartyCreateResponse> createParty(@RequestBody PartyCategoryRequest partyCreateRequest, Authentication authentication) {
        PartyCreateResponse partyCreateResponse = partyService.createPartyWithCategory(partyCreateRequest, authentication.getName());
        return Response.success(partyCreateResponse);
    }


    /**
     * 파티 id를 통해 파티를 상세 조회한다.
     * @param id 조회하고자 하는 파티의 id
     * @return 해당 파티의 상세 정보
     */
    @GetMapping ("/{id}")
    @ApiOperation(value = "파티 상세 조회")
    public Response<PartyInfoResponse> getParty(@PathVariable Long id){
        PartyInfoResponse partyInfoResponse = partyService.getParty(id);
        return Response.success(partyInfoResponse);
    }

    /**
     * 현재 존재하는 모든 파티를 조회한다.
     * @return 페이징된 모든 파티의 상세 정보
     */
    @GetMapping("")
    @ApiOperation(value = "파티 전체 조회")
    public Response<Page<PartyInfoResponse>> getAllParty(@PageableDefault(size = 20, sort ="createdAt",
            direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PartyInfoResponse> partyInfoResponses = partyService.getAllParty(pageable);
        return Response.success(partyInfoResponses);
    }

    /**
     * 파티 id를 통해 파티를 수정한다.
     * @param id 조회하고자 하는 파티의 id
     * @param authentication 로그인한 회원이면서 자신의 파티를 수정하는 경우만 가능
     * @return  수정 후 파티 상세정보
     */
    @PatchMapping("/{id}")
    @ApiOperation(value = "파티 수정")
    public Response<PartyUpdateResponse> modifyParty(@PathVariable Long id, @RequestBody PartyUpdateRequest partyUpdateRequest, Authentication authentication){
        PartyUpdateResponse partyUpdateResponse = partyService.updateParty(id, partyUpdateRequest, authentication.getName());
        return Response.success(partyUpdateResponse);

    }

    /**
     * 파티 id를 통해 파티를 삭제한다.
     * @param id 삭제하고자 하는 파티의 id
     * @param authentication 로그인한 회원중 관리자이거나 자기 자신의 파티를 삭제하는 경우만 가능
     * @return 삭제한 파티의 정보
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "파티 삭제")
    public Response<PartyDeleteResponse> deleteParty(@PathVariable Long id, Authentication authentication){
        PartyDeleteResponse partyDeleteResponse = partyService.deleteParty(id,authentication.getName());
        return Response.success(partyDeleteResponse);
    }

    /**
     * 특정 조건에 부합하는 파티를 검색한다.
     * @param address 해당 주소에 맞는 파티를 검색. null 일 경우 검색 조건에 포함되지 않음
     * @param partyTitle 해당 제목에 맞는 파티를 검색. null 일 경우 검색 조건에 포함되지 않음
     * @param partyContent 해당 내용에 맞는 파티를 검색. null 일 경우 검색 조건에 포함되지 않음
     * @return 검색조건에 부합하는 파티의 상세정보를 페이징 해서 리턴함
     */
    @GetMapping("/search")
    @ApiOperation(value = "파티 검색")
    public Response<Page<PartyInfoResponse>> searchParty(@PageableDefault(size = 20, sort ="createdAt",
            direction = Sort.Direction.DESC) Pageable pageable, @RequestParam String address, @RequestParam String partyContent, @RequestParam String partyTitle) {
        Page<PartyInfoResponse> parties = partyService.searchParty(pageable, address, partyContent, partyTitle);
        return Response.success(parties);
    }

    /**
     * 내가 속한 파티를 조회한다.
     * @param role 내가 속한 파티 중 해당 역할을 맡고 있는 파티만 조회함 (HOST/GUEST)
     * @param authentication 로그인된 유저만 이용 가능
     * @return 조회 조건에 부합하는 파티의 상세 정보들을 페이징 해서 리턴함
     */
    @GetMapping("/my")
    @ApiOperation(value = "내가 참가한 파티 조회")
    public Response<Page<PartyInfoResponse>> getMyParty(@PageableDefault(size = 20, sort ="createdAt",
            direction = Sort.Direction.DESC) Pageable pageable, @RequestParam String role, Authentication authentication) {
        Page<PartyInfoResponse> parties = partyService.getMyParty(pageable, role, authentication.getName());
        return Response.success(parties);
    }

    /**
     * 대기중인 파티를 조회한다.
     * @param authentication 로그인된 유저만 이용 가능
     * @return 대기중인 파티의 상세 정보들을 페이징 해서 리턴함
     */
    @GetMapping("/my-waitings")
    @ApiOperation(value = "대기중인 파티 조회")
    public Response<Page<PartyInfoResponse>> getMyWaitingParty(@PageableDefault(size = 20, sort ="createdAt",
            direction = Sort.Direction.DESC) Pageable pageable, Authentication authentication) {
        log.info("hello");
        Page<PartyInfoResponse> parties = partyService.getMyWaitingParty(pageable,authentication.getName());
        return Response.success(parties);
    }

}
