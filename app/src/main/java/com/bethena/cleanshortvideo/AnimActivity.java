package com.bethena.cleanshortvideo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.shyz.clean.view.shortvideo.CleanShortVideoView;

public class AnimActivity extends AppCompatActivity {

    CleanShortVideoView mCleanShortVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim);

        mCleanShortVideoView = findViewById(R.id.clean_short_view);

        findViewById(R.id.btn_start_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCleanShortVideoView.startScanAnim();
            }
        });

        findViewById(R.id.btn_clean).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCleanShortVideoView.startCleanAnim();
            }
        });

        findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCleanShortVideoView.stopAnim();
            }
        });

        SeekBar seekBar = findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCleanShortVideoView.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        SeekBar seekBar2 = findViewById(R.id.seekbar2);
        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int degress = progress - 45;
                mCleanShortVideoView.setDegress(degress);
                ((TextView) findViewById(R.id.tv_degress)).setText("角度：" + degress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mCleanShortVideoView.destroy();

    }
}
