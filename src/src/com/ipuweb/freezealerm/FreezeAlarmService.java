package com.ipuweb.freezealerm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

public class FreezeAlarmService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// 次回起動時刻を設定
	    Intent intent = new Intent(this, AutoStartReceiver.class);
	    
	    intent.setAction("freezeAlarm");	// TODO 定数に変更        
		PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
		AlarmManager am = (AlarmManager)(this.getSystemService(ALARM_SERVICE));
		
	    Calendar cal = Calendar.getInstance();
	    
	    // Sharedpreferencesから起動時刻を取得
	    // TODO: 定数化
	    SharedPreferences pref = getSharedPreferences("freezealarm", MODE_PRIVATE);
	    String set_hour = pref.getString("hour", "0");
	    String set_minute = pref.getString("minute", "0");
	    cal.setTimeInMillis(System.currentTimeMillis());
	    cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(set_hour));
	    cal.set(Calendar.MINUTE, Integer.parseInt(set_minute));
	    am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);
		
	}
	
}
