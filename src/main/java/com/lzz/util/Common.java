package com.lzz.util;

import java.util.ArrayList;

/**
 * Created by lzz on 17/4/16.
 */
public interface Common {
    int LIMIT = 200;
    int SEND_RANGE_TIME = 300000;
    ArrayList<String> ADMIN_LIST_IP = new ArrayList<String>() {{
        add("127.0.0.1");
        add("192.168.1.101");
    }};
}
