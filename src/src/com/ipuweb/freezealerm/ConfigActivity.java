package com.ipuweb.freezealerm;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;

public class ConfigActivity extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
	Button button1;
	TimePicker timepicker;
	
	private static final String PREF_KEY = "freezealarm";
	private static final String KEY_HOUR ="hour";
	private static final String KEY_MINUTE = "minute";
	private static final String KEY_LOCATE = "locate";
	
	Spinner spinner;
	
	SharedPreferences pref;
	SharedPreferences.Editor editor;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }
    
    private void init()
    {
    	setContentView(R.layout.config);
    	spinner = (Spinner)findViewById(R.id.spinner1);
    	button1 = (Button)findViewById(R.id.button1);
        timepicker = (TimePicker)findViewById(R.id.timePicker1);
        
        pref = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        final String set_hour = pref.getString(KEY_HOUR, "0");
        final String set_minute = pref.getString(KEY_MINUTE, "0");
        
        timepicker.setCurrentHour(Integer.valueOf(set_hour));
        timepicker.setCurrentMinute(Integer.valueOf(set_minute));
        
        button1.setOnClickListener(this);
    }
    
    public void onClick(View v) 
    {
    	if (v == button1) {
    		if (setConfig()) {
    			Log.d("debug", "config set true");
    		} else {
    			Log.d("debug", "config set false");
    		}
    	}
    }
    
    private boolean setConfig() 
    {
    	String locate = spinner.getSelectedItem().toString();
    	int hour = timepicker.getCurrentHour();
    	int minute = timepicker.getCurrentMinute();
    	
    	editor = pref.edit();
    	editor.putString(KEY_LOCATE, locate);
    	editor.putString(KEY_HOUR, String.valueOf(hour));
    	editor.putString(KEY_MINUTE, String.valueOf(minute));
    	editor.commit();
    	
    	return true;
    }
}