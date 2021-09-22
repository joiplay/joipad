package cyou.joiplay.joipad.drawable;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

public class TextDrawable extends Drawable {
    private final int mColor = Color.WHITE;
    private final int mTextSize = 15;
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mIntrinsicWidth;
    private int mIntrinsicHeight;
    private Resources mResources;
    private CharSequence mText;

    public TextDrawable(Resources resources, CharSequence text){
        this.mResources = resources;
        this.mText = text;
        init();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawText(this.mText, 0, mText.length(), getBounds().centerX(), getBounds().centerY() - mPaint.ascent() / 2f, mPaint);
    }

    @Override
    public void setAlpha(int i) {
        mPaint.setAlpha(i);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return mPaint.getAlpha();
    }

    @Override
    public int getIntrinsicWidth() {
        return mIntrinsicWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        return mIntrinsicHeight;
    }

    private void init(){
        mPaint.setColor(mColor);
        mPaint.setTextAlign(Paint.Align.CENTER);
        float textSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                mTextSize, mResources.getDisplayMetrics()
        );
        mPaint.setTextSize(textSize);
        mIntrinsicWidth = (int) Math.round(mPaint.measureText(mText, 0, mText.length()) + .5);
        mIntrinsicHeight = mPaint.getFontMetricsInt(null);
    }
}
