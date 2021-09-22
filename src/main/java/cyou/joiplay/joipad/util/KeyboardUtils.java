package cyou.joiplay.joipad.util;

import android.view.KeyEvent;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class KeyboardUtils {
    public static List<Map<Integer, String>> getKeyboardLayout(){
        List<Map<Integer,String>> keyList = new LinkedList<>();

        Map<Integer,String> firstRow = new LinkedHashMap<>();
        firstRow.put(KeyEvent.KEYCODE_1, "1");
        firstRow.put(KeyEvent.KEYCODE_2, "2");
        firstRow.put(KeyEvent.KEYCODE_3, "3");
        firstRow.put(KeyEvent.KEYCODE_4, "4");
        firstRow.put(KeyEvent.KEYCODE_5, "5");
        firstRow.put(KeyEvent.KEYCODE_6, "6");
        firstRow.put(KeyEvent.KEYCODE_7, "7");
        firstRow.put(KeyEvent.KEYCODE_8, "8");
        firstRow.put(KeyEvent.KEYCODE_9, "9");
        firstRow.put(KeyEvent.KEYCODE_0, "0");

        Map<Integer,String> secondRow = new LinkedHashMap<>();
        secondRow.put(KeyEvent.KEYCODE_Q, "Q");
        secondRow.put(KeyEvent.KEYCODE_W, "W");
        secondRow.put(KeyEvent.KEYCODE_E, "E");
        secondRow.put(KeyEvent.KEYCODE_R, "R");
        secondRow.put(KeyEvent.KEYCODE_T, "T");
        secondRow.put(KeyEvent.KEYCODE_Y, "Y");
        secondRow.put(KeyEvent.KEYCODE_U, "U");
        secondRow.put(KeyEvent.KEYCODE_I, "I");
        secondRow.put(KeyEvent.KEYCODE_O, "O");
        secondRow.put(KeyEvent.KEYCODE_P, "P");
        
        Map<Integer,String> thirdRow = new LinkedHashMap<>();
        thirdRow.put(KeyEvent.KEYCODE_A, "A");
        thirdRow.put(KeyEvent.KEYCODE_S, "S");
        thirdRow.put(KeyEvent.KEYCODE_D, "D");
        thirdRow.put(KeyEvent.KEYCODE_F, "F");
        thirdRow.put(KeyEvent.KEYCODE_G, "G");
        thirdRow.put(KeyEvent.KEYCODE_H, "H");
        thirdRow.put(KeyEvent.KEYCODE_J, "J");
        thirdRow.put(KeyEvent.KEYCODE_K, "K");
        thirdRow.put(KeyEvent.KEYCODE_L, "L");
        thirdRow.put(KeyEvent.KEYCODE_DPAD_UP, "↑");
        thirdRow.put(KeyEvent.KEYCODE_DEL, "⌫");

        Map<Integer,String> fourthRow = new LinkedHashMap<>();
        fourthRow.put(KeyEvent.KEYCODE_Z, "Z");
        fourthRow.put(KeyEvent.KEYCODE_X, "X");
        fourthRow.put(KeyEvent.KEYCODE_C, "C");
        fourthRow.put(KeyEvent.KEYCODE_V, "V");
        fourthRow.put(KeyEvent.KEYCODE_B, "B");
        fourthRow.put(KeyEvent.KEYCODE_N, "N");
        fourthRow.put(KeyEvent.KEYCODE_M, "M");
        fourthRow.put(KeyEvent.KEYCODE_DPAD_LEFT, "←");
        fourthRow.put(KeyEvent.KEYCODE_DPAD_DOWN, "↓");
        fourthRow.put(KeyEvent.KEYCODE_DPAD_RIGHT, "→");

        Map<Integer,String> fifthRow = new LinkedHashMap<>();
        fifthRow.put(KeyEvent.KEYCODE_SHIFT_LEFT, "⇧");
        fifthRow.put(KeyEvent.KEYCODE_SPACE, " Space ");
        fifthRow.put(KeyEvent.KEYCODE_ENTER, "⏎");

        keyList.add(firstRow);
        keyList.add(secondRow);
        keyList.add(thirdRow);
        keyList.add(fourthRow);
        keyList.add(fifthRow);

        return keyList;
    }
}
