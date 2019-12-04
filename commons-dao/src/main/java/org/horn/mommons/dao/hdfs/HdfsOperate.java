package org.horn.mommons.dao.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HdfsOperate {
    public static FileSystem fs;
    String HDFS = "hdfs://hadoop0001.tuniu.org:9000";
	String INPATH = "/kn1/kn1_cre_route_dest";
	String OUTPATH = "/user/hadoop/lhe/output/";
    
	public void initHdfs(){
		Configuration conf = new Configuration();  
		conf.set("fs.default.name", HDFS);// 在hadoop上运行是可以不加这步
        try {
			fs = FileSystem.get(conf);
			if (!fs.exists(new Path(INPATH))) {
				throw new IOException("Input file not found");
			}
			if (fs.exists(new Path(OUTPATH))) {
				fs.delete(new Path(OUTPATH), true);
			}
		} catch (IOException e) {
			System.out.println("initHdfs():异常");
			e.printStackTrace();
		} 
	}
	
	public void hdfsOperate(String in,String out) throws IOException{
		FileStatus[] inputFiles = fs.listStatus(new Path(in));//表名所表示的目录
		int count = 0;
		for (int i = 0; i < inputFiles.length; i++) {
			//read file from hdfs
	//		InputStream in = fs.open(new Path(datapath));
			if(inputFiles[i].isDir()){
				continue;
			}
			FSDataInputStream fsdatain = fs.open(new Path(inputFiles[i].getPath().toString()));
			BufferedReader bufread = new BufferedReader(new InputStreamReader(fsdatain));//or fsdatain
			
			//write file to hdfs
	//		OutputStream out = fs.create(new Path(trainpath));
	//		FSDataOutputStream fsdataout = fs.create(new Path(trainpath));
	//		BufferedWriter write = new BufferedWriter(new OutputStreamWriter(fsdataout)) ;
			String line;
			while((line = bufread.readLine()) != null){
				System.out.println(line);
				String[] st = line.split("\t");
				System.out.println("########"+st.length);
				System.out.println(st[0]);
	//			write.write(line);
	//			write.newLine();
				count++;
			}
			bufread.close();
	//		write.flush();
	//		write.close();
			bufread.close();fsdatain.close();
	//		fsdataout.close();
			System.out.println(count);
		}
	}
	
	private static void hdfsfile(String in) throws IOException{
		FileStatus[] inputFiles = fs.listStatus(new Path(in));//表名所表示的目录
		
		for (int i = 0; i < inputFiles.length; i++) {
			System.out.println(inputFiles[i].getPath());
			System.out.println(inputFiles[i].getPath().getName());
		}
	}
	
	public static void main(String[] args)  throws IOException{
		HdfsOperate ho = new HdfsOperate();
		ho.initHdfs();
		ho.hdfsOperate(ho.INPATH, ho.OUTPATH);
//		hdfsfile(ho.INPATH);
	}
}












