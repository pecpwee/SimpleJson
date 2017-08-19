package com.pecpwee.lib.simplejson;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * Created by pw on 2017/7/31.
 */

class Utils {

    public static Object createInstance(Class clazz) {
        Constructor constructor = null;
        try {
            constructor = clazz.getDeclaredConstructor(new Class[]{});
        } catch (NoSuchMethodException e) {
            return allocWithUnsafeClass(clazz);
        }
        if (constructor != null) {
            try {
                constructor.setAccessible(true);
                return constructor.newInstance(new Object[]{});
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static Method UnsafeAllocateMethod = null;
    private static Object UnsafeObj = null;

    //alloc Object without calling constructor method
    private static Object allocWithUnsafeClass(Class clazz) {
        if (clazz == null || clazz.isInterface()) {
            throw new IllegalArgumentException("illegal argument for unsafe allocation");
        }

        try {
            if (UnsafeAllocateMethod == null || UnsafeObj == null) {
                Class UnsafeClass = Class.forName("sun.misc.Unsafe");
                final Field field = UnsafeClass.getDeclaredField("theUnsafe");//get the instance
                field.setAccessible(true);
                UnsafeObj = field.get(null);
                UnsafeAllocateMethod = UnsafeObj.getClass().getDeclaredMethod("allocateInstance", new Class[]{Class.class});
            }
            return UnsafeAllocateMethod.invoke(UnsafeObj, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isListObject(Class type) {
        Class[] interfaces = type.getInterfaces();
        for (Class clazz : interfaces) {
            if (clazz == List.class) {
                return true;
            }
        }
        Class superClass = type.getSuperclass();
        if (superClass != null) {
            return isListObject(superClass);
        }
        return false;
    }

    public static Class getListComponentType(Field field) {
        ParameterizedType integerListType = (ParameterizedType) field.getGenericType();
        Class<?> integerListClass = (Class<?>) integerListType.getActualTypeArguments()[0];
        return integerListClass;
    }
}
