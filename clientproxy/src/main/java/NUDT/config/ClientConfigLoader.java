package NUDT.config;

import NUDT.config.AbstractConfigLoader;
import NUDT.config.ClientConfig;
import NUDT.config.ConfigReader;

public class ClientConfigLoader extends AbstractConfigLoader<ClientConfig> {

    public ClientConfigLoader(ConfigReader<ClientConfig> configReader) {
        this.configReader = configReader;
    }

    @Override
    public ClientConfig loadConfig(String configName, Class<ClientConfig> clazz) throws Exception {
        ClientConfig clientConfig = super.loadConfig(configName, clazz);
        ClientConfig.INSTANCE = clientConfig;

        return clientConfig;
    }

    @Override
    public void validate(ClientConfig config) {
        // todo
    }
}
