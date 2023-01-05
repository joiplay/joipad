package cyou.joiplay.joipad;

import static cyou.joiplay.joipad.util.ViewUtils.sdpToPx;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
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
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.File;
import java.util.List;
import java.util.Map;

import cyou.joiplay.commons.model.Game;
import cyou.joiplay.commons.model.GamePad;
import cyou.joiplay.commons.parser.GamePadParser;
import cyou.joiplay.commons.utils.AutoCloseTimer;
import cyou.joiplay.joipad.animation.ButtonAnimations;
import cyou.joiplay.joipad.dialog.SettingsDialog;
import cyou.joiplay.joipad.drawable.TextDrawable;
import cyou.joiplay.joipad.util.DirectionUtils;
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

    private int screenWidth;
    private int screenHeight;

    private boolean cheats = false;
    private boolean fastforward = false;
    private boolean volumeOn = true;
    private boolean isKeyboardSpecial = false;

    private int lastScale = 100;

    private GamePadButton xButton;
    private GamePadButton yButton;
    private GamePadButton zButton;
    private GamePadButton aButton;
    private GamePadButton bButton;
    private GamePadButton cButton;
    private GamePadButton lButton;
    private GamePadButton rButton;
    private GamePadButton clButton;
    private GamePadButton crButton;

    private GamePadDPad dPad;
    private AutoCloseTimer autoCloseTimer;

    private boolean isAlternativeButtons = false;

    public void init(Activity activity, GamePad gamePad){
        mActivity = activity;
        mGamePad = gamePad;
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

    public void setAutoCloseTimer(AutoCloseTimer autoCloseTimer){
        this.autoCloseTimer = autoCloseTimer;
    }

    private void rescheduleAutoCloseTimer(){
        if (autoCloseTimer != null) autoCloseTimer.reschedule();
    }

    private void setGamePadButtonKey(GamePadButton gamePadButton, Integer[] keyCodes, boolean isAlternative){
        int keyCode = isAlternative ? keyCodes[1] : keyCodes[0];
        gamePadButton.setForegroundText(KeyEvent.keyCodeToString(keyCode).replace("KEYCODE_", ""));
        gamePadButton.setKey(keyCode);
        gamePadButton.setOnKeyDownListener(key -> {
            if (autoCloseTimer != null) autoCloseTimer.reschedule();
            mOnKeyDownListener.onKeyDown(key);
        });
        gamePadButton.setOnKeyUpListener(key -> mOnKeyUpListener.onKeyUp(key));
    }

    private void initGamePadButtons(boolean isAlternative){
        setGamePadButtonKey(xButton, mGamePad.xKeyCode, isAlternative);
        setGamePadButtonKey(yButton, mGamePad.yKeyCode, isAlternative);
        setGamePadButtonKey(zButton, mGamePad.zKeyCode, isAlternative);
        setGamePadButtonKey(aButton, mGamePad.aKeyCode, isAlternative);
        setGamePadButtonKey(bButton, mGamePad.bKeyCode, isAlternative);
        setGamePadButtonKey(cButton, mGamePad.cKeyCode, isAlternative);
        setGamePadButtonKey(lButton, mGamePad.lKeyCode, isAlternative);
        setGamePadButtonKey(rButton, mGamePad.rKeyCode, isAlternative);
        setGamePadButtonKey(clButton, mGamePad.clKeyCode, isAlternative);
        setGamePadButtonKey(crButton, mGamePad.crKeyCode, isAlternative);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void attachTo(Context context, ViewGroup viewGroup){
        if(mGamePad.hideGamepad) return;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.gamepad_layout,viewGroup);

        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        screenHeight = context.getResources().getDisplayMetrics().heightPixels;

        RelativeLayout gamepadLayout = layout.findViewById(R.id.gamepad_layout);
        RelativeLayout miscLayChild = layout.findViewById(R.id.miscLayChild);
        RelativeLayout padLayChild = layout.findViewById(R.id.padLayChild);
        RelativeLayout buttonLayout = layout.findViewById(R.id.buttonLay);
        LinearLayout keyboardLayout = layout.findViewById(R.id.keyboardLay);

        GamePadButton topShowButton = layout.findViewById(R.id.topShowButton);
        GamePadButton bottomShowButton = layout.findViewById(R.id.bottomShowButton);

        GamePadButton closeButton = layout.findViewById(R.id.closeBtn);
        GamePadButton rotateButton = layout.findViewById(R.id.rotateBtn);
        GamePadButton volumeButton = layout.findViewById(R.id.volumeBtn);
        GamePadButton keyboardButton = layout.findViewById(R.id.keyboardBtn);
        GamePadButton fastforwardButton = layout.findViewById(R.id.fastforwardBtn);
        GamePadButton cheatsButton = layout.findViewById(R.id.cheatsBtn);
        GamePadButton settingsButton = layout.findViewById(R.id.settingsBtn);
        GamePadButton switchButton = layout.findViewById(R.id.switchButton);

        dPad = layout.findViewById(R.id.dpad);

        xButton = layout.findViewById(R.id.xButton);
        yButton = layout.findViewById(R.id.yButton);
        zButton = layout.findViewById(R.id.zButton);
        aButton = layout.findViewById(R.id.aButton);
        bButton = layout.findViewById(R.id.bButton);
        cButton = layout.findViewById(R.id.cButton);
        lButton = layout.findViewById(R.id.lButton);
        rButton = layout.findViewById(R.id.rButton);
        clButton = layout.findViewById(R.id.clButton);
        crButton = layout.findViewById(R.id.crButton);

        gamepadLayout.setOnTouchListener((view, motionEvent) -> {
            rescheduleAutoCloseTimer();
            return false;
        });

        int slope = ViewConfiguration.get(context).getScaledTouchSlop();

        ViewUtils.MovementData topMovementData = new ViewUtils.MovementData();
        topShowButton.setOnTouchListener((v, event) -> ViewUtils.onMoveView(v, event, topMovementData, ViewUtils.GridMovement.X, slope));

        topShowButton.setOnClickListener(view -> {
            if (autoCloseTimer != null) autoCloseTimer.reschedule();
            if (miscLayChild.getVisibility() == View.VISIBLE){
                miscLayChild.setVisibility(View.INVISIBLE);
                topShowButton.setForegroundDrawableResource(R.drawable.arrow_down);
            } else {
                miscLayChild.setVisibility(View.VISIBLE);
                topShowButton.setForegroundDrawableResource(R.drawable.arrow_up);
            }
        });

        ViewUtils.MovementData bottomMovementData = new ViewUtils.MovementData();
        bottomShowButton.setOnTouchListener((v, event) -> ViewUtils.onMoveView(v, event, bottomMovementData, ViewUtils.GridMovement.X, slope));

        bottomShowButton.setOnClickListener(view -> {
            if (autoCloseTimer != null) autoCloseTimer.reschedule();
            if (padLayChild.getVisibility() == View.VISIBLE){
                padLayChild.setVisibility(View.INVISIBLE);
                bottomShowButton.setForegroundDrawableResource(R.drawable.arrow_up);
            } else {
                padLayChild.setVisibility(View.VISIBLE);
                bottomShowButton.setForegroundDrawableResource(R.drawable.arrow_down);
            }
        });

        switchButton.setOnClickListener(view -> {
            if (autoCloseTimer != null) autoCloseTimer.reschedule();
            ButtonAnimations.animateTouchOnce(context, switchButton);

            isAlternativeButtons = !isAlternativeButtons;
            initGamePadButtons(isAlternativeButtons);
        });

        boolean isRPGMorRenPy = false;
        boolean fastforwardUsable = false;
        int cheatKey = KeyEvent.KEYCODE_MOVE_HOME;

        switch (mGame.type){
            case "rpgmxp":
            case "rpgmvx":
            case "rpgmvxace":
            case "mkxp-z":
                padLayChild.setVisibility(View.VISIBLE);
                bottomShowButton.setForegroundDrawableResource(R.drawable.arrow_down);
                isRPGMorRenPy = true;
                fastforwardUsable = true;
                break;
            case "rpgmmv":
            case "rpgmmz":
            case "renpy":
                isRPGMorRenPy = true;
                break;
        }

        if (isRPGMorRenPy && cheats){
            cheatsButton.setVisibility(View.VISIBLE);
            cheatsButton.setKey(cheatKey);
            cheatsButton.setOnKeyDownListener(key -> {
                if (autoCloseTimer != null) autoCloseTimer.reschedule();
                mOnKeyDownListener.onKeyDown(key);
            });
            cheatsButton.setOnKeyUpListener(key -> mOnKeyUpListener.onKeyUp(key));
        }

        if (fastforwardUsable){
            fastforwardButton.setVisibility(View.VISIBLE);

            ColorFilter activeColorFilter = new LightingColorFilter(Color.GREEN, Color.TRANSPARENT);
            fastforwardButton.setOnTouchListener((view, motionEvent) -> {
                if (autoCloseTimer != null) autoCloseTimer.reschedule();
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        mOnKeyDownListener.onKeyDown(KeyEvent.KEYCODE_PAGE_UP);
                        if (fastforward){
                            fastforwardButton.clearColorFilter();
                        } else {
                            fastforwardButton.setColorFilter(activeColorFilter);
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
            if (autoCloseTimer != null) autoCloseTimer.reschedule();

            ButtonAnimations.animateTouchOnce(context, closeButton);
            new AlertDialog.Builder(context)
                    .setTitle(R.string.close)
                    .setMessage(R.string.close_game_message)
                    .setNegativeButton(R.string.no, (dialogInterface, i) -> { })
                    .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        mOnCloseListener.onClose();
                    })
                    .create()
                    .show();
        });

        rotateButton.setOnClickListener(view -> {
            if (autoCloseTimer != null) autoCloseTimer.reschedule();

            ButtonAnimations.animateTouchOnce(context, rotateButton);
            switch (mActivity.getRequestedOrientation()){
                case ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE:
                case ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE:
                case ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE:
                case ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE:
                    mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                    initKeyboard(context, keyboardLayout, Math.min(screenWidth,screenHeight), isKeyboardSpecial);
                    break;
                case ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT:
                case ActivityInfo.SCREEN_ORIENTATION_PORTRAIT:
                case ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT:
                case ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT:
                    mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                    initKeyboard(context, keyboardLayout, Math.max(screenWidth,screenHeight), isKeyboardSpecial);
                    break;
            }
        });

        volumeButton.setOnClickListener(view -> {
            volumeOn = !volumeOn;

            if (volumeOn){
                volumeButton.setForegroundDrawableResource(R.drawable.sound_on);
            } else {
                volumeButton.setForegroundDrawableResource(R.drawable.sound_off);
            }

            mOnKeyDownListener.onKeyDown(KeyEvent.KEYCODE_MUTE);
            mOnKeyUpListener.onKeyUp(KeyEvent.KEYCODE_MUTE);
        });

        keyboardButton.setOnClickListener(view -> {
            if (autoCloseTimer != null) autoCloseTimer.reschedule();

            if (keyboardLayout.getVisibility() == View.VISIBLE){
                keyboardLayout.setVisibility(View.INVISIBLE);
                buttonLayout.setVisibility(View.VISIBLE);
            } else {
                keyboardLayout.setVisibility(View.VISIBLE);
                buttonLayout.setVisibility(View.INVISIBLE);
            }
        });

        settingsButton.setOnClickListener(v -> {
            if (autoCloseTimer != null) autoCloseTimer.reschedule();

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

                mGamePad = gamePad;

                initGamePadButtons(isAlternativeButtons);
            });
            settingsDialog.show();
        });

        dPad.setOnKeyDownListener(key -> {
            if (autoCloseTimer != null) autoCloseTimer.reschedule();
            mOnKeyDownListener.onKeyDown(key);
        });
        dPad.setOnKeyUpListener(key -> mOnKeyUpListener.onKeyUp(key));
        dPad.isDiagonal = mGamePad.diagonalMovement;

        initGamePadButtons(isAlternativeButtons);

        lastScale = mGamePad.btnScale;
        ViewUtils.resize(gamepadLayout, mGamePad.btnScale);
        ViewUtils.changeOpacity(gamepadLayout, mGamePad.btnOpacity);

        topShowButton.bringToFront();
        topShowButton.invalidate();
        bottomShowButton.bringToFront();
        bottomShowButton.invalidate();

        initKeyboard(context, keyboardLayout, screenWidth, isKeyboardSpecial);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initKeyboard(Context context, LinearLayout viewGroup, int width, boolean special){
        isKeyboardSpecial = special;
        viewGroup.removeAllViews();
        viewGroup.invalidate();

        List<Map<Integer,String>> keyboardLayout = special ? KeyboardUtils.getKeyboardLayoutSpecial() : KeyboardUtils.getKeyboardLayout();

        for(Map<Integer,String> row : keyboardLayout){
            LinearLayout rowLayout = new LinearLayout(context);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            lParams.gravity = Gravity.CENTER_HORIZONTAL;
            rowLayout.setLayoutParams(lParams);
            viewGroup.addView(rowLayout);

            int additionalWidth;
            int btnWidth = 0;

            for (Map.Entry<Integer, String> entry : row.entrySet()) {
                btnWidth += ((entry.getKey() == KeyEvent.KEYCODE_SWITCH_CHARSET) ? 1 : entry.getValue().length()) * sdpToPx(context, 32) + sdpToPx(context, 2);
            }

            additionalWidth = (width - btnWidth)/(row.size());

            for (Map.Entry<Integer, String> entry : row.entrySet()){
                Button b = new Button(context);
                b.setText(entry.getValue());
                b.setTextColor(context.getResources().getColor(android.R.color.white));
                b.setPadding(0,0,0,0);
                b.setTextSize(sdpToPx(context,5));
                b.setBackgroundResource(R.drawable.keyboard_button);

                if (entry.getKey() == KeyEvent.KEYCODE_SWITCH_CHARSET){
                    b.setOnClickListener((view) -> {
                        initKeyboard(context, viewGroup, width, !special);
                    });
                } else {
                    b.setOnTouchListener((view, motionEvent) -> {
                        rescheduleAutoCloseTimer();
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
                }

                LinearLayout.LayoutParams bParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                bParams.setMargins(sdpToPx(context, 1),sdpToPx(context, 1),sdpToPx(context, 1),sdpToPx(context,1));
                bParams.width = ((entry.getKey() == KeyEvent.KEYCODE_SWITCH_CHARSET) ? 1 : entry.getValue().length()) * sdpToPx(context, 32) + additionalWidth;
                bParams.height = sdpToPx(context, 24);
                b.setLayoutParams(bParams);
                rowLayout.addView(b);
            }
        }

        viewGroup.invalidate();
    }

    public boolean processGamepadEvent(KeyEvent keyEvent){
        if (autoCloseTimer != null) autoCloseTimer.reschedule();
        int sources = keyEvent.getDevice().getSources();
        if ((( sources & InputDevice.SOURCE_GAMEPAD) != InputDevice.SOURCE_GAMEPAD) && ((sources & InputDevice.SOURCE_DPAD) != InputDevice.SOURCE_DPAD))
            return false;

        Integer keyCode = keyEvent.getKeyCode();
        GamePadButton button = null;
        switch (keyCode){
            case KeyEvent.KEYCODE_BUTTON_X:
                keyCode = isAlternativeButtons ? mGamePad.xKeyCode[1] : mGamePad.xKeyCode[0];
                button = xButton;
                break;
            case KeyEvent.KEYCODE_BUTTON_Y:
                keyCode = isAlternativeButtons ? mGamePad.yKeyCode[1] : mGamePad.yKeyCode[0];
                button = yButton;
                break;
            case KeyEvent.KEYCODE_BUTTON_L1:
                keyCode = isAlternativeButtons ? mGamePad.zKeyCode[1] : mGamePad.zKeyCode[0];
                button = zButton;
                break;
            case KeyEvent.KEYCODE_BUTTON_A:
                keyCode = isAlternativeButtons ? mGamePad.aKeyCode[1] : mGamePad.aKeyCode[0];
                button = aButton;
                break;
            case KeyEvent.KEYCODE_BUTTON_B:
                keyCode = isAlternativeButtons ? mGamePad.bKeyCode[1] : mGamePad.bKeyCode[0];
                button = bButton;
                break;
            case KeyEvent.KEYCODE_BUTTON_R1:
                keyCode = isAlternativeButtons ? mGamePad.cKeyCode[1] : mGamePad.cKeyCode[0];
                button = cButton;
                break;
            case KeyEvent.KEYCODE_BUTTON_SELECT:
                keyCode = isAlternativeButtons ? mGamePad.lKeyCode[1] : mGamePad.lKeyCode[0];
                button = lButton;
                break;
            case KeyEvent.KEYCODE_BUTTON_START:
                keyCode = isAlternativeButtons ? mGamePad.rKeyCode[1] : mGamePad.rKeyCode[0];
                button = rButton;
                break;
        }

        switch (keyEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                if (button != null) ButtonAnimations.animateTouch(mActivity, button, true);
                mOnKeyDownListener.onKeyDown(keyCode);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (button != null) ButtonAnimations.animateTouch(mActivity, button, false);
                mOnKeyUpListener.onKeyUp(keyCode);
                break;
        }

        return true;
    }

    public boolean processDPadEvent(MotionEvent motionEvent){
        if (autoCloseTimer != null) autoCloseTimer.reschedule();
        int sources = motionEvent.getDevice().getSources();
        if ((( sources & InputDevice.SOURCE_DPAD) != InputDevice.SOURCE_DPAD))
            return false;

        float xAxis = motionEvent.getAxisValue(MotionEvent.AXIS_HAT_X);
        float yAxis = motionEvent.getAxisValue(MotionEvent.AXIS_HAT_Y);

        Integer keyCode = null;

        if (Float.compare(yAxis, -1.0f) == 0) {
            keyCode =  KeyEvent.KEYCODE_DPAD_UP;
        } else if (Float.compare(yAxis, 1.0f) == 0) {
            keyCode =  KeyEvent.KEYCODE_DPAD_DOWN;
        } else if (Float.compare(xAxis, -1.0f) == 0) {
            keyCode =  KeyEvent.KEYCODE_DPAD_LEFT;
        } else if (Float.compare(xAxis, 1.0f) == 0) {
            keyCode =  KeyEvent.KEYCODE_DPAD_RIGHT;
        }

        if (keyCode == null)
            return false;

        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                mOnKeyDownListener.onKeyDown(keyCode);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mOnKeyUpListener.onKeyUp(keyCode);
                break;
        }

        return true;
    }

    private float centeredAxis(MotionEvent event, int axis){
        InputDevice.MotionRange motionRange = event.getDevice().getMotionRange(axis, event.getSource());

        if (motionRange == null)
            return 0;

        float flat = motionRange.getFlat();
        float value = event.getAxisValue(axis);;

        if (Math.abs(value) > flat){
            return value;
        } else {
            return 0;
        }
    }

    private float getJoystickX(MotionEvent event){
        float x = centeredAxis(event, MotionEvent.AXIS_X);

        if (x == 0)
            x = centeredAxis(event, MotionEvent.AXIS_HAT_X);

        if (x == 0)
            x = centeredAxis(event, MotionEvent.AXIS_Z);

        return x;
    }

    private float getJoystickY(MotionEvent event){
        float y = centeredAxis(event, MotionEvent.AXIS_Y);

        if (y == 0)
            y = centeredAxis(event, MotionEvent.AXIS_HAT_Y);

        if (y == 0)
            y = centeredAxis(event, MotionEvent.AXIS_RZ);

        return y;
    }

    private boolean applyJoystickMovement(int action, float x, float y){
        DirectionUtils.Direction direction = DirectionUtils.getDir(x, y, false);

        Integer keyCode = null;

        switch (direction){
            case UP:
                keyCode = KeyEvent.KEYCODE_DPAD_UP;
                break;
            case RIGHT:
                keyCode = KeyEvent.KEYCODE_DPAD_RIGHT;
                break;
            case DOWN:
                keyCode = KeyEvent.KEYCODE_DPAD_DOWN;
                break;
            case LEFT:
                keyCode = KeyEvent.KEYCODE_DPAD_LEFT;
                break;
        }

        if (keyCode == null)
            return false;

        switch (action){
            case MotionEvent.ACTION_DOWN:
                mOnKeyDownListener.onKeyDown(keyCode);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mOnKeyUpListener.onKeyUp(keyCode);
                break;
        }

        return true;
    }

    public boolean processJoystickEvent(MotionEvent event){
        if (autoCloseTimer != null) autoCloseTimer.reschedule();
        if ((event.getSource() & InputDevice.SOURCE_JOYSTICK) ==
                InputDevice.SOURCE_JOYSTICK &&
                event.getAction() == MotionEvent.ACTION_MOVE) {

            float x = getJoystickX(event);
            float y = getJoystickY(event);

            return applyJoystickMovement(event.getAction(), x, y);
        }

        return false;
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
