package me.jfenn.colorpickerdialog.utils;

import java.lang.reflect.Constructor;

import androidx.annotation.Nullable;

public class DelayedInstantiation<T> {

    private Class<T> tClass;
    private Instantiator<T> instantiator;

    private DelayedInstantiation(Class<T> tClass, Instantiator<T> instantiator) {
        this.tClass = tClass;
        this.instantiator = instantiator;
    }

    @Nullable
    public static <X> DelayedInstantiation<X> from(Class<X> tClass, Class... args) {
        try {
            return new DelayedInstantiation<>(tClass, new ConstructionInstantiator<>(tClass.getConstructor(args)));
        } catch (Exception e) {
            return null;
        }
    }

    public DelayedInstantiation<T> withInstantiator(Instantiator<T> instantiator) {
        this.instantiator = instantiator;
        return this;
    }

    @Nullable
    public T instantiate(Object... args) {
        return instantiator.instantiate(args);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return obj instanceof DelayedInstantiation && equalsClass(((DelayedInstantiation) obj).tClass);
    }

    public boolean equalsClass(@Nullable Class<T> tClass) {
        return this.tClass.equals(tClass);
    }

    public interface Instantiator<T> {
        T instantiate(Object... args);
    }

    public static class ConstructionInstantiator<T> implements Instantiator<T> {

        private Constructor<T> constructor;

        public ConstructionInstantiator(Constructor<T> constructor) {
            this.constructor = constructor;
        }

        @Override
        @Nullable
        public T instantiate(Object... args) {
            try {
                return constructor.newInstance(args);
            } catch (Exception e) {
                return null;
            }
        }

    }
}
