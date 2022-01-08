package cyou.joiplay.joipad.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.io.File;

import cyou.joiplay.commons.dialog.ConfirmationDialog;
import cyou.joiplay.commons.model.Game;
import cyou.joiplay.commons.model.GamePad;
import cyou.joiplay.commons.parser.GamePadParser;
import cyou.joiplay.joipad.R;

public class SettingsDialog{
    private static final String TAG = "SettingsDialog";
    private Context mContext;
    private GamePad mGamePad;
    private Game mGame;
    private Dialog mDialog;

    private Integer mOpacity;
    private Integer mScale;
    private Integer mLKey;
    private Integer mRKey;
    private Integer mCLKey;
    private Integer mCRKey;
    private Integer mXKey;
    private Integer mYKey;
    private Integer mZKey;
    private Integer mAKey;
    private Integer mBKey;
    private Integer mCKey;

    public interface OnSettingsChanged{
        void onSettingsChanged(GamePad gamePad);
    }

    private OnSettingsChanged mOnSettingsChanged;

    public SettingsDialog(Context context, GamePad gamePad, Game game){
        mContext = context;
        mGamePad = gamePad;
        mGame = game;

        mOpacity = gamePad.btnOpacity;
        mScale = gamePad.btnScale;
        mLKey = gamePad.lKeyCode;
        mRKey = gamePad.rKeyCode;
        mCLKey = gamePad.clKeyCode;
        mCRKey = gamePad.crKeyCode;
        mXKey = gamePad.xKeyCode;
        mYKey = gamePad.yKeyCode;
        mZKey = gamePad.zKeyCode;
        mAKey = gamePad.aKeyCode;
        mBKey = gamePad.bKeyCode;
        mCKey = gamePad.cKeyCode;

        init();
    }

