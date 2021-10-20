package cyou.joiplay.joipad;

import static cyou.joiplay.joipad.util.ViewUtils.sdpToPx;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cyou.joiplay.commons.model.Game;
import cyou.joiplay.commons.model.GamePad;
import cyou.joiplay.commons.parser.GamePadParser;
import cyou.joiplay.joipad.animation.ButtonAnimations;
import cyou.joiplay.joipad.dialog.SettingsDialog;
import cyou.joiplay.joipad.drawable.TextDrawable;
import cyou.joiplay.joipad.util.KeyboardUtils;
import cyou.joiplay.joipad.util.ViewUtils;
import cyou.joiplay.joipad.view.GamePadButton;
import cyou.joiplay.joipad.view.GamePadDPad;

public class JoiPad {
    private static final String TAG = "JoiPad";

    public interface OnKeyDownListener{
        void onKeyDown(int key);
    }

    public interface OnKeyUpListener{
        void onKeyUp(int key);
    }

    public interface OnCloseListener{
        void onClose();
    }

    private OnKeyDownListener mOnKeyDownListener = key -> {};
    private OnKeyUpListener mOnKeyUpListener = key -> {};
    private OnCloseListener mOnCloseListener = () -> {};

    private GamePad mGamePad = null;
    private Game mGame = null;
    private Activity mActivity = null;
    private Map<Integer, Integer> mKeyMapping = Collections.emptyMap();

    private int screenWidth;
    private int screenHeight;

    private boolean paused = false;
    private boolean cheats = false;
    private boolean fastforward = false;

    private int lastScale = 100;

    private GamePadButton xBtn;
    private GamePadButton yBtn;
    private GamePadButton zBtn;
    private GamePadButton aBtn;
    private GamePadButton bBtn;
    private GamePadButton cBtn;
    private GamePadButton lBtn;
    private GamePadButton rBtn;

    private GamePadDPad dPad;


    public void init(Activity activity, GamePad gamePad){
        mActivity = activity;
        mGamePad = gamePad;
        mKeyMapping = getKeymapping();
    }

    private void loadConfig(){
        String configPath;
        if (mGame.folder.startsWith(Environment.getExternalStorageDirectory().getAbsolutePath())){
            configPath = mGame.folder+"/gamepad.json";
        } else {
            configPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/JoiPlay/games/"+mGame.id+"/gamepad.json";
        }
        File configFile = new File(configPath);

        if (configFile.exists()){
            try {
                GamePadParser.loadFromFile(mGamePad, configFile);
            } catch (Exception e){
                Log.d(TAG, Log.getStackTraceString(e));
            }
        }
    }

    public void setGame(Game game){
        this.mGame = game;
        loadConfig();
    }

