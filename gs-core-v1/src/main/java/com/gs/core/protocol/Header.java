package com.gs.core.protocol;



import com.gs.common.util.ByteConvertUtils;

import java.util.BitSet;

/**
 * Created by linjuntan on 2017/11/5.
 * email: ljt1343@gmail.com
 */
public class Header {
    /*

     byte[12] head {
     bit[16] magic;//魔数 2字节
     bit[32] sessionId;// 会话id 21位，范围为0~2097152
     bit[3] control;//业务控制位，1为心跳，2为中断，3未回包，剩余业务自定义
     bit[13] payload;//冗余拓展字段 1字节
     bit[32] length;//长度 4字节
     }

     至此数据包长度则为1462字节。

     //sessionID 实现为 int => byte => java.util.BitSet 去除前3位
     //control 实现为 int => byte => 去除前29位
     */
    private short magic = 0xff;

    private int sessionId;

    private int control;

    private int payload;

    private int length;

    public Header() {
    }

    public Header(int sessionId, int control, int payload, int length) {
        this.sessionId = sessionId;
        this.control = control;
        this.payload = payload;
        this.length = length;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getControl() {
        return control;
    }

    public void setControl(int control) {
        this.control = control;
    }

    public int getPayload() {
        return payload;
    }

    public void setPayload(int payload) {
        this.payload = payload;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    //sessionId to bit,control to bit,payload to bit,length to bit
    private BitSet sessionIdToBitSet() {
        BitSet original = ByteConvertUtils.intToBitSet(sessionId);

//        BitSet sessionBit = new BitSet();
//        for (int i = 11; i < 32; i++) {
//            if (original.get(i)) {
//                sessionBit.set(i);
//            }
//        }
//
//        return sessionBit;
        return original;
    }

    private BitSet bitSetToSessionIdBit(BitSet middleBit) {
        BitSet sessionBit = new BitSet();

//        int index = 11;
//        for (int i = 0; i < 21; i++) {
//            if (middleBit.get(i)) {
//                sessionBit.set(index);
//            }
//
//            index ++;
//        }
        int index = 0;
        for (int i = 0; i < 32; i++) {
            if (middleBit.get(i)) {
                sessionBit.set(index);
            }

            index ++;
        }
        return sessionBit;
    }

    private BitSet controlToBitSet() {
        BitSet original = ByteConvertUtils.intToBitSet(control);

        BitSet controlBit = new BitSet();
        for (int i = 29; i< 32; i++) {
            if (original.get(i)) {
                controlBit.set(i);
            }
        }

        return controlBit;
    }

    private BitSet bitSetToControlBit(BitSet middleBit) {
        BitSet controlBit = new BitSet();

//        int index = 29;
//        for (int i = 21; i < 24; i++) {
//            if (middleBit.get(i)) {
//                controlBit.set(index);
//            }
//
//            index ++;
//        }
        int index = 29;
        for (int i = 32; i < 35; i++) {
            if (middleBit.get(i)) {
                controlBit.set(index);
            }

            index ++;
        }

        return controlBit;
    }

    private BitSet payloadToBitSet() {
        BitSet original = ByteConvertUtils.intToBitSet(payload);

//        BitSet payloadBit = new BitSet();
//        for (int i = 24; i < 32; i++) {
//            if (original.get(i)) {
//                payloadBit.set(i);
//            }
//        }
        BitSet payloadBit = new BitSet();
        for (int i = 19; i < 32; i++) {
            if (original.get(i)) {
                payloadBit.set(i);
            }
        }

        return payloadBit;
    }

    private BitSet bitSetToPayloadBit(BitSet middleBit) {
        BitSet payloadBit = new BitSet();

//        int index = 24;
//        for (int i = 24; i < 32; i++) {
//            if (middleBit.get(i)) {
//                payloadBit.set(index);
//            }
//
//            index ++;
//        }
        int index = 19;
        for (int i = 35; i < 48; i++) {
            if (middleBit.get(i)) {
                payloadBit.set(index);
            }

            index ++;
        }

        return payloadBit;
    }

    private byte[] mergeBitSet() {
        BitSet sessionBit = sessionIdToBitSet();
        BitSet controlBit = controlToBitSet();
        BitSet payloadBit = payloadToBitSet();

        BitSet bits = new BitSet();
        int index = 0;

//        //add sessionId
//        for (int i = 11; i< 32; i++) {
//            if (sessionBit.get(i)) {
//                bits.set(index);
//            }
//            index ++;
//        }
//
//        //add control
//        for (int i = 29; i < 32; i++) {
//            if (controlBit.get(i)) {
//                bits.set(index);
//            }
//
//            index ++;
//        }
//
//        //add payload
//        for (int i = 24; i < 32; i++) {
//            if (payloadBit.get(i)) {
//                bits.set(index);
//            }
//
//            index ++;
//        }

        //add sessionId
        for (int i = 0; i< 32; i++) {
            if (sessionBit.get(i)) {
                bits.set(index);
            }
            index ++;
        }

        //add control
        for (int i = 29; i < 32; i++) {
            if (controlBit.get(i)) {
                bits.set(index);
            }

            index ++;
        }

        //add payload
        for (int i = 19; i < 32; i++) {
            if (payloadBit.get(i)) {
                bits.set(index);
            }

            index ++;
        }

        return ByteConvertUtils.bitSetToByte(bits);
    }

    private void decodeMiddleBitSet(BitSet middleBit) {
        BitSet sessionIdBit = bitSetToSessionIdBit(middleBit);
        BitSet controlBit = bitSetToControlBit(middleBit);
        BitSet payloadBit = bitSetToPayloadBit(middleBit);

        sessionId = ByteConvertUtils.BitSetToInt(sessionIdBit);
        control = ByteConvertUtils.BitSetToInt(controlBit);
        payload = ByteConvertUtils.BitSetToInt(payloadBit);
    }

    public byte[] encode() {
        byte[] magicByte = ByteConvertUtils.shortToByteArray(magic);
        byte[] lengthByte = ByteConvertUtils.intToByteArray(length);
        byte[] middleByte = mergeBitSet();

        byte[] head = new byte[12];
        int index = 0;

        //magic
        System.arraycopy(magicByte, 0, head, index, magicByte.length);
        index += magicByte.length;

        //sessionId, control, payload
        System.arraycopy(middleByte, 0, head, index, 6);
        index += 6;

        //length
        System.arraycopy(lengthByte, 0, head, index, lengthByte.length);

        return head;
    }

    public void decode(byte[] head) throws DecodePacketException {
        byte[] magicByte = new byte[2];

        int index = 0;
        System.arraycopy(head, index, magicByte, 0, 2);
        index += 2;

        short tmpMagic = ByteConvertUtils.byteToShort(magicByte);
        if (tmpMagic != magic) {
            throw new DecodePacketException();
        }

        byte[] middleByte = new byte[6];
        System.arraycopy(head, index, middleByte, 0, 6);
        index += 6;
        decodeMiddleBitSet(ByteConvertUtils.byteToBitSet(middleByte));

        byte[] lengthByte = new byte[4];
        System.arraycopy(head, index, lengthByte, 0, 4);
        length = ByteConvertUtils.byteToInt(lengthByte);
    }

}
