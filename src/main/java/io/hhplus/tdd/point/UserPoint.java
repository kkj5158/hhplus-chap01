package io.hhplus.tdd.point;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserPoint {
    private long id;
    private long point;
    private long updateMillis;

    public UserPoint(long id, long point) {
        this.id = id;
        this.point = point;
    }

    public static UserPoint empty(long id) {
        return new UserPoint(0, 0, System.currentTimeMillis());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof UserPoint userPoint)) return false;
        return getId() == userPoint.getId() && getPoint() == userPoint.getPoint();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getPoint());
    }
}
