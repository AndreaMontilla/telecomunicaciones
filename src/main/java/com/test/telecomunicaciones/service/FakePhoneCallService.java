package com.test.telecomunicaciones.service;

import com.test.telecomunicaciones.model.PhoneCall;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.test.telecomunicaciones.util.UnitConversionUtils.HOUR_TO_MINUTES;

@Service
@RequiredArgsConstructor
public class FakePhoneCallService {

    private static final int MINIMAL_DURATION = 1;
    private static final int MINIMAL_INTERVAL = 1;
    private static final int MAX_CALL_DURATION_SECONDS = HOUR_TO_MINUTES * 8;
    private static final int MAX_CALL_INTERVAL_SECONDS = HOUR_TO_MINUTES * 8;

    private final Random random;

    public List<PhoneCall> getPhoneCalls(int quantity) {
        List<PhoneCall> phoneCalls = new ArrayList<>();

        // Genera llamadas
        for (int i = 1; i <= quantity; i++) {
            LocalDateTime startTime = getNextStartTime(phoneCalls, i);

            long durationSeconds = (long) random.nextInt(MAX_CALL_DURATION_SECONDS) + MINIMAL_DURATION;
            LocalDateTime endTime = startTime.plusSeconds(durationSeconds);

            phoneCalls.add(PhoneCall.builder()
                    .id(i)
                    .startTime(startTime)
                    .endTime(endTime)
                    .nationalCall(random.nextBoolean())
                    .build());
        }
        return phoneCalls;
    }

    private LocalDateTime getNextStartTime(List<PhoneCall> phoneCalls, int i) {
        LocalDateTime startTime;
        if (i == 1) {
            // Primera llamada: Inicio aleatorio
            startTime = getRandomTime();
        } else {
            // Siguientes llamadas: Inicio después de la llamada anterior
            LocalDateTime prevEndTime = phoneCalls.get(i - 2).getEndTime();
            long intervalSeconds = (long) random.nextInt(MAX_CALL_INTERVAL_SECONDS) + MINIMAL_INTERVAL;
            startTime = prevEndTime.plusSeconds(intervalSeconds);
        }
        return startTime;
    }

    private LocalDateTime getRandomTime() {
        int year = 2023;
        int month = random.nextInt(12) + 1;
        int day = random.nextInt(28) + 1;
        int hour = random.nextInt(24);
        int minute = random.nextInt(60);
        int second = random.nextInt(60);

        return LocalDateTime.of(year, month, day, hour, minute, second);
    }
}
