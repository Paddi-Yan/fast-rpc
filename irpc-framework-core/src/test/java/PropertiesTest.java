import com.paddi.core.common.config.RpcConfigEnum;
import com.paddi.core.utils.PropertiesFileUtil;

import java.io.IOException;
import java.util.Properties;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月08日 15:07:17
 */
public class PropertiesTest {
    public static void main(String[] args) throws IOException {
//        PropertiesLoader.loadConfiguration();
//        Properties properties = PropertiesLoader.properties;
//        System.out.println("properties.getProperty(\"rpc.serialize.type\") = " + properties.getProperty("rpc.serialize.type"));
//        InputStream stream = PropertiesTest.class.getResourceAsStream(RpcConfigEnum.RPC_CONFIG_PATH.getPropertyValue());
//        Properties properties = new Properties();
//        properties.load(new InputStreamReader(stream));
//        System.out.println("properties.get(\"rpc.serialize.type\") = " + properties.get("rpc.serialize.type"));
        Properties properties = PropertiesFileUtil.readPropertiesFile(RpcConfigEnum.RPC_CONFIG_PATH.getPropertyValue());

        System.out.println(properties.getProperty(RpcConfigEnum.SERIALIZE_TYPE.getPropertyValue()));
    }
}