    public void cheats(boolean enabled){
        this.cheats = enabled;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void attachTo(Context context, ViewGroup viewGroup){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.gamepad_layout,viewGroup);

        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        screenHeight = context.getResources().getDisplayMetrics().heightPixels;

        RelativeLayout gamepadLayout = layout.findViewById(R.id.gamepad_layout);
        RelativeLayout miscLayChild = layout.findViewById(R.id.miscLayChild);
        RelativeLayout padLayChild = layout.findViewById(R.id.padLayChild);
        RelativeLayout buttonLayout = layout.findViewById(R.id.buttonLay);
        LinearLayout keyboardLayout = layout.findViewById(R.id.keyboardLay);

        ImageButton topShowButton = layout.findViewById(R.id.topShowButton);
        ImageButton bottomShowButton = layout.findViewById(R.id.bottomShowButton);

        ImageButton closeButton = layout.findViewById(R.id.closeBtn);
        ImageButton rotateButton = layout.findViewById(R.id.rotateBtn);
        ImageButton keyboardButton = layout.findViewById(R.id.keyboardBtn);
        ImageButton fastforwardButton = layout.findViewById(R.id.fastforwardBtn);
        ImageButton cheatsButton = layout.findViewById(R.id.cheatsBtn);
        ImageButton settingsButton = layout.findViewById(R.id.settingsBtn);

        GamePadDPad dPad = layout.findViewById(R.id.dpad);

        GamePadButton xButton = layout.findViewById(R.id.xButton);
        GamePadButton yButton = layout.findViewById(R.id.yButton);
        GamePadButton zButton = layout.findViewById(R.id.zButton);
        GamePadButton aButton = layout.findViewById(R.id.aButton);
        GamePadButton bButton = layout.findViewById(R.id.bButton);
        GamePadButton cButton = layout.findViewById(R.id.cButton);
        GamePadButton lButton = layout.findViewById(R.id.lButton);
        GamePadButton rButton = layout.findViewById(R.id.rButton);

        topShowButton.setOnClickListener(view -> {
            if (miscLayChild.getVisibility() == View.VISIBLE){
                miscLayChild.setVisibility(View.INVISIBLE);
                topShowButton.setImageResource(R.drawable.arrow_down);
            } else {
                miscLayChild.setVisibility(View.VISIBLE);
                topShowButton.setImageResource(R.drawable.arrow_up);
            }
        });

        bottomShowButton.setOnClickListener(view -> {
            if (padLayChild.getVisibility() == View.VISIBLE){
                padLayChild.setVisibility(View.INVISIBLE);
                bottomShowButton.setImageResource(R.drawable.arrow_up);
            } else {
                padLayChild.setVisibility(View.VISIBLE);
                bottomShowButton.setImageResource(R.drawable.arrow_down);
            }
        });

        boolean isRPGMorRenPy = false;
        boolean fastforwardUsable = false;
        int cheatKey = KeyEvent.KEYCODE_F6;

        switch (mGame.type){
            case "rpgmxp":
            case "rpgmvx":
            case "rpgmvxace":
            case "mkxp-z":
                padLayChild.setVisibility(View.VISIBLE);
                bottomShowButton.setImageResource(R.drawable.arrow_down);
                isRPGMorRenPy = true;
                fastforwardUsable = true;
                break;
            case "rpgmmv":
            case "rpgmmz":
                isRPGMorRenPy = true;
                cheatKey = KeyEvent.KEYCODE_1;
            case "renpy":
                cheatKey = KeyEvent.KEYCODE_F8;
                isRPGMorRenPy = true;
                break;
        }

        if (isRPGMorRenPy && cheats){
            cheatsButton.setVisibility(View.VISIBLE);
            int finalCheatKey = cheatKey;
            cheatsButton.setOnTouchListener((view, motionEvent) -> {
                if (paused)
                    return false;
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        mOnKeyDownListener.onKeyDown(finalCheatKey);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        mOnKeyUpListener.onKeyUp(finalCheatKey);
                        break;
                }
                ButtonAnimations.animateTouchOnce(context, cheatsButton);
                return true;
            });
        }

