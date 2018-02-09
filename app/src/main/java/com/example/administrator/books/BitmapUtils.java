package com.example.administrator.books;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * Created by Administrator on 2016/6/6 0006.
 */
public class BitmapUtils {
    private static final String TAG = "BitmapUtils";

    /**
     * 计算采样比
     */
    private static int calculateInSampleSize(BitmapFactory.Options opts, int reqHeight, int reqWidth) {
        if (opts == null)
            return -1;
        int width = opts.outWidth;
        int height = opts.outHeight;

        int sampleSize = 1;

        if (width > reqWidth || height > reqHeight) {
            int heightRatio = (int) (height / (float) reqHeight);
            int widthRatio = (int) (width / (float) reqWidth);
            sampleSize = (heightRatio > widthRatio) ? widthRatio : heightRatio;
        }
        return sampleSize;
    }

    /**
     * 根据需要的宽高压缩一张图片
     *
     * @param filePath  文件路径
     * @param reqWidth  需求宽度
     * @param reqHeight 需求高度
     * @return
     */
    public static Bitmap decodeSampledBitmapFromFilePath(String filePath, int reqWidth, int reqHeight) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, opts);
        int sampleSize = calculateInSampleSize(opts, reqHeight, reqWidth);
        Log.i(TAG, "before[width:" + opts.outWidth + ",height:" + opts.outHeight + "]");
        opts.inJustDecodeBounds = false;
        opts.inSampleSize = sampleSize;
        Log.i(TAG, "insamplesize=" + sampleSize);
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, opts);
        Log.i(TAG, "after[width:" + bitmap.getWidth() + ",height:" + bitmap.getHeight() + "]");
        return bitmap;
    }

    /**
     * 根据需要的宽高压缩一张图片
     *
     * @param data      包含图片信息的byte数组
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampledBitmapFromByteArray(byte[] data, int reqWidth, int reqHeight) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, opts);
        Log.i(TAG, "before[width:" + opts.outWidth + ",height:" + opts.outHeight + "]");
        opts.inSampleSize = calculateInSampleSize(opts, reqHeight, reqWidth);
        Log.i(TAG, "insamplesize=" + opts.inSampleSize);
        opts.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
        Log.i(TAG, "after[width:" + bitmap.getWidth() + ",height:" + bitmap.getHeight() + "]");
        return bitmap;
    }
}