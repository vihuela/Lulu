package com.youlongnet.lulu.net.luluRequst;

import android.content.Context;

import com.youlongnet.lulu.net.queue.Net;
import com.youlongnet.lulu.net.utils.INoProguard;

/**
 * Created by lyao on 2015/4/27.
 */
public class AddAddressRequest extends Net<AddAddressRequest.RequestBean, String> {

    public AddAddressRequest(Context ctx) {
        super(ctx, "http://106.186.112.164/add_order_address", "get");
    }

    public static class RequestBean implements INoProguard {
        public String frist_name;
        public String last_name;
        public String user_id;
        public String address;
        public String tax_id;
        public String phone_number;
        public String time;
        public String key;
        public String note;

    }
}
