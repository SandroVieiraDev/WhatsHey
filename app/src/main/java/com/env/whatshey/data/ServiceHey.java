package com.env.whatshey.data;

import android.content.Intent;
import android.net.Uri;

import java.util.Observable;

public class ServiceHey extends Observable {

    public void openChat(String phone){
        setChanged();
        if (phone.length() < 14){
            notifyObservers("Digite um número de telefone válido");
        } else {
            notifyObservers(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("whatsapp://send?phone=+55" + phone)));
        }
    }
}
