package com.lion.pinepeople.service;

import com.lion.pinepeople.domain.dto.admin.AdminUpdateRequest;
import com.lion.pinepeople.domain.dto.admin.AllBlackListResponse;
import com.lion.pinepeople.domain.dto.admin.BlackListRequest;
import com.lion.pinepeople.domain.dto.admin.BlackListResponse;
import com.lion.pinepeople.domain.dto.user.role.UserRoleResponse;
import com.lion.pinepeople.domain.entity.BlackList;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.enums.BlackListStatus;
import com.lion.pinepeople.enums.UserRole;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.customException.AppException;
import com.lion.pinepeople.repository.BlackListRepository;
import com.lion.pinepeople.repository.ReportRepository;
import com.lion.pinepeople.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdminService {
    private final BlackListRepository blackListRepository;
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    /**
     * 계정 등급 변경 메서드
     *
     * @param id     변경할 userId
     * @param userId 변경하는 userId
     * @return UserRoleResponse userName, message
     */
    public UserRoleResponse changeRole(String userId, Long id) {
        isAdmin(userId);

        //changeRole 대상 유저 체크
        User changeUser = userRepository.findById(id).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, "계정 등급을 변경할 유저를 찾을 수 없습니다.");
        });
        //changeRole 대상 유저 role 체크
        if (changeUser.getRole().equals(UserRole.ADMIN)) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, "해당 계정은 ADMIN 계정입니다.");
        }
        //changeRole 대상 유저 role 수정 및 업데이트
        changeUser.updateRole(UserRole.ADMIN);
        userRepository.saveAndFlush(changeUser);
        //UserRoleResponse DTO 반환
        return UserRoleResponse.of("관리자로 권한이 변경되었습니다.", changeUser.getName());
    }

    /**
     * 블랙리스트 추가
     * @param request 블랙리스트에 넣을 유저 아이디
     * @param loginUserId 로그인한 유저 아이디
     * @return 등록 성공 메세지
     */
    public String addBlackList(BlackListRequest request, String loginUserId) {
        isAdmin(loginUserId);

        //블랙리스트에 넣을 유저 확인
        User targetUser = userRepository.findById(request.getUserId()).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_ROLE_NOT_FOUND.getMessage());
        });

        BlackList blackList = BlackList.toEntity(LocalDateTime.now(),targetUser, BlackListStatus.APPROVAL);
        blackListRepository.save(blackList);
        return "블랙리스트 등록 완료하였습니다";
    }

    /**
     * 블랙리스트 상태 변경
     * @param loginUserId 로그인한 유저 아이디
     * @param blackListId 상태변경할 블랙리스트 아이디
     * @return 변경 성공 메세지
     */
    public String updateBlackList(String loginUserId, Long blackListId, AdminUpdateRequest request) {
        isAdmin(loginUserId);

        BlackList blackList = blackListRepository.findById(blackListId).orElseThrow(() -> {
            throw new AppException(ErrorCode.BLACKLIST_NOT_FOUND, ErrorCode.BLACKLIST_NOT_FOUND.getMessage());
        });

        if(request.getStatus() == 1){
            blackList.updateStatus(BlackListStatus.APPROVAL);
            blackListRepository.save(blackList);
            return "승인하였습니다.";
        }
        blackListRepository.deleteById(blackListId);
        return "반려하였습니다.";

    }

    /**
     * 블랙리스트 삭제 메서드
     * @param blackListId 삭제할 블랙리스트 아이디
     * @param loginUserId 로그인한 유저 아이디
     * @return 삭제 성공 메세지
     */
    public String deleteBlackList(Long blackListId, String loginUserId) {
        isAdmin(loginUserId);

        //블랙리스트에서 삭제할 유저 확인
        blackListRepository.findById(blackListId).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_ROLE_NOT_FOUND, ErrorCode.USER_ROLE_NOT_FOUND.getMessage());
        });

        blackListRepository.deleteById(blackListId);
        return "블랙리스트에서 삭제 완료 하였습니다.";
    }

    /**
     * 블랙리스트 상세 조회
     * @param blackListId 조회할 블랙리스트 아이디
     * @param loginUserId 로그인한 유저 아이디
     * @return 블랙리스트 아이디, 시작 시간, 신고한사람들
     */
    public BlackListResponse getBlackList(Long blackListId, String loginUserId) {
        isAdmin(loginUserId);

        //블랙리스트 조회
        BlackList blackList = blackListRepository.findById(blackListId).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_ROLE_NOT_FOUND.getMessage());
        });

        //신고자들 조회
        List<Long> fromUserIdList =  reportRepository.findAllByUser(blackList.getUser()).stream()
                        .map(r -> r.getFromUserId())
                                .collect(Collectors.toList());

        List<String> fromUserEmail = new ArrayList<>();
        //신고내역이 없을 경우 admin으로 판단
        if(fromUserIdList.size() == 0){
            fromUserEmail.add("ADMIN");
        }else{
            //신고자들 아이디를 이메일로 변경
            for (Long fromUserId : fromUserIdList) {
                fromUserEmail.add(userRepository.findById(fromUserId).get().getEmail());
            }
        }

        //report 신고명 반환
        List<String> reportContent = reportRepository.findAllByUser(blackList.getUser()).stream()
                .map(b -> b.getReportContent()).collect(Collectors.toList());

        return BlackListResponse.fromEntity(blackList, fromUserEmail, reportContent);
    }

    /**
     * 블랙리스트 전체 조회
     * @param loginUserId 로그인한 유저 아이디
     * @param pageable 페이징
     * @return 블랙리스트 아이디, 정지 시작시간
     */
    public Page<AllBlackListResponse> getAllBlackList(String loginUserId, PageRequest pageable) {
        isAdmin(loginUserId);

        //블랙리스트 전체 조회
        Page<AllBlackListResponse> allBlackList = blackListRepository.findAll(pageable)
                .map(blackList -> AllBlackListResponse.fromEntity(blackList));
        return allBlackList;
    }

    /**
     * 관리자 확인 로직
     * @param loginUserId 로그인한 유저 아이디
     * @return
     */
    public void isAdmin(String loginUserId){
        //유저 확인
        User user = userRepository.findById(Long.parseLong(loginUserId)).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_ROLE_NOT_FOUND.getMessage());
        });

        //관리자 확인
        if(user.getRole() != UserRole.ADMIN){
            throw new AppException(ErrorCode.INVALID_PERMISSION, "접근 권한이 없습니다.");
        }
    }


}
