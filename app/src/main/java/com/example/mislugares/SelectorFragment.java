package com.example.mislugares;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Created by Jesús Tomás on 19/04/2017.
 */

public class SelectorFragment extends Fragment {
    private RecyclerView recyclerView;
    // public static AdaptadorLugaresBD adaptador;
    public static AdaptadorLugaresFirebaseUI adaptador;
    public static AdaptadorLugaresFirestoreUI adaptador2;

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
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setAutoMeasureEnabled(true); //Quitar si da problemas
       /* adaptador = new AdaptadorLugaresBD(getContext(),
                MainActivity.lugares, MainActivity.lugares.extraeCursor());*/

        // recyclerView.setAdapter(adaptador);


        //  initAdapter();

        com.google.firebase.firestore.Query query = FirebaseFirestore.getInstance().collection("lugares").limit(50);
        FirestoreRecyclerOptions<Lugar> opciones = new FirestoreRecyclerOptions.Builder<Lugar>().setQuery(query, Lugar.class).build();
        adaptador2 = new AdaptadorLugaresFirestoreUI(opciones);
        adaptador2.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adaptador2.startListening();
                ((MainActivity) getActivity()).muestraLugar(
                        recyclerView.getChildAdapterPosition(v));
                /*Intent i = new Intent(getContext(), VistaLugarActivity.class);
                i.putExtra("id", (long)
                        recyclerView.getChildAdapterPosition(v));
                startActivity(i);*/
            }
        });
        recyclerView.setAdapter(adaptador2);
        adaptador2.startListening();
       // recyclerView.setAdapter(adaptador);


    }

    public void initAdapter() {
        Query query = FirebaseDatabase.getInstance().getReference().child("lugares").limitToLast(50);
        FirebaseRecyclerOptions<Lugar> opciones = new FirebaseRecyclerOptions.Builder<Lugar>().setQuery(query, Lugar.class).build();
        adaptador = new AdaptadorLugaresFirebaseUI(opciones);
        adaptador.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adaptador.startListening();
                ((MainActivity) getActivity()).muestraLugar(
                        recyclerView.getChildAdapterPosition(v));
                /*Intent i = new Intent(getContext(), VistaLugarActivity.class);
                i.putExtra("id", (long)
                        recyclerView.getChildAdapterPosition(v));
                startActivity(i);*/
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        //adaptador.startListening();
        adaptador2.startListening();
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
        adaptador2.stopListening();
    }
}