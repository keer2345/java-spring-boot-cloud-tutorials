package com.keer.springboot_microservices.counterservice.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
@RefreshScope
public class CounterController {
    private AtomicLong count = new AtomicLong(0L);

    @Value(value = "${counter.prefixMessage}")
    private String prefixMessage;

    @RequestMapping("/count")
    public String getCount() {
        return prefixMessage + count.getAndIncrement();
    }
}

