package com.example.administrator.books;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2016/6/21.
 */
public class LoadImageAsync extends AsyncTask<String, Nullable, Bitmap> {
    int imageWidth;

    public LoadImageAsync(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap bitmap;
        BitmapFactory.Options ops = new BitmapFactory.Options();
        ops.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(params[0], ops);
        if (ops.outWidth > imageWidth)
            bitmap = BitmapUtils.decodeSampledBitmapFromFilePath(params[0], imageWidth, imageWidth);
        else
            bitmap = BitmapFactory.decodeFile(params[0]);
        return bitmap;
    }


}
