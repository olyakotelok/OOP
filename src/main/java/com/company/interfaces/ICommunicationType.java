package com.company.interfaces;


import com.company.Message;

public interface ICommunicationType {
    void getMsg(Message message, String id);
    void start() throws Exception;
}
