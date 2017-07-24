package com.aygames.twomonth.aybox.util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by MyPC on 2017/3/30.
 */

public class ResultCode {
    public int code;
    public String data;
    public String orderid;
    public String username;
    public String password;
    public String sign;
    public long logintime;
    public String msg;
    public String ptbkey;
    public String url;// 充值时 第三方返回来的url

    public void regJson(JSONObject json)
    {
        try
        {
            code = json.isNull("code") ? 0 : json.getInt("code");//1表示登录成功，2表示用户名错误，3表示密码错误
            username = json.isNull("username") ? "" : json.getString("username");
            password = json.isNull("password") ? "" : json.getString("password");
            msg = json.isNull("msg") ? "" : json.getString("msg");
            sign = json.isNull("sign") ? "" : json.getString("sign");
            ptbkey =json.isNull("ptbkey") ? "" :json.getString("ptbkey");
            if("".equals(json.getString("logintime")))
            {
                logintime = 0;
            }else{
                logintime = json.isNull("logintime") ? 0 : json.getLong("logintime");
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

}
