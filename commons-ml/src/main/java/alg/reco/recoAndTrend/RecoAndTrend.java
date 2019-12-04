package alg.reco.recoAndTrend;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;

/*最后要把用户库userBank中的trend和recolist写入到MySQL数据库中*/
public class RecoAndTrend {
	//配置属性 _
	public static int _recoLength;		//推荐长度
	public static String _mySQLConnection;//链接到MySQL
	public static String _jdbcHiveConnection;//链接到Hive
	public static String _QueryProcess;	//进程表 --查询
	public static String _QueryWeb;		//web历史表--查询
	public static String _ColumnUser;	//用户列
	public static String _ColumnItem;	//项目列
	public static int _trendNum,_classNum;		//倾向性数量
	//类属性
	public static String yestoday="20140418";		//***********昨天的日期格式：20140518
	static int ratingSize;
	static int usersize;
	static int itemsize;
	public static String[] trend = new String[_trendNum];	//倾向表有5个倾向
	public static int[] appclass = new int[_classNum+1];	//分类对应到倾向id表
	public static Map<String, Process>	processBank = new HashMap<String, Process>();//从MySQL读入主进程库
	public static Map<String,HashSet<String>>	history = new HashMap<String,HashSet<String>>();	//用户历史
	public static Map<String, Item> itemBank = new HashMap<String, Item>();	//log数据集中的进程库
	public static Map<String, User>	userBank = new HashMap<String, User>();	//用户库
	
