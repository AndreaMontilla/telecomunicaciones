package com.test.telecomunicaciones.service;

import com.test.telecomunicaciones.model.CallShift;
import com.test.telecomunicaciones.model.Client;
import com.test.telecomunicaciones.model.PhoneCall;

import java.time.DayOfWeek;
import java.time.Duration;
import java.util.List;

public class BillingService {

    public static final int START_NIGHT_SHIFT = 22;
    public static final int END_NIGHT_SHIFT = 4;
    private static final double DAY_RATE_PER_MINUTE = 0.05d;
    private static final double NIGHT_RATE_PER_MINUTE = 0.02d;
    private static final double WEEKEND_RATE_PER_MINUTE = 0.01d;
    private static final int MINUTE_TO_SECONDS = 60;

    private static boolean isDuringWeekend(PhoneCall phoneCall) {
        DayOfWeek dayOfWeek = phoneCall.getStartTime().getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    public double calculateBill(List<PhoneCall> phoneCalls, Client client) {
        return phoneCalls.stream()
                .mapToDouble(phoneCall -> calculateBill(phoneCall, client))
                .sum();
    }

    private double calculateBill(PhoneCall phoneCall, Client client) {
        CallShift callShift = getCallShift(phoneCall);
        Duration duration = Duration.between(phoneCall.getStartTime(), phoneCall.getEndTime());
        long durationInSeconds = duration.getSeconds();
        double ratePerMinute = getRatePerSecond(client, callShift);
        return ratePerMinute * durationInSeconds;
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

    private double getRatePerSecond(Client client, CallShift callShift) {
        double ratePerMinute;

        switch (callShift) {
            case DAY:
                ratePerMinute = client.isNewClient() ? NIGHT_RATE_PER_MINUTE : DAY_RATE_PER_MINUTE;
                break;
            case NIGHT:
                ratePerMinute = NIGHT_RATE_PER_MINUTE;
                break;
            case WEEKEND:
                ratePerMinute = WEEKEND_RATE_PER_MINUTE;
                break;
            default:
                throw new IllegalArgumentException("Tipo de tarifa no reconocido: " + callShift);
        }
        return ratePerMinute / MINUTE_TO_SECONDS;
    }

    private boolean isDuringNight(PhoneCall phoneCall) {
        int hour = phoneCall.getStartTime().getHour();
        return hour >= START_NIGHT_SHIFT || hour < END_NIGHT_SHIFT;
    }
}
