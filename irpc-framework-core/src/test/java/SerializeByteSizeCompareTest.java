import com.paddi.core.serialize.SerializeFactory;
import com.paddi.core.serialize.fastjson.FastJsonSerializeFactory;
import com.paddi.core.serialize.hessian.HessianSerializeFactory;
import com.paddi.core.serialize.jdk.JdkSerializeFactory;
import com.paddi.core.serialize.kryo.KryoSerializeFactory;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月08日 10:09:46
 */
public class SerializeByteSizeCompareTest {
    private static User buildUserDefault() {
        User user = new User();
        user.setAge(11);
        user.setAddress("深圳市南山区");
        user.setBankNo(12897873624813L);
        user.setSex(1);
        user.setId(10001);
        user.setIdCardNo("440308781129381222");
        user.setRemark("备注信息字段");
        user.setUsername("ddd-user-name");
        return user;
    }

    public void jdkSerializeSizeTest() {
        SerializeFactory serializeFactory = new JdkSerializeFactory();
        User user = buildUserDefault();
        byte[] result = serializeFactory.serialize(user);
        System.out.println("jdk's size is "+result.length);
    }

    public void hessianSerializeSizeTest() {
        SerializeFactory serializeFactory = new HessianSerializeFactory();
        User user = buildUserDefault();
        byte[] result = serializeFactory.serialize(user);
        User deserializeUser = serializeFactory.deserialize(result, User.class);
        System.out.println("hessian's size is "+result.length);
    }

    public void fastJsonSerializeSizeTest() {
        SerializeFactory serializeFactory = new FastJsonSerializeFactory();
        User user = buildUserDefault();
        byte[] result = serializeFactory.serialize(user);
        User deserializeUser = serializeFactory.deserialize(result, User.class);
        System.out.println("fastJson's size is "+result.length);
    }

    public void kryoSerializeSizeTest() {
        SerializeFactory serializeFactory = new KryoSerializeFactory();
        User user = buildUserDefault();
        byte[] result = serializeFactory.serialize(user);
        User deserializeUser = serializeFactory.deserialize(result, User.class);
        System.out.println("kryo's size is "+result.length);
    }

    public static void main(String[] args) {
        SerializeByteSizeCompareTest serializeByteSizeCompareTest = new SerializeByteSizeCompareTest();
        serializeByteSizeCompareTest.fastJsonSerializeSizeTest();
        serializeByteSizeCompareTest.jdkSerializeSizeTest();
        serializeByteSizeCompareTest.kryoSerializeSizeTest();
        serializeByteSizeCompareTest.hessianSerializeSizeTest();
    }
}
