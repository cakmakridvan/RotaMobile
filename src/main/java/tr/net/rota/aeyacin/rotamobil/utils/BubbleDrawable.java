package tr.net.rota.aeyacin.rotamobil.utils;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created by ayacin on 22.03.2017.
 */

public class BubbleDrawable extends Drawable {

    private final Drawable mShadow;
    private final Drawable mMask;
    private int mColor = Color.WHITE;

    public BubbleDrawable(Resources res) {
        mMask = res.getDrawable( com.google.maps.android.R.drawable.amu_bubble_mask );
        mShadow = res.getDrawable( com.google.maps.android.R.drawable.amu_bubble_shadow );
    }

    public void setColor(int color) {
        mColor = color;
    }

    @Override
    public void draw(Canvas canvas) {
        mMask.draw( canvas );
        canvas.drawColor( mColor, PorterDuff.Mode.SRC_IN );
        mShadow.draw( canvas );
    }

    @Override
    public void setAlpha(int alpha) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        mMask.setBounds( left, top, right, bottom );
        mShadow.setBounds( left, top, right, bottom );
    }

    @Override
    public void setBounds(Rect bounds) {
        mMask.setBounds( bounds );
        mShadow.setBounds( bounds );
    }

    @Override
    public boolean getPadding(Rect padding) {
        return mMask.getPadding( padding );
    }
}

