package com.gr2.a2016.ease_l.classes;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

public class ImageCanvas {
    protected ImageView imageView;

    public ImageCanvas(ImageView imageView) {
        this.imageView = imageView;
    }

    public void draw(int x1, int y1, int x2, int y2) {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        Paint paint = new Paint();
        paint.setStrokeWidth(2);
        ColorFilter filter = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        paint.setColorFilter(filter);
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);
        canvas.setBitmap(mutableBitmap);
        if (x2 == -1 && y2 == -1) {
            canvas.drawCircle(x1, y1, 2, paint);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(x1, y1, 5, paint);
        } else {
            Rect rect = new Rect(x1, y1, x2, y2);
            rect.setEmpty();
            canvas.drawBitmap(mutableBitmap, null, rect, paint);
        }
        imageView.setImageBitmap(mutableBitmap);
    }
}
