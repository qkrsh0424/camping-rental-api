package com.camping_rental.server.domain.reset_password.vo;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

public class ResetPasswordTokenVo {
    @Data
    @Builder
    public static class Create{
        private UUID resetToken;
    }
}
