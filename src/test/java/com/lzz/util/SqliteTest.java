package com.lzz.util;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * Created by lzz on 17/4/16.
 */
public class SqliteTest {

    @Before
    public void testInitDB(){
        Sqlite.getSqlite().initDB();
    }

    @Test
    public void testInsertId(){
        String sql = "insert into roles(service, role_name, type, ping_url, metric, client_id, members, add_time) " +
                "VALUES ('lzz-cpu','aaa','ping','http://',"+1+",'127.0.0.1','@all',1)";
        int id = Sqlite.getSqlite().insertId( sql );
        System.out.println(id);
    }

    @Test
    public void testSelectRow(){
        String sql = "select id from roles where role_name='0:0:0:0:0:0:0:1_null'";
        Map map = Sqlite.getSqlite().selectRow(sql);
        System.out.println(map);
    }

    @Test
    public void testSelects(){
        String sql = "select id from roles";
        List list = Sqlite.getSqlite().select( sql );
        System.out.println( list );
    }
}
