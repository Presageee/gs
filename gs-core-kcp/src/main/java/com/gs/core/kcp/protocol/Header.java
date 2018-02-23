package com.gs.core.kcp.protocol;


import com.gs.common.utils.ByteConvertUtil;
import com.gs.core.kcp.constant.KcpConstants;

import java.util.BitSet;

/**
 * Created by linjuntan on 2017/11/5.
 * email: ljt1343@gmail.com
 */
public class Header {
    /*
     由于国内网络问题，单个MTU最好不超过512字节
     byte[12] head {
         bit[16] magic;//魔数 2字节
         bit[32] sessionId;// 会话id 32位，范围为int的大小
         bit[3] control;//业务控制位范围为0~8，1为心跳，2为中断，3为回包，剩余业务自定义
         bit[10] businessId;//业务Id 0~1024, 0~200为引擎用指令，201~299为预留，300~1024为业务使用
         bit[3] payload;//冗余拓展字段 0~8
         bit[32] length;//长度 4字节
     }

     至此数据包长度则为500字节。

     //sessionID 实现为 int => byte => java.util.BitSet 去除前3位
     //control 实现为 int => byte => 去除前29位
     */
    private short magic = KcpConstants.MAGIC;

    private int sessionId;

    private int control;

    private int businessId;

    private int payload;

    private int length;

    public Header() {
    }

    public Header(int sessionId, int control, int businessId, int payload, int length) {
        this.sessionId = sessionId;
        this.control = control;
        this.businessId = businessId;
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

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }

    //sessionId to bit,control to bit,payload to bit,length to bit
    private BitSet sessionIdToBitSet() {
        return ByteConvertUtil.intToBitSet(sessionId);
    }

    private BitSet bitSetToSessionIdBit(BitSet middleBit) {
        BitSet sessionBit = new BitSet();

        int index = 0;
        for (int i = 0; i < 32; i++) {
            if (middleBit.get(i)) {
                sessionBit.set(index);
            }

            index++;
        }
        return sessionBit;
    }

    private BitSet controlToBitSet() {
        BitSet original = ByteConvertUtil.intToBitSet(control);

        BitSet controlBit = new BitSet();
        for (int i = 29; i < 32; i++) {
            if (original.get(i)) {
                controlBit.set(i);
            }
        }

        return controlBit;
    }

    private BitSet bitSetToControlBit(BitSet middleBit) {
        BitSet controlBit = new BitSet();

        int index = 29;
        for (int i = 32; i < 35; i++) {
            if (middleBit.get(i)) {
                controlBit.set(index);
            }

            index++;
        }

        return controlBit;
    }

    private BitSet businessIdToBitSet() {
        BitSet original = ByteConvertUtil.intToBitSet(businessId);

        BitSet businessBit = new BitSet();
        for (int i = 22; i < 32; i++) {
            if (original.get(i)) {
                businessBit.set(i);
            }
        }

        return businessBit;
    }

    private BitSet bitSetToBusinessIdSet(BitSet middleBit) {
        BitSet businessBit = new BitSet();

        int index = 22;
        for (int i = 35; i < 45; i++) {
            if (middleBit.get(i)) {
                businessBit.set(index);
            }

            index++;
        }

        return businessBit;
    }

    private BitSet payloadToBitSet() {
        BitSet original = ByteConvertUtil.intToBitSet(payload);

        BitSet payloadBit = new BitSet();
        for (int i = 29; i < 32; i++) {
            if (original.get(i)) {
                payloadBit.set(i);
            }
        }

        return payloadBit;
    }

    private BitSet bitSetToPayloadBit(BitSet middleBit) {
        BitSet payloadBit = new BitSet();

        int index = 29;
        for (int i = 45; i < 48; i++) {
            if (middleBit.get(i)) {
                payloadBit.set(index);
            }

            index++;
        }

        return payloadBit;
    }

    private byte[] mergeBitSet() {
        BitSet sessionBit = sessionIdToBitSet();
        BitSet controlBit = controlToBitSet();
        BitSet businessIdBit = businessIdToBitSet();
        BitSet payloadBit = payloadToBitSet();

        BitSet bits = new BitSet();
        int index = 0;

        //add sessionId
        for (int i = 0; i < 32; i++) {
            if (sessionBit.get(i)) {
                bits.set(index);
            }
            index++;
        }

        //add control
        for (int i = 29; i < 32; i++) {
            if (controlBit.get(i)) {
                bits.set(index);
            }

            index++;
        }

        //add businessId
        for (int i = 22; i < 32; i++) {
            if (businessIdBit.get(i)) {
                bits.set(index);
            }
            index ++;
        }

        //add payload
        for (int i = 29; i < 32; i++) {
            if (payloadBit.get(i)) {
                bits.set(index);
            }

            index++;
        }

        return ByteConvertUtil.bitSetToByte(bits);
    }

    private void decodeMiddleBitSet(BitSet middleBit) {
        BitSet sessionIdBit = bitSetToSessionIdBit(middleBit);
        BitSet controlBit = bitSetToControlBit(middleBit);
        BitSet businessIdSet = bitSetToBusinessIdSet(middleBit);
        BitSet payloadBit = bitSetToPayloadBit(middleBit);

        sessionId = ByteConvertUtil.BitSetToInt(sessionIdBit);
        control = ByteConvertUtil.BitSetToInt(controlBit);
        payload = ByteConvertUtil.BitSetToInt(payloadBit);
    }

    public byte[] encode() {
        byte[] magicByte = ByteConvertUtil.shortToByteArray(magic);
        byte[] lengthByte = ByteConvertUtil.intToByteArray(length);
        byte[] middleByte = mergeBitSet();

        byte[] head = new byte[12];
        int index = 0;

        //magic
        System.arraycopy(magicByte, 0, head, index, magicByte.length);
        index += magicByte.length;

        //sessionId, control, businessId, payload
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

        short tmpMagic = ByteConvertUtil.byteToShort(magicByte);
        if (tmpMagic != magic) {
            throw new DecodePacketException();
        }

        byte[] middleByte = new byte[6];
        System.arraycopy(head, index, middleByte, 0, 6);
        index += 6;
        decodeMiddleBitSet(ByteConvertUtil.byteToBitSet(middleByte));

        byte[] lengthByte = new byte[4];
        System.arraycopy(head, index, lengthByte, 0, 4);
        length = ByteConvertUtil.byteToInt(lengthByte);
    }

    public void validate() throws DecodePacketException {
        if (magic != KcpConstants.MAGIC) {
            throw new DecodePacketException();
        }
    }

}
