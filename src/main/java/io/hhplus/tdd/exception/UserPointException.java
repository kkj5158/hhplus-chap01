package io.hhplus.tdd.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserPointException extends RuntimeException {

    private final int statusCode;
    private final String errMesage;





}
