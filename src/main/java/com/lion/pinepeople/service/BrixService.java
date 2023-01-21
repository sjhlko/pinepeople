package com.lion.pinepeople.service;

import com.lion.pinepeople.domain.dto.brix.BrixRequest;
import com.lion.pinepeople.domain.entity.Brix;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.enums.Star;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.customException.AppException;
import com.lion.pinepeople.repository.BrixRepository;
import com.lion.pinepeople.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BrixService {
    private final UserRepository userRepository;
    private final BrixRepository brixRepository;
    public final double BRIXDEFAULT = 14.0;

    /**
     *
     * @param request 별점
     * @param userId 당도평가 대상 유저 아이디
     * @param loginUserId 로그인 유저 아이디
     * @return 당도 측정 완료 메세지
     */
    public String calculationBrix(BrixRequest request, Long userId, String loginUserId) {
        //로그인 유저 검사
        userRepository.findById(Long.parseLong(loginUserId))
                .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage()));
        //별점을 줄 유저 확인
        User user = userRepository.findById(userId)
                .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage()));

        //해당 유저의 당도 연산 후 저장
        Brix brix = brixRepository.findByUser(user)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage()));
        Double brixNum = Star.starToBrix(request.getStar());
        brix.update(brixNum);
        brixRepository.save(brix);
        return "당도 측정 완료";
    }

    /**
     *
     * @param loginUserId 로그인 유저 아이디
     * @param userId 당도를 조회할 유저 아이디
     * @return 당도
     */
    public Double getBrix(String loginUserId, Long userId) {
        //로그인 유저 검사
        userRepository.findById(Long.parseLong(loginUserId))
                .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage()));
        //별점을 줄 유저 확인
        User user = userRepository.findById(userId)
                .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage()));
        //해당 유저의 당도 찾기
        Brix brix = brixRepository.findByUser(user)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage()));

        return brix.getBrixFigure();
    }

    public void setBrix(User user){
        //로그인 유저 검사
        userRepository.findById(user.getId())
                .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage()));
        //brix 초기값 생성
        Brix brix = Brix.toEntity(BRIXDEFAULT, null, user);
        brixRepository.save(brix);
    }
}
