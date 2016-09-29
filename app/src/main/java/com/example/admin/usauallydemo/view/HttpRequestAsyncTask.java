package com.example.admin.usauallydemo.view;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

/**
 * Created by zengqiang on 2016/7/13 0013.
 * Description:
 */
public class HttpRequestAsyncTask extends AsyncTask<Request, Integer, String> {
    /* 设置编码格式 */
    private static final String CHARSET = "UTF-8";
    private CallBack callBack;
    private Activity activity;

    public HttpRequestAsyncTask(Activity activity, CallBack callBack) {
        this.callBack = callBack;
        this.activity = activity;
    }

    private HttpURLConnection buildHttpClient(String urlStr, int timeout) {
        URL url = null;
        HttpURLConnection conn = null;
        try {
            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(timeout);// 读取超时 单位毫秒
            conn.setConnectTimeout(timeout);// 连接超时
            conn.setUseCaches(false);
            conn.setDoOutput(true);// 是否输入参数
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return conn;
    }

    protected String doInBackground(Request... requests) {//相当于Thread run
        String resultString = "";
        Request request = requests[0];
        if (!isNetDeviceAvailable(activity)) {
            return "网络不可用";
        }
        try {
            HttpURLConnection connection = buildHttpClient(request.getUrl(), request.getRequestTimeOut());
            connection.setRequestMethod(request.getMethod().getMethed());// 请求方式
            StringBuffer params = new StringBuffer();
            if (request.getParams() != null) {
                for (Map.Entry<String, String> entry : request.getParams().entrySet()) {
                    params.append("&").append(entry.getKey()).append("=").append(entry.getValue());
                    Log.e("tag","请求参数：" + entry.getKey() + " : " + entry.getValue());
                }
                byte[] bypes = params.toString().substring(1, params.length()).getBytes();
                connection.getOutputStream().write(bypes);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                Log.e("tag","请求接口失败：responseCode ＝ " + responseCode);
                return null;
            }
            InputStream in = connection.getInputStream();
            InputStreamReader isReader = new InputStreamReader(in, CHARSET);
            BufferedReader bufReader = new BufferedReader(isReader);
            resultString = bufReader.readLine();
            Log.e("tag","接口返回结果：" + resultString);
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return resultString;
    }

    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    protected void onPostExecute(String o) {//运行于主线程
        if (activity != null && !activity.isFinishing()) {//activity结束掉之后不希望返回给主线程，
            // 防止activity还有null对象调用出现NullPointExcption
            if (!TextUtils.isEmpty(o)) {
                if ("网络不可用".equals(o)) {
                    if (callBack != null)
                        callBack.OnFailed("网络不可用");
                }
                if (callBack != null)
                    callBack.OnSuccess(o);
            } else {
                if (callBack != null)
                    callBack.OnFailed("请求超时");
            }
        }
        super.onPostExecute(o);
    }

    /**
     * 回调
     */
    public interface CallBack {
        void OnSuccess(String result);

        void OnFailed(String errMsg);
    }

    /*
    * 判断网络连接是否已打开 false 未打开
    */
    public static boolean isNetDeviceAvailable(Context context) {
        boolean bisConnFlag = false;
        ConnectivityManager conManager = (ConnectivityManager) context.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = conManager.getActiveNetworkInfo();
        if (network != null) {
            bisConnFlag = conManager.getActiveNetworkInfo().isAvailable();
        }
        return bisConnFlag;
    }
}
