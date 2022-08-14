package com.thoughtworks.midquiz.midquiz.infraustruct.util;

import com.thoughtworks.midquiz.midquiz.config.TestRedisConfiguration;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest(classes = TestRedisConfiguration.class)
class RedisUtilsTest {

    @Autowired
    private RedisUtils redisUtils;

    @Spy
    private final RedisTemplate redisTemplate = new RedisTemplate();


}