package Scraper;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class ResultScraper implements Runnable {

	private ArrayList<String> fightcardArray;
	private ArrayList<String> winnerArray;
	private boolean resultFlag;
	URL url;
	
	public ResultScraper(URL url)  throws MalformedURLException {
		this.url = url;
	}

	@Override
	public void run() {
		fightcardArray = new ArrayList<String>();
		resultFlag = false;
		try {
			URLConnection yc = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				if (inputLine.contains("Win                        </td>")) {
					resultFlag = true;
				}

				if (inputLine.contains("/fighters/details/") && resultFlag == true) {
					inputLine = inputLine.replaceAll("\\<.*?>", "");
					inputLine = inputLine.replaceAll("\\(.*?\\)", "");
					inputLine = inputLine.replaceAll("  ", " ");
					fightcardArray.add(inputLine);
				}

			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//public ArrayList<String> getWinnerArray() {
	public String getWinnerArray() {
		winnerArray = new ArrayList<String>();
		int i = 0;
		for (String fighter : fightcardArray) {
			if (i % 2 == 0) {
				winnerArray.add(fighter);
			}
			i++;
		}
		if (winnerArray.isEmpty()) {
			winnerArray.add("Sorry, no results are available yet.");
		}
		return "Winners \n" + winnerArray.toString();
	}

}
