package settings;

import java.util.HashMap;

public class Schema {
	private HashMap<String, Table> tables;
	public Schema() {
		super();
		tables = new HashMap<String, Table>();
	}
	
	public HashMap<String, Table> getTables() {
		return tables;
	}
	
	public Table getTable (String table) {
		return tables.get(table);
	}
	
	public Table addTable (String name) {
		Table temp = new Table();
		tables.put(name, temp);
		return temp;
	}

}