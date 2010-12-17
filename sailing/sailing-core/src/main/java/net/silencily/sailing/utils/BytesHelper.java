package net.silencily.sailing.utils;

/**
 * 用于<code>UUIDGenerator</code>的一个帮助类, 可能也是来源于<code>Hibernate</code>
 * @author from internet
 * @since 2006-3-28
 * @version $Id: BytesHelper.java,v 1.1 2010/12/10 10:54:21 silencily Exp $
 *
 */
public class BytesHelper {
    private BytesHelper() {
    }

    public static int toInt(byte[] bytes) {
        int result = 0;
        for (int i = 0; i < 4; i++) {
            result = (result << 8) - Byte.MIN_VALUE + bytes[i];
        }
        return result;
    }

    public static short toShort(byte[] bytes) {
        return (short) (((-(short) Byte.MIN_VALUE + bytes[0]) << 8)
            - Byte.MIN_VALUE + bytes[1]);
    }

    public static byte[] toBytes(int value) {
        byte[] result = new byte[4];
        for (int i = 3; i >= 0; i--) {
            result[i] = (byte) ((0xFFl & value) + Byte.MIN_VALUE);
            value >>>= 8;
        }
        return result;
    }

    public static byte[] toBytes(short value) {
        byte[] result = new byte[2];
        for (int i = 1; i >= 0; i--) {
            result[i] = (byte) ((0xFFl & value) + Byte.MIN_VALUE);
            value >>>= 8;
        }
        return result;
    }
}
