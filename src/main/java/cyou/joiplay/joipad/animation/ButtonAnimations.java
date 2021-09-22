package cyou.joiplay.joipad.animation;

import android.content.Context;
import android.view.View;
import android.view.animation.AnimationUtils;

import cyou.joiplay.joipad.R;

public class ButtonAnimations {
    static public void animateTouch(Context context, View view, Boolean isTouched){
        if (isTouched) {
            view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.button_touch_down));
        } else {
            view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.button_touch_up));
        }
    }

    static public void animateTouchOnce(Context context, View view){
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.button_touch));
    }
}
