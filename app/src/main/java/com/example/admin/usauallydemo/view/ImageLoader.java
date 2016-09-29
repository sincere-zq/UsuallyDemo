package com.example.admin.usauallydemo.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by zengqiang on 2016/9/9.
 * Description:
 */
public class ImageLoader {
    //图片保存路径
    public static final String CACHES_PATH = Environment.getExternalStorageDirectory().toString() +
            File.separator +"sincere" + File.separator + "cache" + File.separator + "img" + File.separator;

    static File fileDir = new File(CACHES_PATH);

    private static ImageLoader imageLoader;

    private LruCache<String, Bitmap> mLruCache;//缓存

    private boolean isBusy=false;//是否在滑动，适配器应当事先OnScrollChangeListener

    private ImageLoader() {
        initLruCache();
    }

    public static ImageLoader getInstance() {
        if (imageLoader == null)
            imageLoader = new ImageLoader();
        return imageLoader;
    }

    public void setBusy(boolean busy) {
        isBusy = busy;
    }

    /**
     * 初始化LruCache
     */
    private void initLruCache() {
        //获得系统分配给应用程序的最大内存空间，一般每个应用有64MB
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int lruCache = maxMemory / 4;
        mLruCache = new LruCache<String, Bitmap>(lruCache) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                int size = 0;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
                    //3.1版本之后使用此方法获得bitmap内存大小
                    size = value.getByteCount();
                    return size;
                }
                //3.1版本之后使用此方法获得bitmap内存大小
                size = value.getRowBytes() * value.getHeight();
                return size;
            }
        };
    }

    /**
     * 添加bitmap到缓存
     */
    public void addBitmapToCache(String url, Bitmap bitmap) {
        if (getBitmapFromCache(url) == null) {
            mLruCache.put(url, bitmap);
        }
    }

    /**
     * 从缓存取出bitmap
     */
    public Bitmap getBitmapFromCache(String url) {
        return mLruCache.get(url);
    }

    public void showBitmapFromAsyncTask(ImageView imageView, String url) {
        if(isBusy)
            return;
        //设置防错乱标记
        imageView.setTag(url);
        //从缓存中取出对应的图片
        Bitmap bitmap = getBitmapFromCache(url);
        //如果缓存中没有，就去网络下载
        if (bitmap == null) {
            new ImageAsyncTask(imageView, url).execute(url);
        } else //缓存中有，则直接加载缓存中的图片
            imageView.setImageBitmap(mLruCache.get(url));
    }

    /**
     * AsyncTask加载图片
     */
    private class ImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

        private ImageView mImageView;
        private String mUrl;

        public ImageAsyncTask(ImageView mImageView, String mUrl) {
            this.mImageView = mImageView;
            this.mUrl = mUrl;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String url = strings[0];
            //从网络获取图片
            Bitmap bitmap = getBitmapFromURL(url);
            if (bitmap != null) {
                //将不在缓存中的图片加入到缓存
                addBitmapToCache(url, bitmap);
                //存到本地
//                addBitmapToDisk(url,bitmap);
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (mImageView.getTag().equals(mUrl))
                mImageView.setImageBitmap(bitmap);
        }
    }

    /**
     * 从网络下载图片
     */
    private Bitmap getBitmapFromURL(String url) {
        Bitmap bitmap = null;
        HttpURLConnection connection = null;
        InputStream in = null;
        try {
            URL mURL = new URL(url);
            //建立连接
            connection = (HttpURLConnection) mURL.openConnection();
            //建立字节输入流
            InputStream input = connection.getInputStream();
            //建立缓冲字节输入流
            in = new BufferedInputStream(input);
            //通过缓冲字节输入流获取bitmap
            bitmap = BitmapFactory.decodeStream(in);
            //断开HttpURLConnection连接
            connection.disconnect();
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 将bitmap存到本地
     */
    private void addBitmapToDisk(String imgUrl, Bitmap bitmap) {
//        File file  =new File(fileDir, getFileName(imgUrl));
//        if(!file.getParentFile().exists()){
//            file.getParentFile().mkdirs();
//        }
//        FileOutputStream fos;
//        try {
//            fos = new FileOutputStream(file);
//            //保存bitmap  把bitmap对象存进文件中的方法
//            //第二个参数  图片的质量  100为不压缩
//            bitmap.compress(Bitmap.CompressFormat.JPEG, QUALITY, fos);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
    }

}
