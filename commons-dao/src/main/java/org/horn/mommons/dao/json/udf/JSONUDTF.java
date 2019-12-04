package org.horn.mommons.dao.json.udf;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentLengthException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;

import java.util.ArrayList;

public class JSONUDTF extends GenericUDTF {

	public void close() throws HiveException {

	}

	// 返回UDTF的处理行的信息（个数，类型）。
	public StructObjectInspector initialize(ObjectInspector[] args)
			throws UDFArgumentException {
		if (args.length != 1) {
			throw new UDFArgumentLengthException(
					"ExplodeMap takes only one argument");
		}
		if (args[0].getCategory() != ObjectInspector.Category.PRIMITIVE) {
			throw new UDFArgumentException(
					"ExplodeMap takes string as a parameter");
		}
		ArrayList<String> fieldNames = new ArrayList<String>();
		ArrayList<ObjectInspector> fieldOIs = new ArrayList<ObjectInspector>();

		fieldNames.add("col1");
		fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
		fieldNames.add("col2");
		fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
		fieldNames.add("col3");
		fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
		fieldNames.add("col4");
		fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
		fieldNames.add("col5");
		fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
		fieldNames.add("col6");
		fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
		fieldNames.add("col7");
		fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
		fieldNames.add("col8");
		fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);

		return ObjectInspectorFactory.getStandardStructObjectInspector(
				fieldNames, fieldOIs);
	}

	// 对传入的参数进行处理，可以通过forword()方法返回结果
	public void process(Object[] args) throws HiveException {
		String input = args[0].toString();
		String[] splited = input.split("\001");

		String[] result = new String[8];

		for (int i = 0; i < splited.length; i++) {
			if (i == 0) {
				String head = splited[i];
				String userId = head.substring(0, head.indexOf("_"));
				String cookieId = head.substring(head.indexOf("_") + 1);
				
				result[0] = userId;
				result[1] = cookieId;
			} else {
				String json = splited[i];
				JSON jo = JSONSerializer.toJSON(json);
				Object o = JSONSerializer.toJava(jo);
				
				try{
					String sex = PropertyUtils.getProperty(o, "sex").toString();
					result[2] = sex;

					String age = PropertyUtils.getProperty(o, "age").toString();
					result[3] = age;
					
					String ppt = PropertyUtils.getProperty(o, "ppt").toString();
					result[4] = ppt;

					String degree = PropertyUtils.getProperty(o, "degree").toString();
					result[5] = degree;
					
					String favor = PropertyUtils.getProperty(o, "favor").toString();
					result[6] = favor;
					
					String commercial = PropertyUtils.getProperty(o, "commercial").toString();
					result[7] = commercial;
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}

		forward(result);
	}

}
