package com.yi4all.booklib;

import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Application;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Create global configuration and initialize ImageLoader with this configuration
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
        .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
        .memoryCacheSize(2 * 1024 * 1024)
        .discCacheSize(50 * 1024 * 1024)
        .discCacheFileCount(100)
            .build();
        ImageLoader.getInstance().init(config);
    }
}