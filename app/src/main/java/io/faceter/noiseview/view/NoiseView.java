package io.faceter.noiseview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NoiseView extends AppCompatImageView {

    private int width = 100;

    private int height = 100;

    private int noiseMinAlpha = 70;

    private int noiseMaxAlpha = 220;

    private int currentIndex;

    private int count = 10;

    private List<Bitmap> cache = new ArrayList<>();
    private List<BitmapDrawable> cache2 = new ArrayList<>();

    private float scaleFactor;

    Handler handler = new Handler();

    public NoiseView(Context context) {
        super(context);
        drawTiledBackground();
    }

    public NoiseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        drawTiledBackground();
    }

    public NoiseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        drawTiledBackground();
    }

    private void drawTiledBackground() {
        scaleFactor = getContext().getResources().getDisplayMetrics().density;
        generate();
        timedTask.run();
    }

    Runnable timedTask = new Runnable() {
        @Override
        public void run() {
            setImage();
            handler.postDelayed(timedTask, 50);
        }
    };

    private void generate() {
        if (cache.size() == count) return;

        for (int i = 0; i < count; i++) {
            cache2.add(getImage());
        }
    }

    private void setImage() {

        setBackgroundDrawable(cache2.get(currentIndex));

        currentIndex = Math.min(currentIndex + 1, count);

        if (currentIndex == count) {
            currentIndex = 0;
        }
    }

    private BitmapDrawable getImage() {
        Bitmap.Config conf = Bitmap.Config.ARGB_4444;
        Bitmap bmp = Bitmap.createBitmap(width, height, conf);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bmp.setPixel(x, y, Color.argb(random(noiseMinAlpha, noiseMaxAlpha), 0, 0, 0));
            }
        }

        Bitmap scaledBmp = Bitmap.createScaledBitmap(bmp, Math.round(width * scaleFactor), Math.round(height * scaleFactor), true);
        BitmapDrawable tiledDrawable = new BitmapDrawable(getContext().getResources(), scaledBmp);
        tiledDrawable.setTileModeX(Shader.TileMode.REPEAT);
        tiledDrawable.setTileModeY(Shader.TileMode.REPEAT);
        return tiledDrawable;
    }

    public int random(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }
}
