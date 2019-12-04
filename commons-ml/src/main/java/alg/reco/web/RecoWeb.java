package alg.reco.web;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;

public class RecoWeb {
	//配置属性 _
	public static int _recoLength;//
	public static String _mySQLConnection;
	public static String _jdbcHiveConnection;
	public static String _QueryProcess;	//进程表 --查询
	public static String _QueryWeb;		//web历史表--查询
	public static String _ColumnUser;	//用户列
	public static String _ColumnItem;	//项目列
	
	//类属性
	public static String yestoday="20140418";		//昨天的日期格式：20140518
	static int ratingSize;
	static int usersize;
	static int itemsize;

	public static Map<String,HashSet<String>>	history = new HashMap<String,HashSet<String>>();	//用户历史
	public static Map<String, Item> itemBank = new HashMap<String, Item>();	//物品库
	public static Map<String,String>	recolist = new HashMap<String,String>();//格式<用户识别码，推荐列表“，”隔开>
	
	public static void main(String[] args) throws SQLException, IOException {
//		getyestoday();
		siteProperties("webreco-site.properties");
		readHive();
		recommender();
	}
	//获得前一天的日期--每次处理前一天的数据
	public static void getyestoday(){
		Calendar cal=Calendar.getInstance();
        cal.add(Calendar.DATE,-1);
        Date d=(Date) cal.getTime();
        SimpleDateFormat sp=new SimpleDateFormat("yyyyMMdd");
        yestoday = sp.format(d);//获取昨天日期
//        System.out.println("昨天="+yestoday);
	}	
	//JDBC TO HIVE
	public static void readHive() throws IOException, SQLException {
		usersize = 0;
		itemsize = 0;
		ratingSize = 0;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		long runningtime = System.currentTimeMillis();		//记录时间
		
		try {
			Class.forName("org.apache.hadoop.hive.jdbc.HiveDriver"); // Hive			
			conn = DriverManager.getConnection(_jdbcHiveConnection,"",""); 
			stmt = conn.createStatement(); 
			//query语句（终端号，进程名，地区号，<警种>,部门）
			rs = stmt.executeQuery("select devonlyid,host from browsewebpagelog where dt='"+yestoday+"'" ); 
						
			String userNameCache = null;			// 记录数据上一行（用户名）字符串缓存
			int	userIdCache = 0;					//记录数据上一行（用户名）id缓存			
			HashSet<String> currentUserSet = new HashSet<String>();
			
			while(rs.next()) {
				ratingSize++;						//数据总条数			
				String userName = rs.getString("devonlyid");
				String itemName = rs.getString("host");
//				System.out.println(userName);
				
				//识别〈新物品〉,itemname
				if(!itemBank.containsKey(itemName)) {
					itemBank.put(itemName, new Item(itemName,0));//K编码st[1],V(mid)
					itemsize++;
				}
				
				// 判断新用户
				if(userName!=userNameCache) {//相邻两行用户名不一样<可能新用户>
					userNameCache = userName;		//更新当前用户编号		
					// 是否新用户
					if(!history.containsKey(userNameCache)) {	// 新用户				
						currentUserSet = new HashSet<String>();
						history.put(userNameCache, currentUserSet);	//history加薪用户
						usersize++; 							//用户数+1
					}else//老用户
						currentUserSet = history.get(userNameCache);
				}
				
				// 无论新旧用户和物品，history都要载入数据集
				currentUserSet.add(itemName);					//增加当前用户的历史物品表
				itemBank.get(itemName).ratingCount++;			//用户数+1
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace(); 
			System.exit(1); 
		}finally {
			try {
				if(rs != null) {
					rs.close();
					rs = null;
				}
				if(stmt != null) {
					stmt.close();
					stmt = null;
				}
				if(conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("\r<br>读取Hive耗时 : "+(System.currentTimeMillis()-runningtime)/1000f+" 秒 ");//记录时间
		System.out.println("ratingSize="+ratingSize);
		System.out.println("itemsize="+itemsize);
		System.out.println("usersize="+usersize);
		System.out.println("history.size()="+history.size());
		System.out.println("itemBank.size()="+itemBank.size());
	}	
	public static void siteProperties(String proper) throws IOException{
		Properties properties = new Properties();
		properties.load(RecoWeb.class.getResourceAsStream(proper));

		_mySQLConnection = properties.getProperty("MYSQLCONNECTION").trim();
		_jdbcHiveConnection = properties.getProperty("JDBCCONNECTION").trim();
		_ColumnUser = properties.getProperty("COLUMNUSER").trim();
		_ColumnItem = properties.getProperty("COLUMNITEM").trim();
		//int,double,float
		_recoLength = Integer.parseInt(properties.getProperty("RECOLENGTH").trim());
	}
	
	@SuppressWarnings("unchecked")
	public static void recommender(){
		//排序
		Collection<Item> item = itemBank.values();
		Vector<Item>	sortedItemBank = new Vector<Item>();//排序好的物品集合
		sortedItemBank.addAll(item);
		
		Comparator ct = new MyCompare();
		Collections.sort(sortedItemBank, ct);				//按项目的使用次数排序
		
		for (int i = 0; i < itemBank.size(); i++)
			System.out.println(itemBank.get(i).ratingCount);
		
		//推荐10个				
		Set<String> oneuserhis = history.keySet();		//用户为key集合
		for(Iterator it = oneuserhis.iterator(); it.hasNext();){
			String	recoOne = null;						//该用户临时推荐表
			Iterator<Item> ite = sortedItemBank.iterator();
			int recoLength=0;							//标记当前推荐列表长度
			while(ite.hasNext()){
				if(!oneuserhis.contains(ite.next().itemName)&&recoLength<=_recoLength){
					recoOne += ite.next().itemName;		//推荐列表加入itemName
				}else if(recoLength>_recoLength)
					break;
			}
			recolist.put(it.toString(), recoOne);
		}
	}
}

class Item {//项目
	public String itemName;
	public int ratingCount;
	public int classid;//类别
//	public double pseudoAvg;
	
	Item(String item,int rc){
		this.itemName = item;
		this.ratingCount = rc;
	}
}

class MyCompare implements Comparator //实现Comparator，定义自己的比较方法
{
	public int compare(Object o1, Object o2) {
		Item e1=(Item)o1;
		Item e2=(Item)o2;
	
		if(e1.ratingCount > e2.ratingCount){//这样比较是降序,如果把-1改成1就是升序.
			return -1;
		}else if(e1.ratingCount<e2.ratingCount)	{
			return 1;
		}
		else{
			return 0;
		}
	}
}