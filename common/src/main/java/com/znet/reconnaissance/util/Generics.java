package com.znet.reconnaissance.util;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

public class Generics {

    private Generics() {
        super();
    }

    @SuppressWarnings("rawtypes")
    public static Class<?> getRawType(ParameterizedType type) {

        // get the raw object associated with the template
        Type rawtype = type.getRawType();

        // ensure valid type
        if (rawtype instanceof Class) {
            return (Class) rawtype;
        }

        // raw type should only be classes
        else {
            throw new IllegalStateException
            (
                "ParameterizedType cannot have a non-Class " +
                "based rawType: " + rawtype
            );
        }
    }

    public static Class<?> getBoundedType(TypeVariable<?> type) {

        // fail if missing bounds
        if (type.getBounds().length == 0) {
            throw new IllegalStateException
            (
                "TypeVariable has missing bounds information: " + type
            );
        }

        // get the bounds around the type to resolve to actual type
        Type ttype = type.getBounds()[0];
        return getUnderlyingType(ttype);
    }

    public static Type getBoundedType(WildcardType type) {

        // fail if missing bounds
        if (type.getUpperBounds().length == 0) {
            throw new IllegalStateException
            (
                "WildcardType has missing bounds information: " + type
            );
        }

        // get the bounds around the type to resolve to actual type
        Type ttype = type.getUpperBounds()[0];
        return getUnderlyingType(ttype);
    }

    @SuppressWarnings("rawtypes")
    public static Class<?> getUnderlyingType(Type ttype) {

        // handle <? extends Object> case
        if (ttype instanceof Class) {
            return (Class) ttype;
        }

        // handle <? extends E> case
        else if (ttype instanceof TypeVariable) {

            // get underlying bounded type
            return getBoundedType((TypeVariable) ttype);
        }

        // handle <? extends Template<Object>> case
        else if (ttype instanceof ParameterizedType) {

            // get the underlying parameterized type
            return getRawType((ParameterizedType) ttype);
        }

        // handle <? extends Template<Object>[]> case
        else if (ttype instanceof GenericArrayType) {

            // find root type
            return getComponentType((GenericArrayType) ttype);
        }

        // handle <? extends E> case
        else if (ttype instanceof WildcardType) {
            Type[] ubounds = ((WildcardType) ttype).getUpperBounds();
            if (ubounds != null && ubounds.length > 0) {
                return getUnderlyingType(ubounds[0]);
            }

            Type[] lbounds = ((WildcardType) ttype).getLowerBounds();
            if (lbounds != null && lbounds.length > 0) {
                return getUnderlyingType(lbounds[0]);
            }
        }

        // unknown type
        throw new IllegalStateException(
            "type has invalid bounds type: " + ttype
        );
    }

    @SuppressWarnings("rawtypes")
    public static Class<?> getComponentType(GenericArrayType type) {

        // find root type and count dimensions
        int levels = 0;
        Type ctype = type;
        while (ctype instanceof GenericArrayType) {
            levels++;
            ctype = ((GenericArrayType) ctype).getGenericComponentType();
        }

        // handle <E extends Object[]> case
        if (ctype instanceof Class) {
            return Array.newInstance((Class) ctype, new int[levels]).getClass();
        }

        // handle <E extends Template<Object>[]> case
        else if (ctype instanceof ParameterizedType) {

            // get the raw object associated with the template
            Class<?> rawtype = getRawType((ParameterizedType) ctype);
            return Array.newInstance(rawtype, new int[levels]).getClass();
        }

        // handle <E extends F[]> case
        else if (ctype instanceof TypeVariable) {

            // get the bounded underlying type
            Class<?> jtype = getBoundedType((TypeVariable) ctype);
            return Array.newInstance(jtype, new int[levels]).getClass();
        }

        // should not be any other way
        else {
            throw new IllegalStateException
            (
                "GenericArrayType has invalid component " +
                "type: " + ctype
            );
        }
    }

    public static Class<?> getIterationType(Type generic) {
    	return getTypeParameter(generic, 0);
    }

    public static Class<?> getKeyType(Type generic) {
        return getTypeParameter(generic, 0);
    }

    public static Class<?> getValueType(Type generic) {
        return getTypeParameter(generic, 1);
    }
    
    @SuppressWarnings("rawtypes")
    public static Class<?> getTypeParameter(Type generic, int index) {

        // handle parameterized cases (List<E>)
        if (generic instanceof ParameterizedType) {

            // find and return the actual subtype
            Type[] subtypes =
            	((ParameterizedType) generic).getActualTypeArguments();
            if (subtypes != null && subtypes.length > index) {
                Type subtype = subtypes[index];
                return getUnderlyingType(subtype);
            }
        }

        // handle actual classes and look for supercase
        else if (generic instanceof Class) {
        	Type parent = ((Class) generic).getGenericSuperclass();
            return getTypeParameter(parent, index);
        }

        // unknown type, so return object
        return Object.class;
    }
}
