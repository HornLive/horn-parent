package test;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

	private static HashMap<String, String>  aa;
	public static void main(String[] args) {
//		testvector();
//		testset();
//		testfloatarray();
		
		String str = "指挥中心召开全体中层干部会议，传达                    "
				+ "   24312512488.,/-=\435fjsadifwerqwerdgadg                 学习全国公安厅局长座谈会精神16日上午，指挥中心召开全体中层干部会议，";
	    String reg = "[^\u4e00-\u9fa5]";
	    Pattern pat = Pattern.compile(reg);  
	    Matcher mat = pat.matcher(str); 
	    String repickStr = mat.replaceAll("");
		    System.out.println("去中文后:"+repickStr);
		for (int i = 0; i < repickStr.length()-1; i++) {
//			System.out.println(str.charAt(i));
			System.out.println(repickStr.substring(i, i+2));
		}
	}
	
	//测试日期
	public void testdata(){
		Calendar cal=Calendar.getInstance();
        cal.add(Calendar.DATE,-1);
        Date d=(Date) cal.getTime();

        SimpleDateFormat sp=new SimpleDateFormat("yyyyMMdd");
        String ZUOTIAN=sp.format(d);//获取昨天日期
        System.out.println(ZUOTIAN);
	}
	//测试vector
	public static void testvector(){
		Vector<Data> data = new Vector<Data>();
		data.setSize(100);
		data.add(101, new Data(2));
		data.set(1, new Data(23));
//		data.add(new Data(2));
//		data.add(new Data(3));
//		data.add(new Data(4));
		Iterator<Data> ite = data.iterator();
//		while(ite.hasNext()){
//			System.out.println(ite.next().id);
//		}
		System.out.println(data.get(9).id);
		System.out.println(data.get(1));
		System.out.println(data.size());
	}
	//测试set排序
	public static void testset(){
		Set<Integer> testset = new TreeSet<Integer>();
//		Set<Integer> testset = new HashSet<Integer>();
		testset.add(89);
		testset.add(29);
		testset.add(99);
		testset.add(149);
		testset.add(9);
		for(Integer inte:testset){
			System.out.println(inte);
//			inte.intValue();
		}
		Object[] a = testset.toArray();
		System.out.println(((Integer) a[1]).intValue());		
	}
	//测试引用内容的修改
	public static void testpara(HashMap<String, String>  b){
		b.put("ddd", "dfadf");//分别有两个引用a,b指向map
//		b=null;
//		b.clear();
		aa=null;
		System.out.println(b);
	}
	//数组的遍历不能用引用
	public static void testfloatarray(){
		float[] f = new float[10];
		for (int i = 0; i < 100; i++) {
			f[1]++;
		}
		System.out.println(f[1]);
	}
	
	
}


class Data{
	int id;
	int name;
	Data(int id){
		this.id = id;
	}
}


