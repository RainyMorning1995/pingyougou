package com.pinyougou;

import org.junit.Test;

public class TestRandon {



    @Test
    public void test() {
//        String code =  (long) ((Math.random() * 9 + 1) * 100000)+"";
//        System.out.println(code);
        String code = (long) ((Math.random() * 9 + 1) * 100000) + "";
        System.out.println(code);

    }
}
