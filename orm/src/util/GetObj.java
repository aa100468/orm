package util;

import java.util.HashMap;

public class GetObj {
    private static HashMap<String,Object> hashMap = new HashMap<>();
    public static <T>T getObj(String className){
        T obj = (T)hashMap.get(className);
        if(obj == null){
            try {
                obj = (T)Class.forName(className).newInstance();
                hashMap.put(className,obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return obj;
    }
}
