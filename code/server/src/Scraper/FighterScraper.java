package Scraper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import database.Fighter;
import database.StatsHandler;

//import java.util.ArrayList;

public class FighterScraper {
	
	private int id;
	private int age;
	private int str_acc; 
	private int str_def; 
	private int td_acc;
	private int td_def; 
	private int w, l, d, nc; 

	private String name;
	private String nickname;
	private String height, weight, reach;
	private String stance;
	
	private double slpm; 
	private double sapm; 
	private double td_avg;
	private double sub_avg; 

	public FighterScraper(URL url) throws MalformedURLException {
		{
			// fightcardArray = new ArrayList<String>();
			try {
				URLConnection yc = url.openConnection();
				BufferedReader in = new BufferedReader(new InputStreamReader(
						yc.getInputStream()));
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					//Scrape: Age
					if (inputLine.contains(">AGE</td>")) {
						inputLine = in.readLine();
						inputLine = inputLine.replaceAll("\\<.*?>", "");
						inputLine = inputLine.replaceAll("\\(.*?\\)", "");
						inputLine = inputLine.replaceAll("  ", " ");
						inputLine = inputLine.replaceAll("   ", " ");
						inputLine = inputLine.replaceAll("    ", " ");
						age = Integer.parseInt(inputLine);
					}
						
					//Scrape: Striking Accuracy
					if (inputLine.contains("Str. Acc.</td>")) {
						inputLine = in.readLine();
						inputLine = inputLine.replaceAll("\\<.*?>", "");
						inputLine = inputLine.replaceAll("\\(.*?\\)", "");
						inputLine = inputLine.replaceAll("  ", " ");
						inputLine = inputLine.replaceAll("   ", " ");
						inputLine = inputLine.replaceAll("    ", " ");
						str_acc = Integer.parseInt(inputLine);
					}
						
					//Scrape: Striking Defense
					if (inputLine.contains("Str. Def.</td>")) {
						inputLine = in.readLine();
						inputLine = inputLine.replaceAll("\\<.*?>", "");
						inputLine = inputLine.replaceAll("\\(.*?\\)", "");
						inputLine = inputLine.replaceAll("  ", " ");
						inputLine = inputLine.replaceAll("   ", " ");
						inputLine = inputLine.replaceAll("    ", " ");
						str_def = Integer.parseInt(inputLine);
					}
					
					//Scrape: Takedown Accuracy
					if (inputLine.contains("TD Acc.</td>")) {
						inputLine = in.readLine();
						inputLine = inputLine.replaceAll("\\<.*?>", "");
						inputLine = inputLine.replaceAll("\\(.*?\\)", "");
						inputLine = inputLine.replaceAll("  ", " ");
						inputLine = inputLine.replaceAll("   ", " ");
						inputLine = inputLine.replaceAll("    ", " ");
						td_acc = Integer.parseInt(inputLine);
					}
					
					//Scrape: Takedown Defense
					if (inputLine.contains("TD Def.</td>")) {
						inputLine = in.readLine();
						inputLine = inputLine.replaceAll("\\<.*?>", "");
						inputLine = inputLine.replaceAll("\\(.*?\\)", "");
						inputLine = inputLine.replaceAll("  ", " ");
						inputLine = inputLine.replaceAll("   ", " ");
						inputLine = inputLine.replaceAll("    ", " ");
						td_def = Integer.parseInt(inputLine);
					}
					
					//Scrape: Wins/Loses/Draws/No Contests
					if (inputLine.contains(">RECORD</td>")) {
						inputLine = in.readLine();
						inputLine = inputLine.replaceAll("\\<.*?>", "");
						inputLine = inputLine.replaceAll("\\(.*?\\)", "");
						inputLine = inputLine.replaceAll("  ", " ");
						inputLine = inputLine.replaceAll("   ", " ");
						inputLine = inputLine.replaceAll("    ", " ");
						w = Integer.parseInt(inputLine);
						inputLine = in.readLine();
						inputLine = in.readLine();
						inputLine = inputLine.replaceAll("\\<.*?>", "");
						inputLine = inputLine.replaceAll("\\(.*?\\)", "");
						inputLine = inputLine.replaceAll("  ", " ");
						inputLine = inputLine.replaceAll("   ", " ");
						inputLine = inputLine.replaceAll("    ", " ");
						l = Integer.parseInt(inputLine);
						inputLine = in.readLine();
						inputLine = in.readLine();
						inputLine = inputLine.replaceAll("\\<.*?>", "");
						inputLine = inputLine.replaceAll("\\(.*?\\)", "");
						inputLine = inputLine.replaceAll("  ", " ");
						inputLine = inputLine.replaceAll("   ", " ");
						inputLine = inputLine.replaceAll("    ", " ");
						d = Integer.parseInt(inputLine);
						inputLine = in.readLine();
						inputLine = in.readLine();
						inputLine = inputLine.replaceAll("\\<.*?>", "");
						inputLine = inputLine.replaceAll("\\(.*?\\)", "");
						inputLine = inputLine.replaceAll("  ", " ");
						inputLine = inputLine.replaceAll("   ", " ");
						inputLine = inputLine.replaceAll("    ", " ");
						nc = Integer.parseInt(inputLine);
					}	
					// Scrape: Name
					if (inputLine.contains("fighter_details_h1")) {
						inputLine = inputLine.replaceAll("\\<.*?>", "");
						inputLine = inputLine.replaceAll("\\(.*?\\)", "");
						inputLine = inputLine.replaceAll("  ", " ");
						name = inputLine;
					}
					//Scrape: Nickname
					if (inputLine.contains("nickname\">")) {
						inputLine = inputLine.replaceAll("\\<.*?>", "");
						inputLine = inputLine.replaceAll("\\(.*?\\)", "");
						inputLine = inputLine.replaceAll("  ", " ");
						nickname = inputLine;
					}
					// Scrape: Height
					if (inputLine.contains(">HEIGHT</td>")) {
						inputLine = in.readLine();
						inputLine = inputLine.replaceAll("\\<.*?>", "");
						inputLine = inputLine.replaceAll("\\(.*?\\)", "");
						inputLine = inputLine.replaceAll("  ", " ");
						inputLine = inputLine.replaceAll("   ", " ");
						inputLine = inputLine.replaceAll("    ", " ");
						height = inputLine;
					}
					// Scrape: Weight
					if (inputLine.contains(">WEIGHT</td>")) {
						inputLine = in.readLine();
						inputLine = inputLine.replaceAll("\\<.*?>", "");
						inputLine = inputLine.replaceAll("\\(.*?\\)", "");
						inputLine = inputLine.replaceAll("  ", " ");
						inputLine = inputLine.replaceAll("   ", " ");
						inputLine = inputLine.replaceAll("    ", " ");
						// fightcardArray.add(inputLine);
						weight = inputLine;
					}
					// Scrape: Reach
					if (inputLine.contains(">REACH</td>")) {
						inputLine = in.readLine();
						inputLine = inputLine.replaceAll("\\<.*?>", "");
						inputLine = inputLine.replaceAll("\\(.*?\\)", "");
						inputLine = inputLine.replaceAll("  ", " ");
						inputLine = inputLine.replaceAll("   ", " ");
						inputLine = inputLine.replaceAll("    ", " ");
						// fightcardArray.add(inputLine);
						reach = inputLine;
					}
					
					//Scrape: Stance
					if (inputLine.contains(">STANCE</td>")) {
						inputLine = in.readLine();
						inputLine = inputLine.replaceAll("\\<.*?>", "");
						inputLine = inputLine.replaceAll("\\(.*?\\)", "");
						inputLine = inputLine.replaceAll("  ", " ");
						inputLine = inputLine.replaceAll("   ", " ");
						inputLine = inputLine.replaceAll("    ", " ");
						// fightcardArray.add(inputLine);
						stance = inputLine;
					}
					//Scrape: Strikes Landed Per minute
					if (inputLine.contains(">STANCE</td>")) {
						inputLine = in.readLine();
						inputLine = inputLine.replaceAll("\\<.*?>", "");
						inputLine = inputLine.replaceAll("\\(.*?\\)", "");
						inputLine = inputLine.replaceAll("  ", " ");
						inputLine = inputLine.replaceAll("   ", " ");
						inputLine = inputLine.replaceAll("    ", " ");
						//slpm = inputLine;
					}
					//Scrape: Strikes Absorbed Per minute
					if (inputLine.contains(">SLpM</td>")) {
						inputLine = in.readLine();
						inputLine = inputLine.replaceAll("\\<.*?>", "");
						inputLine = inputLine.replaceAll("\\(.*?\\)", "");
						inputLine = inputLine.replaceAll("  ", " ");
						inputLine = inputLine.replaceAll("   ", " ");
						inputLine = inputLine.replaceAll("    ", " ");
						sapm = Double.parseDouble(inputLine);
					}
					//Scrape: Takedown Average 
					if (inputLine.contains("TD Avg.</td>")) {
						inputLine = in.readLine();
						inputLine = inputLine.replaceAll("\\<.*?>", "");
						inputLine = inputLine.replaceAll("\\(.*?\\)", "");
						inputLine = inputLine.replaceAll("  ", " ");
						inputLine = inputLine.replaceAll("   ", " ");
						inputLine = inputLine.replaceAll("    ", " ");
						td_avg = Double.parseDouble(inputLine);
					}
					//Scrape: Submission Average 
					if (inputLine.contains("Sub. Avg.</td>")) {
						inputLine = in.readLine();
						inputLine = inputLine.replaceAll("\\<.*?>", "");
						inputLine = inputLine.replaceAll("\\(.*?\\)", "");
						inputLine = inputLine.replaceAll("  ", " ");
						inputLine = inputLine.replaceAll("   ", " ");
						inputLine = inputLine.replaceAll("    ", " ");
						sub_avg = Double.parseDouble(inputLine);
					}
					}
					in.close();
					eventToDb();
				} catch (Exception e) {
					e.printStackTrace();
					
				}
		}
		}
	public void eventToDb() {
    	// used once for all scrapers
		StatsHandler db = new StatsHandler();
		// once per record (i.e once per event)
		db.update(new Fighter(id, age, str_acc, str_def, td_acc,
						td_def, w, l, d, nc, name,
						nickname, height, weight, reach,
						stance, slpm, sapm, td_avg,
						sub_avg));
		
		System.out.println("Id: " + id + " Fighter: " + name);
		System.out.println("Done");
		// once for all scrapers
		db.close();
    }

	public String getFighterinfo() {
		return id + "\n" + age  + "\n" + str_acc  + "\n" + str_def  + "\n" + td_acc  + "\n" +
				td_def  + "\n" + w  + "\n" + l  + "\n" + d  + "\n" + nc  + "\n" + name  + "\n" +
				nickname  + "\n" + height  + "\n" + weight  + "\n" + reach  + "\n" +
				stance  + "\n" + slpm  + "\n" + sapm  + "\n" + td_avg  + "\n" +
				sub_avg;
	}
}
