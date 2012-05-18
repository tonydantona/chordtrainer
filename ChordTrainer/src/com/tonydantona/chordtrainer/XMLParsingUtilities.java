package com.tonydantona.chordtrainer;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;
import android.content.res.XmlResourceParser;


public class XMLParsingUtilities {
	
	public static int GetEventType(XmlResourceParser myKeys) {
		try {
			return  myKeys.getEventType();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static int GetNextParseEvent(XmlResourceParser myKeys) {
		try {
			return myKeys.next();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

}
