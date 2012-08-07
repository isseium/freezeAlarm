package com.ipuweb.freezealarm;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

public class FreezeAlarmActivity extends Activity implements OnClickListener{
	/**
	 * View とひも付け
	 */
	Button button1;
	Button button2;
	TimePicker timepicker;
	Spinner spinner;
	
	
	/**
	 * SharedPreference用Key定数
	 */
	private static final String PREF_KEY = "freezealarm";
	private static final String KEY_HOUR ="hour";
	private static final String KEY_MINUTE = "minute";
	private static final String KEY_LOCATE = "locate";
	
	/**
	 * SharedPreference
	 */
	SharedPreferences pref;
	SharedPreferences.Editor editor;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		Log.d("START", "START ACTIVITY");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // view と 変数 をひも付け
        init();
        
        // Notification を削除
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
	
	/**
	 * 各種 view とメンバ変数をひもづける
	 * TODO:isseium ボタン名をもっと正確に
	 */
    private void init()
    {
    	spinner = (Spinner)findViewById(R.id.spinner1);
    	button1 = (Button)findViewById(R.id.button1);
    	button2 = (Button)findViewById(R.id.button2);
        timepicker = (TimePicker)findViewById(R.id.timePicker1);
        
        pref = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        final String set_hour = pref.getString(KEY_HOUR, "0");
        final String set_minute = pref.getString(KEY_MINUTE, "0");
        final String set_locate = pref.getString(KEY_LOCATE, "0");
        
        // タップ定義
        // 処理は onClick メソッドに記載
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        
        // デフォルト
        timepicker.setCurrentHour(Integer.valueOf(set_hour));
        timepicker.setCurrentMinute(Integer.valueOf(set_minute));
        timepicker.setIs24HourView(true);	// 24時間表示
        spinner.setSelection(Integer.valueOf(set_locate));
        
    }
    
    
    /**
     * タップ時の動作を定義
     * TODO:isseium onClickListener とどっちがいんだろうね
     */
    public void onClick(View v) {
    	if (v == button1) {
	    	// 設定完了をタップしたとき:
    		// 1. SharedPreferences に値を格納
    		// 2. AlarmManager を再設定
    		// 3. 通知
    		// 4. 終了
    		if (setConfig()) {
    			Log.d("debug", "config set true");
		    	// FreezeAlarmService を再起動し、AlarmManager の起動を再設定する
    			Intent serviceIntent = new Intent(this, FreezeAlarmService.class);
    			stopService(serviceIntent);
    			startService(serviceIntent);
    			
    			// Toast 表示
			    SharedPreferences pref = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
			    String set_location = pref.getString(KEY_LOCATE, "");	// デフォルト値が呼ばれるときってあるかな
			    String set_hour 	= pref.getString(KEY_HOUR, "0");
			    String set_minute 	= pref.getString(KEY_MINUTE, "0");
			    
			    // 別案 String toastMessage = "翌朝の " + set_location + " の最低気温が4度を下回ると" + set_hour + "時" + set_minute + "分にアラームを通知します";
			    String toastMessage = set_hour + "時" + set_minute + "分に水道管凍結アラームをお知らせします";
    			Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
    			
    			this.finish();
    		} else {
    			Log.d("debug", "config set false");
    		}
    	} else if ( v == button2 ){
    		// テストアラーム
    		// TODO:isseium AutoStartReceiver のコピペなので、抽象化したクラスが欲しい。はやめに！
        	String alarmText, alarmTitle;
		    String[] location_labels = getResources().getStringArray(R.array.location_array_label);
	    	String locationId = Long.toString(spinner.getSelectedItemId()); // NOTE: long => int のcastなので桁落ちが発生しうるが、42億以上の地域を指定する見込みはないので
	    	int minC = -10;	// TODO:isseium テストのときも API たたいて実際の値もってくる？
    		alarmText = "明日の" + location_labels[Integer.parseInt(locationId)] + "の最低気温は、" + Integer.toString(minC) + "度です。";
    		if(minC < -4){
    			alarmTitle = "水道管凍結対策が必要です。";
    			alarmText  = "【警報】" + alarmText;
    		}else if(minC < 0){
    			alarmTitle = "水道管凍結に注意してください。";
    			alarmText  = "【注意】" + alarmText;
    		}else{
    			alarmTitle = "水道管凍結の恐れはありません。";
    			alarmText  = "【安全】" + alarmText;
    		}
    		
    		alarmText = "【テスト】" + alarmText;
    		
    		// 通知
	        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
	        Notification notification = new Notification(R.drawable.ic_launcher, alarmText,System.currentTimeMillis());
	        Intent intent = new Intent(this, FreezeAlarmActivity.class);
	        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);
	        notification.setLatestEventInfo(
	                this.getApplicationContext(),
	                alarmTitle,
	                alarmText,
	                contentIntent);
	        mNotificationManager.notify(R.string.app_name, notification);
    	}
    }
    
    /**
     * 設定値を SharedPreferences に格納する
     * @return true 	// 原則 true のみ
     */
    private boolean setConfig() 
    {
    	String locateId = Long.toString(spinner.getSelectedItemId()); // NOTE: long => int のcastなので桁落ちが発生しうるが、42億以上の地域を指定する見込みはないので
    	int hour = timepicker.getCurrentHour();
    	int minute = timepicker.getCurrentMinute();
    	
    	editor = pref.edit();
    	
    	Log.d("weather query id", locateId);
    	editor.putString(KEY_LOCATE, locateId);
    	editor.putString(KEY_HOUR, String.valueOf(hour));
    	editor.putString(KEY_MINUTE, String.valueOf(minute));
    	editor.commit();
    	
    	return true;
    }
        
}
