package cyou.joiplay.joipad.util;

import android.content.Context;
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
}
