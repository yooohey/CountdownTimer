package sample.application.countdowntimer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import java.util.Timer;
import java.util.TimerTask;
import android.content.Context;
import android.os.PowerManager;

public class TimerService extends Service {

	Context mContext;
	int counter;
	Timer timer;
	public PowerManager.WakeLock wl;

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		mContext = this;
		this.counter = intent.getIntExtra("counter", 0);
		if(counter!=0){
			PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
			this.wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK+PowerManager.ON_AFTER_RELEASE, "My Tag");
			this.wl.acquire();
			this.startTimer();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		this.timer.cancel();
		if(this.wl.isHeld()){
			this.wl.release();
		}

	}

	public void startTimer(){
		if(timer != null){
			this.timer.cancel();
		}
		this.timer = new Timer();
		final android.os.Handler handler = new android.os.Handler();

		timer.schedule(new TimerTask(){
			@Override
			public void run(){
				handler.post(new Runnable(){
					public void run(){
						if(counter == -1){
							timer.cancel();
							if(wl.isHeld()){
								wl.release();
							}
							showAlarm();
						}else{
							CountdownTimerActivity.countdown(counter);
							counter = counter -1;
						}
					}
				});
			}
		}, 0, 1000);
	}

	public void showAlarm(){

	}



	@Override
	public IBinder onBind(Intent arg0) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
