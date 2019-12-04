package org.horn.mommons.dao.json.udf;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * 推荐结果列表的json字符串中解析是否包含itemId
 * 
 * @author lihongen
 *
 */
public class IsJsonContainItem extends UDF {
	private JSONArray jsonArray = null;
	private JSONObject jsonObject = null;

	/**
	 * 解析json字符串是否包含 itemid，包含返回true
	 * @param jsonStr
	 * @param itemId
	 * @param jsonType(1 字符串的json数组，2 是jsonObject的json数组)
	 * @return
	 */
	public BooleanWritable evaluate(Text jsonStr, Text itemId, IntWritable jsonType) {
		HashSet<String> itemSet = new HashSet<String>();
		if (jsonType.get() == 1) {
			jsonArray = JSONArray.fromObject(jsonStr.toString());

			for (Object ob : jsonArray) {
				itemSet.add(ob.toString());
			}
			return new BooleanWritable(itemSet.contains(itemId.toString()));

		} else if (jsonType.get() == 2) {
			jsonArray = JSONArray.fromObject(jsonStr.toString());

			for (Object ob : jsonArray) {
				jsonObject = (JSONObject) ob;
				itemSet.add(jsonObject.getString("itemID"));
			}
			return new BooleanWritable(itemSet.contains(itemId.toString()));

		} else {
			return null;
		}
	}

	public static void main(String[] args) throws IOException {
		// read file
		BufferedReader br = new BufferedReader(new FileReader("D:\\MY FILES\\FUSHU\\Local\\data\\jsontest"));
		ArrayList<String> list = new ArrayList<String>();
		String line;
		while ((line = br.readLine()) != null) {
			list.add(line);
		}
		br.close();

		// 1 为item数组
		JSONArray jsonArray1 = JSONArray.fromObject(list.get(0));
		for (Object object : jsonArray1) {
			System.out.println("1-----jsonarray1: " + object);
		}

		// 2为json对象数组
		JSONArray jsonArray2 = JSONArray.fromObject(list.get(1));
		for (Object object : jsonArray2) {
			JSONObject jsonob1 = (JSONObject) object;
			System.out.println("2-----jsonarray2: " + object);
			System.out.println("22----jsonarray2: " + jsonob1.getString("itemID"));
		}

		// test evaluate
		IsJsonContainItem is = new IsJsonContainItem();
		boolean iscontain = is.evaluate(new Text(list.get(1)), new Text("5033000"), new IntWritable(2)).get();
		System.out.println(iscontain);
		boolean iscontain2 = is.evaluate(new Text(list.get(0)), new Text("210049877"), new IntWritable(1)).get();
		System.out.println(iscontain2);
	}
}
