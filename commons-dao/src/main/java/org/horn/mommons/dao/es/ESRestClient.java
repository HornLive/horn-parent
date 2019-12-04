package org.horn.mommons.dao.es;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//import org.apache.http.util.EntityUtils;
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.index.query.QueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;

/**
 * Elasticserach RestClient示例
 * 
 * @author HornLive
 * 
 */
public class ESRestClient {
	private static RestClient restClient;

	public void getRestClient() {
		final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("elastic", "changeme"));

		restClient = RestClient.builder(new HttpHost("localhost", 9200, "http"))
				.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
					public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
						return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
					}
				}).build();
	}

	public void init() {
		restClient = RestClient.builder(new HttpHost("localhost", 9200, "http")).build();
	}

	public ESRestClient() {
		init();
	}

	public static void main(String[] args) throws Exception {
		System.out.println(Math.pow(0.97, 100));
		Hero guanyu = new Hero();
		guanyu.setAge(58);
		guanyu.setName("关羽关云长");
		guanyu.setWeight(80);
		guanyu.setState("蜀汉");
		List<String> list = new ArrayList<String>();
		list.add("刘备刘玄德");
		list.add("张飞张翼德");
		list.add("赵云赵子龙");
		list.add("诸葛亮诸葛孔明");
		guanyu.setFriends(list);

		ESRestClient rest = new ESRestClient();

		rest.CatApi("GET", "/_cat");
		rest.CreateIndex("PUT", "/test-index2");
		rest.putDoc("PUT", "/hero/sanguo/101", JSON.toJSONString(guanyu));
		rest.getDocument("GET", "/test-index3/test/100");

		String queryall = "{\n" + "  \"query\": {\n" + "    \"match_all\": {}\n" + "  }\n" + "}";
		rest.queryAll("POST", "/movies/movie/_search", queryall);

		String queryfeild = "{\n" + "\"query\": {\n" + "\"match\": {\n" + "\"name\": \"关羽\"\n" + "}\n" + "}\n" + "}";
		rest.queryByField("POST", "/test-index3/test/_search", queryfeild);

		String updatestr = "{\n" + "  \"doc\": {\n" + "    \"user\":\"大美女aaa\"\n" + "   }\n" + "}";
		rest.updateByScript("POST", "/test-index/test/1/_update", updatestr);

		String geoQuery = "{\n" + "  \"query\": {\n" + "    \"match_all\": {}\n" + "  },\n" + "  \"post_filter\": {\n"
				+ "    \"geo_bounding_box\": {\n" + "      \"location\": {\n" + "        \"top_left\": {\n"
				+ "          \"lat\": 39.990481,\n" + "          \"lon\": 116.277144\n" + "        },\n"
				+ "        \"bottom_right\": {\n" + "          \"lat\": 39.927323,\n"
				+ "          \"lon\": 116.405638\n" + "        }\n" + "      }\n" + "    }\n" + "  }\n" + "}";
		rest.geoBoundingBox("POST", "/attractions/restaurant/_search", geoQuery);
	}

	/**
	 * 查看api信息
	 * 
	 * @throws Exception
	 */
	public void CatApi(String method, String endpoint) throws Exception {
		Response response = restClient.performRequest(method, endpoint);
		// System.out.println(EntityUtils.toString(response.getEntity()));
	}

	/**
	 * 创建索引
	 * 
	 * @throws Exception
	 */
	public void CreateIndex(String method, String endpoint) throws Exception {
		Response response = restClient.performRequest(method, endpoint);
		// System.out.println(EntityUtils.toString(response.getEntity()));
	}

	/**
	 * 创建文档
	 * 
	 * @throws Exception
	 */
	public void putDoc(String method, String endpoint, String jsonstr) throws Exception {
		HttpEntity entity = new NStringEntity(jsonstr, ContentType.APPLICATION_JSON);
		Response response = restClient.performRequest(method, endpoint, Collections.<String, String>emptyMap(), entity);
		// System.out.println(EntityUtils.toString(response1.getEntity()));
	}

	/**
	 * 获取文档
	 * 
	 * @throws Exception
	 */
	public void getDocument(String method, String endpoint) throws Exception {
		Response response = restClient.performRequest(method, endpoint);
		// System.out.println(EntityUtils.toString(response.getEntity()));
	}

	/**
	 * 查询所有数据
	 * 
	 * @throws Exception
	 */
	public void queryAll(String method, String endpoint, String jsonstr) throws Exception {
		HttpEntity entity = new NStringEntity(jsonstr, ContentType.APPLICATION_JSON);
		Response response = restClient.performRequest(method, endpoint, Collections.<String, String>emptyMap(), entity);
		// System.out.println(EntityUtils.toString(entity));
		// System.out.println(EntityUtils.toString(response.getEntity()));
	}

	/**
	 * 根据字段获取
	 * 
	 * @throws Exception
	 */
	public void queryByField(String method, String endpoint, String jsonstr) throws Exception {
		HttpEntity entity = new NStringEntity(jsonstr, ContentType.APPLICATION_JSON);
		Response response = restClient.performRequest(method, endpoint, Collections.<String, String>emptyMap(), entity);
		// System.out.println(EntityUtils.toString(entity));
		// System.out.println(EntityUtils.toString(response.getEntity()));
	}

	/**
	 * 更新数据
	 * 
	 * @throws Exception
	 */
	public void updateByScript(String method, String endpoint, String jsonstr) throws Exception {
		HttpEntity entity = new NStringEntity(jsonstr, ContentType.APPLICATION_JSON);
		Response response = restClient.performRequest(method, endpoint, Collections.<String, String>emptyMap(), entity);
		// System.out.println(EntityUtils.toString(entity));
		// System.out.println(EntityUtils.toString(response.getEntity()));
	}

	
	/**
	 * 地理查询
	 * 
	 * @param method
	 * @param endpoint
	 * @param jsonstr
	 * @throws IOException
	 */
	public void geoBoundingBox(String method, String endpoint, String jsonstr) throws IOException {
		HttpEntity entity = new NStringEntity(jsonstr, ContentType.APPLICATION_JSON);
		Response response = restClient.performRequest(method, endpoint, Collections.<String, String>emptyMap(), entity);
		// System.out.println(EntityUtils.toString(response.getEntity()));
	}

}

class Hero {
	private String name;
	private int age;
	private int weight;
	private String state;
	private List<String> friends;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public List<String> getFriends() {
		return friends;
	}

	public void setFriends(List<String> friends) {
		this.friends = friends;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}