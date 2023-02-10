import com.paddi.core.common.constants.RpcConstants;
import com.paddi.core.extension.ExtensionLoader;
import com.paddi.core.registry.RegistryService;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月08日 14:11:24
 */
public class SpiTest {
    public static void main(String[] args) {
        /*List<ClientFilter> filters = ExtensionLoader.getExtensionLoader(ClientFilter.class).getAllExtension();
        for(ClientFilter filter : filters) {
            System.out.println(filter.getClass());
        }*/
        System.out.println(ExtensionLoader.getExtensionLoader(RegistryService.class)
                                          .getExtension(RpcConstants.ZOOKEEPER_REGISTRY_TYPE));
    }
}
