package alg.reco.svd;

//数据元组
class Data {
	public int itemid;
	public int userid;
	public byte rating;
	
	Data(int user,int item){
		this.itemid = item;
		this.userid = user;
//		this.rating = 5;///////////////////////////可设置
	}
	Data(int user,int item,byte rat){
		this.itemid = item;
		this.userid = user;
		this.rating = rat;
	}
}

//项目
class Item {
	public String itemName;
	public int ratingCount;
	public int ratingSum;
	public double	ratingAvg;
//	public double pseudoAvg;
	
	Item(String item,int rc,int rs,double ra){
		this.itemName = item;
		this.ratingCount = rc;
		this.ratingSum = rs;
		this.ratingAvg = ra;
	}
}

//用户
class User {
	public String userName;
	public int	ratingCount;
	public int	ratingSum;
	
	User(String user,int rc,int rs){
		this.userName = user;
		this.ratingCount = rc;
		this.ratingSum = rs;
	}
}