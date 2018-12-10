package org.satran.blockchain.graphql.impl.aion.util;

import java.io.UnsupportedEncodingException;
import org.aion.api.sol.IInt;
import org.aion.api.sol.IUint;
import org.aion.base.util.ByteArrayWrapper;
import org.aion.base.util.Hex;
import org.satran.blockchain.graphql.exception.DataConversionException;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;
import org.spongycastle.util.encoders.Base64;

public class TypeUtil {

    public static byte[] toBytes(Object obj, String encoding) {
        if(obj == null)
            return null;

        if(obj instanceof byte[])
            return (byte[])obj;
        else {
            return _toBytes(String.valueOf(obj), encoding);
        }
    }

    private static byte[] _toBytes(String str, String encoding) {
        if(str == null) return null;
        else {
            if(encoding == null)
                return str.getBytes();
            else {
                try {
                    if(encoding.equalsIgnoreCase("hex")) {
                        return Hex.decode(str);
                    } else if(encoding.equalsIgnoreCase("base64")) {
                        return Base64.decode(str);
                    }
                    return str.getBytes(encoding);
                } catch (UnsupportedEncodingException e) {
                    throw new DataConversionException("Unable to convert bytes with encoding : " + encoding);
                }
            }
        }
    }

    public static String toString(byte[] bytes) {
        return ByteArrayWrapper.wrap(bytes).toString();
    }

    public static ByteArrayWrapper toByteArrayWrapper(String hexStr) {
        if(hexStr == null)
            return null;
        else {
            if(hexStr.startsWith("0x") || hexStr.startsWith("0X"))
                hexStr = hexStr.substring(2);

            return ByteArrayWrapper.wrap(Hex.decode(hexStr));
        }
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

    public static IUint toIUint(Object value) {

        if (value instanceof Integer)
            return IUint.copyFrom((Integer) value);
        else if (value instanceof Long) {
            return IUint.copyFrom((Long) value);
        } else if (value instanceof String) {
            return IUint.copyFrom(String.valueOf(value));
        } else if(value instanceof BigInteger) {
            return IUint.copyFrom(((BigInteger)value).longValue());
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

    public static IUint toIUint(List<Object> list) {
        if(list == null)
            return null;

        List intList = list.stream().map(v -> toIUint(v))
            .collect(Collectors.toList());

        return IUint.copyFrom(intList);
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

    public static List<byte[]> toBytesList(List<Object> list, String encoding) {
        if(list == null)
            return null;

        return list.stream().map(v -> toBytes(v, encoding))
                .collect(Collectors.toList());
    }

    public static List<Integer> toIntList(List<Object> list) {
        if(list == null)
            return null;

        return list.stream().map(v -> Integer.valueOf(String.valueOf(v)))
                .collect(Collectors.toList());
    }


}
