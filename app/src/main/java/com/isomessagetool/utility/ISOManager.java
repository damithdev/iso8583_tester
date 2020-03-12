package com.isomessagetool.utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

import com.imohsenb.ISO8583.builders.GeneralMessageClassBuilder;
import com.imohsenb.ISO8583.builders.ISOClientBuilder;
import com.imohsenb.ISO8583.builders.ISOMessageBuilder;
import com.imohsenb.ISO8583.entities.ISOMessage;
import com.imohsenb.ISO8583.enums.FIELDS;
import com.imohsenb.ISO8583.enums.MESSAGE_FUNCTION;
import com.imohsenb.ISO8583.enums.MESSAGE_ORIGIN;
import com.imohsenb.ISO8583.enums.VERSION;
import com.imohsenb.ISO8583.interfaces.DataElement;
import com.imohsenb.ISO8583.interfaces.ISOClient;
import com.isomessagetool.R;
import com.isomessagetool.pojo.MessageBean;
import com.isomessagetool.pojo.MyCallback;
import com.isomessagetool.pojo.ViewItem;

import java.util.ArrayList;
import java.util.Arrays;

import dmax.dialog.SpotsDialog;


public class ISOManager {

    private String host;
    private int port;
    private Context mContext;
    private MyCallback mListener; // listener field
    AlertDialog dialog;

    // setting the listener
    public void registerMyCallback(MyCallback mListener)
    {
        this.mListener = mListener;
    }
    public ISOManager(Context _ctx){
        this.mContext = _ctx;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(_ctx);

        try {
            port = Integer.parseInt(sharedPref.getString("NAC_PORT","0"));
            host = sharedPref.getString("NAC_HOST",null);
        }catch (Exception ex){
            Toast.makeText(mContext,ex.getMessage(),Toast.LENGTH_SHORT).show();
            port = 0;
            host = null;
        }

    }

    public ISOMessage generate(ArrayList<ViewItem> list){

        ISOMessage isoMessage;
        try{
            DataElement<GeneralMessageClassBuilder> element = ISOMessageBuilder.Packer(VERSION.V1987)
                    .networkManagement()
                    .mti(MESSAGE_FUNCTION.Request, MESSAGE_ORIGIN.Acquirer)
                    .processCode("920000");

            for (ViewItem item:list) {
                element.setField(item.getField(),item.getValue());
            }
            element.setHeader("1002230000");

            isoMessage = element.build();

            int idx = 0;
            for(ViewItem item: list){
                item.setStringField(isoMessage.getStringField(item.getField()));
                list.set(idx,item);
                idx++;
            }
            return isoMessage;
        }catch (Exception ex){
            return null;
        }

    }

    public void sendISOMessage(final ISOMessage message){
        dialog = new SpotsDialog.Builder().setContext(mContext).build();

        if(port > 0  && host != null && !host.isEmpty()){
            dialog.show();

            new Thread(new Runnable() {
                public void run()
                {
                    String response = null;
                    ISOClient client = null;
                    try{
                        Thread.sleep(2000);
                        client = ISOClientBuilder.createSocket(host, port)
                                .build();
                        client.connect();
                        response = Arrays.toString(client.sendMessageSync(message));
                        System.out.println("response = " + response);
                        client.disconnect();

                    }catch (Exception ex){
//                        System.out.println(ex.getMessage());
//                        Toast.makeText(mContext,ex.getMessage(),Toast.LENGTH_LONG).show();

                    }finally {
//                        if(client!= null) client.disconnect();
                        if (mListener != null) {

                            // invoke the callback method of class A
                            mListener.onCallback(response);
                        }
                    }
                }
            }).start();
        }else {
            Toast.makeText(mContext,"HOST and PORT not configured correctly",Toast.LENGTH_LONG).show();
        }
    }

    public void hideDialog(){
        try{
            dialog.hide();
        }catch (Exception e){

        }
    }

}
