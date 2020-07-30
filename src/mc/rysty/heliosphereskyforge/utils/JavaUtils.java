package mc.rysty.heliosphereskyforge.utils;

import java.util.HashMap;

public class JavaUtils {

    public static <K, V> K getHashMapKey(HashMap<K, V> map, V value) {
        for (HashMap.Entry<K, V> entry : map.entrySet())
            if (value.equals(entry.getValue()))
                return entry.getKey();
        return null;
    }

    /*
     * This method only converts integers one through ten to Roman Numerals,
     * otherwise it will return the number in base ten.
     */
    public static String getRomanNumeral(int baseTenNumber) {
        String romanNumeral = "" + baseTenNumber;

        if (baseTenNumber == 1)
            romanNumeral = "I";
        else if (baseTenNumber == 2)
            romanNumeral = "II";
        else if (baseTenNumber == 3)
            romanNumeral = "III";
        else if (baseTenNumber == 4)
            romanNumeral = "IV";
        else if (baseTenNumber == 5)
            romanNumeral = "V";
        else if (baseTenNumber == 6)
            romanNumeral = "VI";
        else if (baseTenNumber == 7)
            romanNumeral = "VII";
        else if (baseTenNumber == 8)
            romanNumeral = "VIII";
        else if (baseTenNumber == 9)
            romanNumeral = "IX";
        else if (baseTenNumber == 10)
            romanNumeral = "X";

        return romanNumeral;
    }
}