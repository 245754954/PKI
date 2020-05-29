package NUDT.config;


public abstract class AbstractConfigLoader<T> implements ConfigLoader<T> {
    protected ConfigReader<T> configReader;

    @Override
    public T loadConfig(String configName, Class<T> clazz) throws Exception {
        T config = configReader.readFromFile(configName, clazz);
        validate(config);
        return config;
    }
}
