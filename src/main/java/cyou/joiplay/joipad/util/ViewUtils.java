package cyou.joiplay.joipad.util;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.lang.reflect.Field;

import cyou.joiplay.joipad.R;

public class ViewUtils {
    static public void changeOpacity(ViewGroup viewGroup, int opacity){
        for (int i=0; viewGroup.getChildCount() > i; i++){
            View view = viewGroup.getChildAt(i);
            if ((view instanceof Button) || (view instanceof ImageView)){
                view.getBackground().setAlpha(Math.round(opacity*2.25f));
                view.setAlpha(((opacity + 20) / 100f)*2);
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
    static public boolean onMoveView(View view, MotionEvent motionEvent, float[] vector, GridMovement grid){
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                vector[0] = view.getX() - motionEvent.getRawX();
                vector[1] = view.getY() - motionEvent.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                switch (grid){
                    case X:
                        view.animate()
                                .x(motionEvent.getRawX() + vector[0] - (view.getWidth() / 2f))
                                .setDuration(0)
                                .start();
                        break;
                    case Y:
                        view.animate()
                                .y(motionEvent.getRawY() + vector[1] - (view.getHeight() / 2f))
                                .setDuration(0)
                                .start();
                        break;
                    case XY:
                        view.animate()
                                .x(motionEvent.getRawX() + vector[0] - (view.getWidth() / 2f))
                                .y(motionEvent.getRawY() + vector[1] - (view.getHeight() / 2f))
                                .setDuration(0)
                                .start();
                        break;
                }
                break;
            default:
                return false;
        }

        return false;
    }
}
