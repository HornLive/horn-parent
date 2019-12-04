package alg.cluster.basickmeans;

public class BasicKmeans {
 
	public static void main(String[] args) {	
		double[] p = { 1, 2, 3, 5, 6, 7, 9, 10, 11, 100, 150, 200, 1000 };
		int k = 5;
		double[][] g;
		g = cluster(p, k);
		for (int i = 0; i < g.length; i++) {
			for (int j = 0; j < g[i].length; j++) {
				System.out.print(g[i][j]);
				System.out.print("\t");
			}
			System.out.println();
		}
	}
 
	public static double[][] cluster(double[] p, int k) {
		
		double[] c = new double[k];// 存放聚类旧的聚类中心
		double[] nc = new double[k];// 存放新计算的聚类中心
		double[][] g;// 存放放回结果
		// 初始化聚类中心
		// 经典方法是随机选取 k 个
		// 本例中采用前 k 个作为聚类中心
		// 聚类中心的选取不影响最终结果
		for (int i = 0; i < k; i++)
			c[i] = p[i];
		while (true) { 	// 循环聚类，更新聚类中心，到聚类中心不变为止
			g = group(p, c); // 根据聚类中心将元素分类
			// 计算分类后的聚类中心
			for (int i = 0; i < g.length; i++) {
				nc[i] = center(g[i]);
			}
			if (!equal(nc, c)) {// 如果聚类中心不同
				c = nc;// 为下一次聚类准备
				nc = new double[k];
			} else // 聚类结束
				break;
		}
		return g;// 返回聚类结果
	}
 
	public static double center(double[] p) {
		return sum(p) / p.length;
	}
 
	public static double[][] group(double[] p, double[] c) {
		int[] gi = new int[p.length];// 中间变量，用来分组标记
		// 考察每一个元素 pi 同聚类中心 cj 的距离
		// pi 与 cj 的距离最小则归为 j 类
		for (int i = 0; i < p.length; i++) {
			double[] d = new double[c.length];// 存放距离
			// 计算到每个聚类中心的距离
			for (int j = 0; j < c.length; j++) {
				d[j] = distance(p[i], c[j]);
			}
			int ci = min(d);// 找出最小距离
			gi[i] = ci;// 标记属于哪一组
		}
		double[][] g = new double[c.length][];// 存放分组结果
		for (int i = 0; i < c.length; i++) {// 遍历每个聚类中心，分组
			int s = 0;// 中间变量，记录聚类后每一组的大小
			for (int j = 0; j < gi.length; j++)// 计算每一组的长度
				if (gi[j] == i)
					s++;
			g[i] = new double[s];// 存储每一组的成员
			s = 0;
			// 根据分组标记将各元素归位
			for (int j = 0; j < gi.length; j++)
				if (gi[j] == i) {
					g[i][s] = p[j];
					s++;
				}
		}
		return g;// 返回分组结果
	}
 
	public static double distance(double x, double y) {
		return Math.abs(x - y);
	}
 
	public static double sum(double[] p) {
		double sum = 0.0;
		for (int i = 0; i < p.length; i++)
			sum += p[i];
		return sum;
	}
 
	public static int min(double[] p) {
		int i = 0;
		double m = p[0];
		for (int j = 1; j < p.length; j++) {
			if (p[j] < m) {
				i = j;
				m = p[j];
			}
		}
		return i;
	}
 
	public static boolean equal(double[] a, double[] b) {
		if (a.length != b.length)
			return false;
		else {
			for (int i = 0; i < a.length; i++) {
				if (a[i] != b[i])
					return false;
			}
		}
		return true;
	}
}