package com.lion.pinepeople.service;

import com.lion.pinepeople.domain.dto.admin.AllBlackListResponse;
import com.lion.pinepeople.domain.dto.admin.BlackListRequest;
import com.lion.pinepeople.domain.dto.admin.BlackListResponse;
import com.lion.pinepeople.domain.entity.BlackList;
import com.lion.pinepeople.domain.entity.Report;
import com.lion.pinepeople.domain.entity.User;
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
     * 블랙리스트 추가
     * @param request 블랙리스트에 넣을 유저 아이디
     * @param loginUserId 로그인한 유저 아이디
     * @return 등록 성공 메세지
     */
    public String addBlackList(BlackListRequest request, String loginUserId) {
        //유저 확인
        User user = userRepository.findById(Long.parseLong(loginUserId)).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_ROLE_NOT_FOUND.getMessage());
        });

        //관리자 확인
        if(user.getRole() != UserRole.ADMIN){
            throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_TOKEN.getMessage());
        }

        //블랙리스트에 넣을 유저 확인
        User targetUser = userRepository.findById(request.getUserId()).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_ROLE_NOT_FOUND.getMessage());
        });

        BlackList blackList = BlackList.toEntity(LocalDateTime.now(),targetUser);
        blackListRepository.save(blackList);
        return "블랙리스트 등록 완료하였습니다";
    }

    /**
     * 블랙리스트 삭제 메서드
     * @param userId 블랙리스트에서 삭제할 유저 아이디
     * @param loginUserId 로그인한 유저 아이디
     * @return 삭제 성공 메세지
     */
    public String deleteBlackList(Long userId, String loginUserId) {
        //유저 확인
        User user = userRepository.findById(Long.parseLong(loginUserId)).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_ROLE_NOT_FOUND.getMessage());
        });

        //관리자 확인
        if(user.getRole() != UserRole.ADMIN){
            throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_TOKEN.getMessage());
        }

        //블랙리스트에서 삭제할 유저 확인
        userRepository.findById(userId).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_ROLE_NOT_FOUND.getMessage());
        });

        blackListRepository.deleteById(userId);
        return "블랙리스트에서 삭제 완료 하였습니다.";
    }

    /**
     * 블랙리스트 상세 조회
     * @param userId 조회할 유저 아이디
     * @param loginUserId 로그인한 유저 아이디
     * @return 블랙리스트 아이디, 시작 시간, 신고한사람들
     */
    public BlackListResponse getBlackList(Long userId, String loginUserId) {
        //유저 확인
        User user = userRepository.findById(Long.parseLong(loginUserId)).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_ROLE_NOT_FOUND.getMessage());
        });

        //관리자 확인
        if(user.getRole() != UserRole.ADMIN){
            throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_TOKEN.getMessage());
        }

        //블랙리스트에서 조회할 유저 확인
        User targetUser = userRepository.findById(userId).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_ROLE_NOT_FOUND.getMessage());
        });
        log.info("here1");
        //블랙리스트 조회
        BlackList blackList = blackListRepository.findByUser(targetUser);
        log.info("here2");
        //신고자들 조회
        List<Long> fromUserIdList =  reportRepository.findAllByUser(targetUser).stream()
                        .map(r -> r.getFromUserId())
                                .collect(Collectors.toList());

        //신고자들 아이디로 이메일로 변경해서 반환
        List<String> fromUserEmail = new ArrayList<>();
        if(fromUserIdList.size() == 0){
            fromUserEmail.add("ADMIN");
        }else{
            for (Long fromUserId : fromUserIdList) {
                fromUserEmail.add(userRepository.findById(fromUserId).get().getEmail());
            }
        }

        log.info("fromUserId{}", fromUserEmail);
        return BlackListResponse.fromEntity(blackList, fromUserEmail);
    }

    /**
     * 블랙리스트 전체 조회
     * @param loginUserId 로그인한 유저 아이디
     * @param pageable 페이징
     * @return 블랙리스트 아이디, 정지 시작시간
     */
    public Page<AllBlackListResponse> getAllBlackList(String loginUserId, PageRequest pageable) {
        //유저 확인
        User user = userRepository.findById(Long.parseLong(loginUserId)).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_ROLE_NOT_FOUND.getMessage());
        });

        //관리자 확인
        if(user.getRole() != UserRole.ADMIN){
            throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_TOKEN.getMessage());
        }

        //블랙리스트 전체 조회
        Page<AllBlackListResponse> allBlackList = blackListRepository.findAll(pageable)
                .map(blackList -> AllBlackListResponse.fromEntity(blackList));
        return allBlackList;
    }
}
