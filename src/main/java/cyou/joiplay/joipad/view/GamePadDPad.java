package cyou.joiplay.joipad.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.ImageView;

public class GamePadDPad extends ImageView {
    enum Direction{
        UP,
        UP_RIGHT,
        RIGHT,
        DOWN_RIGHT,
        DOWN,
        DOWN_LEFT,
        LEFT,
        UP_LEFT,
        UNKNOWN
    }

    public interface OnKeyDownListener{
        void onKeyDown(int key);
    }

    public interface OnKeyUpListener{
        void onKeyUp(int key);
    }

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
                nAngle = getAngle(initX,posX,initY,posY);
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
        }

        return true;
    }

    private Direction getDir(double angle) {
        if (isDiagonal){
            return get8dir(angle);
        } else {
            return get4dir(angle);
        }
    }

    private Direction get4dir(double angle)  {
        if ((angle > 45) && (angle < 136)){
            return Direction.UP;
        } else if ((angle > 135) && (angle < 226)){
            return Direction.RIGHT;
        } else if ((angle > 225) && (angle < 316)){
            return Direction.DOWN;
        } else if (((angle >= 0) && (angle < 46)) || ((angle > 315) && (angle <361))){
            return Direction.LEFT;
        } else {
            return Direction.UNKNOWN;
        }
    }

    private Direction get8dir(double angle)  {
        if ((angle >= 67.5) && (angle < 113.5)){
            return Direction.UP;
        } else if ((angle >= 113.5) && (angle < 158.5)){
            return Direction.UP_RIGHT;
        } else if ((angle >= 158.5) && (angle < 203.5)){
            return Direction.RIGHT;
        } else if ((angle >= 203.5) && (angle < 248.5)){
            return Direction.DOWN_RIGHT;
        } else if ((angle >= 248.5) && (angle < 293.5)){
            return Direction.DOWN;
        } else if ((angle >= 293.5) && (angle < 338.5)){
            return Direction.DOWN_LEFT;
        } else if (((angle >= 0) && (angle < 23.5)) || ((angle >= 338.5) && (angle <361))){
            return Direction.LEFT;
        } else if ((angle >= 23.5) && (angle < 67.5)){
            return Direction.UP_LEFT;
        } else {
            return Direction.UNKNOWN;
        }
    }

    private Direction getAngle(float initX, float posX, float initY, float posY){
        double angle = 0;
        try{
            angle = Math.toDegrees(Math.atan2((initY - posY), (initX - posX)));
        } catch (Exception e){}

        if (angle < 0)
            angle += 360;

        return getDir(angle);
    }
}
