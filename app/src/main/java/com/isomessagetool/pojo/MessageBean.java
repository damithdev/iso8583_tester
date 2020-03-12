package com.isomessagetool.pojo;

import com.imohsenb.ISO8583.entities.ISOMessage;

public class MessageBean {
    ISOMessage isoMessage;
    String host;
    Integer port;

    public MessageBean(ISOMessage message, String host, Integer port){
        this.isoMessage = message;
        this.host = host;
        this.port = port;
    }

    public Integer getPort() {
        return port;
    }

    public ISOMessage getIsoMessage() {
        return isoMessage;
    }

    public String getHost() {
        return host;
    }
}
