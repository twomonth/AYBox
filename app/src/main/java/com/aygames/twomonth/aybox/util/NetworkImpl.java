package com.aygames.twomonth.aybox.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.widget.Toast;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/**
 * Created by MyPC on 2017/6/23.
 */

public class NetworkImpl {
    public static HttpClient getHttpClient(Context ctx) {

        if (!isNetWorkConneted(ctx)) {
            Looper.prepare();
            Toast.makeText(ctx, "网络连接错误，请检查当前网络状态！", Toast.LENGTH_SHORT).show();
            Looper.loop();
            return null;
        }
        HttpClient client = null;
        if (isCmwapType(ctx)) {
            HttpParams params = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(params, 30 * 1000);// 设置链接超时时间
            HttpConnectionParams.setSoTimeout(params, 30 * 1000);// 设置请求超时时间
            HttpConnectionParams.setSocketBufferSize(params, 100 * 1024);
            HttpClientParams.setRedirecting(params, true);
            HttpHost httphost = new HttpHost("10.0.0.172", 80);
            params.setParameter(ConnRouteParams.DEFAULT_PROXY, httphost);// 设置代理
            client = new DefaultHttpClient(params);
        } else {
            client = new DefaultHttpClient();
            HttpParams params = client.getParams();
            params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
                    30 * 1000);
            params.setParameter(CoreConnectionPNames.SO_TIMEOUT, 30 * 1000);
        }
        return client;
    }

    /**
     * 网络是否是连接状态
     *
     * @return true表示可能，false网络不可用
     */
    public static boolean isNetWorkConneted(Context ctx) {
        try {
            ConnectivityManager cmr = (ConnectivityManager) ctx
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            if (cmr != null) {
                NetworkInfo networkinfo = cmr.getActiveNetworkInfo();

                if (networkinfo != null && networkinfo.isConnectedOrConnecting()) {
                    return networkinfo.isAvailable();
                }
            }
        } catch (Exception e) {
            //Toast.makeText(ctx, "网络连接错误，请检查当前网络状态！", Toast.LENGTH_SHORT).show();
            return false;
        }
        return false;
        //return null != networkinfo && networkinfo.isConnectedOrConnecting();
    }

    /**
     * 是否使用代理上网
     *
     * @param ctx
     * @return
     */
    private static boolean isCmwapType(Context ctx) {
        ConnectivityManager cmr = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cmr.getActiveNetworkInfo();
        String nettype = netinfo.getExtraInfo();
        if (null == nettype) {
            return false;
        }
        return "cmwap".equalsIgnoreCase(nettype)
                || "3gwap".equalsIgnoreCase(nettype)
                || "uniwap".equalsIgnoreCase(nettype);
    }
}
