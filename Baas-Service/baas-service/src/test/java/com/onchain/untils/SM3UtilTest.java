package com.onchain.untils;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SM3UtilTest {

    @Test
    public void sm3() {
        String src = "123456";
        String hash = SM3Util.sm3(src);
        assertEquals("207cf410532f92a47dee245ce9b11ff71f578ebd763eb3bbea44ebd043d018fb", hash);
    }
}