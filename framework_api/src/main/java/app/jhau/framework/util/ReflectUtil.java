package app.jhau.framework.util;

import androidx.annotation.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectUtil {

    public static Object invokeStaticMethod(Class<?> cls, String methodName, Object... args) throws Throwable {
        return invokeMethod(cls, methodName, null, args);
    }

    public static Object invokeMethod(Class<?> cls, String methodName, @Nullable Object obj, @Nullable Object... args) throws Throwable {
        Class<?>[] argTypes = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            argTypes[i] = args[i].getClass();
        }
        Method method = cls.getDeclaredMethod(methodName, argTypes);
        method.setAccessible(true);
        return method.invoke(obj, args);
    }

    public static <T> T getField(Class<?> cls, @Nullable Object obj, String fieldName) {
        try {
            Field field = cls.getDeclaredField(fieldName);
            return getField(field, obj);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getField(Field field, @Nullable Object obj) {
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
