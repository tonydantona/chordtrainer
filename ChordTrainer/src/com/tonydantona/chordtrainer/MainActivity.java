package com.tonydantona.chordtrainer;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener  {
	/** Called when the activity is first created. */
	
	Button btnNext;
	ArrayList<Key> keys;
	ArrayList<Chord> chords;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        
        XMLKeysParser keysParser = new XMLKeysParser();
        keys =  keysParser.ParseXMLKeysFile(this, MainActivity.class.getSimpleName());
        
        XMLChordsParser chordsParser = new XMLChordsParser();
        chords = chordsParser.ParseXMLChordsFile(this, MainActivity.class.getSimpleName());
        
        btnNext = (Button) findViewById(R.id.buttonNext);
        btnNext.setOnClickListener(this);
    }

	@Override
	public void onClick(View v) 
	{
		try
        {
        	// change the key text
			TextView txtViewKey = (TextView) findViewById(R.id.TextView02);
        	txtViewKey.setText(GetRandomKey().getName());
        	
        	// change the chord text
        	TextView txtViewChord = (TextView) findViewById(R.id.TextView01);
        	txtViewChord.setText(GetRandomChord().getName());
        }
        catch (Exception e)
        {
        	System.out.println(e);
        }		
	}
	
	public Key GetRandomKey()
	{
		int r = (int) (Math.random() * keys.size() );
		return keys.get(r);
	}
	
	public Chord GetRandomChord()
	{
		int r = (int) (Math.random() * chords.size() );
		return chords.get(r);
	}
}









