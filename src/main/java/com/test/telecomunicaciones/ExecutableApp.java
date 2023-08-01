package com.test.telecomunicaciones;

import com.test.telecomunicaciones.model.Client;
import com.test.telecomunicaciones.model.PhoneCall;
import com.test.telecomunicaciones.service.BillingService;
import com.test.telecomunicaciones.service.FakePhoneCallService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

public class ExecutableApp {

    public static final boolean NEW_CLIENT = false;
    public static final int QUANTITY = 20;
    private static final Random random;
    private static final FakePhoneCallService phoneCallService;
    private static final BillingService billingService;

    static {
        random = new Random();
        phoneCallService = new FakePhoneCallService(random);
        billingService = new BillingService();
    }

    public static void main(String[] args) {

        Client client = Client.builder()
                .newClient(NEW_CLIENT)
                .build();

        List<PhoneCall> calls = phoneCallService.getPhoneCalls(QUANTITY);

        calls.forEach(call -> getPrintCallWithBilling(call, client));
    }


    private static void getPrintCallWithBilling(PhoneCall call, Client client) {
        BigDecimal bill = billingService.calculateBill(call, client);
        System.out.println(call.toString() + " tuvo un costo de " + bill);
    }
}

