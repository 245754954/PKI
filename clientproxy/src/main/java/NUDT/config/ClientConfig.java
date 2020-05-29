package NUDT.config;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ClientConfig {
    public static ClientConfig INSTANCE;

    private Integer localPort;

    private Integer serverPort;

    private String serverHost;

    private String password;

    private String encryptMethod;

}
