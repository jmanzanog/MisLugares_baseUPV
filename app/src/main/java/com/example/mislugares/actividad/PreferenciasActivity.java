package com.example.mislugares.actividad;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.mislugares.fragment.PreferenciasFragment;
import com.example.mislugares.fragment.SelectorFragment;
import com.example.mislugares.lugar.LugaresFirebase;
import com.example.mislugares.lugar.LugaresFirestore;
import com.example.mislugares.util.Preferencias;

public class PreferenciasActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new PreferenciasFragment())
                .commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Preferencias pref = Preferencias.getInstance();
        if (pref.usarFirestore()) {
            MainActivity.lugares = new LugaresFirestore();
        } else {
            MainActivity.lugares = new LugaresFirebase();
        }
        SelectorFragment.ponerAdaptador();
    }
}