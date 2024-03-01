package pl.rosmedia.rozmowkipolnorfull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;

public class LessonParser {

	public static ArrayList<Entry> parse(Context appContext, String fileName, String itemTag, String firstTag, String secondTag){

		XmlPullParserFactory pullParserFactory;
		
		try {
			pullParserFactory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = pullParserFactory.newPullParser();

		    InputStream is = appContext.getAssets().open(fileName);
	        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);

            return parseXML(itemTag, firstTag, secondTag, parser);

		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static ArrayList<Entry> parseXML(String itemTag, String firstTag, String secondTag, XmlPullParser parser) throws XmlPullParserException,IOException
	{
		ArrayList<Entry> entries = null;
        int eventType = parser.getEventType();
        Entry currentEntry = null;

        while (eventType != XmlPullParser.END_DOCUMENT){
            String name = null;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                	entries = new ArrayList<Entry>();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase(itemTag)){
                        currentEntry = new Entry();
                    } else if (currentEntry != null){
                        if (name.equalsIgnoreCase(firstTag)){
                            currentEntry.first = parser.nextText();
                        } else if (name.equalsIgnoreCase(secondTag)){
                        	currentEntry.second = parser.nextText();
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase(itemTag) && currentEntry != null){
                    	entries.add(currentEntry);
                    } 
            }
            eventType = parser.next();
        }
        
        return entries;
	}
        
}
