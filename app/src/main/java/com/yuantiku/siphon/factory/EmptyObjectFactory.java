package com.yuantiku.siphon.factory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by wanghb on 15/9/3.
 */
public class EmptyObjectFactory {
    public static <T> T createEmptyObject(Class<T> clazz) {
        T t = (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] {
                clazz
        },
                createEmptyInvocationHandler());
        return t;
    }

    public static <T> T ensureObject(T object, Class<T> clazz) {
        if (object == null) {
            object = createEmptyObject(clazz);
        }
        return object;
    }

    private static InvocationHandler createEmptyInvocationHandler() {
        InvocationHandler invocationHandler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (method == null) {
                    return null;
                }
                final Class<?> returns = method.getReturnType();
                if (returns.isPrimitive()) {
                    if (returns.equals(int.class)) {
                        return 0;
                    } else if (returns.equals(short.class)) {
                        return (short) 0;
                    } else if (returns.equals(long.class)) {
                        return 0l;
                    } else if (returns.equals(double.class)) {
                        return 0.0d;
                    } else if (returns.equals(float.class)) {
                        return 0f;
                    } else if (returns.equals(boolean.class)) {
                        return false;
                    } else if (returns.equals(byte.class)) {
                        return (byte) 0;
                    } else if (returns.equals(char.class)) {
                        return (char) 0;
                    } else {
                        return 0;
                    }

                }
                return null;
            }
        };
        return invocationHandler;
    }
}
