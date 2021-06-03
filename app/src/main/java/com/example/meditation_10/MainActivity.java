package com.example.meditation_10;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    SeekBar seekBar;
    MediaPlayer mediaPlayer;
    Timer timer;
    int idMusic = R.raw.hang;
    float volume = 0.999f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textSeekValue);
        seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String strProgress = progress + " минут";
                textView.setText(strProgress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()){
            case R.id.radioButton_1:
                if (checked) {
                    idMusic = R.raw.hang;
                }
                break;
            case R.id.radioButton_2:
                if (checked) {
                    idMusic = R.raw.supernatural;
                }
                break;
            case R.id.radioButton_3:
                if (checked) {
                    idMusic = R.raw.rain;
                }
                break;
        }
    }
    private void stopPlay(){
        mediaPlayer.stop();
        try {
            mediaPlayer.prepare();
            mediaPlayer.seekTo(0);
        }catch (Throwable t){
            Toast.makeText(this, t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void startFadeIn() {
        final int FADE_DURATION = 10000;
        final int FADE_INTERVAL = 250;
        int numberOfSteps = FADE_DURATION/FADE_INTERVAL;
        final float deltaVolume = volume/(float) numberOfSteps;

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                fadeInStep(deltaVolume);

                if(volume <= 0){
                    timer.cancel();
                    timer.purge();
                    stopPlay();
                }
            }
        };
        int delay = seekBar.getProgress() * 60 * 1000;
        timer.schedule(timerTask, delay, FADE_INTERVAL);
    }
    private void fadeInStep(float deltaVolume) {
        mediaPlayer.setVolume(volume, volume);
        volume -= deltaVolume;
    }
    public void buttonPlay(View view) {
        volume = 0.999f;
        if (mediaPlayer != null){
            if (mediaPlayer.isPlaying()){
                return;
            }
        }
        timer = new Timer(true);
        mediaPlayer = MediaPlayer.create(this, idMusic);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopPlay();
            }
        });
        startFadeIn();
    }
    public void buttonStop(View view) {
        if (timer != null){
            timer.cancel();
            timer.purge();
            stopPlay();
        }
    }

}