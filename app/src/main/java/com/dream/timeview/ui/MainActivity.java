package com.dream.timeview.ui;

import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.SeekBar.*;
import androidx.appcompat.app.*;
import com.dream.timeview.*;
import com.dream.timeview.service.*;
import com.dream.timeview.utils.*;
import com.dream.timeview.view.*;

public class MainActivity extends AppCompatActivity {
	//请求码
    private final static int REQUEST_CODE_SET_WALLPAPER = 0;
	//绘制的时间控件
	private TimeView time;
	//缩放大小拖动条
	private SeekBar seekBar;
	//存储时间控件的大小值
	public static int size;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		//找到控件
       	time = findViewById(R.id.time);
		seekBar = findViewById(R.id.activitymainSeekBar1);
		//监听拖动条改变事件
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

				/**
				 *拖动条进度改变时回调
				 */
				@Override
				public void onProgressChanged(SeekBar p1, int p2, boolean p3) {
					// TODO: Implement this method
					//动态设置罗盘时间大小
					time.setTextSize(p2);
					//将拖动条的数值赋值给size
					size = p2;
				}

				@Override
				public void onStartTrackingTouch(SeekBar p1) {
					// TODO: Implement this method
				}

				@Override
				public void onStopTrackingTouch(SeekBar p1) {
					// TODO: Implement this method
				}
			});
    }

	/**
	 *点击事件监听
	 */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
				//判断时钟罗盘是否已经设置
				if (WallpaperUtil.wallpaperIsUsed(MainActivity.this, TimeWallpaperService.class)) {
					Toast.makeText(MainActivity.this, "壁纸已经设置", Toast.LENGTH_SHORT).show();
				} else {
					//如果没有设置则跳转到系统设置壁纸界面
					WallpaperUtil.setLiveWallpaper(getApplicationContext(), this, REQUEST_CODE_SET_WALLPAPER, TimeWallpaperService.class);
                }
				break;
        }
    }

	/**
	 *返回设置壁纸结果
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO: Implement this method
		super.onActivityResult(requestCode, resultCode, data);
		//判断壁纸是否设置成功
		if (requestCode == REQUEST_CODE_SET_WALLPAPER && resultCode == RESULT_OK) {
			Toast.makeText(MainActivity.this, "设置壁纸成功", Toast.LENGTH_SHORT).show();
			return;
		}
		Toast.makeText(MainActivity.this, "取消设置壁纸", Toast.LENGTH_SHORT).show();
	}
}