        if (fastforwardUsable){
            fastforwardButton.setVisibility(View.VISIBLE);

            ColorFilter activeColorFilter = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
            fastforwardButton.setOnTouchListener((view, motionEvent) -> {
                if (paused)
                    return false;
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        mOnKeyDownListener.onKeyDown(KeyEvent.KEYCODE_PAGE_UP);
                        if (fastforward){
                            fastforwardButton.getDrawable().setColorFilter(null);
                        } else {
                            fastforwardButton.getDrawable().setColorFilter(activeColorFilter);
                        }
                        fastforwardButton.invalidate();
                        fastforward = !fastforward;
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        mOnKeyUpListener.onKeyUp(KeyEvent.KEYCODE_PAGE_UP);
                        break;
                }
                ButtonAnimations.animateTouchOnce(context, fastforwardButton);
                return true;
            });
        }

        closeButton.setOnClickListener(view -> {
            ButtonAnimations.animateTouchOnce(context, closeButton);
            new AlertDialog.Builder(context)
                    .setTitle(R.string.close)
                    .setMessage(R.string.close_game_message)
                    .setNegativeButton(R.string.no, (dialogInterface, i) -> { })
                    .setPositiveButton(R.string.yes, (dialogInterface, i) -> mOnCloseListener.onClose())
                    .create()
                    .show();
        });

        rotateButton.setOnClickListener(view -> {
            ButtonAnimations.animateTouchOnce(context, rotateButton);
            switch (mActivity.getRequestedOrientation()){
                case ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE:
                case ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE:
                case ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE:
                case ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE:
                    mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                    initKeyboard(context, keyboardLayout, Math.min(screenWidth,screenHeight));
                    break;
                case ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT:
                case ActivityInfo.SCREEN_ORIENTATION_PORTRAIT:
                case ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT:
                case ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT:
                    mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                    initKeyboard(context, keyboardLayout, Math.max(screenWidth,screenHeight));
            }
        });

        keyboardButton.setOnClickListener(view -> {
            if (keyboardLayout.getVisibility() == View.VISIBLE){
                keyboardLayout.setVisibility(View.INVISIBLE);
                buttonLayout.setVisibility(View.VISIBLE);
            } else {
                keyboardLayout.setVisibility(View.VISIBLE);
                buttonLayout.setVisibility(View.INVISIBLE);
            }
        });

        settingsButton.setOnClickListener(v -> {
            SettingsDialog settingsDialog = new SettingsDialog(context, mGamePad, mGame);
            settingsDialog.setOnSettingsChanged(gamePad -> {
                if (!mGamePad.btnScale.equals(gamePad.btnScale)){
                    float scaleFactor = (100f/lastScale)*gamePad.btnScale;
                    lastScale = gamePad.btnScale;
                    ViewUtils.resize(gamepadLayout, scaleFactor);
                }

                if (!mGamePad.btnOpacity.equals(gamePad.btnOpacity)){
                    ViewUtils.changeOpacity(gamepadLayout, gamePad.btnOpacity);
                }

                if (!mGamePad.aKeyCode.equals(gamePad.aKeyCode)){
                    aButton.setImageDrawable(new TextDrawable(context.getResources(),KeyEvent.keyCodeToString(gamePad.aKeyCode).replace("KEYCODE_", "")));
                    aButton.setKey(gamePad.aKeyCode);
                }

                if (!mGamePad.bKeyCode.equals(gamePad.bKeyCode)){
                    bButton.setImageDrawable(new TextDrawable(context.getResources(),KeyEvent.keyCodeToString(gamePad.bKeyCode).replace("KEYCODE_", "")));
                    bButton.setKey(gamePad.bKeyCode);
                }

                if (!mGamePad.cKeyCode.equals(gamePad.cKeyCode)){
                    cButton.setImageDrawable(new TextDrawable(context.getResources(),KeyEvent.keyCodeToString(gamePad.cKeyCode).replace("KEYCODE_", "")));
                    cButton.setKey(gamePad.cKeyCode);
                }

                if (!mGamePad.xKeyCode.equals(gamePad.xKeyCode)){
                    xButton.setImageDrawable(new TextDrawable(context.getResources(),KeyEvent.keyCodeToString(gamePad.xKeyCode).replace("KEYCODE_", "")));
                    xButton.setKey(gamePad.xKeyCode);
                }

                if (!mGamePad.yKeyCode.equals(gamePad.yKeyCode)){
                    yButton.setImageDrawable(new TextDrawable(context.getResources(),KeyEvent.keyCodeToString(gamePad.yKeyCode).replace("KEYCODE_", "")));
                    yButton.setKey(gamePad.yKeyCode);
                }

                if (!mGamePad.zKeyCode.equals(gamePad.zKeyCode)){
                    zButton.setImageDrawable(new TextDrawable(context.getResources(),KeyEvent.keyCodeToString(gamePad.zKeyCode).replace("KEYCODE_", "")));
                    zButton.setKey(gamePad.zKeyCode);
                }

                if (!mGamePad.lKeyCode.equals(gamePad.lKeyCode)){
                    lButton.setImageDrawable(new TextDrawable(context.getResources(),KeyEvent.keyCodeToString(gamePad.lKeyCode).replace("KEYCODE_", "")));
                    lButton.setKey(gamePad.lKeyCode);
                }

                if (!mGamePad.rKeyCode.equals(gamePad.rKeyCode)){
                    rButton.setImageDrawable(new TextDrawable(context.getResources(),KeyEvent.keyCodeToString(gamePad.rKeyCode).replace("KEYCODE_", "")));
                    rButton.setKey(gamePad.rKeyCode);
                }

                mGamePad = gamePad;

            });
            settingsDialog.show();
        });

        dPad.setOnKeyDownListener(key -> mOnKeyDownListener.onKeyDown(key));

        dPad.setOnKeyUpListener(key -> mOnKeyUpListener.onKeyUp(key));

        xButton.setImageDrawable(new TextDrawable(context.getResources(),KeyEvent.keyCodeToString(mGamePad.xKeyCode).replace("KEYCODE_", "")));
        xButton.setKey(mGamePad.xKeyCode);
        xButton.setOnKeyDownListener(key -> mOnKeyDownListener.onKeyDown(key));
        xButton.setOnKeyUpListener(key -> mOnKeyUpListener.onKeyUp(key));

        yButton.setImageDrawable(new TextDrawable(context.getResources(),KeyEvent.keyCodeToString(mGamePad.yKeyCode).replace("KEYCODE_", "")));
        yButton.setKey(mGamePad.yKeyCode);
        yButton.setOnKeyDownListener(key -> mOnKeyDownListener.onKeyDown(key));
        yButton.setOnKeyUpListener(key -> mOnKeyUpListener.onKeyUp(key));

        zButton.setImageDrawable(new TextDrawable(context.getResources(),KeyEvent.keyCodeToString(mGamePad.zKeyCode).replace("KEYCODE_", "")));
        zButton.setKey(mGamePad.zKeyCode);
        zButton.setOnKeyDownListener(key -> mOnKeyDownListener.onKeyDown(key));
        zButton.setOnKeyUpListener(key -> mOnKeyUpListener.onKeyUp(key));

        aButton.setImageDrawable(new TextDrawable(context.getResources(),KeyEvent.keyCodeToString(mGamePad.aKeyCode).replace("KEYCODE_", "")));
        aButton.setKey(mGamePad.aKeyCode);
        aButton.setOnKeyDownListener(key -> mOnKeyDownListener.onKeyDown(key));
        aButton.setOnKeyUpListener(key -> mOnKeyUpListener.onKeyUp(key));

        bButton.setImageDrawable(new TextDrawable(context.getResources(),KeyEvent.keyCodeToString(mGamePad.bKeyCode).replace("KEYCODE_", "")));
        bButton.setKey(mGamePad.bKeyCode);
        bButton.setOnKeyDownListener(key -> mOnKeyDownListener.onKeyDown(key));
        bButton.setOnKeyUpListener(key -> mOnKeyUpListener.onKeyUp(key));

        cButton.setImageDrawable(new TextDrawable(context.getResources(),KeyEvent.keyCodeToString(mGamePad.cKeyCode).replace("KEYCODE_", "")));
        cButton.setKey(mGamePad.cKeyCode);
        cButton.setOnKeyDownListener(key -> mOnKeyDownListener.onKeyDown(key));
        cButton.setOnKeyUpListener(key -> mOnKeyUpListener.onKeyUp(key));

        lButton.setImageDrawable(new TextDrawable(context.getResources(),KeyEvent.keyCodeToString(mGamePad.lKeyCode).replace("KEYCODE_", "")));
        lButton.setKey(mGamePad.lKeyCode);
        lButton.setOnKeyDownListener(key -> mOnKeyDownListener.onKeyDown(key));
        lButton.setOnKeyUpListener(key -> mOnKeyUpListener.onKeyUp(key));

        rButton.setImageDrawable(new TextDrawable(context.getResources(),KeyEvent.keyCodeToString(mGamePad.rKeyCode).replace("KEYCODE_", "")));
        rButton.setKey(mGamePad.rKeyCode);
        rButton.setOnKeyDownListener(key -> mOnKeyDownListener.onKeyDown(key));
        rButton.setOnKeyUpListener(key -> mOnKeyUpListener.onKeyUp(key));

        lastScale = mGamePad.btnScale;
        ViewUtils.resize(gamepadLayout, mGamePad.btnScale);
        ViewUtils.changeOpacity(gamepadLayout, mGamePad.btnOpacity);

        topShowButton.bringToFront();
        topShowButton.invalidate();
        bottomShowButton.bringToFront();
        bottomShowButton.invalidate();

        initKeyboard(context, keyboardLayout, screenWidth);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initKeyboard(Context context, LinearLayout viewGroup, int width){
        viewGroup.removeAllViews();
        viewGroup.invalidate();

        List<Map<Integer,String>> keyboardLayout = KeyboardUtils.getKeyboardLayout();

        for(Map<Integer,String> row : keyboardLayout){
            LinearLayout rowLayout = new LinearLayout(context);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            lParams.gravity = Gravity.CENTER_HORIZONTAL;
            rowLayout.setLayoutParams(lParams);
            viewGroup.addView(rowLayout);

            int additionalWidth;
            int btnWidth = 0;

            for (String key : row.values()) {
                btnWidth += key.length() * sdpToPx(context, 32) + sdpToPx(context, 2);
            }

            additionalWidth = (width - btnWidth)/(row.size());

            for (Map.Entry<Integer, String> entry : row.entrySet()){
                Button b = new Button(context);
                b.setText(entry.getValue());
                b.setTextColor(context.getResources().getColor(android.R.color.white));
                b.setPadding(0,0,0,0);
                b.setTextSize(sdpToPx(context,4));
                b.setBackgroundResource(R.drawable.keyboard_button);
                b.setOnTouchListener((view, motionEvent) -> {
                    switch (motionEvent.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            mOnKeyDownListener.onKeyDown(entry.getKey());
                            break;
                        case MotionEvent.ACTION_CANCEL:
                        case MotionEvent.ACTION_UP:
                            mOnKeyUpListener.onKeyUp(entry.getKey());
                            break;
                    }
                    ButtonAnimations.animateTouchOnce(context, b);
                    return true;
                });

                LinearLayout.LayoutParams bParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                bParams.setMargins(sdpToPx(context, 1),sdpToPx(context, 1),sdpToPx(context, 1),sdpToPx(context,1));
                bParams.width = entry.getValue().length() * sdpToPx(context, 32) + additionalWidth;
                bParams.height = sdpToPx(context, 24);
                b.setLayoutParams(bParams);
                rowLayout.addView(b);
            }
        }

        viewGroup.invalidate();
    }

    public Map<Integer, Integer> getKeymapping(){
        Map<Integer, Integer> map = new HashMap<>();
        map.put(KeyEvent.KEYCODE_DPAD_UP,KeyEvent.KEYCODE_DPAD_UP);
        map.put(KeyEvent.KEYCODE_DPAD_RIGHT,KeyEvent.KEYCODE_DPAD_RIGHT);
        map.put(KeyEvent.KEYCODE_DPAD_DOWN, KeyEvent.KEYCODE_DPAD_DOWN);
        map.put(KeyEvent.KEYCODE_DPAD_LEFT, KeyEvent.KEYCODE_DPAD_LEFT);
        map.put(KeyEvent.KEYCODE_BUTTON_X, mGamePad.xKeyCode);
        map.put(KeyEvent.KEYCODE_BUTTON_Y, mGamePad.yKeyCode);
        map.put(KeyEvent.KEYCODE_BUTTON_A, mGamePad.aKeyCode);
        map.put(KeyEvent.KEYCODE_BUTTON_L1, mGamePad.zKeyCode);
        map.put(KeyEvent.KEYCODE_BUTTON_B, mGamePad.bKeyCode);
        map.put(KeyEvent.KEYCODE_BUTTON_R1, mGamePad.cKeyCode);
        map.put(KeyEvent.KEYCODE_BUTTON_SELECT, mGamePad.lKeyCode);
        map.put(KeyEvent.KEYCODE_BUTTON_START, mGamePad.rKeyCode);
        return map;
    }
    public boolean processGamepadEvent(KeyEvent keyEvent){
        if (keyEvent.getSource() == InputDevice.SOURCE_UNKNOWN)
            return false;

        if (!mKeyMapping.containsKey(keyEvent.getKeyCode()))
            return false;

        Integer keyCode = mKeyMapping.get(keyEvent.getKeyCode());
        if (keyCode == null){
            return false;
        }
        switch (keyEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                mOnKeyDownListener.onKeyDown(keyCode);
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mOnKeyUpListener.onKeyUp(keyCode);
        }

        return true;
    }

    public void setOnKeyDownListener(OnKeyDownListener onKeyDownListener){
        mOnKeyDownListener = onKeyDownListener;
    }

    public void setOnKeyUpListener(OnKeyUpListener onKeyUpListener){
        mOnKeyUpListener = onKeyUpListener;
    }

    public void setOnCloseListener(OnCloseListener onCloseListener){
        mOnCloseListener = onCloseListener;
    }
}
