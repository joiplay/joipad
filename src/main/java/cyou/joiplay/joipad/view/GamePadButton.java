package cyou.joiplay.joipad.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageButton;

import cyou.joiplay.joipad.R;
import cyou.joiplay.joipad.animation.ButtonAnimations;

public class GamePadButton extends ImageButton {
    enum Shape{
        Circle,
        Rectangle
    }

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
    }

    public GamePadButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GamePadButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setShape(Shape shape){
        switch (shape){
            case Circle:
                this.setBackgroundResource(R.drawable.action_back_circle);
                break;
            case Rectangle:
                this.setBackgroundResource(R.drawable.action_back_square);
                break;
        }
    }

    public void setOnKeyDownListener(OnKeyDownListener onKeyDownListener){
        mOnKeyDownListener = onKeyDownListener;
    }

    public void setOnKeyUpListener(OnKeyUpListener onKeyUpListener){
        mOnKeyUpListener = onKeyUpListener;
    }

    public void setKey(int key){
        mKey = key;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
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
}
