package database;

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

class Table {
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

class Column {
	public String dbname;
	public String dbtype;
	public String apptype;
	public String formatter;
	public String constraint;
}
