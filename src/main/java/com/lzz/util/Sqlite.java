package com.lzz.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 * Created by lzz on 17/4/10.
 */
public class Sqlite {
    private static Sqlite sqlite;
    private JdbcTemplate jdbcTpl;

    public static Sqlite getSqlite(){
        if( sqlite == null ){
            ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
            sqlite = (Sqlite) ctx.getBean("sqlite");
            return sqlite;
        }
        return sqlite;
    }
    /**
     *  插入一条数据
     * @param sql
     * @return 插入的 ID
     */
    public int insertId(String sql){
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTpl.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException
            {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                return ps;
            }
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    public int insert(String sql){
        int res = jdbcTpl.update(sql);
        return res;
    }

    public int update(String sql){
        int res = jdbcTpl.update(sql);
        return res;
    }

    /**
     * 获取 roles 列表
     * @return
     */
    public List select(String sql){
        List list = jdbcTpl.queryForList(sql);
        return list;
    }

    public boolean delete( String sql ){
        int i = jdbcTpl.update( sql );
        if( i > 0 ){
            return true;
        }
        return false;
    }

    public Map selectRow(String sql ){
        Map map;
        try{
            map = jdbcTpl.queryForMap( sql );
        }catch (Exception e){
            map = null;
        }
        return map;
    }

    public boolean initDB(){
        // 创建 roles 表
        jdbcTpl.execute("CREATE TABLE if not exists roles(" +
                "id int  PRIMARY KEY AUTO_INCREMENT, " +
                "role_name VARCHAR(100), " +
                "service VARCHAR(100), " +
                "client_id VARCHAR(255), " +
                "members VARCHAR(255), " +
                "add_time bigint," +
                "send_time bigint)");
        // 创建 logs 表
        jdbcTpl.execute("create table if not exists logs(" +
                "id int PRIMARY KEY AUTO_INCREMENT, " +
                "roleid int, " +
                "role_name VARCHAR(100), " +
                "service VARCHAR(100), " +
                "metric_value float(10,3), " +
                "metric float(10,3), " +
                "error_message VARCHAR(255), " +
                "error_code int, " +
                "members VARCHAR(255), " +
                "client_id VARCHAR(255), " +
                "add_time bigint," +
                "day int," +
                "hour int," +
                "minute int" +
                ")");
        return true;
    }

    public void setJdbcTpl(JdbcTemplate jdbcTpl) {
        this.jdbcTpl = jdbcTpl;
    }
}
