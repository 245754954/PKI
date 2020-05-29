package NUDT.config;

public class ServerConfigLoader extends AbstractConfigLoader<ServerConfig> {
    public ServerConfigLoader(ConfigReader<ServerConfig> configReader) {
        this.configReader = configReader;
    }

    @Override
    public ServerConfig loadConfig(String configName, Class<ServerConfig> clazz) throws Exception {
        ServerConfig serverConfig = super.loadConfig(configName, clazz);
        ServerConfig.INSTANCE = serverConfig;

        return serverConfig;
    }

    @Override
    public void validate(ServerConfig config) {
        // todo
    }
}
