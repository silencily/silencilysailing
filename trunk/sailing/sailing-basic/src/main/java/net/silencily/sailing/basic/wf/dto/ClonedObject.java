package net.silencily.sailing.basic.wf.dto;

import java.io.Serializable;
import java.util.Map;

public class ClonedObject implements Serializable {
	private String id;

	private Map properties;	
	
	private Map subListMap;		

	public Map getSubListMap() {
		return subListMap;
	}

	public void setSubListMap(Map subListMap) {
		this.subListMap = subListMap;
	}

	public Map getProperties() {
		return properties;
	}

	public void setProperties(Map properties) {
		this.properties = properties;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
