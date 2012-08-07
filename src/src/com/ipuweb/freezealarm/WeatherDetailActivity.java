package com.ipuweb.freezealarm;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class WeatherDetailActivity extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
		Log.d("START", "WEATHER DETAIL ACTIVITY");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_detail);
        
        // Notification を削除
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        
        // Intent 処理
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String location_label = "";
        String minC = "0";
        if (extras!=null) {
			location_label = extras.getString("location_label");
			minC = Integer.toString(extras.getInt("minC"));
		}
        
        // 画面情報
        TextView tv = (TextView)this.findViewById(R.id.textViewMessage);
        tv.setText("明日の" + location_label + "の最低気温は" + minC + "度です");
        
        // Tap定義
        Button button1 = (Button)this.findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				WeatherDetailActivity.this.finish();
			}
		});
        
    }
}
