package com.gr2.a2016.ease_l.classes;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.widget.ImageView;

import java.util.ArrayList;

public class ImageCanvas {

    protected Bitmap bitmap;
    protected ImageView imageView;

    public ImageCanvas(Bitmap bitmap, ImageView imageView) {
        this.bitmap = bitmap;
        this.imageView = imageView;
    }

    public ImageCanvas(ImageView imageView) {
        this.imageView = imageView;
    }

    public void draw(ArrayList<Integer> xx1, ArrayList<Integer> yy1, ArrayList<Integer> xx2, ArrayList<Integer> yy2) {
        Paint paint = new Paint();
        paint.setStrokeWidth(6);
        ColorFilter filter = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        paint.setColorFilter(filter);
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);
        canvas.setBitmap(mutableBitmap);
        for (int i = 0; i < xx1.size(); i++) {
            int x1 = xx1.get(i);
            int y1 = yy1.get(i);
            int x2 = xx2.get(i);
            int y2 = yy2.get(i);

            if (((x2 == -1 || y2 == -1)||(x2 == 0 || y2 == 0) )&& x1 != -1 && y1 != -1 && x1 != 0 && y1 != 0) {
                canvas.drawCircle(x1, y1, 6, paint);
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawCircle(x1, y1, 15, paint);
            } else {
                Rect rect = new Rect(x1, y1, x2, y2);
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawBitmap(mutableBitmap, null, rect, paint);
            }

        }
        imageView.setImageBitmap(mutableBitmap);
    }

    public void drawNoBackground(int x1, int y1, int x2, int y2) {
        int width = this.imageView.getWidth();
        int height = this.imageView.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        paint.setStrokeWidth(6);
        ColorFilter filter = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        paint.setColorFilter(filter);
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);
        canvas.setBitmap(mutableBitmap);
        if (x2 == -1 && y2 == -1 && x1 != -1 && y1 != -1) {
            canvas.drawCircle(x1, y1, 6, paint);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(x1, y1, 15, paint);
        } else {
            Rect rect = new Rect(x1, y1, x2, y2);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawBitmap(mutableBitmap, null, rect, paint);
        }
        imageView.setImageBitmap(mutableBitmap);
    }


}
