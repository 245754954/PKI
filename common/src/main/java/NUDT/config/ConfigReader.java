package NUDT.config;

public interface ConfigReader<T> {
    T readFromFile(String configName, Class<T> clazz) throws Exception;
}
