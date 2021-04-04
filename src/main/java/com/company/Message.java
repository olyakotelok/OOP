package com.company;

public class Message {
    public final String Text;
    public final String Image;

    public Message(String text, String image) {
        Text = text;
        Image = image;
    }

    public Message(String text) {
        Text = text;
        Image = null;
    }
}
