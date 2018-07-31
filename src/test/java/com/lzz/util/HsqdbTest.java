package com.lzz.util;


import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class HsqdbTest{

    private static final Logger log = LoggerFactory.getLogger(HsqdbTest.class);

    @Test
    public void selectTest(){
        List list = Sqlite.getSqlite().select("select * from roles");
        System.out.println(list);
    }
}