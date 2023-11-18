package core.general;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;

public class setconfig {

    public static ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    public static Config readyaml() throws IOException {
        File file = new File("src/main/resources/config.yaml");
        return mapper.readValue(file, Config.class);
    }
}
