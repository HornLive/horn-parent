package org.horn.mommons.dao.es.transport;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;

public class TransportClientUtil {

	public static void main(String[] args) {

		try {

			// 设置集群名称
			Settings settings = Settings.builder().put("cluster.name", "elasticsearch")
					.put("client.transport.ignore_cluster_name", true).build();
			// 创建client
			@SuppressWarnings("resource")
			TransportClient client = new PreBuiltTransportClient(settings)
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
			// 搜索数据
			GetResponse response = client.prepareGet("movies", "movie", "6").execute().actionGet();
			// 输出结果
			System.out.println(response.getSourceAsString());
			// 关闭client
			client.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}