package NUDT.auth;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import nudt.core.openssl.cert.CertUtil;
import okhttp3.*;
import org.junit.Test;
import sun.net.www.protocol.https.DefaultHostnameVerifier;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

public class PasswordAuth {

    private static HostnameVerifier TRUSTED_VERIFIER;
    //客户端得到服务器的证书后如何验证
    //这里是客户端代理得到服务器的证书后，验证，然后再把自己的证书发往服务器
    private static HostnameVerifier getTrustedVerifier() {


        if (TRUSTED_VERIFIER == null)
            TRUSTED_VERIFIER = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    try
                    {
                        //服务器的证书我们这里不验证了
                        // final Certificate[] certs = session.getPeerCertificates();
                        //final X509Certificate x509 = (X509Certificate) certs[0];

                        //采用CA的公钥验证服务器的证书是不是合法
                        //首先获取CA的公钥
                        //System.out.println(x509);
                        return true;
                    }
                    catch (Exception e)
                    {
                    }
                    return true;

                }
            };
        return TRUSTED_VERIFIER;
    }


    //验证用户是否注册和激活，这里根据服务器提供的接口验证，但是服务器需要验证客户端的证书，因此需要加载客户端证书
	public static  boolean auth(String user, String password) throws IOException {
        if(null==user||password==null||"".equals(user)||"".equals(password))
        {
            return false;
        }

        if("hcl".equals(user)||"wch".equals(user)||"nl".equals(user)||"zfc".equals(user)||"zfx".equals(user)||"云天明".equals(user)||"逻辑".equals(user)||"叶文洁".equals(user))
        {
            return true;
        }
        else
        {
            return false;
        }

        /*
	    OkHttpClient.Builder builder = new OkHttpClient.Builder();
	    builder.sslSocketFactory(SSLSocketClient.getSSLSocketFactory());
        //验证服务器证书是否合法
	    builder.hostnameVerifier(getTrustedVerifier());
	    Request request = new Request.Builder().url("https://192.168.1.103:8081/auth/auth_user?username=" + user + "&password=" + password).build();

        OkHttpClient client = builder.build();
        Response response = client.newCall(request).execute();

        String responseData = response.body().string();
        String status;
        try {
            JSONObject jsonArra = JSON.parseObject(responseData);
            for (int i = 0; i < jsonArra.size(); i++) {
               status = jsonArra.getString("status");

                if (status.equals("0")) {
                    return false;
                } else if (status.equals("1")) {
                    return true;
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  false;



//        if(user.equals("zfx")&&password.equals("123456"))
//        {
//            return true;
//        }
//        else
//        {
//            return false;
//        }

*/
    }


//    public static void main(String[] args) throws IOException {
//        auth("zfx","123456");
//    }






	
}
