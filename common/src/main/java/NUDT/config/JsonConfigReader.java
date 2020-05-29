package NUDT.config;


import NUDT.handler.Client2DestHandler;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;


public class JsonConfigReader<T> implements ConfigReader<T> {

    private static final Logger LOG = LoggerFactory.getLogger(JsonConfigReader.class);
    public T readFromFile(String configName, Class<T> clazz) throws Exception {
        LOG.info("Read config {}", configName);
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(configName);
        if (resourceAsStream == null) {
            throw new Exception("无法加载配置文件");
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(resourceAsStream));
        String configText = reader.lines().collect(Collectors.joining());

        return JSONObject.parseObject(configText, clazz);
    }
}
