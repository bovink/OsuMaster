package util;

/**
 * Created by Bovink on 2016/5/8 0008.
 */
public class ByteUtil {
    /**
     * 将int转换成byte数组
     *
     * @param intValue
     * @return
     */
    public static byte[] int2Byte(int intValue) {
        byte[] byteArray = new byte[4];
        for (int i = 0; i < byteArray.length; i++) {
            byteArray[byteArray.length - 1 - i] = (byte) (intValue >> 8 * (byteArray.length - 1 - i) & 0xFF);
        }
        return byteArray;
    }

    /**
     * 将byte数组转换成int
     *
     * @param byteArray
     * @return
     */
    public static int byte2Int(byte[] byteArray) {
        int intValue = 0;
        for (int i = 0; i < byteArray.length; i++) {
            intValue += (byteArray[byteArray.length - 1 - i] & 0xFF) << (8 * (byteArray.length - 1 - i));
        }
        return intValue;
    }
}
