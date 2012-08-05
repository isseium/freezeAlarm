package com.ipuweb.freezealerm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class FreezeAlermActivity extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
    Button button1;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(this);
    }
    
    public void onClick(View v) {
    	if (v == button1) {
    		Intent intent = new Intent(this, ConfigActivity.class);
    		startActivity(intent);
    	}
    }
        
}
