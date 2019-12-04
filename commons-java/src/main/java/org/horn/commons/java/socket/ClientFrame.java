package org.horn.commons.java.socket;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientFrame {
	public static void main(String args[]) throws UnknownHostException, IOException	{
		System.out.println("客户端启动了...........");
		// 1、创建客户端Socket对象同时制定将要连接的服务器的ip地址和端口号
		Socket s = new Socket(InetAddress.getByName("172.30.30.149"), 10005);// 此处会抛出连接失败的异常，根据自己的需要给出处理办法，这里就直接抛了
		// 2、设置io操作源，这里有多种选择，因为是操作文本文件，所以就选择可以一行一行读取数据的Io输入流BufferedReader
		BufferedReader brfu = new BufferedReader(new FileReader("data/client.txt"));
		// 3、设置目的：网络
		PrintWriter out = new PrintWriter(s.getOutputStream(), true);// 此处也可以选择别的Io输出流，例如：BufferedWriter
		// 4、开始读取文件内容并上传到服务器端
		String line = null;
		while ((line = brfu.readLine()) != null) {
			out.println(line);
			// 此处初学者一定要注意：使用out.print(line);out.flush();也解决不了上传不成功的问题，就是读取文本内容带有回车换行符的原因，如果不使用
			// out.println(line),当然也可以使用out.print(line+"\r\n")来解决
		}
		s.shutdownOutput();// 此方法是经过socket对象封装后的，目的就是告诉服务器自己发送内容操作结束了，避免服务器阻塞等待而不给客户端发送完成信息
		// 这里也可以使用发送一个和服务器协商好的结束标示符解决问题，但是那个标示符的选择一定要唯一，并且要做到确保上传文件中不会出现这个标示符(时间的毫秒值)
		// 5、创建接受服务器端返回数据的IO输入流
		BufferedReader brIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
		// 6、输出服务器返回的数据
		System.out.println("Server data :" + brIn.readLine());
		// 7、关闭资源
		brfu.close();
		s.close();// 其他的IO流对象不用关闭了，因为它们都依靠s，s关闭就可以了

	}
}
