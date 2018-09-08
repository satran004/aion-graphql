package org.satran.blockchain.graphql.impl.aion.util;

import org.aion.base.util.ByteArrayWrapper;

public class StringByteUtil {

    public static byte[] toByte(String str) {
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
}
