package cyou.joiplay.joipad.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import cyou.joiplay.joipad.R;

public class KeyCodeSpinner extends Spinner {
    private ArrayAdapter<CharSequence> adapter;
    private int keyCode = 0;

    public KeyCodeSpinner(Context context) {
        super(context);
        init(context);
    }

    public KeyCodeSpinner(Context context, int mode) {
        super(context, mode);
        init(context);
    }

    public KeyCodeSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public KeyCodeSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public KeyCodeSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
        init(context);
    }

    public KeyCodeSpinner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, int mode) {
        super(context, attrs, defStyleAttr, defStyleRes, mode);
        init(context);
    }

    @TargetApi(23)
    public KeyCodeSpinner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, int mode, Resources.Theme popupTheme) {
        super(context, attrs, defStyleAttr, defStyleRes, mode, popupTheme);
        init(context);
    }

    private void init(Context context){
        adapter = ArrayAdapter.createFromResource(context, R.array.key_array, R.layout.spinner_item_keycode);
        adapter.setDropDownViewResource(R.layout.spinner_item_keycode);
        setAdapter(adapter);


        setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    keyCode = getKeyCode((String) parent.getItemAtPosition(position));
                } catch (Exception e){/*ignore*/}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
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
            return 0;
        }
    }

    public void setKey(int keyCode){
        this.keyCode = keyCode;
        setSelection(getPosition(getKeyName(keyCode), adapter));
    }

    public int getKey(){
        return this.keyCode;
    }
}
