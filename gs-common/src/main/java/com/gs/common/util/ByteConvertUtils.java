package com.gs.common.util;

import java.util.BitSet;

/**
 * Created by linjuntan on 2017/11/12.
 * email: ljt1343@gmail.com
 */
public class ByteConvertUtils {
    public static byte[] shortToByteArray(short s) {
        byte[] res = new byte[2];
        res[1] = (byte)(s & 0xff);
        res[0] = (byte)((s >> 8) & 0xff);
        return res;
    }

    public static short byteToShort(byte[] b) {
        short s = 0;
        short s0 = (short) (b[1] & 0xff);// 最低位
        short s1 = (short) (b[0] & 0xff);
        s1 <<= 8;
        s = (short) (s0 | s1);
        return s;
    }

    public static byte[] intToByteArray(int res) {
        byte[] targets = new byte[4];
        targets[3] = (byte) (res & 0xff);
        targets[2] = (byte) ((res >> 8) & 0xff);
        targets[1] = (byte) ((res >> 16) & 0xff);
        targets[0] = (byte) (res >>> 24);
        return targets;
    }

    public static int byteToInt(byte[] b) {
        int s = 0;
        int s0 = b[3] & 0xff;
        int s1 = b[2] & 0xff;
        int s2 = b[1] & 0xff;
        int s3 = b[0] & 0xff;
        s3 <<= 24;
        s2 <<= 16;
        s1 <<= 8;
        s = s0 | s1 | s2 | s3;
        return s;
    }

    public static byte[] bitSetToByte(BitSet bitSet) {
        byte[] bytes = new byte[bitSet.size() / 8];
        for (int i = 0; i < bitSet.size(); i++) {
            int index = i / 8;
            int offset = 7 - i % 8;
            bytes[index] |= (bitSet.get(i) ? 1 : 0) << offset;
        }
        return bytes;
    }

    public static BitSet byteToBitSet(byte[] bytes) {
        BitSet bitSet = new BitSet(bytes.length * 8);
        int index = 0;
        for (int i = 0; i < bytes.length; i++) {
            for (int j = 7; j >= 0; j--) {
                bitSet.set(index++, (bytes[i] & (1 << j)) >> j == 1);
            }
        }
        return bitSet;
    }

    public static BitSet shortToBitSet(short val) {
        return byteToBitSet(shortToByteArray(val));
    }

    public static short BitSetToShort(BitSet val) {
        return byteToShort(bitSetToByte(val));
    }

    public static BitSet intToBitSet(int val) {
        return byteToBitSet(intToByteArray(val));
    }

    public static int BitSetToInt(BitSet val) {
        return byteToInt(bitSetToByte(val));
    }

}
