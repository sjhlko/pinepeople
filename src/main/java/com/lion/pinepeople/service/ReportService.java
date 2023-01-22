package com.lion.pinepeople.service;

import com.lion.pinepeople.domain.entity.BlackList;
import com.lion.pinepeople.domain.entity.Report;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.customException.AppException;
import com.lion.pinepeople.repository.BlackListRepository;
import com.lion.pinepeople.repository.ReportRepository;
import com.lion.pinepeople.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {
    private final UserRepository userRepository;
    private final ReportRepository reportRepository;
    private final BlackListRepository blackListRepository;


    /**
     * 유저 신고하기
     * @param loginUserId 신고한 유저
     * @param userId 신고할 타겟 유저
     * @return 신고 성공 여부
     */
    public String addReport(Long loginUserId, Long userId) {
        //로그인 유저 검사
        userRepository.findById(loginUserId)
                .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage()));
        //신고할 유저 확인
        User targetUser = userRepository.findById(userId)
                .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage()));

        // 신고 중복 확인 후 저장
        Optional<Report> confirmReport = reportRepository.findByFromUserIdAndUser(loginUserId, targetUser);
        if(confirmReport.isPresent()){
            throw new AppException(ErrorCode.DUPLICATED_REPORT, ErrorCode.DUPLICATED_REPORT.getMessage());
        }
        Report report = Report.toEntity(loginUserId, targetUser);
        reportRepository.save(report);

        // 신고당한사람의 정보를 플러스
        targetUser.updateWarningCnt();
        userRepository.save(targetUser);

        //3이상이면 블랙리스트처리
        if (targetUser.getWarningCnt() >= 3){
            BlackList blackList = BlackList.toEntity(LocalDateTime.now(), targetUser);
            blackListRepository.save(blackList);
//            userRepository.delete(user); softdelete적용후 넣기
        }

        return "신고가 정상적으로 접수되었습니다.";
    }
}
