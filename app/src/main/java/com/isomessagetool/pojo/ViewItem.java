package com.isomessagetool.pojo;

import androidx.annotation.NonNull;

public class ViewItem {
    private String name;
    private String value;

    public ViewItem(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @NonNull
    @Override
    public String toString() {
        return "Bit: "+name+ " Value:"+value;
    }
}
