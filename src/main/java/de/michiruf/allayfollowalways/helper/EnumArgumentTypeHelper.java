package de.michiruf.allayfollowalways.helper;

import io.wispforest.owo.command.EnumArgumentType;

import java.util.WeakHashMap;

/**
 * @author Michael Ruf
 * @since 2022-12-11
 */
public class EnumArgumentTypeHelper {

    private static final WeakHashMap<Class<?>, EnumArgumentType<?>> argumentTypes = new WeakHashMap<>();

    public static <T extends Enum<T>> EnumArgumentType<T> create(Class<T> enumClass) {
        if (!argumentTypes.containsKey(enumClass))
            argumentTypes.put(enumClass, EnumArgumentType.create(enumClass));

        //noinspection unchecked
        return (EnumArgumentType<T>) argumentTypes.get(enumClass);
    }
}
