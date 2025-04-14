package io.hhplus.tdd.point;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PointHistory {
    private long id;
    private long userId;
    private long amount;
    private TransactionType type;
    private long updateMillis;

    }
