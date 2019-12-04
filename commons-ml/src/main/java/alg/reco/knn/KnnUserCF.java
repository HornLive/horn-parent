package alg.reco.knn;

import java.sql.*;
import java.util.*;


public class KnnUserCF {
	static int 		K=20,	RecoN=10;	// 为k最近邻，RecoN推荐列表长度
	static String	SP = "::";			// 分割符号
	static int		rateNum,userNum,itemNum;
	static int		usersize,itemsize;//用于用户和物品index和bank计数
	static Vector<Vector<Float>>	w = new Vector(new Vector<Float>());	//item相似度矩阵
	static Vector<Set<Integer>>	table = new Vector(new TreeSet<Integer>());	//user->item
	static Vector<short[]>		RecoList = new Vector();					//推荐列表
	static Vector<Set<Integer>>	sortW = new Vector(new TreeSet<Integer>());	//相似度排序表
	//双向索引
	static Map<String,Integer>	userIndex = new HashMap<String,Integer>();
	static Map<String,Integer> itemIndex = new HashMap<String,Integer>();
	static Vector<String>	itemBank = new Vector<String>();
	static Vector<String>	userBank = new Vector<String>();
	static Vector<Integer>	N_item = new Vector<Integer>();//N(i),使用i的用户数目
	
	static String PATHin = "data/ratings.dat",PATHout = "data/output.txt";
	/**
	 * @param args 
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException{
		// TODO Auto-generated method stub
		LoadingData();
		similarity() ;
		Recommendation();
		for(int i=0;i<usersize;i++){
			System.out.println("用户"+userBank.get(i)+"的推荐列表");
			for(int j=0;j<10;j++){
				System.out.print(itemBank.get(RecoList.get(i)[j])+"\t");
			}System.out.println();			
		}
	}
	
/**222222222222222222222222相似度矩阵计算w********************/
	public static void similarity() {
		for(int i=0;i<itemsize;i++){	
			w.add(new Vector<Float>());
			for(int j = 0;j<itemsize;j++){
				w.get(i).add((float)0);
			}
		}
		
		for (Set<Integer> user:table) {
			for (Integer mid1:user) {
				for (Integer mid2:user) {
					short simTmp;
					if(mid1 != mid2) {//w[mid1][mid2]+=1						
						w.get(mid1).set(mid2, (float) (w.get(mid1).get(mid2).floatValue()+1));
					}
				}
			}
		}//计算相似度
		for (int mid =0;mid<w.size();mid++) {
			for (int mid2=0;mid2<w.size();mid2++) {
				float sim = w.get(mid).get(mid2).floatValue();
				sim = (float) (sim/Math.sqrt(N_item.get(mid)*N_item.get(mid2)));
				w.get(mid).set(mid2, sim);
			}
		}
		//相似度排序
		for(int itid=0;itid<w.size();itid++) {
    		Vector<Integer>	now = new Vector<Integer>();
    		Set<Integer>	sortK = new TreeSet<Integer>();
    		//本层循环k次，找出K个最大相似值的id
    		for(int i = 0;i<K;i++) {    			
    			int maxid = 0;							//最大相似度值的id
    			//本层循环找出一个最大值对应id
    			for(int itid1=0;itid1<w.size();itid1++) {
    				now.add(0);
        			if(w.get(itid).get(itid1).floatValue() != 0 
        					&& w.get(itid).get(itid1).floatValue()>w.get(itid).get(itid1).floatValue() 
        					&& now.get(itid1) !=1) {
        				maxid = itid1;
        			}
        		}
    			now.set(maxid, 1);//now[maxid]=1
    			sortK.add(maxid);
    		}
    		sortW.add(sortK);
    	}     	
	}
	
/**33333333333333***********u对i的喜好度*************************/
	 public static double uToJ(int uid,int itid) {
		 TreeSet<Integer> unionSet = new TreeSet<Integer>();//用户u的物品集并上物品it的K个近邻
		 unionSet.addAll(table.get(uid));
		 unionSet.addAll(sortW.get(itid));
		 float interest = 0;
		 for(Integer it:unionSet) {
			 interest += w.get(itid).get(it.intValue());//矩阵 +=w[i][j]
		 }    	
    	return interest;
    }
	 
