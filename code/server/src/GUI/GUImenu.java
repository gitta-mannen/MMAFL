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
			try {
				event = new EventScraper(new URL(eventUrl));
			} catch (MalformedURLException e1) {
				System.out.println("Invalid url");
				
			}
			JOptionPane.showMessageDialog(rootPane, event.getEvent());
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
        ResultScraper result;
        @Override
        public void actionPerformed(ActionEvent e) {
			try {
				result = new ResultScraper(new URL(eventUrl));
			} catch (MalformedURLException e1) {
				System.out.println("Invalid url");
				
			}
			JOptionPane.showMessageDialog(rootPane, result.getWinnerArray());
        }
    });

        //add the sub menu items to their respective menus
        filemenu.add(quititem);
        cardsmenu.add(EventItem);
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
