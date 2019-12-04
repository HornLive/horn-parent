package org.horn.commons.java.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerFrame {
	public static void main(String[] args) throws IOException {
		System.out.println("服务器端启动了....");
		// 1、创建服务器端对象,同时绑定监听的端口号
		ServerSocket ss = new ServerSocket(10005);
		// 2、获取客户端对象
		Socket s = ss.accept();
		// 3、获取客户端ip地址和客户端名称
		String ip = s.getInetAddress().getHostAddress();
		String name = s.getInetAddress().getHostName();
		System.out.println("接受到来自名称为：" + name + " 和ip为：" + ip + "的连接");
		// 4、获取IO数据源
		BufferedReader brfu = new BufferedReader(new InputStreamReader(s.getInputStream()));
		// 5、设置目的，创建本地的文本文件
		PrintWriter out = new PrintWriter(new FileWriter("data/server.txt"), true);

		// 上面的代码等效于BufferedWriter bw=new BufferedWriter(new
		// FileWriter("server.txt"))

		// 6、开始保存数据的操作
		String line = null;
		while ((line = brfu.readLine()) != null) {

			out.write(line + "\r\n");
			System.out.println(line);
		}
		// 7、创建服务器端源：网络
		PrintWriter outSer = new PrintWriter(s.getOutputStream(), true);
		outSer.println("接受并保存完成！");

		// 8、关闭IO和网络资源
		out.close();
		s.close();
		ss.close();

	}
}
