package io.hhplus.tdd.point;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserPoint {
    private long id;
    private long point;
    private long updateMillis;

    public static UserPoint empty(long id) {
        return new UserPoint(0, 0, System.currentTimeMillis());
    }
}
