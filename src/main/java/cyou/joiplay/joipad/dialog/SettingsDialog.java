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
import cyou.joiplay.joipad.view.KeyCodeSpinner;

public class SettingsDialog{
    private static final String TAG = "SettingsDialog";
    private Context mContext;
    private GamePad mGamePad;
    private Game mGame;
    private Dialog mDialog;

    public interface OnSettingsChanged{
        void onSettingsChanged(GamePad gamePad);
    }

    private OnSettingsChanged mOnSettingsChanged;

    public SettingsDialog(Context context, GamePad gamePad, Game game){
        mContext = context;
        mGamePad = gamePad;
        mGame = game;

        init();
    }

    void init(){
        mDialog = new Dialog(mContext);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setCancelable(true);
        mDialog.setContentView(R.layout.settings_layout);

        Spinner opacitySpinner = mDialog.findViewById(R.id.opacitySpinner);
        Spinner sizeSpinner = mDialog.findViewById(R.id.sizeSpinner);
        KeyCodeSpinner leftSpinner = mDialog.findViewById(R.id.leftSpinner);
        KeyCodeSpinner rightSpinner = mDialog.findViewById(R.id.rightSpinner);
        KeyCodeSpinner cleftSpinner = mDialog.findViewById(R.id.cleftSpinner);
        KeyCodeSpinner crightSpinner = mDialog.findViewById(R.id.crightSpinner);
        KeyCodeSpinner firstSpinner = mDialog.findViewById(R.id.firstSpinner);
        KeyCodeSpinner secondSpinner = mDialog.findViewById(R.id.secondSpinner);
        KeyCodeSpinner thirdSpinner = mDialog.findViewById(R.id.thirdSpinner);
        KeyCodeSpinner fourthSpinner = mDialog.findViewById(R.id.fourthSpinner);
        KeyCodeSpinner fifthSpinner = mDialog.findViewById(R.id.fifthSpinner);
        KeyCodeSpinner sixthSpinner = mDialog.findViewById(R.id.sixthSpinner);
        KeyCodeSpinner leftSpinner1 = mDialog.findViewById(R.id.leftSpinner1);
        KeyCodeSpinner rightSpinner1 = mDialog.findViewById(R.id.rightSpinner1);
        KeyCodeSpinner cleftSpinner1 = mDialog.findViewById(R.id.cleftSpinner1);
        KeyCodeSpinner crightSpinner1 = mDialog.findViewById(R.id.crightSpinner1);
        KeyCodeSpinner firstSpinner1 = mDialog.findViewById(R.id.firstSpinner1);
        KeyCodeSpinner secondSpinner1 = mDialog.findViewById(R.id.secondSpinner1);
        KeyCodeSpinner thirdSpinner1 = mDialog.findViewById(R.id.thirdSpinner1);
        KeyCodeSpinner fourthSpinner1 = mDialog.findViewById(R.id.fourthSpinner1);
        KeyCodeSpinner fifthSpinner1 = mDialog.findViewById(R.id.fifthSpinner1);
        KeyCodeSpinner sixthSpinner1 = mDialog.findViewById(R.id.sixthSpinner1);
        Button saveButton = mDialog.findViewById(R.id.saveButton);

        leftSpinner.setKey(mGamePad.lKeyCode[0]);
        rightSpinner.setKey(mGamePad.rKeyCode[0]);
        cleftSpinner.setKey(mGamePad.clKeyCode[0]);
        crightSpinner.setKey(mGamePad.crKeyCode[0]);
        firstSpinner.setKey(mGamePad.xKeyCode[0]);
        secondSpinner.setKey(mGamePad.yKeyCode[0]);
        thirdSpinner.setKey(mGamePad.zKeyCode[0]);
        fourthSpinner.setKey(mGamePad.aKeyCode[0]);
        fifthSpinner.setKey(mGamePad.bKeyCode[0]);
        sixthSpinner.setKey(mGamePad.cKeyCode[0]);

        leftSpinner1.setKey(mGamePad.lKeyCode[1]);
        rightSpinner1.setKey(mGamePad.rKeyCode[1]);
        cleftSpinner1.setKey(mGamePad.clKeyCode[1]);
        crightSpinner1.setKey(mGamePad.crKeyCode[1]);
        firstSpinner1.setKey(mGamePad.xKeyCode[1]);
        secondSpinner1.setKey(mGamePad.yKeyCode[1]);
        thirdSpinner1.setKey(mGamePad.zKeyCode[1]);
        fourthSpinner1.setKey(mGamePad.aKeyCode[1]);
        fifthSpinner1.setKey(mGamePad.bKeyCode[1]);
        sixthSpinner1.setKey(mGamePad.cKeyCode[1]);

        ArrayAdapter<CharSequence> opacityAdapter = ArrayAdapter.createFromResource(mContext, R.array.opacity_array, R.layout.spinner_item);
        ArrayAdapter<CharSequence> sizeAdapter = ArrayAdapter.createFromResource(mContext, R.array.scale_array, R.layout.spinner_item);

        opacityAdapter.setDropDownViewResource(R.layout.spinner_item);
        sizeAdapter.setDropDownViewResource(R.layout.spinner_item);

        opacitySpinner.setAdapter(opacityAdapter);
        sizeSpinner.setAdapter(sizeAdapter);

        opacitySpinner.setSelection(getPosition(mGamePad.btnOpacity.toString(), opacityAdapter));
        sizeSpinner.setSelection(getPosition(mGamePad.btnScale.toString(), sizeAdapter));

        opacitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    mGamePad.btnOpacity = Integer.parseInt((String) parent.getItemAtPosition(position));
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
                    mGamePad.btnScale = Integer.parseInt((String) parent.getItemAtPosition(position));
                } catch (Exception e){
                    Log.d(TAG, Log.getStackTraceString(e));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        saveButton.setOnClickListener(v -> {
            GamePad gamePad = new GamePad();
            gamePad.btnOpacity = mGamePad.btnOpacity;
            gamePad.btnScale = mGamePad.btnScale;
            gamePad.aKeyCode = new Integer[]{fourthSpinner.getKey(), fourthSpinner1.getKey()};
            gamePad.bKeyCode = new Integer[]{fifthSpinner.getKey(), fifthSpinner1.getKey()};
            gamePad.cKeyCode = new Integer[]{sixthSpinner.getKey(), sixthSpinner1.getKey()};
            gamePad.xKeyCode = new Integer[]{firstSpinner.getKey(), firstSpinner1.getKey()};
            gamePad.yKeyCode = new Integer[]{secondSpinner.getKey(), secondSpinner1.getKey()};
            gamePad.zKeyCode = new Integer[]{thirdSpinner.getKey(), thirdSpinner1.getKey()};
            gamePad.lKeyCode = new Integer[]{leftSpinner.getKey(), leftSpinner1.getKey()};
            gamePad.rKeyCode = new Integer[]{rightSpinner.getKey(), rightSpinner1.getKey()};
            gamePad.clKeyCode = new Integer[]{cleftSpinner.getKey(), cleftSpinner1.getKey()};
            gamePad.crKeyCode = new Integer[]{crightSpinner.getKey(), crightSpinner1.getKey()};

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

    int getPosition(String value, ArrayAdapter<CharSequence> arrayAdapter){
        try {
            return arrayAdapter.getPosition(value);
        } catch (Exception e){
            Log.d(TAG, Log.getStackTraceString(e));
            return 0;
        }
    }
}
