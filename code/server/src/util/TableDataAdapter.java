package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.TreeSet;

public class TableDataAdapter {

	private LinkedList<Object[]> rows = new LinkedList<Object[]>();
	private LinkedList<ArrayList<Object>> cols = new LinkedList<ArrayList<Object>>();
	
	private final int colCount;
	
	public TableDataAdapter(int colCount) {		
		this.colCount = colCount;
		for (int i = 0; i < colCount; i++) {
			cols.add(new ArrayList<Object>());
		}
	}
	
	public void addRow(Object[] row) {
		if (row.length == colCount) {
			rows.add(row);
			
			for (int colIndex = 0; colIndex < cols.size(); colIndex++) {
				cols.get(colIndex).add(row[colIndex]); 
			}
			
		} else {
			throw new IndexOutOfBoundsException();
		}
		
	}
	
	public void setCol(Object[] col, int colIndex) {
		if (rows.isEmpty() && colIndex < cols.size() && col.length == rows.size()) {
			cols.set(colIndex, new ArrayList<Object>(Arrays.asList(col)));
			
			for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
				rows.get(rowIndex)[colIndex] = col[colIndex]; 
			}
		} else {
			throw new IndexOutOfBoundsException();
		}
	}

	public Object getItem(int rowIndex, int colIndex) {
		return rows.get(rowIndex)[colIndex];
	}
	
	public Object[] getRow(int rowIndex) {
		return rows.get(rowIndex);
	}
	
	public Object[] getCol(int colIndex) {
		return cols.get(colIndex).toArray(new Object[rows.size()]);
	}
}
