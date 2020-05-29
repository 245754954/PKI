package NUDT;


import NUDT.config.ConfigLoader;
import NUDT.config.JsonConfigReader;
import NUDT.config.ServerConfig;
import NUDT.config.ServerConfigLoader;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.concurrent.DefaultThreadFactory;

/**
 * @author jqChan
 * @date 2018/6/13
 */
public class JGateWay {




    public static final boolean DEFAULT_SHOULD_AUTH = false;

    public static Socks5Server create(int port, boolean shoudAuth) {
        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.ADVANCED);
        EventLoopGroup bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("boss-thread"));
        EventLoopGroup workGroup = new NioEventLoopGroup(10, new DefaultThreadFactory("worker-thread"));
        return new Socks5Server().setPort(port).setBossGroup(bossGroup).setWorkGroup(workGroup).setShouldAuth
                (shoudAuth);
    }


    public static Socks5Server create() throws Exception {
        ConfigLoader<ServerConfig> configLoader = new ServerConfigLoader(new JsonConfigReader<>());
        configLoader.loadConfig("server_config.json", ServerConfig.class);
        int DEFUALT_PORT = ServerConfig.INSTANCE.getPort();
        return create(DEFUALT_PORT, DEFAULT_SHOULD_AUTH);
    }

    public static void main(String[] args) throws Exception {
        JGateWay.create().start();
    }

}
