package com.onchain;

import com.onchain.entities.dao.ContractApp;
import com.onchain.util.ECCUtils;
import com.onchain.util.SHA;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommonTest {

    @Test
    public void SHA256() {
        String hash = ECCUtils.SHA256("你好");
        assertEquals("670d9743542cae3ea7ebe36af56bd53648b0a1126162e78d81a32934a711302e", hash);

        String hash2 = SHA.SHA256("你好");
        assertEquals("670d9743542cae3ea7ebe36af56bd53648b0a1126162e78d81a32934a711302e", hash2);

        System.out.println(ECCUtils.SHA256(ECCUtils.SHA256("Onchain@2021")));
    }

    @Test
    public void Decode() throws DecoderException {
        String hex = "5b7b22616374696f6e223a223030222c2264657461696c48617368223a2261653166636137376138316561386235363865663630636461643164656536626165316661616637313663646463613266353735306237636463396236656434222c226d656d6265724964223a22313233343536373839222c226d656d6265724e616d65223a2241b9abcbbe222c2273746f7261676544617465223a313538353239323931333431332c2273746f726167654964223a22313030222c2276657273696f6e223a307d2c7b22616374696f6e223a223030222c2264657461696c48617368223a2261653166636137376138316561386235363865663630636461643164656536626165316661616637313663646463613266353735306237636463396236656434222c226d656d6265724964223a22313233343536373839222c226d656d6265724e616d65223a2241b9abcbbe222c2273746f7261676544617465223a313538353239323931333431332c2273746f726167654964223a22313030222c2276657273696f6e223a307d2c7b22616374696f6e223a223031222c2264657461696c48617368223a2236613134653561393334363538333361353330393238326134633739663561666162613733663561626433343066643439616133353131646338356130333431222c226d656d6265724964223a22313233343536373839222c226d656d6265724e616d65223a2241b9abcbbe222c2273746f7261676544617465223a313538353239323931333431332c2273746f726167654964223a22313030222c2276657273696f6e223a317d2c7b22616374696f6e223a223031222c2264657461696c48617368223a2236613134653561393334363538333361353330393238326134633739663561666162613733663561626433343066643439616133353131646338356130333431222c226d656d6265724964223a22313233343536373839222c226d656d6265724e616d65223a2241e585ace58fb8222c2273746f7261676544617465223a313538353239323931333431332c2273746f726167654964223a22313030222c2276657273696f6e223a327d2c7b22616374696f6e223a223030222c2264657461696c48617368223a2236613134653561393334363538333361353330393238326134633739663561666162613733663561626433343066643439616133353131646338356130333431222c226d656d6265724964223a22313233343536373839222c226d656d6265724e616d65223a2241e585ace58fb8222c2273746f7261676544617465223a313538353239323931333431332c2273746f726167654964223a22313030222c2276657273696f6e223a307d5d";
        byte[] bytes = Hex.decodeHex(hex.toCharArray());
        System.out.println(new String(bytes, StandardCharsets.UTF_8));
    }

    @Test
    public void GetFields() {
        Field[] allFields = ContractApp.class.getDeclaredFields();

        StringBuilder cols = new StringBuilder();
        StringBuilder itemInsert = new StringBuilder();
        StringBuilder itemSet = new StringBuilder();
        for (Field f : allFields) {
            String name = f.getName();
            String sName = camelToSnake(f.getName());
            cols.append(sName);
            cols.append(", ");

            itemInsert.append("#{item.");
            itemInsert.append(name);
            itemInsert.append("}, ");

            itemSet.append(sName);
            itemSet.append(" = #{item.");
            itemSet.append(name);
            itemSet.append("}, ");
        }
        System.out.println(cols);
        System.out.println(itemInsert.toString().replace("item.", ""));
        System.out.println(itemSet.toString().replace("item.", ""));
        System.out.println(itemInsert);
        System.out.println(itemSet);

    }

    public static String camelToSnake(String str) {
        // Regular Expression
        String regex = "([a-z])([A-Z]+)";

        // Replacement string
        String replacement = "$1_$2";

        // Replace the given regex
        // with replacement string
        // and convert it to lower case.
        str = str
                .replaceAll(
                        regex, replacement)
                .toLowerCase();

        // return string
        return str;
    }
}
