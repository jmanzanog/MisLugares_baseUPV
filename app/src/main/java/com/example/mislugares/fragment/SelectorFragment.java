package com.example.mislugares.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mislugares.adaptador.AdaptadorLugaresFirebase;
import com.example.mislugares.adaptador.AdaptadorLugaresFirebaseUI;
import com.example.mislugares.adaptador.AdaptadorLugaresFirestore;
import com.example.mislugares.adaptador.AdaptadorLugaresInterface;
import com.example.mislugares.util.Preferencias;
import com.example.mislugares.R;
import com.example.mislugares.actividad.MainActivity;
import com.example.mislugares.adaptador.AdaptadorLugaresFirestoreUI;
import com.example.mislugares.pojo.Lugar;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.example.mislugares.util.Preferencias.SELECCION_MIOS;
import static com.example.mislugares.util.Preferencias.SELECCION_TIPO;

/**
 * Created by Jesús Tomás on 19/04/2017.
 */

public class SelectorFragment extends Fragment {
    //private RecyclerView recyclerView;
    // public static AdaptadorLugaresBD adaptador;
    // public static AdaptadorLugaresFirebaseUI adaptador;
    // public static AdaptadorLugaresFirestoreUI adaptador2;

    public static RecyclerView.Adapter adaptador2;
    private static RecyclerView recyclerView;
    private static Context context;
    private static RecyclerView.LayoutManager layoutManager;
    private static Preferencias pref;

    @Override
    public View onCreateView(LayoutInflater inflador, ViewGroup contenedor,
                             Bundle savedInstanceState) {
        View vista = inflador.inflate(R.layout.fragment_selector,
                contenedor, false);
        recyclerView = (RecyclerView) vista.findViewById(R.id.recycler_view);
        return vista;
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        context = getContext();
        ponerAdaptador();
        getAdaptador().setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAdaptador().startListening();
                ((MainActivity) getActivity()).muestraLugar(
                        recyclerView.getChildAdapterPosition(v));
                /*Intent i = new Intent(getContext(), VistaLugarActivity.class);
                i.putExtra("id", (long)
                        recyclerView.getChildAdapterPosition(v));
                startActivity(i);*/
            }
        });


    }

    private static void defaultadaptor(){
        com.google.firebase.firestore.Query query = FirebaseFirestore.getInstance().collection("lugares").orderBy(pref.criterioOrdenacion()).limit(pref.maximoMostrar());
        switch (pref.criterioSeleccion()) {
            case SELECCION_MIOS:
                query = query.whereEqualTo("uidUsuariCreador", FirebaseAuth.getInstance().getCurrentUser().getUid());
                break;
            case SELECCION_TIPO:
                query = query.whereEqualTo("tipo", pref.tipoSeleccion());
                break;

        }
        FirestoreRecyclerOptions<Lugar> opciones = new FirestoreRecyclerOptions.Builder<Lugar>().setQuery(query, Lugar.class).build();
        adaptador2 = new AdaptadorLugaresFirestoreUI(opciones);
    }

    /*public void initAdapter() {
        Query query = FirebaseDatabase.getInstance().getReference().child("lugares").limitToLast(50);
        FirebaseRecyclerOptions<Lugar> opciones = new FirebaseRecyclerOptions.Builder<Lugar>().setQuery(query, Lugar.class).build();
        adaptador = new AdaptadorLugaresFirebaseUI(opciones);
        adaptador.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adaptador.startListening();
                ((MainActivity) getActivity()).muestraLugar(
                        recyclerView.getChildAdapterPosition(v));
                *//*Intent i = new Intent(getContext(), VistaLugarActivity.class);
                i.putExtra("id", (long)
                        recyclerView.getChildAdapterPosition(v));
                startActivity(i);*//*
            }
        });
    }*/

    @Override
    public void onStart() {
        super.onStart();
        //adaptador.startListening();
        getAdaptador().startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        // adaptador.stopListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //adaptador.stopListening();
        getAdaptador().stopListening();
    }

    public static void ponerAdaptador() {
        pref = Preferencias.getInstance();
        pref.inicializa(context);
// Poner el código aquí

        layoutManager.setAutoMeasureEnabled(true); //Quitar si da problemas
       /* adaptador = new AdaptadorLugaresBD(getContext(),
                MainActivity.lugares, MainActivity.lugares.extraeCursor());*/
        // recyclerView.setAdapter(adaptador);
        //  initAdapter();
        //  com.google.firebase.firestore.Query query = FirebaseFirestore.getInstance().collection("lugares").limit(50);
        /*com.google.firebase.firestore.Query query = FirebaseFirestore.getInstance().collection("lugares").orderBy(pref.criterioOrdenacion()).limit(pref.maximoMostrar());
        switch (pref.criterioSeleccion()) {
            case SELECCION_MIOS:
                query = query.whereEqualTo("uidUsuariCreador", FirebaseAuth.getInstance().getCurrentUser().getUid());
                break;
            case SELECCION_TIPO:
                query = query.whereEqualTo("tipo", pref.tipoSeleccion());
                break;

        }
        FirestoreRecyclerOptions<Lugar> opciones = new FirestoreRecyclerOptions.Builder<Lugar>().setQuery(query, Lugar.class).build();
        adaptador2 = new AdaptadorLugaresFirestoreUI(opciones);*/


        if (pref.usarFirestore()) {
            com.google.firebase.firestore.Query query = FirebaseFirestore.getInstance().collection("lugares").orderBy(pref.criterioOrdenacion()).limit(pref.maximoMostrar());
            switch (pref.criterioSeleccion()) {
                case SELECCION_MIOS:
                    query = query.whereEqualTo("creador", FirebaseAuth.getInstance().getCurrentUser().getUid());

                    break;
                case SELECCION_TIPO:
                    query = query.whereEqualTo("tipo", pref.tipoSeleccion());
                    break;
            } if (pref.usarFirebaseUI()) {
                FirestoreRecyclerOptions<Lugar> options = new FirestoreRecyclerOptions.Builder<Lugar>().setQuery(query, Lugar.class).build();
                adaptador2 = new AdaptadorLugaresFirestoreUI(options);
            } else {
                adaptador2 = new AdaptadorLugaresFirestore(context, query);
            }
        } else {
            if (pref.usarFirebaseUI()) {
                com.google.firebase.database.Query query = FirebaseDatabase.getInstance().getReference().child("lugares").limitToLast(pref.maximoMostrar());
                switch (pref.criterioSeleccion()) {
                    case SELECCION_MIOS:
                        query = query.orderByChild("creador").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        break;
                    case SELECCION_TIPO:
                        query = query.orderByChild("tipo").equalTo(pref.tipoSeleccion());
                        break;
                    default:
                        query = query.orderByChild(pref.criterioOrdenacion());
                        break;
                }
                FirebaseRecyclerOptions<Lugar> options = new FirebaseRecyclerOptions.Builder<Lugar>().setQuery(query, Lugar.class).build();
                adaptador2 = new AdaptadorLugaresFirebaseUI(options);
            } else {
                adaptador2 = new AdaptadorLugaresFirebase(context, FirebaseDatabase.getInstance().getReference("lugares"));
            }
        }

        defaultadaptor();

        getAdaptador().setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).muestraLugar(recyclerView.getChildAdapterPosition(v));
            }
        });

        getAdaptador().startListening();
        recyclerView.setAdapter(adaptador2);

        // recyclerView.setAdapter(adaptador);

    }

    public static AdaptadorLugaresInterface getAdaptador() {
        return (AdaptadorLugaresInterface) adaptador2;
    }
}