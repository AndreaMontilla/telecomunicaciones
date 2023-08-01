package com.test.telecomunicaciones.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class Client {
    long id;
    String name;
    boolean newClient;
}
