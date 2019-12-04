package org.horn.mommons.dao.json;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.*;

public class NetSFJsonAPI {

	public static void main(String[] args) {
		// TestJsonBean();
		test2Bean();
		// TestJsonAttribute();
		// TestJsonArray();
	}

	@SuppressWarnings("rawtypes")
	private static void TestJsonArray() {
		Student student1 = new Student(1, "jon", "man", 25, new String[] { "篮球", "游戏" });
		Student student2 = new Student(2, "mary", "woman", 23, new String[] { "上网", "跳舞" });
		List<Student> list = new ArrayList<Student>();
		list.add(student1);
		list.add(student2);

		JSONArray jsonArray_list = JSONArray.fromObject(list);
		JSONArray jsonArray_array = JSONArray.fromObject(jsonArray_list.toArray());

		Collection java_collection = JSONArray.toCollection(jsonArray_array);

		for (Object o : java_collection) {
			JSONObject jsonObj = JSONObject.fromObject(o);
			Student stu = (Student) JSONObject.toBean(jsonObj, Student.class);
			System.out.println(stu.getName());
		}
	}

	private static void TestJsonAttribute() {
		// 添加JSONObject型属性
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("Int_att", 25);// 添加int型属性
		jsonObj.put("String_att", "str");// 添加string型属性
		jsonObj.put("Double_att", 12.25);// 添加double型属性
		jsonObj.put("Boolean_att", true);// 添加boolean型属性
		// 添加JSONArray型属性
		JSONArray jsonArray = new JSONArray();
		jsonArray.add("array0");
		jsonArray.add("array1");
		jsonArray.add("array2");
		jsonArray.add("array3");
		jsonObj.put("JSONArray_att", jsonArray);
		// test out
		System.out.println(jsonObj.toString());
		System.out.println("Int_att:" + jsonObj.getInt("Int_att"));
		System.out.println("String_att:" + jsonObj.getString("String_att"));
		System.out.println("Double_att:" + jsonObj.getDouble("Double_att"));
		System.out.println("Boolean_att:" + jsonObj.getBoolean("Boolean_att"));
		System.out.println("JSONObject_att:" + jsonObj.getJSONObject("JSONObject_att"));
		System.out.println("JSONArray_att:" + jsonObj.getJSONArray("JSONArray_att"));
	}

	/**
	 * java对象与json对象互相转换
	 */
	@SuppressWarnings("unchecked")
	private static void TestJsonBean() {
		// ob->jsonOb->ob
		// string->jsonOb->ob
		String student_json_str = "{\"age\":25,\"hobby\":[\"篮球\",\"上网\",\"跑步\",\"游戏\"],\"id\":1,\"name\":\"tom\",\"sex\":\"man\"}";
		Student student = new Student(2, "jon", "man", 25, new String[] { "篮球", "游戏" });

		JSONObject student1_json_ob = JSONObject.fromObject(student_json_str);
		JSONObject student2_json_ob = JSONObject.fromObject(student);

		Student stu1 = (Student) JSONObject.toBean(student1_json_ob, Student.class);
		Student stu2 = (Student) JSONObject.toBean(student2_json_ob, Student.class);

		System.out.println(stu1.getName());
		System.out.println(stu2.getName());

		// string json map 转换
		HashMap<String, Student> studentMap = new HashMap<>();
		studentMap.put(stu1.getName(), stu1);
		studentMap.put(stu2.getName(), stu2);

		JSONObject studentMap_json_ob = JSONObject.fromObject(studentMap);

		String studentMap_json_str = studentMap_json_ob.toString();
		JSONObject studentMap_json_ob2 = JSONObject.fromObject(studentMap_json_str);
		Map<String, Student> map = (Map<String, Student>) studentMap_json_ob2;

		System.out.println(map.get("tom"));
	}

	private static void test2Bean() {
		String returnString = "{\"age\":25,\"hobby\":[\"篮球\",\"上网\",\"跑步\",\"游戏\"],\"id\":1,\"name\":\"tom\",\"sex\":\"man\"}";
		JSONObject jsonObject = JSONObject.fromObject(returnString);
		Object returnObject = null;
		// 办法一 class+map config的方式三
		Map config = new HashMap();
		config.put("Student", Student.class);
		// config.put("sameTest", Person.class);
		returnObject = JSONObject.toBean(jsonObject, Student.class, config);
		System.out.println(returnObject);
	}
}
