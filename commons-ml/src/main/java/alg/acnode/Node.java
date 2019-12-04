package alg.acnode;

import java.util.ArrayList;
import java.util.List;

public class Node {
	private String nodeName; 		// 样本点名
	private double[] dimensioin; 	// 样本点的维度
	private double kDistance; 		// k-距离
	private List kNeighbor = new ArrayList();// k-领域
	private double distance; 		//到给定点的欧几里得距离
	private double reachDensity;	// 可达密度
	private double reachDis;		// 可达距离
	private double lof;				//局部离群因子
	
	//构造
	public Node(){
	}
	public Node(String nodeName,double[] dimensioin){
		this.nodeName=nodeName;
		this.dimensioin=dimensioin;
	}
	//名字
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	//特征维度
	public double[] getDimensioin() {
		return dimensioin;
	}
	public void setDimensioin(double[] dimensioin) {
		this.dimensioin = dimensioin;
	}
	//k距离
	public double getkDistance() {
		return kDistance;
	}
	public void setkDistance(double kDistance) {
		this.kDistance = kDistance;
	}
	//k近邻
	public List getkNeighbor() {
		return kNeighbor;
	}
	public void setkNeighbor(List kNeighbor) {
		this.kNeighbor = kNeighbor;
	}
	//欧几里得等 距离
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
	this.distance = distance;
	}
	//可达密度
	public double getReachDensity() {
		return reachDensity;
	}
	public void setReachDensity(double reachDensity) {
		this.reachDensity = reachDensity;
	}
	//可达距离
	public double getReachDis() {
		return reachDis;
	}
	public void setReachDis(double reachDis) {
		this.reachDis = reachDis;
	}
	//局部离群因子
	public double getLof() {
		return lof;
	}
	public void setLof(double lof) {
		this.lof = lof;
	}
}