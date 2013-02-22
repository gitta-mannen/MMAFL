package GUI;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import Scraper.EventScraper;
import Scraper.ResultScraper;
 
@SuppressWarnings("serial")
public class GUImenu extends JFrame {
 //   private BufferedImage image;
final String url = "http://hosteddb.fightmetric.com/events/details/122";
      
 
    public GUImenu() {
 
        //set the Frame size
        this.setSize(600, 600);
 
        //Create the menu bar.
        JMenuBar menuBar = new JMenuBar();
 
        //Build the first menu.
        JMenu filemenu = new JMenu("File");
        JMenu usermenu = new JMenu("User");
        JMenu cardsmenu = new JMenu("Card");
 
        //add the submenu's to the menu bar (File, Edit, Color)
        menuBar.add(filemenu);
        menuBar.add(usermenu);
        menuBar.add(cardsmenu);
 

        
        
        
        JMenuItem quititem = new JMenuItem("Quit");
        quititem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(-1);
            }
        });
        
        JMenuItem signinitem = new JMenuItem("Sign In");
        quititem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(-1);
            }
        });
 
        JMenuItem FightCardItem = new JMenuItem("Fightcard");
        FightCardItem.addActionListener(new ActionListener() {
        EventScraper fightevent;
        @Override
        public void actionPerformed(ActionEvent e) {
			try {
				fightevent = new EventScraper(new URL(url));
			} catch (MalformedURLException e1) {
				System.out.println("Invalid url");
				
			}
			JOptionPane.showMessageDialog(rootPane, fightevent.getFightcard());
        }
    });
        
        JMenuItem ResultCardItem = new JMenuItem("Result");
        ResultCardItem.addActionListener(new ActionListener() {
        ResultScraper result;
        @Override
        public void actionPerformed(ActionEvent e) {
			try {
				result = new ResultScraper(new URL(url));
			} catch (MalformedURLException e1) {
				System.out.println("Invalid url");
				
			}
			JOptionPane.showMessageDialog(rootPane, result.getWinnerArray());
        }
    });

        //add the sub menu items to their respective menus
        filemenu.add(quititem);
        usermenu.add(signinitem);
        cardsmenu.add(FightCardItem);
        cardsmenu.add(ResultCardItem);
 
        //exit the program when the user clicks on the X in the window
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Set awesome UFC-logo as background.
        String separator = System.getProperty("file.separator");
        String rootPath = System.getProperty("user.dir");
        String imgPath = rootPath + separator + "img" + separator;
        BufferedImage myPicture;
        try {
            //myPicture = ImageIO.read(new File(imgPath + "logo3.png"));
            myPicture = ImageIO.read(new File("img\\logo3.png"));
            JLabel picLabel = new JLabel(new ImageIcon( myPicture ));
            this.getContentPane().add(picLabel);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        //add title to main window
        this.setJMenuBar(menuBar); //set the Frames JMenuBar
        this.setTitle("UFC Fantasy League 2012"); //title of the frame
        this.setVisible(true); //show the Frame
       
    }
    
 
//    public static void main(String[] args) {
//        new GUImenu();
//    }
}