    void init(){
        mDialog = new Dialog(mContext);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setCancelable(true);
        mDialog.setContentView(R.layout.settings_layout);

        Spinner opacitySpinner = mDialog.findViewById(R.id.opacitySpinner);
        Spinner sizeSpinner = mDialog.findViewById(R.id.sizeSpinner);
        Spinner leftSpinner = mDialog.findViewById(R.id.leftSpinner);
        Spinner rightSpinner = mDialog.findViewById(R.id.rightSpinner);
        Spinner cleftSpinner = mDialog.findViewById(R.id.cleftSpinner);
        Spinner crightSpinner = mDialog.findViewById(R.id.crightSpinner);
        Spinner firstSpinner = mDialog.findViewById(R.id.firstSpinner);
        Spinner secondSpinner = mDialog.findViewById(R.id.secondSpinner);
        Spinner thirdSpinner = mDialog.findViewById(R.id.thirdSpinner);
        Spinner fourthSpinner = mDialog.findViewById(R.id.fourthSpinner);
        Spinner fifthSpinner = mDialog.findViewById(R.id.fifthSpinner);
        Spinner sixthSpinner = mDialog.findViewById(R.id.sixthSpinner);
        Button saveButton = mDialog.findViewById(R.id.saveButton);

        ArrayAdapter<CharSequence> opacityAdapter = ArrayAdapter.createFromResource(mContext, R.array.opacity_array, R.layout.spinner_item);
        ArrayAdapter<CharSequence> sizeAdapter = ArrayAdapter.createFromResource(mContext, R.array.scale_array, R.layout.spinner_item);
        ArrayAdapter<CharSequence> leftAdapter = ArrayAdapter.createFromResource(mContext, R.array.key_array, R.layout.spinner_item);
        ArrayAdapter<CharSequence> rightAdapter = ArrayAdapter.createFromResource(mContext, R.array.key_array, R.layout.spinner_item);
        ArrayAdapter<CharSequence> cleftAdapter = ArrayAdapter.createFromResource(mContext, R.array.key_array, R.layout.spinner_item);
        ArrayAdapter<CharSequence> crightAdapter = ArrayAdapter.createFromResource(mContext, R.array.key_array, R.layout.spinner_item);
        ArrayAdapter<CharSequence> firstAdapter = ArrayAdapter.createFromResource(mContext, R.array.key_array, R.layout.spinner_item);
        ArrayAdapter<CharSequence> secondAdapter = ArrayAdapter.createFromResource(mContext, R.array.key_array, R.layout.spinner_item);
        ArrayAdapter<CharSequence> thirdAdapter = ArrayAdapter.createFromResource(mContext, R.array.key_array, R.layout.spinner_item);
        ArrayAdapter<CharSequence> fourthAdapter = ArrayAdapter.createFromResource(mContext, R.array.key_array, R.layout.spinner_item);
        ArrayAdapter<CharSequence> fifthAdapter = ArrayAdapter.createFromResource(mContext, R.array.key_array, R.layout.spinner_item);
        ArrayAdapter<CharSequence> sixthAdapter = ArrayAdapter.createFromResource(mContext, R.array.key_array, R.layout.spinner_item);

        opacityAdapter.setDropDownViewResource(R.layout.spinner_item);
        sizeAdapter.setDropDownViewResource(R.layout.spinner_item);
        leftAdapter.setDropDownViewResource(R.layout.spinner_item);
        rightAdapter.setDropDownViewResource(R.layout.spinner_item);
        cleftAdapter.setDropDownViewResource(R.layout.spinner_item);
        crightAdapter.setDropDownViewResource(R.layout.spinner_item);
        firstAdapter.setDropDownViewResource(R.layout.spinner_item);
        secondAdapter.setDropDownViewResource(R.layout.spinner_item);
        thirdAdapter.setDropDownViewResource(R.layout.spinner_item);
        fourthAdapter.setDropDownViewResource(R.layout.spinner_item);
        fifthAdapter.setDropDownViewResource(R.layout.spinner_item);
        sixthAdapter.setDropDownViewResource(R.layout.spinner_item);

        opacitySpinner.setAdapter(opacityAdapter);
        sizeSpinner.setAdapter(sizeAdapter);
        leftSpinner.setAdapter(leftAdapter);
        rightSpinner.setAdapter(rightAdapter);
        cleftSpinner.setAdapter(leftAdapter);
        crightSpinner.setAdapter(rightAdapter);
        firstSpinner.setAdapter(firstAdapter);
        secondSpinner.setAdapter(secondAdapter);
        thirdSpinner.setAdapter(thirdAdapter);
        fourthSpinner.setAdapter(fourthAdapter);
        fifthSpinner.setAdapter(fifthAdapter);
        sixthSpinner.setAdapter(sixthAdapter);

        opacitySpinner.setSelection(getPosition(mOpacity.toString(), opacityAdapter));
        sizeSpinner.setSelection(getPosition(mScale.toString(), sizeAdapter));
        leftSpinner.setSelection(getPosition(getKeyName(mLKey), leftAdapter));
        rightSpinner.setSelection(getPosition(getKeyName(mRKey), rightAdapter));
        cleftSpinner.setSelection(getPosition(getKeyName(mCLKey), cleftAdapter));
        crightSpinner.setSelection(getPosition(getKeyName(mCRKey), crightAdapter));
        firstSpinner.setSelection(getPosition(getKeyName(mXKey), firstAdapter));
        secondSpinner.setSelection(getPosition(getKeyName(mYKey), secondAdapter));
        thirdSpinner.setSelection(getPosition(getKeyName(mZKey), thirdAdapter));
        fourthSpinner.setSelection(getPosition(getKeyName(mAKey), fourthAdapter));
        fifthSpinner.setSelection(getPosition(getKeyName(mBKey), fifthAdapter));
        sixthSpinner.setSelection(getPosition(getKeyName(mCKey), sixthAdapter));

        opacitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    mOpacity = Integer.parseInt((String) parent.getItemAtPosition(position));
                } catch (Exception e){
                    Log.d(TAG, Log.getStackTraceString(e));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    mScale = Integer.parseInt((String) parent.getItemAtPosition(position));
                } catch (Exception e){
                    Log.d(TAG, Log.getStackTraceString(e));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        leftSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    mLKey = getKeyCode((String) parent.getItemAtPosition(position));
                } catch (Exception e){
                    Log.d(TAG, Log.getStackTraceString(e));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        rightSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    mRKey = getKeyCode((String) parent.getItemAtPosition(position));
                } catch (Exception e){
                    Log.d(TAG, Log.getStackTraceString(e));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        cleftSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    mCLKey = getKeyCode((String) parent.getItemAtPosition(position));
                } catch (Exception e){
                    Log.d(TAG, Log.getStackTraceString(e));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        crightSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    mCRKey = getKeyCode((String) parent.getItemAtPosition(position));
                } catch (Exception e){
                    Log.d(TAG, Log.getStackTraceString(e));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        firstSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    mXKey = getKeyCode((String) parent.getItemAtPosition(position));
                } catch (Exception e){
                    Log.d(TAG, Log.getStackTraceString(e));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        secondSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    mYKey = getKeyCode((String) parent.getItemAtPosition(position));
                } catch (Exception e){
                    Log.d(TAG, Log.getStackTraceString(e));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        thirdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    mZKey = getKeyCode((String) parent.getItemAtPosition(position));
                } catch (Exception e){
                    Log.d(TAG, Log.getStackTraceString(e));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        fourthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    mAKey = getKeyCode((String) parent.getItemAtPosition(position));
                } catch (Exception e){
                    Log.d(TAG, Log.getStackTraceString(e));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        fifthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    mBKey = getKeyCode((String) parent.getItemAtPosition(position));
                } catch (Exception e){
                    Log.d(TAG, Log.getStackTraceString(e));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        sixthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    mCKey = getKeyCode((String) parent.getItemAtPosition(position));
                } catch (Exception e){
                    Log.d(TAG, Log.getStackTraceString(e));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        saveButton.setOnClickListener(v -> {
            GamePad gamePad = new GamePad();
            gamePad.btnOpacity = mOpacity;
            gamePad.btnScale = mScale;
            gamePad.aKeyCode = mAKey;
            gamePad.bKeyCode = mBKey;
            gamePad.cKeyCode = mCKey;
            gamePad.xKeyCode = mXKey;
            gamePad.yKeyCode = mYKey;
            gamePad.zKeyCode = mZKey;
            gamePad.lKeyCode = mLKey;
            gamePad.rKeyCode = mRKey;
            gamePad.clKeyCode = mCLKey;
            gamePad.crKeyCode = mCRKey;

            try{
                String configPath;
                if (mGame.folder.startsWith(Environment.getExternalStorageDirectory().getAbsolutePath())){
                    configPath = mGame.folder+"/gamepad.json";
                } else {
                    configPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/JoiPlay/games/"+mGame.id+"/gamepad.json";
                }

                File configFile = new File(configPath);
                if (!configFile.getParentFile().exists()){
                    configFile.getParentFile().mkdirs();
                }

                GamePadParser.saveToFile(gamePad, configFile);
            } catch (Exception e){
                Log.d(TAG, Log.getStackTraceString(e));
                mDialog.dismiss();
                ConfirmationDialog errorDialog = new ConfirmationDialog();
                errorDialog.setTitle(R.string.error);
                errorDialog.setMessage(R.string.could_not_save_settings);
                errorDialog.show(mContext);
            }

            mDialog.dismiss();
            mOnSettingsChanged.onSettingsChanged(gamePad);
        });
    }

    public void show(){
        mDialog.show();
        Window window = mDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public void setOnSettingsChanged(OnSettingsChanged onSettingsChanged){
        mOnSettingsChanged = onSettingsChanged;
    }

    Integer getKeyCode(String keyString){
        return KeyEvent.keyCodeFromString("KEYCODE_"+keyString);
    }

    String getKeyName(Integer keyCode){
        return KeyEvent.keyCodeToString(keyCode).replace("KEYCODE_","");
    }

    int getPosition(String value, ArrayAdapter<CharSequence> arrayAdapter){
        try {
            return arrayAdapter.getPosition(value);
        } catch (Exception e){
            Log.d(TAG, Log.getStackTraceString(e));
            return 0;
        }
    }
}
