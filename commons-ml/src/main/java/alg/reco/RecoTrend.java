package alg.reco;

import alg.reco.svd.SVDEngine;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;

public class RecoTrend {
	//配置属性 _
	public static int _recoLength;//
	public static String _mySQLConnection;
	public static String _jdbcHiveConnection;
	public static String _QueryProcess;	//进程表 --查询
	public static String _QueryWeb;		//web历史表--查询
	public static String _ColumnUser;	//用户列
	public static String _ColumnItem;	//项目列
	
	//类属性
	public static String yestoday="20140418";		//昨天的日期格式：20140518，真正使用时要把="20140418"去掉，测试不用去
	static int ratingSize;
	static int usersize;
	static int itemsize;

	public static Vector<Set<Integer>>	table = new Vector(new HashSet<Integer>());	//用户历史
	public static Map<String,Integer>	userIndex = new HashMap<String,Integer>();	//用户索引
	public static Map<String,Integer> itemIndex = new HashMap<String,Integer>();	//物品索引
	public static Vector<Item>	itemBank = new Vector<Item>();		//物品库
	public static Vector<String>	userBank = new Vector<String>();//用户库
	public static Vector<String>	recolist = new Vector<String>();//推荐列表“，”隔开
	
	public static void main(String[] args) throws SQLException, IOException {
//		getyestoday();//真正使用时，要处理昨天的数据，要是在我们服务器上时，昨天的数据不一定有，测试就要设置yestoday为哪一天
		siteProperties("svd-site.properties");
		readMySQL();
		readHive();
		recommender();
	}
	public static void getyestoday(){
		Calendar cal=Calendar.getInstance();
        cal.add(Calendar.DATE,-1);
        Date d=(Date) cal.getTime();
        SimpleDateFormat sp=new SimpleDateFormat("yyyyMMdd");
        yestoday = sp.format(d);//获取昨天日期
//        System.out.println("昨天="+yestoday);
	}
	//JDBC TO MYSQL
	public static void readMySQL() throws IOException, SQLException{
		//MySQL
//		Connection conMySQL = DriverManager.getConnection(_mySQLConnection);		
//		Statement stmtMySQL = conMySQL.createStatement();
//		ResultSet res = stmtMySQL.executeQuery("select * from data"); //browsewebpagelog where dt='20140424'"
	}
	
	//JDBC TO HIVE
	public static void readHive() throws IOException, SQLException {
		usersize = 0;
		itemsize = 0;
		ratingSize = 0;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		long a=System.currentTimeMillis();		//记录时间
		
		try {
			Class.forName("org.apache.hadoop.hive.jdbc.HiveDriver"); // Hive			
			conn = DriverManager.getConnection(_jdbcHiveConnection,"",""); 
			stmt = conn.createStatement(); 
			//query语句（终端号，进程名，地区号，<警种>,部门）
			rs = stmt.executeQuery("select devonlyid,name from processinfolog where dt='"+yestoday+"'" ); 
			
			String userNameCache = null;			// 记录数据上一行（用户名）字符串缓存
			int	userIdCache = 0;					//记录数据上一行（用户名）id缓存			
		
			while(rs.next()) {
				ratingSize++;						//数据总条数			
				String userName = rs.getString(_ColumnUser);
				String itemName = rs.getString(_ColumnItem);
//				System.out.println(userName);
				// 入库map and vector,识别st[0],username
				if(userName!=userNameCache) {//相邻两行用户名不一样<可能新用户>
					userNameCache = userName;				
					// 是否新用户
					if(!userIndex.containsKey(userNameCache)) {// <新用户>map和bank中新加入
						userIdCache = usersize;//新用户id
						userIndex.put(userNameCache, usersize);//K(编码st[0],V(uid=usersize))
						userBank.add(userNameCache);			//Bank收入，bank[usersize]=st[0]
						table.add(new HashSet<Integer>()); //加表 table[usersize]->vector
						usersize++; //用户数+1
					}else//老用户
						userIdCache = userIndex.get(userName).intValue();
				}
				//识别〈新物品〉,itemname
				if(!itemIndex.containsKey(itemName)) {
					itemIndex.put(itemName, itemsize);//K编码st[1],V(mid)
					itemBank.add(new Item(itemName,0,itemsize));//新增加的物品，初始化使用次数为0
					itemsize++;
				}
				// 无论新旧用户和物品，table都要载入数据集
				int mid = itemIndex.get(itemName).intValue();	//uid=userIdCache		
				table.get(userIdCache).add(mid);				// 增加usersize用户的历史物品表
				itemBank.get(mid).ratingCount++;				//用户数+1
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
		
		System.out.println("\r<br>执行耗时 : "+(System.currentTimeMillis()-a)/1000f+" 秒 ");//记录时间
		System.out.println(ratingSize);
		System.out.println(itemsize);
		System.out.println(usersize);
		System.out.println(userIndex.size());
		System.out.println(userBank.size());
		System.out.println(itemIndex.size());
		System.out.println(itemBank.size());
	}	
	public static void siteProperties(String proper) throws IOException{
		Properties properties = new Properties();
		properties.load(SVDEngine.class.getResourceAsStream(proper));

		_mySQLConnection = properties.getProperty("MYSQLCONNECTION").trim();
		_jdbcHiveConnection = properties.getProperty("JDBCCONNECTION").trim();
		_QueryProcess = properties.getProperty("QUERYPROCESS").trim();
		_QueryWeb = properties.getProperty("QUERYWEB").trim();		

		_ColumnUser = properties.getProperty("COLUMNUSER").trim();
		_ColumnItem = properties.getProperty("COLUMNITEM").trim();
		//int,double,float
		_recoLength = Integer.parseInt(properties.getProperty("RECOLENGTH").trim());
	}
	
	@SuppressWarnings("unchecked")
	public static void recommender(){
		//排序
		Comparator ct = new MyCompare();
		Collections.sort(itemBank, ct);				//按项目的使用次数排序
		
		for (int i = 0; i < itemBank.size(); i++)
			System.out.println(itemBank.get(i).ratingCount);
		
		//推荐10个				
		for(Set<Integer> oneUser:table){
			Vector<Integer>	recotmp = new Vector<Integer>();//每个用户临时推荐表
			Iterator<Item> ite = itemBank.iterator();
			while(ite.hasNext()){
				if(!oneUser.contains(ite.next().id)&&recotmp.size()<=_recoLength){
					recotmp.add(ite.next().id);
				}else if(recotmp.size()>_recoLength)
					break;
			}
//			recolist.add(recotmp);//》》》》》》》》》》》》错误地方
		}
	}
}

class Item {//项目
	public String itemName;
	public int ratingCount;
	public int id;
	public int classid;//类别
//	public double pseudoAvg;
	
	Item(String item,int rc,int iid){
		this.itemName = item;
		this.ratingCount = rc;
		this.id = iid;
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