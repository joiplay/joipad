package cyou.joiplay.joipad.util;

import android.view.KeyEvent;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class KeyboardUtils {
    public static List<Map<Integer, String>> getKeyboardLayoutSpecial() {
        List<Map<Integer,String>> keyList = new LinkedList<>();

        Map<Integer,String> specialFirstRow = new LinkedHashMap<>();
        specialFirstRow.put(KeyEvent.KEYCODE_1, "1");
        specialFirstRow.put(KeyEvent.KEYCODE_2, "2");
        specialFirstRow.put(KeyEvent.KEYCODE_3, "3");
        specialFirstRow.put(KeyEvent.KEYCODE_4, "4");
        specialFirstRow.put(KeyEvent.KEYCODE_5, "5");
        specialFirstRow.put(KeyEvent.KEYCODE_6, "6");
        specialFirstRow.put(KeyEvent.KEYCODE_7, "7");
        specialFirstRow.put(KeyEvent.KEYCODE_8, "8");
        specialFirstRow.put(KeyEvent.KEYCODE_9, "9");
        specialFirstRow.put(KeyEvent.KEYCODE_0, "0");

        Map<Integer,String> specialSecondRow = new LinkedHashMap<>();
        specialSecondRow.put(KeyEvent.KEYCODE_AT, "@");
        specialSecondRow.put(KeyEvent.KEYCODE_POUND, "#");
        specialSecondRow.put(KeyEvent.KEYCODE_MINUS, "-");
        specialSecondRow.put(KeyEvent.KEYCODE_NUMPAD_ADD, "+");
        specialSecondRow.put(KeyEvent.KEYCODE_NUMPAD_MULTIPLY, "*");
        specialSecondRow.put(KeyEvent.KEYCODE_NUMPAD_DIVIDE, "/");
        specialSecondRow.put(KeyEvent.KEYCODE_EQUALS, "=");
        specialSecondRow.put(KeyEvent.KEYCODE_NUMPAD_LEFT_PAREN, "(");
        specialSecondRow.put(KeyEvent.KEYCODE_NUMPAD_RIGHT_PAREN, ")");
        specialSecondRow.put(KeyEvent.KEYCODE_BACKSLASH, "\\");

        Map<Integer,String> specialThirdRow = new LinkedHashMap<>();
        specialThirdRow.put(KeyEvent.KEYCODE_SWITCH_CHARSET, "ABC");
        specialThirdRow.put(KeyEvent.KEYCODE_GRAVE, "`");
        specialThirdRow.put(KeyEvent.KEYCODE_APOSTROPHE, "'");
        specialThirdRow.put(KeyEvent.KEYCODE_PERIOD, ".");
        specialThirdRow.put(KeyEvent.KEYCODE_COMMA, ",");
        specialThirdRow.put(KeyEvent.KEYCODE_SEMICOLON, ";");
        specialThirdRow.put(KeyEvent.KEYCODE_LEFT_BRACKET, "[");
        specialThirdRow.put(KeyEvent.KEYCODE_RIGHT_BRACKET, "]");
        specialThirdRow.put(KeyEvent.KEYCODE_DEL, "⌫");

        Map<Integer,String> specialFourthRow = new LinkedHashMap<>();
        specialFourthRow.put(KeyEvent.KEYCODE_SHIFT_LEFT, "⇧");
        specialFourthRow.put(KeyEvent.KEYCODE_SPACE, " Space ");
        specialFourthRow.put(KeyEvent.KEYCODE_ENTER, "⏎");

        keyList.add(specialFirstRow);
        keyList.add(specialSecondRow);
        keyList.add(specialThirdRow);
        keyList.add(specialFourthRow);

        return keyList;
    }

    public static List<Map<Integer, String>> getKeyboardLayout(){
        List<Map<Integer,String>> keyList = new LinkedList<>();

        Map<Integer,String> firstRow = new LinkedHashMap<>();
        firstRow.put(KeyEvent.KEYCODE_Q, "Q");
        firstRow.put(KeyEvent.KEYCODE_W, "W");
        firstRow.put(KeyEvent.KEYCODE_E, "E");
        firstRow.put(KeyEvent.KEYCODE_R, "R");
        firstRow.put(KeyEvent.KEYCODE_T, "T");
        firstRow.put(KeyEvent.KEYCODE_Y, "Y");
        firstRow.put(KeyEvent.KEYCODE_U, "U");
        firstRow.put(KeyEvent.KEYCODE_I, "I");
        firstRow.put(KeyEvent.KEYCODE_O, "O");
        firstRow.put(KeyEvent.KEYCODE_P, "P");
        
        Map<Integer,String> secondRow = new LinkedHashMap<>();
        secondRow.put(KeyEvent.KEYCODE_A, "A");
        secondRow.put(KeyEvent.KEYCODE_S, "S");
        secondRow.put(KeyEvent.KEYCODE_D, "D");
        secondRow.put(KeyEvent.KEYCODE_F, "F");
        secondRow.put(KeyEvent.KEYCODE_G, "G");
        secondRow.put(KeyEvent.KEYCODE_H, "H");
        secondRow.put(KeyEvent.KEYCODE_J, "J");
        secondRow.put(KeyEvent.KEYCODE_K, "K");
        secondRow.put(KeyEvent.KEYCODE_L, "L");
        secondRow.put(KeyEvent.KEYCODE_DPAD_UP, "↑");
        secondRow.put(KeyEvent.KEYCODE_DEL, "⌫");

        Map<Integer,String> thirdRow = new LinkedHashMap<>();
        thirdRow.put(KeyEvent.KEYCODE_SWITCH_CHARSET, "123");
        thirdRow.put(KeyEvent.KEYCODE_Z, "Z");
        thirdRow.put(KeyEvent.KEYCODE_X, "X");
        thirdRow.put(KeyEvent.KEYCODE_C, "C");
        thirdRow.put(KeyEvent.KEYCODE_V, "V");
        thirdRow.put(KeyEvent.KEYCODE_B, "B");
        thirdRow.put(KeyEvent.KEYCODE_N, "N");
        thirdRow.put(KeyEvent.KEYCODE_M, "M");
        thirdRow.put(KeyEvent.KEYCODE_DPAD_LEFT, "←");
        thirdRow.put(KeyEvent.KEYCODE_DPAD_DOWN, "↓");
        thirdRow.put(KeyEvent.KEYCODE_DPAD_RIGHT, "→");

        Map<Integer,String> fourthRow = new LinkedHashMap<>();
        fourthRow.put(KeyEvent.KEYCODE_SHIFT_LEFT, "⇧");
        fourthRow.put(KeyEvent.KEYCODE_SPACE, " Space ");
        fourthRow.put(KeyEvent.KEYCODE_ENTER, "⏎");

        keyList.add(firstRow);
        keyList.add(secondRow);
        keyList.add(thirdRow);
        keyList.add(fourthRow);

        return keyList;
    }
}
