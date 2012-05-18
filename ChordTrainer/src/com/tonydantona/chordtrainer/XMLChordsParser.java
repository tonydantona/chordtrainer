package com.tonydantona.chordtrainer;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.content.res.XmlResourceParser;

public class XMLChordsParser {
	
	private ArrayList<Chord> chords = new ArrayList<Chord>();
	
	public ArrayList<Chord> ParseXMLChordsFile(Context cx, String TAG)
	{
        XmlResourceParser myChords = cx.getResources().getXml(R.xml.chords);
        
        //Get next parse event
		XMLParsingUtilities.GetNextParseEvent(myChords);
				
		//Get current xml event i.e., START_DOCUMENT etc.
		int eventType = XMLParsingUtilities.GetEventType(myChords);
        
        String NodeValue = null;
        String name = null;
        String filter = null;
        
        while (eventType != XmlPullParser.END_DOCUMENT)  //Keep going until end of xml document
        {  
            if(eventType == XmlPullParser.START_DOCUMENT)   
            {                    
            	//Start of XML, can check this with myxml.getName() in Log, see if your xml has read successfully
            	//Log.d(TAG, "In start document"); //myKeys.getName());
            }    
            else if(eventType == XmlPullParser.START_TAG)   
            {     
                // get the node name to be tested on the next event to capture value
            	NodeValue = myChords.getName();//Start of a Node
            }   
            else if(eventType == XmlPullParser.END_TAG)   
            {     
                //End of tag 
            }    
            else if(eventType == XmlPullParser.TEXT && NodeValue != null)   
            {    
            	if (NodeValue.equalsIgnoreCase("Name"))
                 {
                 	name = myChords.getText();
                 }
                 else if (NodeValue.equalsIgnoreCase("Filter"))
                 {
                	filter = (myChords.getText());
                	
                	Chord chord = new Chord();
                	chord.setName(name);
                	chord.setFilter(filter);
                	
                  	chords.add(chord);
                 } 
            }

            eventType = XMLParsingUtilities.GetNextParseEvent(myChords); //Get next event from xml parser
        }
        
        return chords;
    }

}
