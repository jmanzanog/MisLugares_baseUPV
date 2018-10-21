package com.example.mislugares.util;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ValoracionesFirestore {
    public static void guardarValoracion(String lugar, String usuario, Double valor) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> datos = new HashMap<>();
        datos.put("valoracion", valor);
        db.collection("lugares").document(lugar).collection("valoraciones").document(usuario).set(datos);
    }

    public interface EscuchadorValoracion {
        void onRespuesta(Double valor);

        void onNoExiste();

        void onError(Exception e);
    }

    public static void leerValoracion(String lugar, String usuario, final EscuchadorValoracion escuchador) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("lugares").document(lugar).collection("valoraciones").document(usuario).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().exists()) {
                        escuchador.onNoExiste();
                    } else {
                        double val = task.getResult().getDouble("valoracion");
                        escuchador.onRespuesta(val);
                    }
                } else {
                    Log.e("Mis Lugares", "No se puede leer valoraciones", task.getException());
                    escuchador.onError(task.getException());
                }
            }
        });
    }
}
