package GUI;

import javax.swing.*;

import util.GuiTable;

import database.Settings;
import database.StatsHandler;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import Scraper.EventScraper;
import Scraper.FighterScraper;
import Scraper.IterativeUrlFeeder;
 
@SuppressWarnings("serial")
public class GUImenu extends JFrame {
 //   private BufferedImage image;
final String eventUrl = "http://hosteddb.fightmetric.com/events/details/122";
final String fighterUrl = "http://hosteddb.fightmetric.com/fighters/details/372";

public GUImenu() {
 
        //set the Frame size
        this.setSize(600, 600);
 
        //Create the menu bar.
        JMenuBar menuBar = new JMenuBar();
 
        //Build the first menu.
        JMenu filemenu = new JMenu("File");
        JMenu cardsmenu = new JMenu("Card");
 
        //add the submenu's to the menu bar (File, Edit, Color)
        menuBar.add(filemenu);
        menuBar.add(cardsmenu);
 
        	
        	// fulhack för att testa tables i gui. 
        	StatsHandler db = new StatsHandler();
	        Container content = this.getContentPane();
	        content.setBackground(Color.white);
	        content.setLayout(new GridBagLayout()); 
	        
	        Iterator<String> tables = Settings.getInstance().getSchema().getTables().keySet().iterator();
	        while (tables.hasNext()) {
	        	JButton button = new JButton(tables.next());//The JButton name.
	        	GridBagConstraints c = new GridBagConstraints();
	    		add(button);//Add the button to the JFrame.
	        }
	        
	        GridBagConstraints c = new GridBagConstraints();
	        c.anchor = GridBagConstraints.PAGE_END;
	        c.fill = GridBagConstraints.HORIZONTAL;
	        c.ipady = 40;      //make this component tall
	        c.weightx = 0.0;
	        c.gridwidth = 3;
	        c.gridx = 0;
	        c.gridy = 1;
	        add(new GuiTable(new String[]{"id", "org", "loc", "att", "name", "date"}, db.getTable("events")), c);
	        db.close();

        
        
        JMenuItem quititem = new JMenuItem("Quit");
        quititem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(-1);
            }
        });
 
        JMenuItem EventItem = new JMenuItem("Event");
        EventItem.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
        	SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
        	    @Override
        	    public String doInBackground() throws MalformedURLException {
        	        final EventScraper result = new EventScraper(new URL(eventUrl));

        	        return result.getEvent();
        	    }

        	    @Override
        	    public void done() {
        	        try {
        	            JOptionPane.showMessageDialog(rootPane, get());
        	        } catch (InterruptedException ignore) {}
        	        catch (java.util.concurrent.ExecutionException e) {
        	            String why = null;
        	            Throwable cause = e.getCause();
        	            if (cause != null) {
        	                why = cause.getMessage();
        	            } else {
        	                why = e.getMessage();
        	            }
        	            System.out.println(why);
        	        }
        	    }
        	};         	
        	worker.execute();
        }
    });
        JMenuItem IterativeEventItem = new JMenuItem("Iterative Event");
        IterativeEventItem.addActionListener(new ActionListener() {
        IterativeUrlFeeder IterativeEvent;
        @Override
        public void actionPerformed(ActionEvent e) {
			
			IterativeEvent = new IterativeUrlFeeder();
			String s1 = JOptionPane.showInputDialog("Enter event-id start value (min 1, max 616):");
			String s2 = JOptionPane.showInputDialog("Enter start value (min " + (Integer.parseInt(s1)+1) + ", max 616):");
			IterativeEvent.setInterval("Event" ,Integer.parseInt(s1),Integer.parseInt(s2));
			JOptionPane.showMessageDialog(rootPane, IterativeEvent.getInterval());
			IterativeEvent.setEventsToDb();
        }
    });
        JMenuItem IterativeFighterItem = new JMenuItem("Iterative Fighter");
        IterativeFighterItem.addActionListener(new ActionListener() {
        IterativeUrlFeeder IterativeFighter;
        @Override
        public void actionPerformed(ActionEvent e) {
			
			IterativeFighter = new IterativeUrlFeeder();
			String ss1 = JOptionPane.showInputDialog("Enter fighter-id start value (min 1, max 2055):");
			String ss2 = JOptionPane.showInputDialog("Enter start value (min " + (Integer.parseInt(ss1)+1) + ", max 2055):");
			IterativeFighter.setInterval("Fighter", Integer.parseInt(ss1),Integer.parseInt(ss2));
			JOptionPane.showMessageDialog(rootPane, IterativeFighter.getInterval());
			IterativeFighter.setFightersToDb();
			
        }
    });
        JMenuItem FighterItem = new JMenuItem("Fighter");
        FighterItem.addActionListener(new ActionListener() {
        FighterScraper fighter;
        @Override
        public void actionPerformed(ActionEvent e) {
			try {
				fighter = new FighterScraper(new URL(fighterUrl));
			} catch (MalformedURLException e1) {
				System.out.println("Invalid url");
				
			}
			JOptionPane.showMessageDialog(rootPane, fighter.getFighterinfo());
        }
    });
        
        JMenuItem ResultCardItem = new JMenuItem("Result");
        ResultCardItem.addActionListener(new ActionListener() {
    	@Override
        public void actionPerformed(ActionEvent e) {
    		//not implemented
    	}
			
        
    });

        //add the sub menu items to their respective menus
        filemenu.add(quititem);
        cardsmenu.add(EventItem);
        cardsmenu.add(IterativeEventItem);
        cardsmenu.add(IterativeFighterItem);
        cardsmenu.add(FighterItem);
        cardsmenu.add(ResultCardItem);
 
        //exit the program when the user clicks on the X in the window
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //add title to main window
        this.setJMenuBar(menuBar); //set the Frames JMenuBar        
        this.setTitle("UFC Fantasy League 2012"); //title of the frame
        this.setVisible(true); //show the Frame
       
    }
    
    


}
