package com.delta.core.assembler;

import com.delta.core.assembler.annotation.Detachable;
import com.delta.core.assembler.except.DetachException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Assembler {
    public static <T> T detach(T target, Class<?> implementClazz) throws DetachException {
        if (implementClazz.getInterfaces().length == 0) {
            throw new DetachException(implementClazz + " has no associated interfaces.");
        }
        for (Method method : target.getClass().getMethods()) {
            if (method.getAnnotation(Detachable.class) != null
                    && method.getParameterCount() == 1
                    && method.getParameterTypes()[0] == implementClazz.getInterfaces()[0]) {
                try {
                    method.invoke(target, implementClazz.newInstance());
                } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                    throw new DetachException("something has wrong when inject implements (" + e.getMessage() + ").");
                }
                return target;
            }
        }
        throw new DetachException("nothing detached, only setters are detachable, check if the target is no need to detach or illegal implements provided.");
    }

    public static <T> T detach(T target, Object object) throws DetachException {
        Class<?> implementClazz = object.getClass();
        if (implementClazz.getInterfaces().length == 0) {
            throw new DetachException(implementClazz + " has no associated interfaces.");
        }
        for (Method method : target.getClass().getMethods()) {
            if (method.getAnnotation(Detachable.class) != null
                    && method.getParameterCount() == 1
                    && method.getParameterTypes()[0] == implementClazz.getInterfaces()[0]) {
                try {
                    method.invoke(target, object);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new DetachException("something has wrong when inject implements (" + e.getMessage() + ").");
                }
                return target;
            }
        }
        throw new DetachException("nothing detached for " + target.getClass() + ", only setters are detachable, check if the target is no need to detach or illegal implements provided.");
    }

}
