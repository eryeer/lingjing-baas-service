package com.onchain.dna2explorer;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Set;

public class SimTest {

    @Test
    public void test(){
        HashMap<String, Long> m1 = new HashMap<>();
        m1.put("1", 2l);
        m1.put("2", 4l);
        m1.put("2", 4l);
        m1.put("3", 4l);
        HashMap<String, Long> m2 = new HashMap<>();
        m2.put("1", 6l);
        m2.put("2", 8l);
        m2.put("5", 1l);
        HashMap<String, Long> m3 = new HashMap<>();
        Set<String> accountInKey = m1.keySet();
        Set<String> accountOutInKey = m2.keySet();
        Sets.SetView<String> transferKey = Sets.union(accountInKey, accountOutInKey);
        System.out.println(accountInKey);
        System.out.println(accountOutInKey);
        System.out.println(transferKey);


    }

}
