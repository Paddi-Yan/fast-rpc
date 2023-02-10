package com.paddi.core.common;

import java.io.Serializable;

import static com.paddi.core.common.constants.RpcConstants.MAGIC_NUMBER;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月07日 00:10:44
 */
public class RpcProtocol implements Serializable {
    private static final long serialVersionUID = -3040906001610738935L;

    private short magicNumber = MAGIC_NUMBER;

    private int contentLength;

    private byte[] content;

    public RpcProtocol(byte[] content) {
        this.contentLength = content.length;
        this.content = content;
    }

    public short getMagicNumber() {
        return magicNumber;
    }

    public void setMagicNumber(short magicNumber) {
        this.magicNumber = magicNumber;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
