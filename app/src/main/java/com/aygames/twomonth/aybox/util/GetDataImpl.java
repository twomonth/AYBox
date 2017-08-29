package com.aygames.twomonth.aybox.util;

import android.content.Context;
import android.util.Log;



import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by MyPC on 2017/6/23.
 */

public class GetDataImpl {

    private static final String TAG = "GetDataImpl";
    private static GetDataImpl getdataImpl;
    private static Context ctx;

    private GetDataImpl(Context ctxs) {
        this.ctx = ctxs;
    }

    /**
     * 单利模式获得 GetDataImpl实例
     * @param ctxs
     * @return
     * GetDataImple实例
     */

    public static GetDataImpl getInstance(Context ctxs) {
        if (null == getdataImpl) {
            getdataImpl = new GetDataImpl(ctxs);
        }
        if (ctx==null) {
            ctx=ctxs;
        }
        return getdataImpl;
    }

    /**
     * 注册
     *
     * @param jasonStr
     * @return
     */
    public ResultCode register(String jasonStr) {
        InputStream request = doRequesttwo(Constans.URL_USER_REGISTER, jasonStr);
        ResultCode result = new ResultCode();

        try {
/*			Logger.msg("注册返回的字符串为 = "+parseIs2Str(request));
			String str = unzip(request);*/
            //获取返回的字符串信息
            String str = parseIs2Str(request);
            Log.w("str",str);
            if(null != str){
                JSONObject json = new JSONObject(str);
                result.regJson(json);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 负责发送请求与请求后获取数据(无加密压缩发送)
     * @param url 请求地址
     * @param str 请求携带的参数
     * @return inputstream服务端返回的输入流
     */
    public InputStream doRequesttwo(String url,String str){
        HttpClient client=NetworkImpl.getHttpClient(ctx);
        if(null==client){
            return null;
        }
        HttpPost post=new HttpPost(url);
        post.setHeader("content-type", "text/html");
        Log.i("url+data",url+str);
        if(str!=null){
            HttpEntity entity=new ByteArrayEntity(str.getBytes());
            post.setEntity(entity);
        }
        int count=0;
        //等待3秒在请求2次
        while(count<2){
            try {
                HttpResponse response= client.execute(post);
                if(response.getStatusLine().getStatusCode()== HttpStatus.SC_OK){
                    return response.getEntity().getContent();
                }
            } catch (ClientProtocolException e) {
                Log.i("网络连接","异常");
                e.printStackTrace();
            } catch (IOException e) {
                Log.i("网络连接","异常");
                e.printStackTrace();
            }
            count++;
            try {
                Thread.currentThread().sleep(3000);
            } catch (InterruptedException e) {
                Log.i("网络连接","异常");
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 解析服务端返回过来的输入流
     *
     * @param is
     * @return
     */
    public String parseIs2Str(InputStream is) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        try {
            while ((len = is.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            String str = new String(bos.toByteArray());
            //Logger.msg("service response data:" + str);
            return str;
        } catch (ClientProtocolException e) {
            Log.i("网络连接","异常");
            e.printStackTrace();
        } catch (IOException e) {
            Log.i("网络连接","异常");
            e.printStackTrace();
        } finally {
            if (null != bos) {
                try {
                    bos.close();
                } catch (IOException e) {
                    Log.i("网络连接","异常");
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
