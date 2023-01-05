package cyou.joiplay.joipad.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.lang.reflect.Field;

import cyou.joiplay.joipad.R;
import cyou.joiplay.joipad.view.GamePadButton;

public class ViewUtils {
    static public void changeOpacity(ViewGroup viewGroup, int opacity){
        for (int i=0; viewGroup.getChildCount() > i; i++){
            View view = viewGroup.getChildAt(i);
            if (view instanceof Button){
                view.getBackground().setAlpha(Math.round(opacity*2.25f));
                view.setAlpha(((opacity + 20) / 100f)*2);
            } else if(view instanceof GamePadButton){
                ((GamePadButton) view).setImageAlpha(Math.round(Math.min(opacity+20, 100)*2.55f));
            } else if (view instanceof ViewGroup){
                changeOpacity((ViewGroup) view, opacity);
            }
        }
    }

    static public void resize(View view, float scale){
        ViewGroup.LayoutParams lParams = view.getLayoutParams();
        if (lParams.width > 0)
            lParams.width = Math.round(lParams.width * (scale/100f));

        if (lParams.height > 0)
            lParams.height = Math.round(lParams.height * (scale/100f));

        view.setLayoutParams(lParams);

        int tPadding = Math.round(view.getPaddingTop() * (scale/100f));
        int bPadding = Math.round(view.getPaddingBottom() * (scale/100f));
        int lPadding = Math.round(view.getPaddingLeft() * (scale/100f));
        int rPadding = Math.round(view.getPaddingRight() * (scale/100f));
        view.setPadding(lPadding, tPadding, rPadding, bPadding);

        if (view instanceof ViewGroup){
            for (int i=0; ((ViewGroup) view).getChildCount() > i; i++){
                View v = ((ViewGroup) view).getChildAt(i);
                if (v != null)
                    resize(v,scale);
            }
        }
    }

    static public int sdpToPx(Context context, int sdp){
        try{
            Field field = R.dimen.class.getDeclaredField(String.format("_%ssdp", sdp));
            int id = field.getInt(field);
            return Math.round(context.getResources().getDimension(id));
        } catch (Exception e){
            return 0;
        }
    }

    public enum GridMovement{
        XY,
        X,
        Y
    }


    public static class MovementData{
        public long downTime = 0L;
        public float x0 = 0;
        public float y0 = 0;
        public float x1 = 0;
        public float y1 = 0;
    }

    static public boolean onMoveView(View view, MotionEvent motionEvent, MovementData movementData, GridMovement grid, int slope){
        long longTime = 500L;
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                movementData.downTime = System.currentTimeMillis();
                movementData.x0 = view.getX();
                movementData.y0 = view.getY();

                movementData.x1 = view.getX() - motionEvent.getRawX();
                movementData.y1 = view.getY() - motionEvent.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                switch (grid){
                    case X:
                        if (Math.abs(movementData.x1) > slope)
                            view.animate()
                                    .x(motionEvent.getRawX() + movementData.x1 - (view.getWidth() / 2f))
                                    .setDuration(0)
                                    .start();
                        break;
                    case Y:
                        if (Math.abs(movementData.y1) > slope)
                            view.animate()
                                    .y(motionEvent.getRawY() + movementData.y1 - (view.getHeight() / 2f))
                                    .setDuration(0)
                                    .start();
                        break;
                    case XY:
                        if (Math.abs(movementData.x1) > slope || Math.abs(movementData.y1) > slope)
                            view.animate()
                                    .x(motionEvent.getRawX() + movementData.x1 - (view.getWidth() / 2f))
                                    .y(motionEvent.getRawY() + movementData.y1 - (view.getHeight() / 2f))
                                    .setDuration(0)
                                    .start();
                        break;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (System.currentTimeMillis() - movementData.downTime < longTime){
                    view.animate()
                            .x(movementData.x0)
                            .y(movementData.y0)
                            .setDuration(0)
                            .start();
                }
                break;
            default:
                return false;
        }

        return false;
    }
}
