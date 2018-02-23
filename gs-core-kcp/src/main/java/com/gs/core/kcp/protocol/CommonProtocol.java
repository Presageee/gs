package com.gs.core.kcp.protocol;

import com.gs.common.utils.AESUtil;

/**
 * Created by linjuntan on 2017/11/5.
 * email: ljt1343@gmail.com
 */
public class CommonProtocol {
    private byte[] head;

    private byte[] body;

    private Header header;

    public CommonProtocol() {
    }

    public CommonProtocol(byte[] head, byte[] body) {
        this.head = head;
        this.body = body;
    }

    public CommonProtocol(Header header, byte[] body) {
        this.head = header.encode();
        this.header = header;
        this.body = body;
    }

    public byte[] getHead() {
        return head;
    }

    public void setHead(byte[] head) {
        this.head = head;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public byte[] toByte() {
        byte[] data = new byte[head.length + body.length];
        System.arraycopy(head, 0, data, 0, head.length);
        System.arraycopy(body, 0, data, head.length, body.length);
        return data;
    }

    public void decode(byte[] data) throws DecodePacketException {
        head = new byte[12];
        System.arraycopy(data, 0, head, 0, 12);

        header = new Header();
        header.decode(head);

        if (header.getLength() == 0) return;

        byte[] encrypt = new byte[header.getLength()];
        System.arraycopy(data, 12, encrypt, 0, encrypt.length);

        body = AESUtil.decrypt(encrypt);
    }
}
