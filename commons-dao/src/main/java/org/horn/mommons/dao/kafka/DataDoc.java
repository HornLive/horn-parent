package org.horn.mommons.dao.kafka;

import java.util.List;

public class DataDoc {
	private int id;
	private String routename;
	private List<String> tags;
	
	public DataDoc(int id,String routeName,List<String> tags){
		this.setId(id);
		this.setRoutename(routeName);
		this.setTags(tags);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRoutename() {
		return routename;
	}

	public void setRoutename(String routename) {
		this.routename = routename;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}
}
