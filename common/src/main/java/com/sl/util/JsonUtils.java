package com.sl.util;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import net.sf.json.JSONObject;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * json转换，适应老接口
 */
public class JsonUtils {

	private static ObjectMapper MAPPER = new ObjectMapper();
	
	private static JsonFactory JSON_FACTORY = new JsonFactory();
	
	private static TypeFactory TYPE_FACTORY = TypeFactory.defaultInstance();

	public static <T> String beanToJson(T bean) {
	    if (null == bean) return "";
	    JSONObject jsonObject = JSONObject.fromObject(bean);
	    return jsonObject.toString();
	}

	


    /**
     * JSON 转换为 JAVA 对象
     * 
     * 示例: 
     * {
     *   MemcacheBean bean=Mapper.readValue(jsonAsString,MemcacheBean.class);
     *   
     *   #转Map
     *   Map<String, String> map =Mapper.readValue(jsonAsString, Map.class);
     * }
    */	
	public static <T> T readValue(String jsonAsString, Class<T> pojoClass)
			throws JsonMappingException, JsonParseException, IOException {
		return MAPPER.readValue(jsonAsString, pojoClass);
	}	

    
    /**
     * 序列化为集合类
     * 
     * 示例: 
     * {
     *  List<MemcacheBean> list=
     *    Mapper.readValue(listObjJsonStr, 
     *      TypeFactory.collectionType(ArrayList.class, MemcacheBean.class)
     *    );
     * }
    */
    @SuppressWarnings({ "unchecked", "deprecation" })
    public static <T> T readValue(String content, JavaType valueType)
        throws IOException, JsonParseException, JsonMappingException
    {
        return (T)MAPPER.readValue(JSON_FACTORY.createJsonParser(content), valueType);
    } 	
    
    public static String writeValueAsString(Object value)
        throws IOException, JsonGenerationException, JsonMappingException
    {        
        return MAPPER.writeValueAsString(value);
    }
    /**
     * Object -> String 转换异常时 输出默认String
     */
    public static String writeValueAsString(Object value,String _default){        
		try {
			return MAPPER.writeValueAsString(value);
		}catch (Exception e) {
			return _default;
		}
    }
    public static JsonNode readTree(String content)throws IOException, JsonProcessingException
    {
       return MAPPER.readTree(content);
    }
    
    /**
     * 获取一个类型工厂用来设置复杂对象，诸如Map<String,String>
     * @return
     */
    public static TypeFactory getDefaultTypeFactory(){
    	return TYPE_FACTORY;
    }
    
    /**
     * 获取一个集合的JavaType
     * @param collectionClass
     * @param elementClass
     * @return
     */
    @SuppressWarnings("rawtypes")
	public static CollectionType constructCollectionType(
			  Class<? extends Collection> collectionClass
			, Class<?> elementClass
	){    	
    	return TYPE_FACTORY.constructCollectionType(collectionClass,elementClass);
    }
    
    /**
     * 获得一个Map型的JavaType
     * @param mapClass
     * @param keyClass
     * @param valueClass
     * @return
     */
	@SuppressWarnings("rawtypes")
	public static MapType constructMapType(
			  Class<? extends Map> mapClass
			, Class<?> keyClass
			, Class<?> valueClass
	){    	
		return TYPE_FACTORY.constructMapType(mapClass, keyClass, valueClass);
	}
    public boolean isArray( String jsonStr ){
    	JSONObject object = JSONObject.fromObject(jsonStr);
    	return object.isArray();
    }



    
}
