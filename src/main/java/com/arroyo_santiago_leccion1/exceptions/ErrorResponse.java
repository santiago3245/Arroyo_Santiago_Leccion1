package com.arroyo_santiago_leccion1.exceptions;

import java.util.Map;

public record ErrorResponse(
        String timestamp,
        int status,
        String message,
        Map<String, String> errors
) {}
