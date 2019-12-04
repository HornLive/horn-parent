package alg.reco.svd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

class SVDRecommender {
	//配置属性 _
	public static String _input,_output,_split;
	public static int _ratingNum,_userNum,_itemNum,_featureNum,_minEpochs,_maxEpochs,_recoLength;
	public static double _minImprovement,_init,_alpha,_lambda;
	public static String _jdbcHiveDriver,_jdbcHiveConnection,_User,_password,_Query,_ColumnUser,_ColumnItem;
	//类属性
	int	ratingCount;	//当前
	int	ratingSize,usersize,itemsize;
	
	Map<String,Integer>	userIndex = new HashMap<String,Integer>();	//影射
	Map<String,Integer> itemIndex = new HashMap<String,Integer>();
	private	Vector<Data>	ratingBank = new Vector<Data>();		//评分记录库，项目库，用户库
	private Vector<Item>	itemBank = new Vector<Item>();
	private Vector<User>	userBank = new Vector<User>();
	private Vector<Float[]>	item_Feature = new Vector<Float[]>();	//特征矩阵
	private Vector<Float[]> user_Feature = new Vector<Float[]>();
	private Vector<int[]>	recoList = new Vector<int[]>();			//推荐列表

	private final double predictRating(int userid,int itemid,boolean bTrail) {	//预测函数
		double sum = 1;
		for(int f=0;f<_featureNum;f++){
			sum += item_Feature.get(itemid-1)[f] * user_Feature.get(userid-1)[f];
			if (sum > 5) sum = 5;
			if (sum < 1) sum = 1;
		}
		return sum;
	}		
	public static void siteProperties(String proper) throws IOException{
		Properties properties = new Properties();
		properties.load(SVDEngine.class.getResourceAsStream(proper));
		//String		
		_input = properties.getProperty("IN_PATH").trim();
		_output = properties.getProperty("OUT_PATH").trim();
		_split = properties.getProperty("SPLIT").trim();
		_jdbcHiveDriver = properties.getProperty("JDBCHIVEDRIVER").trim();
		_jdbcHiveConnection = properties.getProperty("JDBCCONNECTION").trim();
		_User = properties.getProperty("USER").trim();
		_password = properties.getProperty("PASSWORD").trim();
		_Query = properties.getProperty("QUERY").trim();
		_ColumnUser = properties.getProperty("COLUMNUSER").trim();
		_ColumnItem = properties.getProperty("COLUMNITEM").trim();
		//int,double,float
//		_ratingNum = Integer.parseInt(properties.getProperty("MAX_RATINGS").trim());
//		_userNum = Integer.parseInt(properties.getProperty("MAX_CUSTOMERS").trim());
//		_itemNum = Integer.parseInt(properties.getProperty("MAX_MOVIES").trim());
		_recoLength = Integer.parseInt(properties.getProperty("RECOLENGTH").trim());
		_featureNum = Integer.parseInt(properties.getProperty("MAX_FEATURES").trim());
		_minEpochs = Integer.parseInt(properties.getProperty("MIN_EPOCHS").trim());
		_maxEpochs = Integer.parseInt(properties.getProperty("MAX_EPOCHS").trim());
		
		_minImprovement = Double.parseDouble(properties.getProperty("MIN_IMPROVEMENT").trim());		
		_init = Double.parseDouble(properties.getProperty("INIT").trim());
		_alpha = Double.parseDouble(properties.getProperty("ALPHA").trim());
		_lambda = Double.parseDouble(properties.getProperty("LAMBDA").trim());
	}
	//JDBC to Hive,loading data	
	public void loadJDBCHiveData() throws SQLException, IOException {
		usersize = 0;
		itemsize = 0;

		String userNameCache = null;	// 记录数据上一行（用户名）字符串缓存
		int	userIdCache = 0;			//记录数据上一行（用户名）id缓存		
		try {
			Class.forName("org.apache.hadoop.hive.jdbc.HiveDriver"); 
			} catch (ClassNotFoundException e) {
				e.printStackTrace(); 
				System.exit(1); 
			} 
		Connection con = DriverManager.getConnection(_jdbcHiveConnection,_User,_password); 
		Statement stmt = con.createStatement(); 
		ResultSet res = stmt.executeQuery("_Query"); //browsewebpagelog where dt='20140424'"
				
		while(res.next()) {
//			System.out.println(res.getString("host")+"\t"+res.getString("classid"));
			String userName = res.getString(_ColumnUser);
			String itemName = res.getString(_ColumnItem);
			// 入库map and vector
			//识别st[0],username
			if(userName!=userNameCache) {//<可能新用户>相邻两行用户名不一样
				userNameCache = userName;			
				// 是否新用户
				if(!userIndex.containsKey(userNameCache)) {	// <新用户>map和bank中新加入
				userIndex.put(userNameCache, usersize);		//K(编码st[0],V(uid))
				userBank.add(new User(userNameCache,0,0));	//Bank收入，bank[usersize]=st[0]
				usersize++; 
				}
			}
			//识别〈新物品〉,itemname
			if(!itemIndex.containsKey(itemName)) {
				itemIndex.put(itemName, itemsize);//K编码st[1],V(mid)
				itemBank.add(new Item(itemName,0,0,0));
				itemsize++;
			}
			// 无论新旧用户和物品，table都要载入数据集
			int mid = itemIndex.get(itemName).intValue();			//uid=userIdCache		
			userIdCache = userIndex.get(userName).intValue();		//更改缓存
			ratingBank.add(new Data(userIdCache,mid));		//Bank
			ratingSize++;					//数据总条数
		}		
		System.out.println(ratingSize);
		System.out.println(itemsize);
		System.out.println(usersize);
		System.out.println(userIndex.size());
		System.out.println(userBank.size());
		System.out.println(itemIndex.size());
		System.out.println(itemBank.size());
	}
	//读文件数据源
	public void loadFileData() throws IOException{

		try {
			BufferedReader readUser = new BufferedReader(new FileReader(_input)); // users
			while (readUser.ready()) { // user数据集******************************
				String strLine = readUser.readLine();
				if (strLine != null) {					
					String[] st = strLine.split(_split);
					int uid = Integer.parseInt(st[0]);
					int mid = Integer.parseInt(st[1]);
					byte rat = Byte.parseByte(st[2]);
					ratingCount++;
										
					ratingBank.add(new Data(uid,mid,rat));
//					System.out.println(uid);
//					if(uid==3||uid==0) System.out.println(uid+"\t"+mid);
					if(uid>usersize)		//追踪最大用户id
						usersize= uid;
					if(mid>itemsize)
						itemsize = mid;
				}
			}
			for(int i=0;i<usersize;i++)
				userBank.add(new User(null,0,0));
			for(int j=0;j<itemsize;j++)
				itemBank.add(new Item(null, 0, 0, 0));
			System.out.println(userBank.size()+"\t"+itemBank.size());
//			System.out.println(itemsize+"\t"+usersize+"\t"+ratingBank.size());
//			System.out.println(ratingCount+"ddddd");
			readUser.close();//关闭
			
		} catch (Exception ee) {
			ee.printStackTrace();
			System.out.println("File Wrong!!!");
		}
	}
	// 初始化
	public void init(){
		Float[] f = new Float[_featureNum];
		for (int i = 0; i < f.length; i++) {
			f[i] = (float) _init;
		}
		
		for(int i=0;i<itemsize;i++){
			item_Feature.add(f);
		}
		for(int i=0;i<usersize;i++){
			user_Feature.add(f);
		}
	}

