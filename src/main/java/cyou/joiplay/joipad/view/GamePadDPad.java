package cyou.joiplay.joipad.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;

import cyou.joiplay.joipad.util.DirectionUtils;
import cyou.joiplay.joipad.util.DirectionUtils.Direction;

public class GamePadDPad extends GamePadButton {

    private OnKeyDownListener mOnKeyDownListener = key -> {};

    private OnKeyUpListener mOnKeyUpListener = key -> {};

    private Direction angle = Direction.UNKNOWN;
    private Direction nAngle = Direction.UNKNOWN;
    private float posX = 0f;
    private float posY = 0f;

    public Boolean isDiagonal = false;


    public GamePadDPad(Context context) {
        super(context);
    }

    public GamePadDPad(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GamePadDPad(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GamePadDPad(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setOnKeyDownListener(OnKeyDownListener onKeyDownListener){
        mOnKeyDownListener = onKeyDownListener;
    }

    public void setOnKeyUpListener(OnKeyUpListener onKeyUpListener){
        mOnKeyUpListener = onKeyUpListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float initX = this.getWidth() / 2f;
        float initY = this.getHeight() / 2f;
        posX = this.getWidth() / 2f;
        posY = this.getHeight() / 2f;

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_POINTER_DOWN:
                posX = event.getX();
                posY = event.getY();
                nAngle = DirectionUtils.getAngle(initX,posX,initY,posY,isDiagonal);
                if (angle == nAngle){
                    return false;
                } else {
                    switch (angle){
                        case UP:
                            mOnKeyUpListener.onKeyUp(KeyEvent.KEYCODE_DPAD_UP);
                            break;
                        case UP_RIGHT:
                            mOnKeyUpListener.onKeyUp(KeyEvent.KEYCODE_DPAD_UP);
                            mOnKeyUpListener.onKeyUp(KeyEvent.KEYCODE_DPAD_RIGHT);
                            break;
                        case UP_LEFT:
                            mOnKeyUpListener.onKeyUp(KeyEvent.KEYCODE_DPAD_UP);
                            mOnKeyUpListener.onKeyUp(KeyEvent.KEYCODE_DPAD_LEFT);
                            break;
                        case DOWN:
                            mOnKeyUpListener.onKeyUp(KeyEvent.KEYCODE_DPAD_DOWN);
                            break;
                        case DOWN_RIGHT:
                            mOnKeyUpListener.onKeyUp(KeyEvent.KEYCODE_DPAD_DOWN);
                            mOnKeyUpListener.onKeyUp(KeyEvent.KEYCODE_DPAD_RIGHT);
                            break;
                        case DOWN_LEFT:
                            mOnKeyUpListener.onKeyUp(KeyEvent.KEYCODE_DPAD_DOWN);
                            mOnKeyUpListener.onKeyUp(KeyEvent.KEYCODE_DPAD_LEFT);
                            break;
                        case RIGHT:
                            mOnKeyUpListener.onKeyUp(KeyEvent.KEYCODE_DPAD_RIGHT);
                            break;
                        case LEFT:
                            mOnKeyUpListener.onKeyUp(KeyEvent.KEYCODE_DPAD_LEFT);
                            break;
                    }

                    angle = nAngle;

                    switch (angle){
                        case UP:
                            mOnKeyDownListener.onKeyDown(KeyEvent.KEYCODE_DPAD_UP);
                            break;
                        case UP_RIGHT:
                            mOnKeyDownListener.onKeyDown(KeyEvent.KEYCODE_DPAD_UP);
                            mOnKeyDownListener.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT);
                            break;
                        case UP_LEFT:
                            mOnKeyDownListener.onKeyDown(KeyEvent.KEYCODE_DPAD_UP);
                            mOnKeyDownListener.onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT);
                            break;
                        case DOWN:
                            mOnKeyDownListener.onKeyDown(KeyEvent.KEYCODE_DPAD_DOWN);
                            break;
                        case DOWN_RIGHT:
                            mOnKeyDownListener.onKeyDown(KeyEvent.KEYCODE_DPAD_DOWN);
                            mOnKeyDownListener.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT);
                            break;
                        case DOWN_LEFT:
                            mOnKeyDownListener.onKeyDown(KeyEvent.KEYCODE_DPAD_DOWN);
                            mOnKeyDownListener.onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT);
                            break;
                        case RIGHT:
                            mOnKeyDownListener.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT);
                            break;
                        case LEFT:
                            mOnKeyDownListener.onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT);
                            break;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_OUTSIDE:
                posX = this.getWidth() / 2f;
                posY = this.getHeight() / 2f;

                switch (angle){
                    case UP:
                        mOnKeyUpListener.onKeyUp(KeyEvent.KEYCODE_DPAD_UP);
                        break;
                    case UP_RIGHT:
                        mOnKeyUpListener.onKeyUp(KeyEvent.KEYCODE_DPAD_UP);
                        mOnKeyUpListener.onKeyUp(KeyEvent.KEYCODE_DPAD_RIGHT);
                        break;
                    case UP_LEFT:
                        mOnKeyUpListener.onKeyUp(KeyEvent.KEYCODE_DPAD_UP);
                        mOnKeyUpListener.onKeyUp(KeyEvent.KEYCODE_DPAD_LEFT);
                        break;
                    case DOWN:
                        mOnKeyUpListener.onKeyUp(KeyEvent.KEYCODE_DPAD_DOWN);
                        break;
                    case DOWN_RIGHT:
                        mOnKeyUpListener.onKeyUp(KeyEvent.KEYCODE_DPAD_DOWN);
                        mOnKeyUpListener.onKeyUp(KeyEvent.KEYCODE_DPAD_RIGHT);
                        break;
                    case DOWN_LEFT:
                        mOnKeyUpListener.onKeyUp(KeyEvent.KEYCODE_DPAD_DOWN);
                        mOnKeyUpListener.onKeyUp(KeyEvent.KEYCODE_DPAD_LEFT);
                        break;
                    case RIGHT:
                        mOnKeyUpListener.onKeyUp(KeyEvent.KEYCODE_DPAD_RIGHT);
                        break;
                    case LEFT:
                        mOnKeyUpListener.onKeyUp(KeyEvent.KEYCODE_DPAD_LEFT);
                        break;
                }
                angle = Direction.UNKNOWN;
                break;
        }

        return true;
    }
}
