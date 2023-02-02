package com.onchain.dna2explorer;

import com.onchain.dna2explorer.model.dao.Transfer;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

//@SpringBootTest
public class Dna2ExplorerApplicationTests {

    @Test
    public void getFields() {
        Field[] allFields = Transfer.class.getDeclaredFields();

        StringBuilder left = new StringBuilder();
        StringBuilder middle = new StringBuilder();
        StringBuilder builder = new StringBuilder();
        for (Field f : allFields) {
            String name = f.getName();
            String sName = camelToSnake(f.getName());
            left.append(sName);
            left.append(", ");

            middle.append("#{item.");
            middle.append(name);
            middle.append("}, ");

            builder.append(sName);
            builder.append(" = #{item.");
            builder.append(name);
            builder.append("}, ");
        }
        System.out.println(left.toString());
        System.out.println(middle.toString());
        System.out.println(builder.toString());

    }

    public static String camelToSnake(String str) {
        // Regular Expression
        String regex = "([a-z])([A-Z]+)";

        // Replacement string
        String replacement = "$1_$2";

        // Replace the given regex
        // with replacement string
        // and convert it to lower case.
        str = str.replaceAll(regex, replacement).toLowerCase();

        // return string
        return str;
    }


}
