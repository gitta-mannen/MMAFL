package settings;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Table {
	List<String> children;
	List<String> sources;
	String baseUrl;
	String parent;
	String primaryKey;
	String inherit;
	boolean index;
	String name;
	
	public List<String> getChildren() {
		return children;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public String getParent() {
		return parent;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public String getInherit() {
		return inherit;
	}

	public boolean isIndex() {
		return index;
	}

	HashMap<String, Column> columns;	
	
	public Table(String name) {
		super();
		this.name = name;
		columns = new HashMap<String, Column>();
		children = new LinkedList<String>();
		sources = new LinkedList<String>();
	}
	
	public HashMap<String, Column> getColumns() {
		return columns;
	}
	
	public Column getColumn (String column) {
		return columns.get(column);
	}
		
	public Column addColumn (String name) {
		Column temp = new Column();
		columns.put(name, temp);
		return temp;
	}	
}
