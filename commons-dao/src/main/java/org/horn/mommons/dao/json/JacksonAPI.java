package org.horn.mommons.dao.json;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.codehaus.jackson.*;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.JsonNodeFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

//import net.sf.json.JSONArray;
//hive-exc 的包（和jackson map包冲突）
//import org.codehaus.jackson.xml.XmlMapper;

/**
 * 将java对象转换成JSON字符串，也可以将JSON字符串转换成java对象 jar-lib-version: jackson-all-1.6.2
 * jettison-1.0.1
 * 
 * @author hoojo
 * @createDate 2010-11-23 下午04:54:53
 * @file JacksonTest.java
 * @package com.hoo.test
 * @project Spring3
 * @blog http://blog.csdn.net/IBM_hoojo
 * @email hoojo_@126.com
 * @version 1.0
 */
public class JacksonAPI {
	@SuppressWarnings("unchecked")
	private JsonGenerator jsonGenerator = null;
	public ObjectMapper objectMapper = null;
	private People bean = null;

	public void init() {
		bean = new People();
		bean.setAddress("china-Guangzhou");
		bean.setEmail("hoojo_@126.com");
		bean.setId(1);
		bean.setName("hoojo");

		objectMapper = new ObjectMapper();
		try {
			jsonGenerator = objectMapper.getJsonFactory().createJsonGenerator(System.out, JsonEncoding.UTF8);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void destory() {
		try {
			if (jsonGenerator != null) {
				jsonGenerator.flush();
			}
			if (!jsonGenerator.isClosed()) {
				jsonGenerator.close();
			}
			jsonGenerator = null;
			objectMapper = null;
			bean = null;
			System.gc();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		JacksonAPI jt = new JacksonAPI();
		jt.init();

		 jt.writeJava2JSONStr();
//		jt.writeOthersJSON();
		//
		//
		// jt.readJson2Map();
		// jt.readJson2List();
		// jt.readJson2ArrayString();

		String jsonarr = "[\"210053266\",\"210054795\"]";
		String jsonobarr = "[{\"address\": \"address2\",\"name\":\"haha2\",\"id\":2,\"email\":\"email2\"},"
				+ "{\"address\":\"address\",\"name\":\"haha\",\"id\":1,\"email\":\"email\"}]";
		List<LinkedHashMap<String, Object>> obarr = jt.objectMapper.readValue(jsonarr, List.class);
		List jsonlist = jt.toJsonListString(jsonarr);
		// List jsonlist2 = jt.toJsonListString(jsonobarr);

	}

	public List toJsonListString(String injsonarr) throws JsonParseException, JsonMappingException, IOException {
		List<String> outjsonarr = new ArrayList<String>();
		List<Object> obarr = objectMapper.readValue(injsonarr, List.class);
		if (injsonarr.startsWith("[{")) {
			for (Object object : obarr) {
				String objson = objectMapper.writeValueAsString(object);
				outjsonarr.add(objson);
			}
		} else {
			for (Object object : obarr) {
				outjsonarr.add(object.toString());
			}
		}
		return outjsonarr;
	}

	/********************************** 序列化 **************************************/
	/**
	 * 1-将java对象转换成json字符串
	 * 
	 * @author hoojo
	 * @throws IOException
	 * @throws JsonProcessingException
	 * @createDate 2010-11-23 下午06:01:10
	 */
	public void writeJava2JSONStr() throws JsonProcessingException, IOException {
		//ob
		People bean1 = bean;
		//map
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", bean.getName());
		map.put("account", bean);
		bean = new People();
		bean.setAddress("china-Beijin");
		bean.setEmail("hoojo@qq.com");
		map.put("account2", bean);
		//list
		List<People> list = new ArrayList<People>();
		list.add(bean);
		bean = new People();
		bean.setId(2);
		bean.setAddress("address2");
		bean.setEmail("email2");
		bean.setName("haha2");
		list.add(bean);
		
		System.out.println("------jsonGenerator");
		// writeObject可以转换java对象，eg:JavaBean/Map/List/Array等
		jsonGenerator.writeObject(bean);	System.out.println();
		jsonGenerator.writeObject(map);		System.out.println();
		jsonGenerator.writeObject(list);	System.out.println();
		
		// writeValue具有和writeObject相同的功能
		System.out.println("------ObjectMapper");
		
//		objectMapper.writeValue(System.out, bean);	System.out.println();
//		objectMapper.writeValue(System.out, map);	System.out.println();
//		objectMapper.writeValue(System.out, list);	System.out.println();
		
		String jsonstr_ob = objectMapper.writeValueAsString(bean);
		String jsonstr_map = objectMapper.writeValueAsString(map);
		String jsonstr_list = objectMapper.writeValueAsString(list);
		System.out.println(jsonstr_ob);
		System.out.println(jsonstr_map);
		System.out.println(jsonstr_list);
	}

	/**
	 * 下面来看看jackson提供的一些类型，用这些类型完成json转换；
	 * 如果你使用这些类型转换JSON的话，那么你即使没有JavaBean(Entity)也可以完成复杂的Java类型的JSON转换。
	 * 下面用到这些类型构建一个复杂的Java对象，并完成JSON转换。
	 */
	public void writeOthersJSON() {
		try {
			String[] arr = { "a", "b", "c" };
			System.out.println("jsonGenerator");
			String str = "hello world jackson!";
			// byte
			jsonGenerator.writeBinary(str.getBytes());
			// boolean
			jsonGenerator.writeBoolean(true);
			// null
			jsonGenerator.writeNull();
			// float
			jsonGenerator.writeNumber(2.2f);
			// char
			jsonGenerator.writeRaw("c");
			// String
			jsonGenerator.writeRaw(str, 5, 10);
			// String
			jsonGenerator.writeRawValue(str, 5, 5);
			// String
			jsonGenerator.writeString(str);
			jsonGenerator.writeTree(JsonNodeFactory.instance.POJONode(str));
			System.out.println();

			// Object
			jsonGenerator.writeStartObject();// {
			jsonGenerator.writeObjectFieldStart("user");// user:{
			jsonGenerator.writeStringField("name", "jackson");// name:jackson
			jsonGenerator.writeBooleanField("sex", true);// sex:true
			jsonGenerator.writeNumberField("age", 22);// age:22
			jsonGenerator.writeEndObject();// }

			jsonGenerator.writeArrayFieldStart("infos");// infos:[
			jsonGenerator.writeNumber(22);// 22
			jsonGenerator.writeString("this is array");// this is array
			jsonGenerator.writeEndArray();// ]

			jsonGenerator.writeEndObject();// }

			People bean = new People();
			bean.setAddress("address");
			bean.setEmail("email");
			bean.setId(1);
			bean.setName("haha");
			// complex Object
			jsonGenerator.writeStartObject();// {
			jsonGenerator.writeObjectField("user", bean);// user:{bean}
			jsonGenerator.writeObjectField("infos", arr);// infos:[array]
			jsonGenerator.writeEndObject();// }

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/********************************* 反序列化 **************************************/
	/**
	 * 4-将json字符串转换成JavaBean对象
	 */
	public void readJson2Entity() {
		String json = "{\"address\":\"address\",\"name\":\"haha\",\"id\":1,\"email\":\"email\"}";
		try {
			People acc = objectMapper.readValue(json, People.class);
			System.out.println(acc.getName());
			System.out.println(acc);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 5-json字符串转换成list<map>
	 * 
	 * @author hoojo
	 * @createDate 2010-11-23 下午06:12:01
	 */
	public void readJson2List() {
		String json = "[{\"address\": \"address2\",\"name\":\"haha2\",\"id\":2,\"email\":\"email2\"},"
				+ "{\"address\":\"address\",\"name\":\"haha\",\"id\":1,\"email\":\"email\"}]";

		try {
			List<LinkedHashMap<String, Object>> list = objectMapper.readValue(json, List.class);
			System.out.println(list.size());
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);

				Set<String> set = map.keySet();
				for (Iterator<String> it = set.iterator(); it.hasNext();) {
					String key = it.next();
					System.out.println(key + ":" + map.get(key));
				}
				System.out.println(list.get(i));
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 6-json字符串转换成Array
	 * 
	 * @author hoojo
	 * @createDate 2010-11-23 下午06:14:01
	 */
	public void readJson2Array() {
		String json = "[{\"address\": \"address2\",\"name\":\"haha2\",\"id\":2,\"email\":\"email2\"},"
				+ "{\"address\":\"address\",\"name\":\"haha\",\"id\":1,\"email\":\"email\"}]";
		try {
			People[] arr = objectMapper.readValue(json, People[].class);
			System.out.println(arr.length);
			for (int i = 0; i < arr.length; i++) {
				System.out.println(arr[i]);
			}

		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 7-json字符串转换Map集合
	 * 
	 * @author hoojo
	 * @createDate Nov 27, 2010 3:00:06 PM
	 */
	public void readJson2Map() {
		String json = "{\"success\":true,\"A\":{\"address\": \"address2\",\"name\":\"haha2\",\"id\":2,\"email\":\"email2\"},"
				+ "\"B\":{\"address\":\"address\",\"name\":\"haha\",\"id\":1,\"email\":\"email\"}}";
		try {
			Map<String, Map<String, Object>> maps = objectMapper.readValue(json, Map.class);
			System.out.println(maps.size());
			Set<String> key = maps.keySet();
			Iterator<String> iter = key.iterator();
			while (iter.hasNext()) {
				String field = iter.next();
				System.out.println(field + ":" + maps.get(field));
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 8-java对象转换成xml文档 需要额外的jar包 stax2-api.jar
	 * 
	 * @author hoojo
	 * @createDate 2010-11-23 下午06:11:21
	 */
	public void writeObject2Xml() {
		// stax2-api-3.0.2.jar
		System.out.println("XmlMapper");
		XmlMapper xml = new XmlMapper();

		try {
			// javaBean转换成xml
			// xml.writeValue(System.out, bean);
			StringWriter sw = new StringWriter();
			xml.writeValue(sw, bean);
			System.out.println(sw.toString());
			// List转换成xml
			List<People> list = new ArrayList<People>();
			list.add(bean);
			list.add(bean);
			System.out.println(xml.writeValueAsString(list));

			// Map转换xml文档
			Map<String, People> map = new HashMap<String, People>();
			map.put("A", bean);
			map.put("B", bean);
			System.out.println(xml.writeValueAsString(map));
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

// /////////////////////////////////////////////////////////////////////////////////////////
class People {
	private int id;
	private String name;
	private String email;
	private String address;
	private Birthday birthday;

	// getter、setter

	@Override
	public String toString() {
		return this.name + "#" + this.id + "#" + this.address + "#" + this.birthday + "#" + this.email;
	}

	public Object getName() {
		// TODO Auto-generated method stub
		return this.name;
	}

	public void setName(String string) {
		// TODO Auto-generated method stub
		this.name = string;
	}

	public void setId(int i) {
		// TODO Auto-generated method stub
		this.id = i;
	}

	public int getId() {
		return this.id;
	}

	public void setEmail(String string) {
		// TODO Auto-generated method stub
		this.email = string;
	}

	public String getEmail() {
		return this.email;
	}

	public void setAddress(String string) {
		// TODO Auto-generated method stub
		this.address = string;
	}

	public String getAddress() {
		return this.address;
	}
}

class Birthday {
	private String birthday;

	public Birthday(String birthday) {
		super();
		this.birthday = birthday;
	}

	// getter、setter

	public Birthday() {
	}

	@Override
	public String toString() {
		return this.birthday;
	}
}