	//为每一个用户推荐N个物品
    public static void Recommendation() {
    	for(int uid=0;uid<usersize;uid++) {
    		Vector<Float> puj = new Vector<Float>(); //puj[]可能很短
    		short listN[] = new short[10];
    		for(int itid=0;itid<itemsize;itid++) {
    			puj.add((float)0);
    			if(!table.get(uid).contains(itid)) {
    				puj.set(itid, (float) uToJ(uid,itid));
    				//System.out.println(puj[itid][1]);
    			}
//    			System.out.println(puj.size());
    		}
    		//System.out.println(puj[100]);
    		for(int i=0; i<RecoN; i++) {
    			int indexofmax = 0;						//跟踪最大puj的下标
				for(int itid = 0; itid < itemsize; itid++) {
	    			if(puj.get(itid) != 0 && puj.get(itid) > puj.get(indexofmax)) {
	    				indexofmax = itid;
	    			}
	    		}
				listN[i] = (short) indexofmax;
				puj.set(indexofmax, (float) 0);//
			}
    		RecoList.add(listN);
    	}
    }
	
/**11111111111111111* 输入格式暂定（userid,itemid,others）
 * @throws SQLException *********************/
	public static void LoadingData() throws SQLException {
		String driverName = "org.apache.hadoop.hive.jdbc.HiveDriver";
		usersize = 0;
		itemsize = 0;
		rateNum = 0;
		String userNameCache = null;	// 记录数据上一行（用户名）字符串缓存
		int	userIdCache = 0;//记录数据上一行（用户名）id缓存
		try {
			Class.forName(driverName); 
			} catch (ClassNotFoundException e) {
				e.printStackTrace(); 
				System.exit(1); 
			} 
		Connection con = DriverManager.getConnection("jdbc:hive://192.168.2.102:10002/cems_data","",""); 
		Statement stmt = con.createStatement(); 
		ResultSet res = stmt.executeQuery("select * from processinfolog"); //browsewebpagelog where dt='20140424'"
		while(res.next()){
//			System.out.println(res.getString("host")+"\t"+res.getString("classid"));
			String userName = res.getString("devonlyid");
			String itemName = res.getString("name");
			System.out.println(userName);
			// 入库map and vector
			//识别st[0],username
			if(userName!=userNameCache) {//<可能新用户>相邻两行用户名不一样
				userNameCache = userName;			
				// 是否新用户
				if(!userIndex.containsKey(userNameCache)) {// <新用户>map和bank中新加入
				userIndex.put(userNameCache, usersize);//K(编码st[0],V(uid))
				userBank.add(userNameCache);			//Bank收入，bank[usersize]=st[0]
				table.add(new TreeSet<Integer>()); //加表 table[usersize]->vector
				usersize++; 
				}
			}
			//识别〈新物品〉,itemname
			if(!itemIndex.containsKey(itemName)) {
				itemIndex.put(itemName, itemsize);//K编码st[1],V(mid)
				itemBank.add(itemName);
				N_item.add(0);					//新增加的物品，初始化N_item[i]=0
				itemsize++;
			}
			// 无论新旧用户和物品，table都要载入数据集
			int mid = itemIndex.get(itemName).intValue();			//uid=userIdCache		
			userIdCache = userIndex.get(userName).intValue();		//更改缓存
			table.get(userIdCache).add(mid);				// 增加usersize用户的历史物品表
			N_item.set(mid, N_item.get(mid).intValue()+1);//N(i)+=1
			rateNum++;					//数据总条数
		}		
		System.out.println(rateNum);
		System.out.println(itemsize);
		System.out.println(usersize);
		System.out.println(userIndex.size());
		System.out.println(userBank.size());
		System.out.println(itemIndex.size());
		System.out.println(itemBank.size());
		System.out.println(table.size());
	}	
}
