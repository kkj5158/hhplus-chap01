package io.hhplus.tdd.point;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PointService {

    // 1.long - id를 받아서 유저의 포인트를 UserPoing 형태로 반환

    public UserPoint searchUserPoint(long userid);

    // 2. long - id를 받아서 유저의 포인트 히스토리를 리스트로 반환 하는 기능

    public List<PointHistory> searchUserHistory(long userid);

    // 3.long - id, amount 를 받아서 유저의 포인트를 충전하는 기능 , 충천 후 userPoint 반환한다.

    public UserPoint chargeUserPoint(long userid, long amount);

    // 4. id , amount 를 방아서 유저포인트 사용 , 사용 후 남은 user point 반환한다.

    public UserPoint useUserPoint(long userid, long amount);




}
