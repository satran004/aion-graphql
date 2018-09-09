package org.satran.blockchain.graphql.impl.aion.util;

import org.aion.api.sol.IInt;
import org.aion.base.util.ByteArrayWrapper;
import org.satran.blockchain.graphql.exception.DataConversionException;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

public class TypeUtil {

    public static byte[] toBytes(String str) {
        if(str == null) return null;
        else
            return str.getBytes();
    }

    public static String toString(byte[] bytes) {
        return ByteArrayWrapper.wrap(bytes).toString();
    }

    public static ByteArrayWrapper toByteArrayWrapper(String str) {
        if(str == null)
            return null;
        else
            return ByteArrayWrapper.wrap(str.getBytes());
    }

    public static Boolean toBoolean(Object value) {
        if(value instanceof Boolean)
            return (Boolean)value;
        else if(value instanceof String)
            return Boolean.valueOf((String)value);
        else
            throw new DataConversionException("Cannot convert the value to Boolean: " + value);
    }

    public static IInt toIInt(Object value) {

        if (value instanceof Integer)
            return IInt.copyFrom((Integer) value);
        else if (value instanceof Long) {
            return IInt.copyFrom((Long) value);
        } else if (value instanceof String) {
            return IInt.copyFrom(String.valueOf(value));
        } else if(value instanceof BigInteger) {
            return IInt.copyFrom(((BigInteger)value).longValue());
        } else
            throw new DataConversionException("Cannot convert the value to Integer: " + value);
    }

    public static IInt toIInt(List<Object> list) {
        if(list == null)
            return null;

        List intList = list.stream().map(v -> toIInt(v))
                .collect(Collectors.toList());

        return IInt.copyFrom(intList);
    }

   /* public static int toInteger(Object value) {
        if(value instanceof Integer)
            return (Integer)value;
        else if(value instanceof Long) {

        } else if(value instanceof String)
            return Integer.valueOf((String)value);
        else
            throw new DataConversionException("Cannot convert the value to Integer: " + value);
        return 0;
    }*/

    public static List<String> toStringList(List<Object> list) {
        if(list == null)
            return null;

        return list.stream().map(v -> String.valueOf(v)).collect(Collectors.toList());
    }

    public static List<Boolean> toBooleanList(List<Object> list) {
        if(list == null)
            return null;

        return list.stream().map(v -> Boolean.valueOf(String.valueOf(v)))
                .collect(Collectors.toList());
    }

    public static List<byte[]> toBytesList(List<Object> list) {
        if(list == null)
            return null;

        return list.stream().map(v -> toBytes(String.valueOf(v)))
                .collect(Collectors.toList());
    }

    public static List<Integer> toIntList(List<Object> list) {
        if(list == null)
            return null;

        return list.stream().map(v -> Integer.valueOf(String.valueOf(v)))
                .collect(Collectors.toList());
    }


}
