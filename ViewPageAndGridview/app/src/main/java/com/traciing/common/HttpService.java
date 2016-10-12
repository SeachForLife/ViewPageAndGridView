package com.traciing.common;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carl_yang on 2016/6/13.
 */
public class HttpService {

    public static final int TIMEOUT_TO_OPEN_CONNECT = 5 * 1000;
    public static final int TIMEOUT_SOCKET = 10 * 1000;


    /**
     *  设置默认的httpclient
     * @return
     */
    private static HttpClient buildHttpClient() {
        HttpClient client = new DefaultHttpClient();
        client.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIMEOUT_TO_OPEN_CONNECT);
        client.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, TIMEOUT_SOCKET);
        return client;
    }

    /**
     * 检测版本号
     * @param version
     * @return
     */
    public static String CheckVersion(String version){
        InputStream in = null;
        try {
            HttpClient client = buildHttpClient();
            HttpPost post = new HttpPost("http://traciingip1.cn:16051/drug/AppServlet");
            List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
            parameters.add(new BasicNameValuePair("method", "checkVersion"));
            parameters.add(new BasicNameValuePair("version", version));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters,
                    "utf-8");
            post.setEntity(entity);
            HttpResponse response = client.execute(post);
            int code = response.getStatusLine().getStatusCode();
            if (code == 200) {
                in = response.getEntity().getContent();
            }
            if(in==null){
                return "11";
            }
            String content= null;
            content = new String(SystemUtil.getBytes(in));
            System.out.println("获取的数据值为:"+content);
            return content;
        } catch (Exception e) {
            return "11";
        }
    }
}
