package com.isomessagetool.pojo;

import androidx.annotation.NonNull;
import com.imohsenb.ISO8583.enums.FIELDS;

import java.io.Serializable;

public class ViewItem implements Comparable, Serializable {
    private int bit;
    private String value;
    private String stringField;
    private FIELDS field;

    public ViewItem(int bit, String value) {
        this.bit = bit;
        this.value = value;
        this.field = FIELDS.valueOf(bit);
    }

    public int getBit() {
        return bit;
    }


    public String getValue() {
        return value;
    }

    public FIELDS getField(){
        return field;
    }

    public void setStringField(String field){
        this.stringField = field;
    }

    public String getStringField(){return stringField;}

    @NonNull
    @Override
    public String toString() {
        return "Bit: "+String.valueOf(bit)+ " Value:"+value;
    }

    @Override
    public int compareTo(Object o) {
        int compareage=((ViewItem)o).getBit();
        /* For Ascending order*/
        return this.bit-compareage;
    }
}
