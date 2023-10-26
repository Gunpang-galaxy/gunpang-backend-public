package com.galaxy.gunpang.user.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class UserUtil {

    public long generateRandomId() {
        Random random = new Random();
        long userId = 1000000000000L + (long) (random.nextDouble() * 9000000000000L);
        return userId;
    }

}
