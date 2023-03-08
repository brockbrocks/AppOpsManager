package app.jhau.framework.util;

import java.lang.reflect.Field;

public class ReflectUtil {

    public static <T> T getField(Class<?> cls, Object obj, String fieldName) {
        try {
            Field field = cls.getDeclaredField(fieldName);
            return getField(field, obj);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getField(Field field, Object obj) {
        try {
            field.setAccessible(true);
            return (T) field.get(obj);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T getStaticField(Class<?> cls, String fieldName) {
        try {
            Field field = cls.getDeclaredField(fieldName);
            return getStaticField(field);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T getStaticField(Field field) {
        return getField(field, null);
    }
}
