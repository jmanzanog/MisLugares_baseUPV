package com.example.mislugares.adaptador;

import android.view.View;

import com.example.mislugares.pojo.Lugar;

public interface AdaptadorLugaresInterface {
    public String getKey(int pos);

    public Lugar getItem(int pos);

    public void setOnItemClickListener(View.OnClickListener onClick);

    public void startListening();

    public void stopListening();
}
