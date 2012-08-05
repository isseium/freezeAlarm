package com.ipuweb.freezealerm;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AutoStartReceiver extends BroadcastReceiver {
	private Context ctx;

    // BroadcastIntentを受信した場合の処理 //
    @Override
    public void onReceive(Context context, Intent intent) {
    	ctx = context;
    	Intent serviceIntent = new Intent(context, FreezeAlarmService.class);
	    context.startService(serviceIntent);
	    
	    // freezeAlarmのときに起動
	    if(intent.getAction().equals("freezeAlarm")){
			WeatherAccessor wa = new WeatherAccessor();
			JSONArray weathers = wa.getTommorowWeather();
			
			try {
		        String targetDate = this.getTargetDate();
		        for (int i = 0; i < weathers.length(); i++) {
		            JSONObject weather = weathers.getJSONObject(i);
		            String date = weather.getString("date");
		            Integer minC = Integer.parseInt(weather.getString("tempMinC"));
		            
		            // 対象日であったら判定
		        	if(targetDate.equals(date)){
			            Log.d("Loading date", date);
			            Log.d("Target date", targetDate);
			            
		        		// アラートレベル
		            	String alarmText;
		            
		        		if(minC < -4){
		        			alarmText = "翌朝の最低気温は、" + Integer.toString(minC) + "度です。水道管凍結対策が必要です";
		        		}else if(minC < 0){
		        			alarmText = "翌朝の最低気温は、" + Integer.toString(minC) + "度です。水道管凍結に注意してください";
		        		}else{
		        			alarmText = "翌朝の最低気温は、" + Integer.toString(minC) + "度です。水道管凍結の恐れはありません";
		        		}
		        		
	        			Log.d("ALERM", alarmText);
	        			this.alarm(alarmText);
		        	}
		        }
			}catch(Exception e){
				Log.e("ERROR", e.getMessage());
			}
	    }
    }
    
	public void alarm(String description){
        NotificationManager mNotificationManager = (NotificationManager) this.ctx.getSystemService(this.ctx.NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.drawable.ic_launcher, description,System.currentTimeMillis());
        Intent intent = new Intent(this.ctx, FreezeAlermActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this.ctx, 0, intent, 0);
        notification.setLatestEventInfo(
                ctx.getApplicationContext(),
                ctx.getString(R.string.app_name),
                description,
                contentIntent);
        notification.flags = Notification.FLAG_ONGOING_EVENT;  
        mNotificationManager.notify(R.string.app_name, notification);
        
	}
    
	public String getTargetDate(){
        // 現在時刻取得
		long timeMillisEnd = System.currentTimeMillis() + 86400 * 1000; // 明日
		// TODO 現在時刻が午前0-3時のときは、当日の日付を返す
		
		// フォーマット定義
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		return sdf1.format(new Date(timeMillisEnd));
	}
	
}