package settings;

import java.util.HashMap;

public class Table {
	HashMap<String, Column> columns;
	public Table() {
		super();
		columns = new HashMap<String, Column>();
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
