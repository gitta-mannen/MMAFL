package util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public final class Text {
	private Text(){
		throw new IllegalStateException( "Do not instantiate this class." );
	}

	public static String textFromUrl (String url) throws MalformedURLException, IOException {
		Logger.log("connecting to: " + url, true);
		return util.IO.streamToString( (new URL(url)).openConnection().getInputStream());
	}
}
