package mc.rysty.heliosphereskyforge.utils;

import java.util.HashMap;

public class JavaUtils {

    public static <K, V> K getHashMapKey(HashMap<K, V> map, V value) {
        for (HashMap.Entry<K, V> entry : map.entrySet())
            if (value.equals(entry.getValue()))
                return entry.getKey();
        return null;
    }
}