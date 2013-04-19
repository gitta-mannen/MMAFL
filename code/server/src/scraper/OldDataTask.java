package scraper;

import java.sql.SQLException;

import settings.Settings;
import util.Pair;

public class OldDataTask extends OldTask {
	private int stCount;
	
	public OldDataTask(String name) throws SQLException, Exception {
		super(name);
		compileStatements();
	}

	@Override
	protected void compileStatements() throws SQLException {		
		String[] pStatements = Settings.getNodeText("data-tasks:" + name + ":prepared-statement");
		stCount = pStatements.length;
		for (int i = 0; i < stCount; i++) {				
			db.addStatement(new Pair<String, String>(name + "-" + i, pStatements[i]));						
		}		
	}

	@Override
	protected void task() throws Exception {
		for (int i = 0; i < stCount; i++) {
			db.executePs(name + "-" + i);			
		}
	}

}
