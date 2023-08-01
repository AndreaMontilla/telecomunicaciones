package com.test.telecomunicaciones.service;

import com.test.telecomunicaciones.model.CallShift;
import com.test.telecomunicaciones.model.Client;
import com.test.telecomunicaciones.model.PhoneCall;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.Duration;
import java.util.List;

import static com.test.telecomunicaciones.util.UnitConversionUtils.MINUTE_TO_SECONDS;

@Service
public class BillingService {

    public static final int START_NIGHT_SHIFT = 22;
    public static final int END_NIGHT_SHIFT = 4;
    private static final BigDecimal DAY_RATE_PER_MINUTE = new BigDecimal("0.05");
    private static final BigDecimal NIGHT_RATE_PER_MINUTE = new BigDecimal("0.02");
    private static final BigDecimal WEEKEND_RATE_PER_MINUTE = new BigDecimal("0.01");
    public static final int NATIONA_RATIO = 1;
    public static final int INTERNATIONAL_RATIO = 2;

    public BigDecimal calculateBill(List<PhoneCall> phoneCalls, Client client) {
        return phoneCalls.stream()
                .map(phoneCall -> calculateBill(phoneCall, client))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateBill(PhoneCall phoneCall, Client client) {
        CallShift callShift = getCallShift(phoneCall);
        long durationInMinutes = phoneCall.getDurationInMinutes();
        BigDecimal ratePerSecond = getRatePerMinute(client, callShift, phoneCall.isNationalCall());
        BigDecimal durationInBigDecimal = BigDecimal.valueOf(durationInMinutes);
        return ratePerSecond.multiply(durationInBigDecimal);
    }

    private CallShift getCallShift(PhoneCall phoneCall) {
        if (isDuringWeekend(phoneCall)) {
            return CallShift.WEEKEND;
        } else if (isDuringNight(phoneCall)) {
            return CallShift.NIGHT;
        } else {
            return CallShift.DAY;
        }
    }

    private BigDecimal getRatePerMinute(Client client, CallShift callShift, boolean nationalCall) {
        BigDecimal internationalFactor = BigDecimal.valueOf(nationalCall ? NATIONA_RATIO : INTERNATIONAL_RATIO);
        BigDecimal baseRate;
        switch (callShift) {
            case DAY:
                baseRate =  client.isNewClient() ? NIGHT_RATE_PER_MINUTE : DAY_RATE_PER_MINUTE;
                break;
            case NIGHT:
                baseRate =  NIGHT_RATE_PER_MINUTE;
                break;
            case WEEKEND:
                baseRate =  WEEKEND_RATE_PER_MINUTE;
                break;
            default:
                throw new IllegalArgumentException("Tipo de tarifa no reconocido: " + callShift);
        }
        return baseRate.multiply(internationalFactor);
    }

    private boolean isDuringNight(PhoneCall phoneCall) {
        int hour = phoneCall.getStartTime().getHour();
        return hour >= START_NIGHT_SHIFT || hour < END_NIGHT_SHIFT;
    }

    private boolean isDuringWeekend(PhoneCall phoneCall) {
        DayOfWeek dayOfWeek = phoneCall.getStartTime().getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }
}
