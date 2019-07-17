package com.dream.timeview.service;

import android.os.*;
import android.service.wallpaper.*;
import android.util.*;
import android.view.*;
import com.dream.timeview.ui.*;
import com.dream.timeview.view.*;

public class TimeWallpaperService extends WallpaperService {
    private final static long REFLESH_GAP_TIME = 20L;//如果想播放的流畅，需满足1s 16帧   62ms切换间隔时间

	/**
	 * 返回TimeWallpaperEngine实例
	 */
	@Override
    public Engine onCreateEngine() {
        return new TimeWallpaperEngine();
    }

	/**
	 * 动态壁纸引擎
	 */
    private class TimeWallpaperEngine extends TimeWallpaperService.Engine {
        private Runnable viewRunnable;
        private Handler handler;
        private Time tsv;
        private final SurfaceHolder surfaceHolder;
		//屏幕宽
        private int screenWidth;
		//屏幕高
        private int screenHeight;

        public TimeWallpaperEngine() {
			//获取SurfaceHolder
            this.surfaceHolder = getSurfaceHolder();
			//实例化时钟罗盘
            this.tsv = new Time();
			//设置时钟罗盘大小
			this.tsv.setTextSize(MainActivity.size);
			//实例化Handler
            this.handler = new Handler();
			//初始化Runnable
            this.initRunnable();
            this.handler.post(this.viewRunnable);
			//获取屏幕宽高
            DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
            screenWidth = dm.widthPixels;
            screenHeight = dm.heightPixels;
        }

		/**
		 * 初始化Runnable
		 */
        private void initRunnable() {
            this.viewRunnable = new Runnable() {
                @Override
                public void run() {
                    TimeWallpaperEngine.this.drawView();
                }
            };
        }

		/**
		 * 绘制时钟罗盘
		 */
        private void drawView() {
            if (this.tsv == null) {
                return;
            } else {
                this.handler.removeCallbacks(this.viewRunnable);
                this.tsv.onDrawTime(this.surfaceHolder, screenWidth, screenHeight);
                if (!(isVisible())) {
                    return;
                } else {
                    this.handler.postDelayed(this.viewRunnable, REFLESH_GAP_TIME);
                }
            }
        }

		/**
		 * Surface创建时回调
		 */
        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            this.drawView();
        }

		/**
		 * Surface改变时回调
		 */
        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            this.drawView();
        }

		/**
		 * 当壁纸可见改变时回调
		 */
        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            if (this.handler != null) {
				//判断当前壁纸是否可见
                if (visible) {
                    this.handler.removeCallbacks(this.viewRunnable);
                    this.handler.post(this.viewRunnable);
                } else {
                    this.handler.removeCallbacks(this.viewRunnable);
                }
            }
        }

		/**
		 * Surface摧毁时回调
		 */
        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            if (this.handler != null) {
                this.handler.removeCallbacks(this.viewRunnable);
            }
        }

		/**
		 * Service摧毁时回调
		 */
        @Override
        public void onDestroy() {
            super.onDestroy();
            if (this.handler != null) {
                this.handler.removeCallbacks(this.viewRunnable);
            }
        }
    }
}
