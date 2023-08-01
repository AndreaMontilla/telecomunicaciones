package com.test.telecomunicaciones.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
public class PhoneCall {
    long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    boolean nationalCall;
}
