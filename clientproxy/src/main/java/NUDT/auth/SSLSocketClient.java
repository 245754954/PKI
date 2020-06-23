package NUDT.auth;



import NUDT.config.*;
import org.springframework.core.io.ClassPathResource;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

public class SSLSocketClient {

    private static final String KEY_STORE_TYPE_JKS = "jks";//证书类型
    private static final String KEY_STORE_TYPE_P12 = "PKCS12";//证书类型

   // 读取证书配置文件
    private static  String KEY_STORE_PASSWORD = "";
    private static  String KEY_STORE_TRUST_PASSWORD ="";
    private  static  String  KEY_STORE_PATH ="";
    private static   String KEY_STORE_TRUST_PATH ="";

    static
    {

        try
        {    CertConfigLoader certConfigLoader = new CertConfigLoader(new JsonConfigReader<>());
             certConfigLoader.loadConfig("cert_config.json", CertConfig.class);
             KEY_STORE_PASSWORD = CertConfig.INSTANCE.getKeyStorePassword();//证书密码（应该是客户端证书密码）
             KEY_STORE_TRUST_PASSWORD = CertConfig.INSTANCE.getKeyStoreTrustPassword();//授信证书密码（应该是服务端证书密码）
             KEY_STORE_PATH = CertConfig.INSTANCE.getKeyStorePath();
             KEY_STORE_TRUST_PATH =CertConfig.INSTANCE.getKeyStoreTrustPath();


        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }




    public static SSLSocketFactory getSSLSocketFactory() throws IOException {

        //信任证书库jks
        InputStream trust_input = SSLSocketClient.class.getClassLoader().getResourceAsStream(KEY_STORE_TRUST_PATH);
        //自己的密钥库pkcs12
        InputStream client_input = SSLSocketClient.class.getClassLoader().getResourceAsStream(KEY_STORE_PATH);
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            KeyStore trustStore = KeyStore.getInstance(KEY_STORE_TYPE_JKS);
            trustStore.load(trust_input, KEY_STORE_TRUST_PASSWORD.toCharArray());

            KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE_P12);
            keyStore.load(client_input, KEY_STORE_PASSWORD.toCharArray());

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, KEY_STORE_PASSWORD.toCharArray());

            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
            SSLSocketFactory factory = sslContext.getSocketFactory();
            return factory;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                trust_input.close();
                client_input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
