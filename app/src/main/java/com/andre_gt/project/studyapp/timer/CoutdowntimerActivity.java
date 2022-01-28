package com.andre_gt.project.studyapp.timer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.andre_gt.project.studyapp.R;

public class CoutdowntimerActivity extends AppCompatActivity {
    private EditText timeEt;
    private Button startBtn;
    private boolean started;
    private CountDownTimer countDownTimer;
    private static final Handler HANDLER = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coutdowntimer);
        timeEt = findViewById(R.id.timeEt);
        startBtn = findViewById(R.id.startBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (started) {
                    started = false;
                    startBtn.setText(R.string.start);
                    timeEt.setCursorVisible(true);
                    countDownTimer.cancel(); // we cancel the countdown timer execution when user click on the stop button
                } else {
                    started = true;
                    startBtn.setText(R.string.stop);
                    timeEt.setCursorVisible(false);

                    // we get raw time entered by user at min:sec format
                    String rawTime = timeEt.getText().toString();
                    String[] tmp = rawTime.split(":");

                    long time = 60 * 1000; // default time = 60 sec. Obviously, for the a boiled egg it is rather 9 minutes :D

                    // we try to parse the time entered by user
                    try {
                        time = (Integer.parseInt(tmp[0]) * 60 + Integer.parseInt(tmp[1])) * 1000;
                    } catch (Exception e) {
                        timeEt.setText(R.string.default_time); // default time is set if error !
                    }

                    countDownTimer = new CountDownTimer(time, 100) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            // we update the counter during the execution
                            long remainingSeconds = millisUntilFinished / 1000;
                            long min = remainingSeconds / 60;
                            long seconds = remainingSeconds % 60;

                            timeEt.setText(min + ":" + (seconds < 10 ? "0" + seconds : seconds));
                        }

                        @Override
                        public void onFinish() {
                            // execution is finished, we set default values
                            timeEt.setText(R.string.start_time);
                            startBtn.setText(R.string.start);

                            // we display a user message
                            new AlertDialog.Builder(CoutdowntimerActivity.this).
                                    setTitle(R.string.app_name).
                                    setMessage(R.string.lets_eat).
                                    show();

                            // we reset to default time in the future (1.5 seconds seem good)
                            HANDLER.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    timeEt.setText(R.string.default_time);
                                }
                            }, 1500);
                        }
                    };

                    countDownTimer.start();

                }
            }
        });
    }
}