package com.tonydantona.chordtrainer;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener, OnSharedPreferenceChangeListener  
{
	private static final String TAG = MainActivity.class.getSimpleName();
	
	Button btnNext;
	TextView txtViewKey;
	TextView txtViewChord;
	ImageView imgChart;
	
	TextView txtTimer;
	long mStartTime = 0;
	
	boolean boolKeyViewLocked = false;
	boolean boolChordViewLocked = false;
	
	private boolean mDisplayShells = true;
	private boolean mDisplayJazz = true;
	private boolean mDisplayMoveables = true;
	private long mDuration;
	
	ArrayList<Key> keys;
	ArrayList<Chord> chords;
	Map<String,Boolean> mFilter;
		
	SharedPreferences prefs;
	private Random mRand = new Random();
	
	private boolean mIsTimerEnabled = false;
	private CountDownTimer mCountDown;
	
	private final long INTERVAL = 1000;
	private String pad;
	   	
    @Override
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        
        // get access to the standard shared preferences for this context
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
		
		// get the count down time
		long dur = Long.parseLong(prefs.getString("timer", "5"));
		mDuration =  dur * INTERVAL;
      
        XMLKeysParser keysParser = new XMLKeysParser();
        keys =  keysParser.ParseXMLKeysFile(this, MainActivity.class.getSimpleName());
        
        XMLChordsParser chordsParser = new XMLChordsParser();
        chords = chordsParser.ParseXMLChordsFile(this, MainActivity.class.getSimpleName());
           
        btnNext = (Button) findViewById(R.id.buttonNext);
        btnNext.setOnClickListener(this);
        
        txtViewKey = (TextView) findViewById(R.id.txtViewKey);
        txtViewKey.setOnTouchListener(onTxtKeyTouch);
               
        txtViewChord = (TextView) findViewById(R.id.txtViewChord);
        txtViewChord.setOnTouchListener(onTxtChordTouch);  
        
        txtTimer = (TextView) findViewById(R.id.txtViewTimer);
        txtTimer.setOnTouchListener(onTimerTouch);
        
        imgChart = (ImageView) findViewById(R.id.ImageView02);
        imgChart.setOnClickListener(onChartClick);
        
        // keeps the window from dimming or sleeping for this activity only (eg. prefs screen will still dim/sleep)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        Log.d(TAG, String.format("onCreate"));
    }
    
    private CountDownTimer CreateCountDownTimer(long duration, long interval)
    {
    	  return new CountDownTimer(duration + 1000, interval) 
			     {
			            public void onTick(long millisUntilFinished) 
			            {
			                if(String.valueOf(millisUntilFinished / 1000).length() == 1)
			                {
			                	pad = ":0";
			                }
			                else
			                {
			                	pad = ":";
			                }
			            	txtTimer.setText(pad + millisUntilFinished / 1000);
			            }
	
			            public void onFinish() 
			            {
			                if(mIsTimerEnabled)
			                {
			            	    // init the view by forcing a 'next' click
			                    View v = new View(MainActivity.this);
			                    v.setId(R.id.buttonNext);
			                    onClick(v);

			                	beginTimer(mDuration, INTERVAL);
			                }
			            }
			            
		         };
    }
        
    private OnClickListener onChartClick = new OnClickListener()
    {
		@Override
		public void onClick(View v)
		{			
			v.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slideleft));
			imgChart.setImageDrawable( getResources().getDrawable(R.drawable.major5) );
		}
     };
     
    private OnTouchListener onTimerTouch = new OnTouchListener()
    {
    	@Override
		public boolean onTouch(View v, MotionEvent event)
		{
    		if(mIsTimerEnabled)
    		{
    			mIsTimerEnabled = false;
    			endTimer();
    		}
    		else
    		{
    			mIsTimerEnabled = true;
    			beginTimer(mDuration, INTERVAL);
    		}
    			        			
			return false;
		}   	
    };
    
    private void beginTimer(long duration, long interval)
    {
		mCountDown = CreateCountDownTimer(duration,interval);
		mCountDown.start();   			
    }
    
    private void endTimer()
    {
		mCountDown.cancel();
		txtTimer.setText(":00");
    }
    
    @Override
	protected void onPause() 
    {
		super.onPause();
		Log.d(TAG, String.format("onPause")); 
	}

	@Override
	protected void onRestart() 
	{
		super.onRestart();
		Log.d(TAG, String.format("onRestart")); 
	}

	@Override
	protected void onStart() 
	{
		super.onStart();
		Log.d(TAG, String.format("onStart")); 
	}

	@Override
	protected void onStop() 
	{
		super.onStop();
		
		Log.d(TAG, String.format("onStop")); 
	}

	@Override
	protected void onResume() 
	{
		super.onResume();
		
		mDisplayShells = prefs.getBoolean("shells", false);
		mDisplayJazz = prefs.getBoolean("jazz", false);
		mDisplayMoveables = prefs.getBoolean("moveables", false);
		
		long dur = Long.parseLong(prefs.getString("timer", "5"));
		mDuration =  dur * INTERVAL;

		
	    // init the view by forcing a 'next' click
        View v = new View(this);
        v.setId(R.id.buttonNext);
        onClick(v);
        
 		Log.d(TAG, String.format("onResume")); 
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
        	switch(v.getId())
        	{
        	case R.id.buttonNext:
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
	        	break;
	        	
        	default:
        		Toast.makeText(MainActivity.this, "Oops don't recognize that click", Toast.LENGTH_LONG).show();
        	}
			
        }
        catch (Exception e)
        {
        	System.out.println(e);
        }		
	}
	
	public Key GetRandomKey()
	{
		int r = mRand.nextInt(keys.size() );
		return keys.get(r);		
	}
	
	public Chord GetRandomChord()
	{
		ArrayList<Chord> filteredList = new ArrayList<Chord>();
		for(Chord curr : chords)
			{
				if(mDisplayShells && curr.getFilter().equalsIgnoreCase("Shell")  )
				{
					filteredList.add(curr);
				}
				else if (mDisplayJazz && curr.getFilter().equalsIgnoreCase("Jazz"))
				{
					filteredList.add(curr);
				}
				else if (mDisplayMoveables && curr.getFilter().equalsIgnoreCase("Moveable"))
				{
					filteredList.add(curr);
				}
			}
		
		int r = mRand.nextInt(filteredList.size());
		return filteredList.get(r);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// get our menu.xml with the 3 simple attribs
		// (id, title and icon) and create a java object
		getMenuInflater().inflate(R.menu.menu, menu);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			// our menu has only one item: Preferences for now - that's what must have been clicked
			// (remember the menu is what comes up when the menu button is clicked - there could have been
			// multiple choices)
			case R.id.itemPrefs:
				startActivity(new Intent(this, Prefs.class));
				return true;
		}
		
		return false;
	}
	
	@Override
	// someone clicked the filter(s) on the pref menu - retrieve the value
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) 
	{
		// the following sharedPreferences.get???? calls use a id, default if not found as the arguments
		
		if(key.equalsIgnoreCase(MainActivity.this.getString(R.string.shells_key)))
		{
			mDisplayShells = sharedPreferences.getBoolean("shells", false);
			Log.d(TAG, String.format(key + ": now = " + mDisplayShells));
		}
		else if(key.equalsIgnoreCase(MainActivity.this.getString(R.string.jazz_key)))
		{
			mDisplayJazz = sharedPreferences.getBoolean("jazz", false);
			Log.d(TAG, String.format(key + ": now = " + mDisplayJazz));
		}
		else if(key.equalsIgnoreCase(MainActivity.this.getString(R.string.moveables_key)))
		{
			mDisplayMoveables = sharedPreferences.getBoolean("moveables", false);
			Log.d(TAG, String.format(key + ": now = " + mDisplayMoveables));
		}
		else if(key.equalsIgnoreCase(MainActivity.this.getString(R.string.timer_key)))
		{
			long dur = Long.parseLong(sharedPreferences.getString("timer", "5"));
			mDuration =  dur * INTERVAL;
			Log.d(TAG, String.format(key + ": now = " + dur));
		}
	}	
}









