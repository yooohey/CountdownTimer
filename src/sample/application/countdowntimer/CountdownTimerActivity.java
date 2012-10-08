package sample.application.countdowntimer;

import java.text.SimpleDateFormat;
import java.util.prefs.Preferences;

import android.os.Bundle;
import android.content.Intent;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class CountdownTimerActivity extends Activity {
	static TextView tv;
	static SeekBar sb;
	static Context mContext;
	static int timeLeft=0;
	static Button btnStart, btnStop;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);
        mContext = this;
        tv = (TextView) this.findViewById(R.id.textView1);
        btnStart = (Button) this.findViewById(R.id.buttonStart);
        btnStop = (Button) this.findViewById(R.id.buttonStop);
        sb = (SeekBar) this.findViewById(R.id.seekBar1);
        sb.setBackgroundDrawable(drawScale());
        setListeners();

    }

    public BitmapDrawable drawScale(){
    	Paint paint;
    	Path path;
    	Canvas canvas;
    	Bitmap bitmap;

    	paint = new Paint();
    	paint.setStrokeWidth(0);
    	paint.setStyle(Paint.Style.STROKE);

    	bitmap = Bitmap.createBitmap(241, 30, Bitmap.Config.ARGB_8888);
    	path = new Path();
    	canvas = new Canvas(bitmap);

    	for (int i=0; i<17; i++){
    		path.reset();
    		if(i==5||i==10||i==15){
    			paint.setColor(Color.WHITE);
    		}else{
    			paint.setColor(Color.GRAY);
    		}

    		path.moveTo(i*16, 5);
    		path.quadTo(i*16, 5, i*16, 15);
    		canvas.drawPath(path,  paint);
    	}
    	BitmapDrawable bd = new BitmapDrawable(bitmap);
    	return bd;
    }


    public void setListeners() {

		sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				CountdownTimerActivity.timeLeft = progress*60;
				if (fromUser) {
					CountdownTimerActivity.showTime(progress*60);
				}
				if(fromUser && (progress > 0)) {
					CountdownTimerActivity.btnStart.setEnabled(true);
				}
				else {
					CountdownTimerActivity.btnStart.setEnabled(false);
				}
				if (progress == 0) {
					CountdownTimerActivity.btnStop.setEnabled(false);
				}
			}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

		});

		CountdownTimerActivity.btnStart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CountdownTimerActivity.mContext, TimerService.class);
				intent.putExtra("counter", CountdownTimerActivity.timeLeft);
				startService(intent);
				CountdownTimerActivity.btnStart.setEnabled(false);
				CountdownTimerActivity.btnStop.setEnabled(true);
				CountdownTimerActivity.sb.setEnabled(false);
			}
		});
		CountdownTimerActivity.btnStop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(CountdownTimerActivity.mContext, TimerService.class);
				CountdownTimerActivity.mContext.stopService(i);
				CountdownTimerActivity.btnStop.setEnabled(false);
				CountdownTimerActivity.btnStart.setEnabled(true);
				CountdownTimerActivity.sb.setEnabled(true);
			}
		});

		((Button) this.findViewById(R.id.buttonSettings)).setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						Intent intent = new Intent(CountdownTimerActivity.this, Preferences.class);
						startActivity(intent);
					}
		});

	}



    static void showTime(int timeSeconds){
    	SimpleDateFormat form = new SimpleDateFormat("mm:ss");
    	tv.setText(form.format(timeSeconds*1000));
    }

    public static void countdown(int counter){
    	showTime(counter);
    	timeLeft = counter;
    	if(counter % 60 ==0){
    		sb.setProgress(counter/60);
    	}else{
    		btnStop.setEnabled(false);
    		btnStart.setEnabled(false);
    		sb.setEnabled(true);
    	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_countdown, menu);
        return true;
    }


}
