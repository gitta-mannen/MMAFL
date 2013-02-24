package Scraper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

//import java.util.ArrayList;

public class FighterScraper {

	private String fighterName;
	private String nickname;
	private String height, weight, reach;
	private String stance;
	private String age;

	public FighterScraper(URL url) throws MalformedURLException {
		{
			// fightcardArray = new ArrayList<String>();
			try {
				URLConnection yc = url.openConnection();
				BufferedReader in = new BufferedReader(new InputStreamReader(
						yc.getInputStream()));
				String inputLine;
				while ((inputLine = in.readLine()) != null) {

					// Scrape: Fighter Names
					if (inputLine.contains("fighter_details_h1")) {
						inputLine = inputLine.replaceAll("\\<.*?>", "");
						inputLine = inputLine.replaceAll("\\(.*?\\)", "");
						inputLine = inputLine.replaceAll("  ", " ");
						// fightcardArray.add(inputLine);
						fighterName = inputLine;
					}
					//Scrape: Nickname
					if (inputLine.contains("nickname\">")) {
						inputLine = inputLine.replaceAll("\\<.*?>", "");
						inputLine = inputLine.replaceAll("\\(.*?\\)", "");
						inputLine = inputLine.replaceAll("  ", " ");
						// fightcardArray.add(inputLine);
						nickname = inputLine;
					}
					// Scrape: Fighter Height
					if (inputLine.contains(">HEIGHT</td>")) {
						inputLine = in.readLine();
						inputLine = inputLine.replaceAll("\\<.*?>", "");
						inputLine = inputLine.replaceAll("\\(.*?\\)", "");
						inputLine = inputLine.replaceAll("  ", " ");
						inputLine = inputLine.replaceAll("   ", " ");
						inputLine = inputLine.replaceAll("    ", " ");
						// fightcardArray.add(inputLine);
						height = inputLine;
					}
					// Scrape: Fighter Weight
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
					// Scrape: Fighter Weight
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
					if (inputLine.contains(">AGE</td>")) {
						inputLine = in.readLine();
						inputLine = inputLine.replaceAll("\\<.*?>", "");
						inputLine = inputLine.replaceAll("\\(.*?\\)", "");
						inputLine = inputLine.replaceAll("  ", " ");
						inputLine = inputLine.replaceAll("   ", " ");
						inputLine = inputLine.replaceAll("    ", " ");
						// fightcardArray.add(inputLine);
						age = inputLine;
					}
				}
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public String getFighterinfo() {
		return getFighterName() + "\n" + getNickname() + "\n" + getHeight() + "\n" + getWeight() + "\n" 
	+ getReach() + "\n" + getStance() + "\n" + getAge();
	}
	public String getFighterName() {
		return fighterName;
	}
	public String getNickname() {
		return nickname;
	}
	public String getHeight() {
		return height;
	}
	public String getWeight() {
		return weight;
	}
	public String getReach() {
		return reach;
	}
	public String getStance() {
		return stance;
	}
	public String getAge() {
		return age;
	}
	
	/*public ArrayList<String> getFightcardArray() {
	// Make arrayList to stringArray
	String[] fighterArrayToString = fightcardArray.toArray(new String[fightcardArray.size()]);

	// Put every fighter as elements by split at divederToken
	List<String> temp1 = new ArrayList<String>();
	List<String> temp2 = new ArrayList<String>();
	String dividerToken = ".vs";
	for (int i = 0; i < fighterArrayToString.length; i++) {
		temp1 = Arrays.asList(fighterArrayToString[i].split(dividerToken));
		temp2.addAll(temp1);
	}

	return  fightcardArray;
}*/

/*public String getFightcard() {
	String fightcard = "";
	String tempString = "";
	int i = 0;
	for (String fighter : fightcardArray) {
		if (i % 2 == 0) {
			fightcard += (fighter + " vs. ");
			i++;
		} else {
			fightcard += (tempString + (fighter) + " \n\n");
			i++;
		}
	}
	if( fightcard.isEmpty() )
	{
		return "FightcardArray seems to be empty.";
	}
	return  getEventName() + "\n" + getEventLocation() + "\n" + getEventDate() + "\n\n" + fightcard;
}*/
}
