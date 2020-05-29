package NUDT.config;


import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CertConfig {


    public static CertConfig INSTANCE;
    //服务器密钥库密码
    private String keyStorePassword;
    //服务器证书信任库密钥
    private String keyStoreTrustPassword;
    //服务器密钥库位置
    private String keyStorePath;
    //服务器信任库位置
    private String keyStoreTrustPath;


}