	public static void main(String[] args) throws SQLException, IOException {
//		getyestoday();
		siteProperties("recotrend-site.properties");
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
	//JDBC TO MYSQL 读取MySQL中的主进程-分类 表ga_appmainprocess提取倾向
	public static void readMySQL() throws IOException, SQLException{
		trend[0] = "上网";
		trend[1] = "休闲娱乐";
		trend[2] = "游戏";
		trend[3] = "其他";
		trend[4] = "办公";
		appclass[1] = 0;
		appclass[2] = 1;
		appclass[3] = 2;
		appclass[4] = 3;
		appclass[5] = 4;
		appclass[6] = 1;
		appclass[7] = 0;
		appclass[8] = 4;
		appclass[9] = 3;
		appclass[10] = 3;
		//MySQL
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");

			conn = DriverManager
					.getConnection(_mySQLConnection);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from ga_appmainprocess");
			while (rs.next()) {
				String pro = rs.getString("processName");	//进程名
				String app = rs.getString("appName");		//对应app名
				int classid =rs.getInt("classid");			//所属分类
				Process process = new Process(pro,app,classid);//新建进程对象
				process.trend = appclass[classid];		//设置该进程对象的所属倾向
				processBank.put(pro, process);			//加入进程库
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		} finally {
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
	}
	
	//JDBC TO HIVE 读取log数据processinfolog
	public static void readHive() throws IOException, SQLException {
		usersize = 0;		//记录用户数
		itemsize = 0;		//记录物品数
		ratingSize = 0;		//记录数据数
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		long runningtime = System.currentTimeMillis();		//记录时间
		
		try {
			Class.forName("org.apache.hadoop.hive.jdbc.HiveDriver"); // Hive			
			conn = DriverManager.getConnection(_jdbcHiveConnection,"",""); 
			stmt = conn.createStatement(); 
			//query语句（终端号，进程名，地区号，<警种>,部门）
			rs = stmt.executeQuery("select devonlyid,name from processinfolog where dt='"+yestoday+"'" ); 
						
			String userNameCache = null;			// 记录数据上一行（用户名）字符串缓存
			int	userIdCache = 0;					//记录数据上一行（用户名）id缓存			
			HashSet<String> currentUserSet = null;	//当前用户使用过的进程集合
			User currentUser = null;				//当前用户对象
			while(rs.next()) {
				ratingSize++;						//数据总条数			
				String userName = rs.getString(_ColumnUser);//终端机器唯一识别码（列）
				String itemName = rs.getString(_ColumnItem);//进程名（列）
//				System.out.println(userName);
				
				//识别〈新物品〉,itemname
				if(!itemBank.containsKey(itemName)) {
					itemBank.put(itemName, new Item(itemName,0));//Item（进程名，使用用户的数量）
					itemsize++;
				}
				
				// 判断新用户
				if(userName!=userNameCache) {		//<可能新用户>相邻两行用户名不一样
					userNameCache = userName;		//更新当前用户编号		
					// 是否新用户
					if(!history.containsKey(userNameCache)) {	// 新用户				
						currentUserSet = new HashSet<String>(); // 对于新用户，建立新的用户使用的进程集合，此时集合为空
						history.put(userNameCache, currentUserSet);	//history加薪用户
						usersize++; 							//用户数+1
						currentUser = new User();				//新建用户对象
						userBank.put(userNameCache, currentUser);//加入到用户库
					}else//数据集相邻两行用户识别码不一样，但是该用户已经存在，老用户，由于数据集每个用户的数用记录基本在一起，所以改行代码执行较少
						currentUserSet = history.get(userNameCache);//如果是老用户，其使用进程集合已经存在，获取集合对象
						currentUser = userBank.get(userNameCache);//获取改行用户
				}//数据集相邻两行用户识别码一样，也为老用户，继续使用上一次缓存的用户对象和该用户进程集合对象
				
				// 无论新旧用户和物品，history都要载入数据集
				currentUserSet.add(itemName);					//增加当前用户使用的进程集合-〉新加进程
				if(processBank.containsKey(itemName))			//如果mysql里进程表由此进程，则更新该用户相应的倾向
					currentUser.trend[processBank.get(itemName).trend]++;
				itemBank.get(itemName).ratingCount++;			//该进程的用户数+1
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
		properties.load(RecoAndTrend.class.getResourceAsStream(proper));

		_mySQLConnection = properties.getProperty("MYSQLCONNECTION").trim();
		_jdbcHiveConnection = properties.getProperty("JDBCCONNECTION").trim();
		_ColumnUser = properties.getProperty("COLUMNUSER").trim();
		_ColumnItem = properties.getProperty("COLUMNITEM").trim();
		//int,double,float
		_recoLength = Integer.parseInt(properties.getProperty("RECOLENGTH").trim());
		_trendNum = Integer.parseInt(properties.getProperty("TRENDNUM").trim());
		_classNum = Integer.parseInt(properties.getProperty("CLASSNUM").trim());
	}
	
	@SuppressWarnings("unchecked")
	public static void recommender(){
		//排序
		Collection<Item> item = itemBank.values();
		Vector<Item>	sortedItemBank = new Vector<Item>();//排序好的物品集合
		sortedItemBank.addAll(item);						//把所有进程对象加入到进程集合Vector中
		
		Comparator ct = new MyCompare();					//建立比较机制，方便按进程使用次数排序
		Collections.sort(sortedItemBank, ct);				//按进程的使用次数降序排序
		//测试
//		for (int i = 0; i < itemBank.size(); i++)
//			System.out.println(itemBank.get(i).ratingCount);
		
		//推荐10个				
		Set<String> userKey = history.keySet();			//所有用户识别码的集合
		
		for(Iterator thisuser = userKey.iterator(); thisuser.hasNext();){//遍历每个用户
			String	thisuerReco = null;						//该用户临时推荐表
			Iterator<Item> thisitem = sortedItemBank.iterator();
			int recoLength=0;							//标记当前推荐列表长度
			while(thisitem.hasNext()){//遍历进程库的每个进程
				if(!history.get(thisuser).contains(thisitem.next().itemName)&&recoLength<=_recoLength){//该用户没用过该进程，且此时给用户的推荐列表长度小于阈值
					if(processBank.containsKey(thisitem.next().itemName)){//若该进程在MySQL进程表库里
						String st1 = processBank.get(thisitem.next().itemName).app;//获得该进程对于app名
						thisuerReco += st1;								//推荐列表中加入app名
					}else
						thisuerReco += thisitem.next().itemName;		//推荐列表加入进程名itemName
				}else if(recoLength>_recoLength)
					break;
			}
			userBank.get(thisuser).recolist = thisuerReco;				//推荐列表加入到该用户
		}
	}
}

class User {
	public String userName;
	public	String recolist;
	public	int[]	trend = new int[RecoAndTrend._trendNum];
}

class Item {//从hive，log数据读取的进程对象（物品）
	public String itemName;
	public int ratingCount;
	public int classid;//类别
//	public double pseudoAvg;
	
	Item(String item,int rc){
		this.itemName = item;
		this.ratingCount = rc;
	}
}

class Process{//从MySQL读取的进程对象
	String process;
	String app;
	int classid;
	int trend;
	Process(String pro,String ap,int cid){
		this.process = pro;
		this.app = ap;
		this.classid = cid;
	}
}

class MyCompare implements Comparator //实现Comparator，定义自己的比较方法
{
	public int compare(Object o1, Object o2) {
		Item e1=(Item)o1;
		Item e2=(Item)o2;
	
		if(e1.ratingCount > e2.ratingCount){//这样比较是降序,如果把-1改成1就是升序.
			return -1;
		}else if(e1.ratingCount < e2.ratingCount)	{
			return 1;
		}
		else{
			return 0;
		}
	}
}