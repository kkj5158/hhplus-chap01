package io.hhplus.tdd.unitTest;


import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.exception.UserPointException;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.PointServiceImpl;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PointServiceTests {
    @Mock
    private PointHistoryTable pointHistoryTable;

    @Mock
    private UserPointTable userPointTable;

    @InjectMocks
    private PointServiceImpl pointService;

    @Test
    void searchUserPointTests_성공() {

        // given
        UserPoint expectedUserPoint = new UserPoint(1L, 2000L);
        when(userPointTable.selectById(1L)).thenReturn(new UserPoint(1L, 2000L));

        // when
        UserPoint actualUserPoint = pointService.searchUserPoint(1L);

        // then
        assertThat(actualUserPoint)
                .as("UserPoint 조회 결과가 예상과 일치해야합니다.")
                .isEqualTo(expectedUserPoint);

    }

    @Test
    void searchUserPointTests_없는id_성공() {

        // given
        UserPoint expectedEmptyUserPoint = UserPoint.empty(1L);
        when(userPointTable.selectById(1L)).thenReturn(expectedEmptyUserPoint);

        // when
        UserPoint actualUserPoint = pointService.searchUserPoint(1L);

        // then
        assertThat(actualUserPoint)
                .as("id가 없을 경우에는 기본값으로 설정된 값을 반환받습니다.")
                .isEqualTo(expectedEmptyUserPoint);

    }


    @Test
    void searchUserHistory_성공() {

        //given
        List<PointHistory> expectedPointHistories = new ArrayList<>();

        PointHistory pointHistory1 = new PointHistory(1L, 1L, 100L, TransactionType.USE, 1L);
        PointHistory pointHistory2 = new PointHistory(2L, 1L, 1000L, TransactionType.USE, 2L);

        expectedPointHistories.add(pointHistory1);
        expectedPointHistories.add(pointHistory2);

        when(pointHistoryTable.selectAllByUserId(1L)).thenReturn(expectedPointHistories);

        //when

        List<PointHistory> actualPointHistories = pointHistoryTable.selectAllByUserId(1L);

        //then
        assertThat(actualPointHistories)
                .as("유저 포인트 사용 내역 조회 내역이 동일해야합니다.")
                .isEqualTo(expectedPointHistories);
    }

    @Test
    void chargeUserPoint_성공() {

        // given
        UserPoint beforePoint = new UserPoint(1L, 200L);
        UserPoint chargePoint = new UserPoint(1L, 1000L);

        UserPoint afterPoint = new UserPoint(1L, beforePoint.getPoint() + chargePoint.getPoint());

        when(pointHistoryTable.insert(eq(chargePoint.getId()), eq(chargePoint.getPoint()), eq(TransactionType.CHARGE), anyLong())
        ).thenReturn(null);

        when(userPointTable.selectById(1L))
                .thenReturn(beforePoint)
                .thenReturn(afterPoint);

        when(userPointTable.insertOrUpdate(afterPoint.getId(), afterPoint.getPoint())).thenReturn(null);


        // when

        UserPoint actualPoint = pointService.chargeUserPoint(chargePoint.getId(), chargePoint.getPoint());


        // then

        assertThat(actualPoint)
                .as("충전후 포인트가 예상 포인트와 동일 해야합니다.")
                .isEqualTo(afterPoint);

        verify(pointHistoryTable).insert((eq(chargePoint.getId())), eq(chargePoint.getPoint()), eq(TransactionType.CHARGE), anyLong());
        verify(userPointTable).insertOrUpdate(afterPoint.getId(), afterPoint.getPoint());


    }

    @Test
    void useUserPoint_성공() {

        // given
        UserPoint beforeUserPoint = new UserPoint(1L, 1000L);
        UserPoint useeUserPoint = new UserPoint(1L, 500L);
        UserPoint afterUserPoint = new UserPoint(1L, beforeUserPoint.getPoint() - useeUserPoint.getPoint());


        when(userPointTable.selectById(1L))
                .thenReturn(beforeUserPoint)
                .thenReturn(afterUserPoint);

        when(userPointTable.insertOrUpdate(afterUserPoint.getId(), afterUserPoint.getPoint())).thenReturn(null);
        when(pointHistoryTable.insert(eq(useeUserPoint.getId()), eq(useeUserPoint.getPoint()), eq(TransactionType.USE), anyLong())).thenReturn(null);


        // when

        UserPoint actualPoint = pointService.useUserPoint(useeUserPoint.getId(), useeUserPoint.getPoint());

        // then

        assertThat(actualPoint).as("포인트 사용 후 결과가 예상 포인트와 일치해야 합니다.")
                .isEqualTo(afterUserPoint);

        verify(userPointTable).insertOrUpdate(afterUserPoint.getId(), afterUserPoint.getPoint());
        verify(pointHistoryTable).insert(eq(useeUserPoint.getId()), eq(useeUserPoint.getPoint()), eq(TransactionType.USE), anyLong());


    }

    @Test
    void useUserPoint_잔액부족_예외() {

        // given
        UserPoint beforeUserPoint = new UserPoint(1L, 400L);
        UserPoint useeUserPoint = new UserPoint(1L, 500L);

        when(userPointTable.selectById(1L))
                .thenReturn(beforeUserPoint);

        //when

        UserPointException exception = assertThrows(UserPointException.class, () -> {
            pointService.useUserPoint(useeUserPoint.getId(), useeUserPoint.getPoint());
        });

        // then

        assertThat(exception.getErrMesage()).isEqualTo("잔액이 부족합니다.");
        assertThat(exception.getStatusCode()).isEqualTo(500);
    }


}
