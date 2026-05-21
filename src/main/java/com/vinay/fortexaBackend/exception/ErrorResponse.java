package com.vinay.fortexaBackend.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        String code,
        String message,
        int Status,
        LocalDateTime timeStamp
) {
}
