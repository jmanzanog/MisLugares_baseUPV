package com.example.mislugares.util;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
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

    public static void guardarValoracionYRecalcular(final String lugar, final String usuario, final float nuevaVal) {
        leerValoracion(lugar, usuario, new EscuchadorValoracion() {
            @Override
            public void onRespuesta(Double viejaVal) {
                actualizarValoracionMedia(lugar, usuario, viejaVal, nuevaVal);
            }

            @Override
            public void onNoExiste() {
                actualizarValoracionMedia(lugar, usuario, Double.NaN, nuevaVal);
            }


            @Override
            public void onError(Exception e) {
            }
        });
    }

    private static void actualizarValoracionMedia(final String lugar, final String usuario, final double viejaVal, final double nuevaVal) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference refLugar = db.collection("lugares").document(lugar);
        refLugar.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult().exists()) {
                    double media = task.getResult().getDouble("valoracion");
                    long nValor =0;
                    try {
                        nValor = task.getResult().getLong("n_valoraciones");
                    }catch (Exception e){

                    }
                    double nuevaMedia = nuevaMedia(media, nValor, viejaVal, nuevaVal);
                    if (Double.isNaN(viejaVal)) nValor++;
                    refLugar.update("valoracion", nuevaMedia, "n_valoraciones", nValor);
                    guardarValoracion(lugar, usuario, nuevaVal);
                } else {
                    Log.e("Mis Lugares", "ERROR al leer", task.getException());
                }
            }
        });
    }

    private static Double nuevaMedia(double media, long nValoraciones, double viejaVal, double nuevaVal) {
        if (nValoraciones == 0) {
            return nuevaVal;
        } else if (Double.isNaN(viejaVal)) {
            return (nValoraciones * media + nuevaVal) / (nValoraciones + 1);
        } else {
            return (nValoraciones * media - viejaVal + nuevaVal) / nValoraciones;
        }
    }
}