	public void calcMetrics(){
		for (int i = 0; i < ratingBank.size(); i++) {
			int userid = ratingBank.get(i).userid;
			int itemid = ratingBank.get(i).itemid;
//			System.out.println(userid+"\t"+itemid);
			userBank.get(userid-1).ratingCount++;
			userBank.get(userid-1).ratingSum++;
			itemBank.get(itemid-1).ratingCount++;
			itemBank.get(itemid-1).ratingSum++;
		}
	}
	public void studyFeatures() {
		int i, cnt = 0;
		double err, p, sq, rmse_last = 0, rmse = 2.0;
		float uf, mf;
		
		for(int e=0; (e < _minEpochs) || (rmse <= rmse_last - _minImprovement); e++){
			System.out.println("第"+e+"次迭代");
			cnt++;
			err = sq = 0;
			rmse_last = rmse;
			
			for(Data data:ratingBank){
				int userid = data.userid;
				int itemid = data.itemid;
				p = predictRating(userid, itemid, true);
				
				err = data.rating - p;
//				System.out.println("P"+p);
				for (int f=0;f<_featureNum;f++){
					uf = user_Feature.get(userid-1)[f];
					mf = item_Feature.get(itemid-1)[f];
					//替换旧的特征值
					user_Feature.get(userid-1)[f] += (float)(_alpha * (err * mf - _lambda * uf));
					item_Feature.get(itemid-1)[f] += (float)(_alpha * (err * uf - _lambda * mf));
				}
				sq += err*err;	//方差
	//			System.out.println(err);
			}
			_alpha *= 0.9;
			rmse = Math.sqrt(sq/ratingCount);	// 训练集均方根误差
			System.out.println(rmse);
//			rmse = ProcessTest();	//均方根误差
		}
	}

	public void recommender() {				
		for(int uid =0;uid<usersize;uid++) {//用户id=uid+1
			double[] per = new double[itemsize];//每个用户uid+1对所有项目的兴趣表
			int[]	oneUserRecoN =	new int[_recoLength]; 
			for(int mid=0;mid<itemsize;mid++) {//该用户的id为=mid+1
				per[mid] = predictRating(uid, mid, true);	//该用户的mid个物品的喜好度
			}
			for(int i=0;i<_recoLength;i++){// 便利N次找出N个最大兴趣的项目id
				int indexOfMax =0;
				for(int mid = 0; mid < itemsize; mid++) {// 每个项目表遍历 超出一个最大值的项目id
	    			if(per[mid] != 0 && per[mid] > per[indexOfMax]) {
	    				indexOfMax = mid;
	    			}
	    		}
				oneUserRecoN[i] = indexOfMax+1;//+1 代表项目的真实id
				per[indexOfMax] = 0;
			}
		}
	}	
}