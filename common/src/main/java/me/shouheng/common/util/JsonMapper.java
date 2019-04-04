package me.shouheng.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * 基于 fastxml 的 json 映射工具
 *
 * @author shouh, 2019/4/4-23:07
 */
public class JsonMapper {

    private static Logger logger = LoggerFactory.getLogger(JsonMapper.class);

    private ObjectMapper mapper;

    private static SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public JsonMapper() {
        this(null);
    }

    public JsonMapper(JsonInclude.Include include) {
        this(include, fmt);
    }

    public JsonMapper(JsonInclude.Include include, DateFormat dateFormat) {
        mapper = new ObjectMapper();

        if (dateFormat != null) {
            mapper.setDateFormat(dateFormat);
        }

        // 设置输出时包含属性的风格
        if (include != null) {
            mapper.setSerializationInclusion(include);
        }

        // 设置输入时忽略在 JSON 字符串中存在但 Java 对象实际没有的属性
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    /**
     * 创建只输出非 Null 且非 Empty (如 List.isEmpty) 的属性到 Json 字符串的 Mapper, 建议在外部接口中使用.
     *
     * @return mapper 对象
     */
    public static JsonMapper nonEmptyMapper() {
        return new JsonMapper(JsonInclude.Include.NON_EMPTY);
    }

    /**
     * 创建只输出初始值被改变的属性到 Json 字符串的 Mapper, 最节约的存储方式，建议在内部接口中使用。
     *
     * @return mapper 对象
     */
    public static JsonMapper nonDefaultMapper() {
        return new JsonMapper(JsonInclude.Include.NON_DEFAULT);
    }

    /**
     * 移除 null 字段
     *
     * @return mapper 对象
     */
    public static JsonMapper nonNullMapper() {
        return new JsonMapper(JsonInclude.Include.NON_NULL);
    }

    /**
     * 时间格式默认为 long
     *
     * @return mapper 对象
     */
    public static JsonMapper nonNullAndDefaultDateFormatMapper() {
        return new JsonMapper(JsonInclude.Include.NON_NULL, null);
    }

    /**
     * Object 可以是 POJO，也可以是 Collection 或数组。
     * 如果对象为Null, 返回 "null"，如果集合为空集合, 返回" []".
     *
     * @param object 对象
     * @return json 字符串
     */
    public String toJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (IOException e) {
            logger.warn("write to json string error:" + object, e);
            return null;
        }
    }

    /**
     * 转换成 json
     *
     * @param object 对象
     * @param outerClass 外部类型
     * @param innerClass 嵌套对象，默认会根据 JsonTypeInfo 加上 @class 字段
     * @return json 字符串
     */
    public String toJsonWithInnerType(Object object, Class outerClass, Class innerClass) {
        try {
            return mapper.writerWithType(this.createCollectionType(outerClass, innerClass)).writeValueAsString(object);
        } catch (IOException e) {
            logger.warn("write to json string error:" + object, e);
            return null;
        }
    }

    /**
     * 反序列化POJO或简单Collection如List<String>.
     * <p/>
     * 如果JSON字符串为Null或"null"字符串, 返回Null.
     * 如果JSON字符串为"[]", 返回空集合.
     * <p/>
     * 如需反序列化复杂Collection如List<MyBean>, 请使用fromJson(String,JavaType)
     *
     * @see #fromJson(String, JavaType)
     */
    public <T> T fromJson(String jsonString, Class<T> clazz) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }

        try {
            return mapper.readValue(jsonString, clazz);
        } catch (IOException e) {
            logger.warn("parse json string error:" + jsonString, e);
            return null;
        }
    }

    /**
     * 反序列化复杂Collection如List<Bean>, 先使用函數createCollectionType构造类型,然后调用本函数.
     *
     * @see #createCollectionType(Class, Class...)
     */
    @SuppressWarnings("unchecked")
    public <T> T fromJson(String jsonString, JavaType javaType) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }

        try {
            return (T) mapper.readValue(jsonString, javaType);
        } catch (IOException e) {
            logger.warn("parse json string error:" + jsonString, e);
            return null;
        }
    }

    /**
     * 反序列化复杂Collection如List<Bean>,
     *
     * @see #createCollectionType(Class, Class...)
     */
    @SuppressWarnings("unchecked")
    public <T> T fromJson(String jsonString, Class<T> outClass, Class<?> innerClass) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }

        try {
            return (T) mapper.readValue(jsonString, this.createCollectionType(outClass, innerClass));
        } catch (IOException e) {
            logger.warn("parse json string error:" + jsonString, e);
            return null;
        }
    }

    /**
     * 構造泛型的Collection Type如:
     * ArrayList<MyBean>, 则调用constructCollectionType(ArrayList.class,MyBean.class)
     * HashMap<String,MyBean>, 则调用(HashMap.class,String.class, MyBean.class)
     */
    public JavaType createCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    /**
     * 當JSON裡只含有Bean的部分屬性時，更新一個已存在Bean，只覆蓋該部分的屬性.
     */
    public <T> T update(String jsonString, T object) {
        try {
            return (T) mapper.readerForUpdating(object).readValue(jsonString);
        } catch (IOException e) {
            logger.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
        }
        return null;
    }

    /**
     * 輸出JSONP格式數據.
     */
    public String toJsonP(String functionName, Object object) {
        return toJson(new JSONPObject(functionName, object));
    }

    /**
     * 設定是否使用Enum的toString函數來讀寫Enum,
     * 為False時時使用Enum的name()函數來讀寫Enum, 默認為False.
     * 注意本函數一定要在Mapper創建後, 所有的讀寫動作之前調用.
     */
    public void enableEnumUseToString() {
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
    }

    /**
     * 取出Mapper做进一步的设置或使用其他序列化 API.
     */
    public ObjectMapper getMapper() {
        return mapper;
    }

}
