package com.loop.utils;


/**
 * @author Kinovi
 * @version $
 * @des
 * @updateAuthor $
 * @updateDes
 */
public class ByteArrayConvertUtil {


    @SuppressWarnings("DefaultLocale")
    public static String convertHexString(byte... data) {
        StringBuilder builder = new StringBuilder();
        for (byte datum : data) {
            builder.append(String.format("%02d", datum));
        }
        return builder.toString();

    }

    /**
     * @param MSB_First true 高位在前，低位在后
     * @param data
     * @return
     */
    public static int convertInt(boolean MSB_First, byte... data) {
        if (MSB_First) {
            return bytesToInt2(data, 0);
        } else {
            return bytesToInt(data, 0);
        }
    }


    /**
     * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序，和和intToBytes（）配套使用
     *
     * @param src    byte数组
     * @param offset 从数组的第offset位开始
     * @return int数值
     */
    public static int bytesToInt(byte[] src, int offset) {
//        int value;
//        value = (int) ((src[offset] & 0xFF)
//                | ((src[offset + 1] & 0xFF) << 8)
//                | ((src[offset + 2] & 0xFF) << 16)
//                | ((src[offset + 3] & 0xFF) << 24));

        int minLen = Math.min(4, src.length);
        int value = 0;
        for (int i = 0; i < minLen; i++) {
            value |= (src[i] & 0xFF) << (i * 8);
        }

        return value;
    }


    /**
     * byte数组中取int数值，本方法适用于(低位在后，高位在前)的顺序。和intToBytes2（）配套使用
     */
    public static int bytesToInt2(byte[] src, int offset) {
        int minLen = Math.min(4, src.length);
        int value = 0;
        for (int i = 0; i < minLen; i++) {
//            value |= (src[i] & 0xFF) << ((3 - i) * 8);
            value |= (src[i] & 0xFF) << ((minLen - 1 - i) * 8);
        }
//        value = (int) (((src[offset] & 0xFF) << 24)
//                | ((src[offset + 1] & 0xFF) << 16)
//                | ((src[offset + 2] & 0xFF) << 8)
//                | (src[offset + 3] & 0xFF));
        return value;
    }

    /**
     * 字节数据转Long
     *
     * @param MSB_First true 高位在前，低位在后
     * @param src
     * @return
     */
    public static long convertToLong(byte[] src, boolean MSB_First) {
        int minLen = Math.min(8, src.length);
        int value = 0;
        for (int i = 0; i < minLen; i++) {
//            value |= (src[i] & 0xFF) << ((3 - i) * 8);
            if (MSB_First) {//高位在前，低位在后
                value |= (src[i] & 0xFF) << ((minLen - 1 - i) * 8);
            } else {//低位在前，高位在后
                value |= (src[i] & 0xFF) << (i * 8);
            }

        }
//        value = (int) (((src[offset] & 0xFF) << 24)
//                | ((src[offset + 1] & 0xFF) << 16)
//                | ((src[offset + 2] & 0xFF) << 8)
//                | (src[offset + 3] & 0xFF));
        return value;
    }

    public static byte convertToByte(byte[] src) {
        if (src.length < 1) {
            return 0;
        }
        return src[0];
    }

    public static Object convert(String fieldType, byte... value) {
        if ("java.lang.String".equalsIgnoreCase(fieldType)) {
            return convertHexString(value);
        } else if ("int".equalsIgnoreCase(fieldType)) {
            return convertInt(true, value);
        } else if ("long".equalsIgnoreCase(fieldType)) {
            return convertToLong(value, true);
        } else if ("byte".equalsIgnoreCase(fieldType)) {
            return convertToByte(value);
        } else {
            return value;
        }
    }
}
