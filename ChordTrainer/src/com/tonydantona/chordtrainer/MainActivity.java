package com.tonydantona.chordtrainer;

import java.util.ArrayList;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

public class MainActivity extends Activity implements OnClickListener  
{
	/** Called when the activity is first created. */
	
	Button btnNext;
	TextView txtViewKey;
	TextView txtViewChord;
	boolean boolKeyViewLocked = false;
	boolean boolChordViewLocked = false;
	
	ArrayList<Key> keys;
	ArrayList<Chord> chords;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        
        XMLKeysParser keysParser = new XMLKeysParser();
        keys =  keysParser.ParseXMLKeysFile(this, MainActivity.class.getSimpleName());
        
        XMLChordsParser chordsParser = new XMLChordsParser();
        chords = chordsParser.ParseXMLChordsFile(this, MainActivity.class.getSimpleName());
        
        btnNext = (Button) findViewById(R.id.buttonNext);
        btnNext.setOnClickListener(this);
        
        txtViewKey = (TextView) findViewById(R.id.TextView02);
        txtViewKey.setOnTouchListener(onTxtKeyTouch);
        
        txtViewChord = (TextView) findViewById(R.id.TextView01);
        txtViewChord.setOnTouchListener(onTxtChordTouch);
    }

	private OnTouchListener onTxtKeyTouch = new OnTouchListener()
	{
		@Override
		public boolean onTouch(View v, MotionEvent event) 
		{
			if(boolKeyViewLocked == false)
			{
				Toast.makeText(MainActivity.this, "Locking Key", Toast.LENGTH_SHORT).show();
				v.setBackgroundResource(R.drawable.keylockedback);
				boolKeyViewLocked = true;
			}
			else
			{
				Toast.makeText(MainActivity.this, "Unlocking Key", Toast.LENGTH_SHORT).show();
				v.setBackgroundResource(R.drawable.keyunlockedback);
				boolKeyViewLocked = false;
			}
						
			return false;
		}
	};
	
	private OnTouchListener onTxtChordTouch = new OnTouchListener()
	{

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(boolChordViewLocked == false)
			{
				Toast.makeText(MainActivity.this, "Locking Chord", Toast.LENGTH_SHORT).show();
				v.setBackgroundResource(R.drawable.chordlockedback);
				boolChordViewLocked = true;
			}
			else
			{
				Toast.makeText(MainActivity.this, "Unlocking Chord", Toast.LENGTH_SHORT).show();
				v.setBackgroundResource(R.drawable.chordunlockedback);
				boolChordViewLocked = false;
			}
			
			return false;
		}
		
	};
	

    @Override
	public void onClick(View v) 
	{
		try
        {
        	// change the key text			
        	if(boolKeyViewLocked == false)
			{
        		txtViewKey.setText(GetRandomKey().getName());
			}
        	
        	// change the chord text        	
        	if(boolChordViewLocked == false)
        	{
        		txtViewChord.setText(GetRandomChord().getName());
        	}
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









