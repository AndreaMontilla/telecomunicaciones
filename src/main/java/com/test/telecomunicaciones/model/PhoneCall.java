package com.test.telecomunicaciones.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

import static com.test.telecomunicaciones.util.UnitConversionUtils.MINUTE_TO_SECONDS;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhoneCall {
    long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    boolean nationalCall;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(id);
        sb.append(", inicio: ").append(startTime);
        sb.append(", fin: ").append(endTime);
        sb.append(", es nacional: ").append(nationalCall);
        sb.append(", tiene una duracion: ").append(getDurationInMinutes()).append(" minutos");
        return sb.toString();
    }

    public Duration getDuration(){
        return Duration.between(getStartTime(), getEndTime());
    }

    public long getDurationInMinutes() {
        return (long) Math.ceil((double)getDuration().getSeconds() /MINUTE_TO_SECONDS);
    }
}
