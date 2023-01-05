package cyou.joiplay.joipad.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cyou.joiplay.joipad.R;
import cyou.joiplay.joipad.animation.ButtonAnimations;
import cyou.joiplay.joipad.drawable.TextDrawable;

public class GamePadButton extends ImageView {
    enum Variant{
        Action,
        Gamepad
    }

    Variant mVariant = Variant.Action;
    Drawable mBackgroundDrawable;
    Drawable mForegroundDrawable;
    String mForegroundText;
    Integer mBackgroundTint;
    Integer mForegroundTint;

    Bitmap mBitmap;

    private int mTextSize = 32;
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public interface OnKeyDownListener{
        void onKeyDown(int key);
    }

    public interface OnKeyUpListener{
        void onKeyUp(int key);
    }

    private OnKeyDownListener mOnKeyDownListener = key -> {};

    private OnKeyUpListener mOnKeyUpListener = key -> {};

    private int mKey = 0;

    public GamePadButton(Context context) {
        super(context);
    }

    public GamePadButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGamepadButton(attrs, null);
    }

    public GamePadButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initGamepadButton(attrs, defStyleAttr);
    }

    public GamePadButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initGamepadButton(attrs, defStyleAttr);
    }

    public void initGamepadButton(AttributeSet attrs, Integer defStyleAttr){
        if (attrs != null){
            TypedArray a;
            if (defStyleAttr != null){
                a = getContext().obtainStyledAttributes(attrs, R.styleable.GamePadButton, defStyleAttr, R.style.GamePadButton);
            } else {
                a = getContext().obtainStyledAttributes(attrs, R.styleable.GamePadButton);
            }

            int variant = a.getInt(R.styleable.GamePadButton_gp_variant, 0);
            String foregroundText = a.getString(R.styleable.GamePadButton_gp_foregroundText);
            Drawable backgroundDrawable = a.getDrawable(R.styleable.GamePadButton_gp_backgroundDrawable);
            Drawable foregroundDrawable = a.getDrawable(R.styleable.GamePadButton_gp_foregroundDrawable);
            int backgroundTint = a.getInteger(R.styleable.GamePadButton_gp_backgroundTint, Color.rgb(32,32,32));
            int foregroundTint = a.getInteger(R.styleable.GamePadButton_gp_foregroundTint, Color.rgb(255,255,255));

            this.mVariant = variant == 2 ? Variant.Action : Variant.Gamepad;
            this.mBackgroundDrawable = backgroundDrawable;
            this.mForegroundText = foregroundText;
            this.mForegroundDrawable = foregroundDrawable;
            this.mBackgroundTint = backgroundTint;
            this.mForegroundTint = foregroundTint;
        }
    }

    @Override
    public void setBackgroundDrawable(Drawable backgroundDrawable){
        this.mBackgroundDrawable = backgroundDrawable;

        initBitmap();
    }

    public void setBackgroundDrawableResource(int backgroundDrawableResource){
        Drawable backgroundDrawable = getContext().getResources().getDrawable(backgroundDrawableResource);

        setBackgroundDrawable(backgroundDrawable);
    }

    public void setForegroundDrawable(Drawable foregroundDrawable){
        this.mForegroundDrawable = foregroundDrawable;
        this.mForegroundText = null;

        initBitmap();
    }

    public void setForegroundDrawableResource(int foregroundDrawableResource){
        Drawable foregroundDrawable = getContext().getResources().getDrawable(foregroundDrawableResource);

        setForegroundDrawable(foregroundDrawable);
    }

    public void setForegroundText(String text){
        this.mForegroundText = text;
        this.mForegroundDrawable = null;

        initBitmap();
    }

    public void setBackgroundTint(Integer tint) {
        this.mBackgroundTint = tint;
    }

    public void setForegroundTint(Integer tint) {
        this.mForegroundTint = tint;
    }

    public void initBitmap(){
        if(this.getWidth() < 1 || this.getHeight() < 1) return;

        //Reset text size
        mTextSize = 32;

        Bitmap tmpBitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas tmpCanvas = new Canvas(tmpBitmap);

        if (mBackgroundDrawable != null) {
            mBackgroundDrawable.setBounds(0, 0, tmpCanvas.getWidth(), tmpCanvas.getHeight());
            mBackgroundDrawable.setTint(mBackgroundTint);
            mBackgroundDrawable.draw(tmpCanvas);
        }

        int w = (int) Math.round(tmpCanvas.getWidth()*0.9);
        int h = (int) Math.round(tmpCanvas.getHeight()*0.9);
        int pw = (tmpCanvas.getWidth() - w)/2;
        int ph = (tmpCanvas.getHeight() - h)/2;

        if (mForegroundDrawable != null){
            mForegroundDrawable.setBounds(pw, ph, w+pw, h+ph);
            mForegroundDrawable.setTint(mForegroundTint);
            mForegroundDrawable.draw(tmpCanvas);
        }

        if (mForegroundText != null){
            initPaint();
            tmpCanvas.drawText(mForegroundText, 0, mForegroundText.length(), tmpCanvas.getWidth() /2, tmpCanvas.getHeight() / 2 - mPaint.ascent() / 2f, mPaint);
        }

        this.mBitmap = tmpBitmap;

        this.setImageBitmap(mBitmap);
    }

    private void initPaint(){
        mPaint.setColor(mForegroundTint);
        mPaint.setTypeface(Typeface.create("sans-serif",Typeface.BOLD));
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(getTextSize());
    }

    private int getTextSize(){
        mPaint.setTextSize(mTextSize);
        int mIntrinsicWidth = (int) Math.round(mPaint.measureText(mForegroundText, 0, mForegroundText.length()) + .5);
        int mIntrinsicHeight = mPaint.getFontMetricsInt(null);

        while(mIntrinsicHeight > this.getHeight() * 0.85 || mIntrinsicWidth > this.getWidth() * 0.85){
            mTextSize -= 2;
            mPaint.setTextSize(mTextSize);
            mIntrinsicWidth = (int) Math.round(mPaint.measureText(mForegroundText, 0, mForegroundText.length()) + .5);
            mIntrinsicHeight = mPaint.getFontMetricsInt(null);
        }

        return mTextSize;
    }

    public void setOnKeyDownListener(OnKeyDownListener onKeyDownListener){
        mOnKeyDownListener = onKeyDownListener;
    }

    public void setOnKeyUpListener(OnKeyUpListener onKeyUpListener){
        mOnKeyUpListener = onKeyUpListener;
    }

    public void setKey(int key){
        mKey = key;
        initBitmap();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        initBitmap();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mVariant == Variant.Action) return super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ButtonAnimations.animateTouch(getContext(), this, true);
                mOnKeyDownListener.onKeyDown(mKey);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                ButtonAnimations.animateTouch(getContext(), this, false);
                mOnKeyUpListener.onKeyUp(mKey);
                break;
        }
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initBitmap();
    }
}
