package Scraper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.net.URL;

import database.Fighter;
import database.StatsHandler;

public class IterativeFighterScraper {
	
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

	
	private StatsHandler db = new StatsHandler();

	public IterativeFighterScraper(URL url, int id) throws MalformedURLException {
		try {
			this.id = id;
			URLConnection yc = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));
			String inputLine;
			
			while ((inputLine = in.readLine()) != null) {
				// Scrape: Age
				if (inputLine.contains("AGE</td>")) {
					inputLine = in.readLine();
					inputLine = inputLine.replaceAll("\\<.*?>", "");
					inputLine = inputLine.replaceAll("\\(.*?\\)", "");
					inputLine = inputLine.replaceAll("  ", " ");
					System.out.println(inputLine);
					age = Integer.parseInt(inputLine.trim());
				}

				// Scrape: Striking Accuracy
				if (inputLine.contains("Str. Acc.</td>")) {
					inputLine = in.readLine();
					inputLine = inputLine.replaceAll("\\<.*?>", "");
					inputLine = inputLine.replaceAll("\\(.*?\\)", "");
					inputLine = inputLine.replaceAll("  ", " ");
					inputLine = inputLine.trim();
					inputLine = inputLine.substring(0,inputLine.length()-1);
					str_acc = Integer.parseInt(inputLine);
				}

				// Scrape: Striking Defense
				if (inputLine.contains("Str. Def.</td>")) {
					inputLine = in.readLine();
					inputLine = inputLine.replaceAll("\\<.*?>", "");
					inputLine = inputLine.replaceAll("\\(.*?\\)", "");
					inputLine = inputLine.replaceAll("  ", " ");
					inputLine = inputLine.trim();
					inputLine = inputLine.substring(0,inputLine.length()-1);
					str_def = Integer.parseInt(inputLine);
				}

				// Scrape: Takedown Accuracy
				if (inputLine.contains("TD Acc.</td>")) {
					inputLine = in.readLine();
					inputLine = inputLine.replaceAll("\\<.*?>", "");
					inputLine = inputLine.replaceAll("\\(.*?\\)", "");
					inputLine = inputLine.replaceAll("  ", " ");
					inputLine = inputLine.trim();
					inputLine = inputLine.substring(0,inputLine.length()-1);
					td_acc = Integer.parseInt(inputLine);
				}

				// Scrape: Takedown Defense
				if (inputLine.contains("TD Def.</td>")) {
					inputLine = in.readLine();
					inputLine = inputLine.replaceAll("\\<.*?>", "");
					inputLine = inputLine.replaceAll("\\(.*?\\)", "");
					inputLine = inputLine.replaceAll("  ", " ");
					inputLine = inputLine.trim();
					inputLine = inputLine.substring(0,inputLine.length()-1);
					td_def = Integer.parseInt(inputLine);
				}

				// Scrape: Wins/Loses/Draws/No Contests OK
				if (inputLine.contains("RECORD</td>")) {
					inputLine = in.readLine();
					inputLine = inputLine.replaceAll("\\<.*?>", "");
					inputLine = inputLine.replaceAll("\\(.*?\\)", "");
					inputLine = inputLine.replaceAll("  ", " ");
					w = Integer.parseInt(inputLine.trim());
					inputLine = in.readLine();
					inputLine = in.readLine();
					inputLine = inputLine.replaceAll("\\<.*?>", "");
					inputLine = inputLine.replaceAll("\\(.*?\\)", "");
					inputLine = inputLine.replaceAll("  ", " ");
					l = Integer.parseInt(inputLine.trim());
					inputLine = in.readLine();
					inputLine = in.readLine();
					inputLine = inputLine.replaceAll("\\<.*?>", "");
					inputLine = inputLine.replaceAll("\\(.*?\\)", "");
					inputLine = inputLine.replaceAll("  ", " ");
					d = Integer.parseInt(inputLine.trim());
					inputLine = in.readLine();
					inputLine = in.readLine();
					inputLine = inputLine.replaceAll("\\<.*?>", "");
					inputLine = inputLine.replaceAll("\\(.*?\\)", "");
					inputLine = inputLine.replaceAll("  ", " ");
					nc = Integer.parseInt(inputLine.trim());
				}
				
				// Scrape: Name OK
				if (inputLine.contains("fighter_details_h1")) {
					inputLine = inputLine.replaceAll("\\<.*?>", "");
					inputLine = inputLine.replaceAll("\\(.*?\\)", "");
					inputLine = inputLine.replaceAll("  ", " ");
					name = inputLine.trim();
				}
				// Scrape: Nickname OK
				if (inputLine.contains("nickname\">")) {
					inputLine = inputLine.replaceAll("\\<.*?>", "");
					inputLine = inputLine.replaceAll("\\(.*?\\)", "");
					inputLine = inputLine.replaceAll("  ", " ");
					nickname = inputLine.trim();
				}
				// Scrape: Height OK
				if (inputLine.contains(">HEIGHT</td>")) {
					inputLine = in.readLine();
					inputLine = inputLine.replaceAll("\\<.*?>", "");
					inputLine = inputLine.replaceAll("\\(.*?\\)", "");
					inputLine = inputLine.replaceAll("  ", " ");
					height = inputLine.trim();
				}
				// Scrape: Weight OK
				if (inputLine.contains(">WEIGHT</td>")) {
					inputLine = in.readLine();
					inputLine = inputLine.replaceAll("\\<.*?>", "");
					inputLine = inputLine.replaceAll("\\(.*?\\)", "");
					inputLine = inputLine.replaceAll("  ", " ");
					weight = inputLine.trim();
				}
				// Scrape: Reach OK
				if (inputLine.contains(">REACH</td>")) {
					inputLine = in.readLine();
					inputLine = inputLine.replaceAll("\\<.*?>", "");
					inputLine = inputLine.replaceAll("\\(.*?\\)", "");
					inputLine = inputLine.replaceAll("  ", " ");
					reach = inputLine.trim();
				}

				// Scrape: Stance OK
				if (inputLine.contains(">STANCE</td>")) {
					inputLine = in.readLine();
					inputLine = inputLine.replaceAll("\\<.*?>", "");
					inputLine = inputLine.replaceAll("\\(.*?\\)", "");
					inputLine = inputLine.replaceAll("  ", " ");
					stance = inputLine.trim();
				}
				// Scrape: Strikes Landed Per minute
				if (inputLine.contains("SLpM</td>")) {
					inputLine = in.readLine();
					inputLine = inputLine.replaceAll("\\<.*?>", "");
					inputLine = inputLine.replaceAll("\\(.*?\\)", "");
					inputLine = inputLine.replaceAll("  ", " ");
					inputLine = inputLine.trim();
					inputLine = inputLine.substring(0,inputLine.length());
					System.out.println(inputLine);
					slpm = Double.parseDouble(inputLine);
				}
				// Scrape: Strikes Absorbed Per minute
				if (inputLine.contains(">SApM</td>")) {
					inputLine = in.readLine();
					inputLine = inputLine.replaceAll("\\<.*?>", "");
					inputLine = inputLine.replaceAll("\\(.*?\\)", "");
					inputLine = inputLine.replaceAll("  ", " ");
					inputLine = inputLine.trim();
					inputLine = inputLine.substring(0,inputLine.length());
					sapm = Double.parseDouble(inputLine);
				}
				// Scrape: Takedown Average
				if (inputLine.contains("TD Avg.</td>")) {
					inputLine = in.readLine();
					inputLine = inputLine.replaceAll("\\<.*?>", "");
					inputLine = inputLine.replaceAll("\\(.*?\\)", "");
					inputLine = inputLine.replaceAll("  ", " ");
					inputLine = inputLine.trim();
					inputLine = inputLine.substring(0,inputLine.length());
					td_avg = Double.parseDouble(inputLine);
				}
				// Scrape: Submission Average
				if (inputLine.contains("Sub. Avg.</td>")) {
					inputLine = in.readLine();
					inputLine = inputLine.replaceAll("\\<.*?>", "");
					inputLine = inputLine.replaceAll("\\(.*?\\)", "");
					inputLine = inputLine.replaceAll("  ", " ");
					inputLine = inputLine.trim();
					inputLine = inputLine.substring(0,inputLine.length());
					sub_avg = Double.parseDouble(inputLine);
				}
			}
				in.close();
				eventToDb();
			} catch (Exception e) {
				e.printStackTrace();
				
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
    }
    public void closeEventToDb() {
    	db.close();
    	System.out.println("\n" + "Done writing to DB.");
    }
    public String getFighterinfo() {
		return id + "\n" + age  + "\n" + str_acc  + "\n" + str_def  + "\n" + td_acc  + "\n" +
				td_def  + "\n" + w  + "\n" + l  + "\n" + d  + "\n" + nc  + "\n" + name  + "\n" +
				nickname  + "\n" + height  + "\n" + weight  + "\n" + reach  + "\n" +
				stance  + "\n" + slpm  + "\n" + sapm  + "\n" + td_avg  + "\n" +
				sub_avg;
	}
}
