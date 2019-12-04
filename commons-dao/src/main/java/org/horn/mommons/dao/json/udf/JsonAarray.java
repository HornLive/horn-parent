package org.horn.mommons.dao.json.udf;

import net.sf.json.JSONArray;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorConverters;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.io.Text;

import java.util.ArrayList;

public class JsonAarray extends GenericUDF{

	private transient ObjectInspectorConverters.Converter converter;
	private JSONArray jsonArray = null;
	private ArrayList<Text> result = new ArrayList<Text>();

	public Object evaluate(DeferredObject[] arg0) throws HiveException {
		assert(arg0.length ==1 );
		
		if(arg0[0].get() == null){
			return null;
		}
		
		String jsonArrayStr = ((Text) converter.convert(arg0[0].get())).toString();
		
		if(jsonArrayStr != null && !jsonArrayStr.equals("")){
			jsonArray = JSONArray.fromObject(jsonArrayStr);
			result.clear();
			for (Object ob : jsonArray) {
				result.add(new Text(ob.toString()));
			}
		}
		return result;
	}

	public String getDisplayString(String[] arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public ObjectInspector initialize(ObjectInspector[] args) throws UDFArgumentException {
		if(args.length != 1){
			throw new UDFArgumentException("参数个数唯一：json数组对象的字符串");
		}
		converter = ObjectInspectorConverters.getConverter(args[0], PrimitiveObjectInspectorFactory.writableStringObjectInspector);
		
		return ObjectInspectorFactory.getStandardListObjectInspector(PrimitiveObjectInspectorFactory.writableStringObjectInspector);
	}
}
