package com.vinay.fortexaBackend.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    INVALID_SIGNUP_REQUEST(
            HttpStatus.BAD_REQUEST,
            "AUTH_001",
            "Invalid signup request"
    ),

    USERNAME_REQUIRED(
            HttpStatus.BAD_REQUEST,
            "AUTH_002",
            "Username is required"
    ),

    USER_ALREADY_EXISTS(
            HttpStatus.CONFLICT,
            "AUTH_003",
            "User already exist"
    ),

    USER_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "AUTH_004",
            "User not found"
    ),

    INVALID_CREDENTIALS(
            HttpStatus.UNAUTHORIZED,
            "AUTH_005",
            "Invalid username and password"
    ),

    ACCOUNT_LOCKED(
            HttpStatus.LOCKED,
            "AUTH_006",
            "Account is temporarily locked"
    ),

    USER_ALREADY_DELETED(
            HttpStatus.BAD_REQUEST,
            "AUTH_007",
            "User already deleted"
    ),

    ACCESS_DENIED(
            HttpStatus.FORBIDDEN,
            "AUTH_008",
            "Access denied"
    ),

    INTERNAL_SERVER_ERROR(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "SERVER_001",
            "Something went wrong"
    );


    private final HttpStatus status;
    private final String code;
    private final String message;

     ErrorCode(HttpStatus status, String code, String message){
        this.status = status;
        this.code = code;
        this.message = message;
    }


    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }
}
