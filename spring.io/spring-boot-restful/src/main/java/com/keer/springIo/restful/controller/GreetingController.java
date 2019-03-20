package com.keer.springIo.restful.controller;

import com.keer.springIo.restful.entities.Greeting;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/greeting")
public class GreetingController {
    private static final String template = "Hello, %s";
    private final AtomicLong counter=new AtomicLong();

    @RequestMapping("/{name}")
    public Greeting greeting(@PathVariable String name) {
        return new Greeting(counter.incrementAndGet(),String.format(template,name));
    }

}
