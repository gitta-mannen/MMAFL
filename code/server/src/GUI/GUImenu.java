package GUI;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
import javax.swing.*;
import java.awt.event.*;
import java.net.MalformedURLException;
import java.net.URL;

import Scraper.EventScraper;
import Scraper.FighterScraper;
import Scraper.IterativeUrlFeeder;
import Scraper.ResultScraper;
 
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
 

        
        
        
        JMenuItem quititem = new JMenuItem("Quit");
        quititem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(-1);
            }
        });
 
        JMenuItem EventItem = new JMenuItem("Event");
        EventItem.addActionListener(new ActionListener() {
        EventScraper event;
        @Override
        public void actionPerformed(ActionEvent e) {
        	SwingWorker worker = new SwingWorker<String, Void>() {
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
    		//do nothing
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
