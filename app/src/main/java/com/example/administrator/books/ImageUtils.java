package com.example.administrator.books;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Administrator on 2016/6/7 0007.
 */
public class ImageUtils {
    public static String saveImageFromUrl(String url) {
        String local = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath().concat("/pics/");
        File dir = new File(local);
        boolean success = dir.mkdirs();
        local += System.currentTimeMillis() + url.substring(url.lastIndexOf("."));
        HttpsURLConnection conn = null;
        try {
            url = URLEncoder.encode(url, "utf-8").replace("\\+", "%20");
            url = url.replaceAll("%3A", ":").replaceAll("%2F", "/");
            conn = (HttpsURLConnection) new URL(url).openConnection();
            conn.connect();
            InputStream stream = conn.getInputStream();
            FileOutputStream out = new FileOutputStream(local);
            byte buf[] = new byte[512];
            int numRead;
            while ((numRead = stream.read(buf)) != -1)
                out.write(buf, 0, numRead);
            out.flush();
            stream.close();
        } catch (IOException e) {
            return "";
        } finally {
            if (conn != null)
                conn.disconnect();
        }


        return local;
    }


}
