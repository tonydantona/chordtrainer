package com.tonydantona.chordtrainer;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.content.res.XmlResourceParser;
import com.tonydantona.chordtrainer.R;


public class XMLKeysParser {

	private ArrayList<Key> keys = new ArrayList<Key>();
	
	public ArrayList<Key> ParseXMLKeysFile(Context cx, String TAG)
	{
        XmlResourceParser myKeys = cx.getResources().getXml(R.xml.keys);
        
        //Get next parse event
		XMLParsingUtilities.GetNextParseEvent(myKeys);
				
		//Get current xml event i.e., START_DOCUMENT etc.
		int eventType = XMLParsingUtilities.GetEventType(myKeys);
        
        String NodeValue = null;
        String name = null;
        String position = null;
        String type = null;
        
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
            	NodeValue = myKeys.getName();//Start of a Node
            }   
            else if(eventType == XmlPullParser.END_TAG)   
            {     
                //End of tag 
            }    
            else if(eventType == XmlPullParser.TEXT && NodeValue != null)   
            {    
            	if (NodeValue.equalsIgnoreCase("Name"))
                 {
                 	name = myKeys.getText();
                 }
                 else if (NodeValue.equalsIgnoreCase("Position"))
                 {
                	position = myKeys.getText();
                 } 
                 else if (NodeValue.equalsIgnoreCase("Type"))
                 {
                	type = (myKeys.getText());
                	
                	Key key = new Key();
                	key.setName(name);
                	key.setPosition(position);
                	key.setType(type);
                	
                  	keys.add(key);
                 } 
            }

            eventType = XMLParsingUtilities.GetNextParseEvent(myKeys); //Get next event from xml parser
        }
        
        return keys;
    }

}

