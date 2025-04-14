package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

    private final PointHistoryTable pointHistoryTable;
    private final UserPointTable userPointTable;

    @Override
    public UserPoint searchUserPoint(long userid) {
        return null;
    }

    @Override
    public List<PointHistory> searchUserHistory(long userid) {
        return null;
    }

    @Override
    public UserPoint chargeUserPoint(long userid, long amount) {
        return null;
    }

    @Override
    public UserPoint useUserPoint(long userid, long amount) {
        return null;
    }
}
