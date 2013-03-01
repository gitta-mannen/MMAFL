package Scraper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.net.URL;
import java.util.HashMap;

import database.Fighter;
import database.StatsHandler;

//import java.util.ArrayList;

public class FighterScraper {
	private String id = "1";
	private String table = "fighters";
	private StatsHandler db = new StatsHandler();
	private HashMap<String, String> values = new HashMap<String, String>();
	
	private String first;
	private String last;
	private String age;
	private String str_acc;
	private String str_def;
	private String td_acc;
	private String td_def;
	private String w, l, d, nc;

	private String name;
	private String nickname;
	private String height, weight, reach;
	private String stance;

	private String slpm;
	private String sapm;
	private String td_avg;
	private String sub_avg;

	public FighterScraper(URL url) throws MalformedURLException {
			values.put("id", id);
			// fightcardArray = new ArrayList<String>();
			try {
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
						age = inputLine.trim();
						values.put("age", age);
					}

					// Scrape: Striking Accuracy
					if (inputLine.contains("Str. Acc.</td>")) {
						inputLine = in.readLine();
						inputLine = inputLine.replaceAll("\\<.*?>", "");
						inputLine = inputLine.replaceAll("\\(.*?\\)", "");
						inputLine = inputLine.replaceAll("  ", " ");
						inputLine = inputLine.trim();
						inputLine = inputLine.substring(0,inputLine.length()-1);
						str_acc = inputLine;
						values.put("striking accuracy", str_acc);
					}

					// Scrape: Striking Defense
					if (inputLine.contains("Str. Def.</td>")) {
						inputLine = in.readLine();
						inputLine = inputLine.replaceAll("\\<.*?>", "");
						inputLine = inputLine.replaceAll("\\(.*?\\)", "");
						inputLine = inputLine.replaceAll("  ", " ");
						inputLine = inputLine.trim();
						inputLine = inputLine.substring(0,inputLine.length()-1);
						str_def = inputLine;
						values.put("striking defense", str_def);
					}

					// Scrape: Takedown Accuracy
					if (inputLine.contains("TD Acc.</td>")) {
						inputLine = in.readLine();
						inputLine = inputLine.replaceAll("\\<.*?>", "");
						inputLine = inputLine.replaceAll("\\(.*?\\)", "");
						inputLine = inputLine.replaceAll("  ", " ");
						inputLine = inputLine.trim();
						inputLine = inputLine.substring(0,inputLine.length()-1);
						td_acc = inputLine;
						values.put("takedown accuracy", td_acc);
					}

					// Scrape: Takedown Defense
					if (inputLine.contains("TD Def.</td>")) {
						inputLine = in.readLine();
						inputLine = inputLine.replaceAll("\\<.*?>", "");
						inputLine = inputLine.replaceAll("\\(.*?\\)", "");
						inputLine = inputLine.replaceAll("  ", " ");
						inputLine = inputLine.trim();
						inputLine = inputLine.substring(0,inputLine.length()-1);
						td_def = inputLine;
						values.put("takedown defense", td_def);
					}

					// Scrape: Wins/Loses/Draws/No Contests OK
					if (inputLine.contains("RECORD</td>")) {
						inputLine = in.readLine();
						inputLine = inputLine.replaceAll("\\<.*?>", "");
						inputLine = inputLine.replaceAll("\\(.*?\\)", "");
						inputLine = inputLine.replaceAll("  ", " ");
						w = inputLine.trim();
						inputLine = in.readLine();
						inputLine = in.readLine();
						inputLine = inputLine.replaceAll("\\<.*?>", "");
						inputLine = inputLine.replaceAll("\\(.*?\\)", "");
						inputLine = inputLine.replaceAll("  ", " ");
						l = inputLine.trim();
						inputLine = in.readLine();
						inputLine = in.readLine();
						inputLine = inputLine.replaceAll("\\<.*?>", "");
						inputLine = inputLine.replaceAll("\\(.*?\\)", "");
						inputLine = inputLine.replaceAll("  ", " ");
						d = inputLine.trim();
						inputLine = in.readLine();
						inputLine = in.readLine();
						inputLine = inputLine.replaceAll("\\<.*?>", "");
						inputLine = inputLine.replaceAll("\\(.*?\\)", "");
						inputLine = inputLine.replaceAll("  ", " ");
						nc = inputLine.trim();
						values.put("no contest", nc);
					}
					
					// Scrape: Name OK
					if (inputLine.contains("fighter_details_h1")) {
						inputLine = inputLine.replaceAll("\\<.*?>", "");
						inputLine = inputLine.replaceAll("\\(.*?\\)", "");
						inputLine = inputLine.replaceAll("  ", " ");
						inputLine = inputLine.trim();
						name = inputLine;
						first = inputLine.split("\\s")[0];
						last = inputLine.split("\\s")[1];
						values.put("name", name);
						//values.put("first", first);
						//values.put("last", last);
					}
					// Scrape: Nickname OK
					if (inputLine.contains("nickname\">")) {
						inputLine = inputLine.replaceAll("\\<.*?>", "");
						inputLine = inputLine.replaceAll("\\(.*?\\)", "");
						inputLine = inputLine.replaceAll("  ", " ");
						nickname = inputLine.trim();
						values.put("nickname", nickname);
					}
					// Scrape: Height OK
					if (inputLine.contains(">HEIGHT</td>")) {
						inputLine = in.readLine();
						inputLine = inputLine.replaceAll("\\<.*?>", "");
						inputLine = inputLine.replaceAll("\\(.*?\\)", "");
						inputLine = inputLine.replaceAll("  ", " ");
						height = inputLine.trim();
						values.put("height", height);
					}
					// Scrape: Weight OK
					if (inputLine.contains(">WEIGHT</td>")) {
						inputLine = in.readLine();
						inputLine = inputLine.replaceAll("\\<.*?>", "");
						inputLine = inputLine.replaceAll("\\(.*?\\)", "");
						inputLine = inputLine.replaceAll("  ", " ");
						weight = inputLine.trim();
						values.put("weight", weight);
					}
					// Scrape: Reach OK
					if (inputLine.contains(">REACH</td>")) {
						inputLine = in.readLine();
						inputLine = inputLine.replaceAll("\\<.*?>", "");
						inputLine = inputLine.replaceAll("\\(.*?\\)", "");
						inputLine = inputLine.replaceAll("  ", " ");
						reach = inputLine.trim();
						values.put("reach", reach);
					}

					// Scrape: Stance OK
					if (inputLine.contains(">STANCE</td>")) {
						inputLine = in.readLine();
						inputLine = inputLine.replaceAll("\\<.*?>", "");
						inputLine = inputLine.replaceAll("\\(.*?\\)", "");
						inputLine = inputLine.replaceAll("  ", " ");
						stance = inputLine.trim();
						values.put("stance", stance);
					}
					// Scrape: Strikes Landed Per minute
					if (inputLine.contains("SLpM</td>")) {
						inputLine = in.readLine();
						inputLine = inputLine.replaceAll("\\<.*?>", "");
						inputLine = inputLine.replaceAll("\\(.*?\\)", "");
						inputLine = inputLine.replaceAll("  ", " ");
						inputLine = inputLine.trim();
						inputLine = inputLine.substring(0,inputLine.length());
						slpm = inputLine;
						values.put("strikes landed", slpm);
					}
					// Scrape: Strikes Absorbed Per minute
					if (inputLine.contains(">SApM</td>")) {
						inputLine = in.readLine();
						inputLine = inputLine.replaceAll("\\<.*?>", "");
						inputLine = inputLine.replaceAll("\\(.*?\\)", "");
						inputLine = inputLine.replaceAll("  ", " ");
						inputLine = inputLine.trim();
						inputLine = inputLine.substring(0,inputLine.length());
						sapm = inputLine;
						values.put("strikes absorbed", sapm);
					}
					// Scrape: Takedown Average
					if (inputLine.contains("TD Avg.</td>")) {
						inputLine = in.readLine();
						inputLine = inputLine.replaceAll("\\<.*?>", "");
						inputLine = inputLine.replaceAll("\\(.*?\\)", "");
						inputLine = inputLine.replaceAll("  ", " ");
						inputLine = inputLine.trim();
						inputLine = inputLine.substring(0,inputLine.length());
						td_avg = inputLine;
						values.put("average takedowns", td_avg);
					}
					// Scrape: Submission Average
					if (inputLine.contains("Sub. Avg.</td>")) {
						inputLine = in.readLine();
						inputLine = inputLine.replaceAll("\\<.*?>", "");
						inputLine = inputLine.replaceAll("\\(.*?\\)", "");
						inputLine = inputLine.replaceAll("  ", " ");
						inputLine = inputLine.trim();
						inputLine = inputLine.substring(0,inputLine.length());
						sub_avg = inputLine;
						values.put("average submissions", sub_avg);
					}
				}
				in.close();
				db.update(table, values);
				db.close();
				System.out.println(values);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	public String getFighterinfo() {
		return  id + "\n" + age + "\n" + str_acc + "\n" + str_def + "\n"
				+ td_acc + "\n" + td_def + "\n" + w + "\n" + l + "\n" + d
				+ "\n" + nc + "\n" + name + "\n" + nickname + "\n" + height
				+ "\n" + weight + "\n" + reach + "\n" + stance + "\n" + slpm
				+ "\n" + sapm + "\n" + td_avg + "\n" + sub_avg;
	}
}
