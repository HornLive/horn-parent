package org.horn.mommons.dao.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FastjsonAPI {
	// JSON的主要api
	// public static final Object parse(String text); //
	// 把JSON文本parse为JSONObject或者JSONArray
	// public static final JSONObject parseObject(String text); //
	// 把JSON文本parse成JSONObject
	// public static final <T> T parseObject(String text, Class<T> clazz); //
	// 把JSON文本parse为JavaBean

	// public static final JSONArray parseArray(String text); //
	// 把JSON文本parse成JSONArray
	// public static final <T> List<T> parseArray(String text, Class<T> clazz);
	// // 把JSON文本parse成JavaBean集合

	// public static final String toJSONString(Object object); //
	// 将JavaBean序列化为JSON文本
	// public static final String toJSONString(Object object, boolean
	// prettyFormat); // 将JavaBean序列化为带格式的JSON文本
	// public static final Object toJSON(Object
	// javaObject);//将JavaBean转换为JSONObject或者JSONArray。

	public static void main(String[] args) {
		User rootUser = new User(2l,"root");
//		rootUser.setId(2l);
//		rootUser.setName("root");
		User guestUser = new User(3l,"guest");
//		guestUser.setId(3l);
//		guestUser.setName("guest");
		
		Group group = new Group(0l,"admins");
//		group.setId(0l);
//		group.setName("admin");
		group.getUsers().add(rootUser);
		group.getUsers().add(guestUser);

		// 测试map和list和object
		HashMap<String, User> map = new HashMap<>();
		map.put("root", rootUser);
		map.put("guest", guestUser);

		List<User> list = new ArrayList<>();
		list.add(rootUser);
		list.add(guestUser);

		// encode
		String jsonUser = JSON.toJSONString(rootUser);
		
		String jsonString = JSON.toJSONString(group);
		System.out.println("object 2 json str :: " + jsonString);

		String jsonstr_map = JSON.toJSONString(map);
		System.out.println("map 2 json str :: " + jsonstr_map);

		String jsonstr_list = JSON.toJSONString(list);
		System.out.println("list 2 json str :: " + jsonstr_list);

		// Decode 代码示例：
		User user1 = JSON.parseObject(jsonUser, User.class);
		System.out.println(user1.getName());
		
		Group group2 = JSON.parseObject(jsonString, Group.class);
		System.out.println(group2.getUsers().get(0).getName());

		//JSONObject
		System.out.println("#################");
		System.out.println(jsonstr_map);
		Map<String, JSONObject> map2 = JSON.parseObject(jsonstr_map, Map.class);
		System.out.println(map2.get("root").toString());

		List<User> list2 = JSON.parseArray(jsonstr_list, User.class);
		System.out.println(list2.get(1).getName());

		JSONObject jsonOb = JSON.parseObject(jsonString);
		System.out.println(jsonOb.toString());

		JSONArray jsonArr = JSON.parseArray(jsonstr_list);
		System.out.println(jsonArr.toString());
	}
}

class User {
	private Long id;
	private String name;

	public User(Long id, String name) {
		setId(id);
		setName(name);
	}
	
	public User(){
		
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

class Group {
	private Long id;
	private String name;
	private List<User> users = new ArrayList<User>();

	public Group(Long id, String name) {
		setId(id);
		setName(name);
	}
	
	public Group(){
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
}