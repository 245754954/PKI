package NUDT.config;

public class CertConfigLoader extends AbstractConfigLoader<CertConfig> {

    public CertConfigLoader(ConfigReader<CertConfig> configReader) {
        this.configReader = configReader;
    }

    @Override
    public CertConfig loadConfig(String configName, Class<CertConfig> clazz) throws Exception {
        CertConfig certConfig = super.loadConfig(configName, clazz);
        CertConfig.INSTANCE = certConfig;

        return certConfig;
    }

    @Override
    public void validate(CertConfig config) {
        // todo
    }
}
