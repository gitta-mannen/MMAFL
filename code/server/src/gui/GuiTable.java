package gui;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
 

public class GUItable extends JPanel {
    /**
	 * http://docs.oracle.com/javase/tutorial/uiswing/components/table.html
	 */
	private static final long serialVersionUID = 1L;
	private boolean DEBUG = true;
	private JTable table;
	
    public GUItable(String[] columnNames, Object[][] data) {
        super(new GridLayout(1,0));
 
        table = new JTable(data, columnNames);
        table.setPreferredScrollableViewportSize(new Dimension(580, 300));
        table.setFillsViewportHeight(true);
 
//        if (DEBUG) {
//            table.addMouseListener(new MouseAdapter() {
//                public void mouseClicked(MouseEvent e) {
//                    printDebugData(table);
//                }
//            });
//        }
        //sdfsd
 
        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);
 
        //Add the scroll pane to this panel.
        add(scrollPane);
    }
    
    public void setModel(TableModel tm) {
    	table.setModel(tm);
    }
    
    private void printDebugData(JTable table) {
        int numRows = table.getRowCount();
        int numCols = table.getColumnCount();
        javax.swing.table.TableModel model = table.getModel();
 
        System.out.println("Value of data: ");
        for (int i=0; i < numRows; i++) {
            System.out.print("    row " + i + ":");
            for (int j=0; j < numCols; j++) {
                System.out.print("  " + model.getValueAt(i, j));
            }
            System.out.println();
        }
        System.out.println("--------------------------");
    }
 
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
//    private static void createAndShowGUI() {
//        //Create and set up the window.
//        JFrame frame = new JFrame("SimpleTableDemo");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
// 
//        //Create and set up the content pane.
//        GuiTable newContentPane = new GuiTable();
//        newContentPane.setOpaque(true); //content panes must be opaque
//        frame.setContentPane(newContentPane);
// 
//        //Display the window.
//        frame.pack();
//        frame.setVisible(true);
//    }
 
//    public static void main(String[] args) {
//        //Schedule a job for the event-dispatching thread:
//        //creating and showing this application's GUI.
//        javax.swing.SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                createAndShowGUI();
//            }
//        });
//    }
}