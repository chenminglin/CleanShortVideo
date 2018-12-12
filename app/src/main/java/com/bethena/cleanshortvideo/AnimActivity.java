package com.bethena.cleanshortvideo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bethena.cleanshortvideo.utils.DisplayUtils;
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

        findViewById(R.id.btn_focus_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCleanShortVideoView.stopAnimForce();
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

        SeekBar seekBar3 = findViewById(R.id.seekbar3);
        seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                final float scale = getApplication().getResources().getDisplayMetrics().density;
                int height = (int) ((progress + 300) * scale + 0.5f);
                Log.d("onProgressChanged", "height = " + height);

                mCleanShortVideoView.getLayoutParams().height = height;
                findViewById(R.id.framelayout).requestLayout();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        SeekBar seekBar4 = findViewById(R.id.seekbar4);
        seekBar4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCleanShortVideoView.setbottomToCenterDistance(DisplayUtils.dp2px(progress, getApplicationContext()));
                TextView textView = findViewById(R.id.tv_center_distance);
                textView.setText("center_distance：" + DisplayUtils.dp2px(progress, getApplicationContext()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        SeekBar seekBar5 = findViewById(R.id.seekbar5);
        seekBar5.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = progress - 50;
                mCleanShortVideoView.setMatrixY(DisplayUtils.dp2px(progress, getApplicationContext()));
                TextView textView = findViewById(R.id.tv_matrix_y);
                textView.setText("matrix_y：" + DisplayUtils.dp2px(progress, getApplicationContext()));
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
