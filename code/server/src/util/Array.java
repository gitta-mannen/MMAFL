package util;

public abstract class Array {

	private Array() {}

	/**
	 * Takes a two dimension array and returns the n:th element
	 *  from each row as an array.
	 * @param a - Two dimension array.
	 * @param c - Index of the element from each row.
	 * @return The n:th 'column' if you view the array as a table.
	 */
	public static <K> K[] getColumn(K[][] a, int c) {
		@SuppressWarnings("unchecked")
		K[] result = (K[]) java.lang.reflect.Array.newInstance(Object.class, a.length); 

		for (int i = 0; i < a.length; i++) {
			result[i] = (a[i][c]);
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static <K> K[][] transpose(K[][] inMatrix, int rows) {
		int cols = inMatrix.length;	
		K[][] result = (K[][]) java.lang.reflect.Array.newInstance(Object[].class, rows); 

		for (int row = 0; row < rows; row++) {
			result[row] = (K[]) java.lang.reflect.Array.newInstance(Object.class, cols); 
			for (int col = 0; col < cols; col++) {
				if (inMatrix[col].length != rows) {
					throw new IndexOutOfBoundsException("the column count for the in matrix " +
							"must match the row count for the result array on each row in the matrix." +
							"Matrix row: " + col + " count of: " + inMatrix[col].length + " , should be: " + rows);
				}
				result[row][col] = (inMatrix[col][row]);
			}
		}
		return result;
	}
	
	/**
	 * Inserts an element at the top or bottom of an array.
	 * @param a - The array 
	 * @param e - The element
	 * @param pos - Zero or negative puts the element at index 0.
	 * 	One and above puts the element on index length - 1.
	 * @return - A copy of the array including the inserted element.
	 */
	public static <K> K[] insert(K[] a, K e, int pos) {
		@SuppressWarnings("unchecked")
		K[] result = (K[]) java.lang.reflect.Array.newInstance(Object.class, a.length + 1);
		
		if (pos <= 0) {
			result[0] = e;
			System.arraycopy(a, 0, result, 1, a.length);
		} else {
			result[result.length - 1] = e;
			System.arraycopy(a, 0, result, 0, a.length);
		}
		return result;
	}
}
