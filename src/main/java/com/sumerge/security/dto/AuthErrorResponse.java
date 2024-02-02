package com.sumerge.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthErrorResponse {
    private int status;
    private String message;
    private long timeStamp;
}
