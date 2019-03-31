package me.shouheng.service.base;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author shouh, 2019/3/31-17:59
 */
public class MockTestUtil {

    private static double curD = 100.1;

    private static float curF = 10000.2f;

    private static int curInt = 9000005;

    private static long curL = 1000;

    private static int curS = 1;

    private static String[] rs = new String[] { "A", "C", "D", "E", "F", "J", "H", "I", "K", "L", "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "c", "d", "e", "f", "j", "h", "i", "k", "l", "m", "n", "o", "p", "q", "r",
            "s", "t", "u", "v", "w", "x", "y", "z", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "f" };

    private static String getFieldName(String methodName) {
        String s = "set";
        String ups = methodName.toLowerCase();
        String key = null;
        if (ups.contains(s)) {
            key = ups.substring(s.length());
        }
        return key;
    }

    public static <T> T getJavaBean(Class<T> c) {
        return getJavaBean(c, true);
    }

    public static <T> T getJavaBean(Class<T> c, boolean ignoreId) {
        T object = null;
        List<Method> allMethods = new ArrayList<>();
        List<Field> allFields = new ArrayList<>();
        try {
            object = c.newInstance();
            Method[] methods = c.getDeclaredMethods();
            Field[] fields = c.getDeclaredFields();
            allMethods.addAll(Arrays.asList(methods));
            allFields.addAll(Arrays.asList(fields));
            Class superClass = c.getSuperclass();
            while (superClass != null) {
                allMethods.addAll(Arrays.asList(superClass.getDeclaredMethods()));
                allFields.addAll(Arrays.asList(superClass.getDeclaredFields()));
                superClass = superClass.getSuperclass();
            }
            for (Method m : allMethods) {
                if (m.getName().contains("set")) {
                    String field = getFieldName(m.getName());
                    if (ignoreId && field.equals("id")) {
                        continue;
                    }
                    String type = "string";
                    for (Field f : allFields) {
                        if (f.getName().toLowerCase().equals(field)) {
                            type = f.getType().getSimpleName();
                            break;
                        }
                    }
                    m.invoke(object, getValue(type));
                }
            }

        } catch (Exception e) {
            // e.printStackTrace();
        }
        return object;
    }

    private static String getRand(int size) {
        Random random = new Random();
        StringBuilder rvs = new StringBuilder();
        for (int i = 0; i < size; i++) {
            int status = random.nextInt(size);
            if (status < rs.length && status > 0) {
                rvs.append(rs[status]);
            } else {
                rvs.append("A");
            }

        }
        return rvs.toString();
    }

    private static Object getValue(String s) {
        Object temp = null;
        String st = s.toLowerCase();
        Random random = new Random(10010);
        switch (st) {
            case "int":
            case "integer":
                temp = curInt;
                curInt++;
                break;
            case "long":
                temp = curL;
                curL++;
                break;
            case "string":
                temp = curS + getRand(6);
                curS++;
                break;
            case "double":
                temp = curD;
                curD++;
                break;
            case "float":
                temp = curF;
                curF++;
                break;
            case "boolean":
                temp = random.nextBoolean();
                break;
            case "date":
                temp = new Date();
                break;
        }
        return (temp);
    }
}
