package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.exception.UserPointException;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

    private final PointHistoryTable pointHistoryTable;
    private final UserPointTable userPointTable;

    @Override
    public UserPoint searchUserPoint(long userid) {
        return userPointTable.selectById(userid);
    }

    @Override
    public List<PointHistory> searchUserHistory(long userid) {
        return pointHistoryTable.selectAllByUserId(userid);
    }

    @Override
    public UserPoint chargeUserPoint(long userid, long amount) {

        long beforeUserPoint = userPointTable.selectById(userid).getPoint();
        long afterUserPoint = beforeUserPoint + amount;

        userPointTable.insertOrUpdate(userid, afterUserPoint);
        long updateTime = System.currentTimeMillis();
        pointHistoryTable.insert(userid, amount, TransactionType.CHARGE, updateTime);

        return userPointTable.selectById(userid);
    }

    @Override
    public UserPoint useUserPoint(long userid, long amount) {

        long beforeUserPoint = userPointTable.selectById(userid).getPoint();

        if (beforeUserPoint - amount >= 0) {
            long afterUserPoint = beforeUserPoint - amount;
            userPointTable.insertOrUpdate(userid, afterUserPoint);
            long updateTime = System.currentTimeMillis();
            pointHistoryTable.insert(userid, amount, TransactionType.USE, updateTime);
            return userPointTable.selectById(userid);
        } else {
            throw new UserPointException(500, "잔액이 부족합니다.");
        }

    }
}
