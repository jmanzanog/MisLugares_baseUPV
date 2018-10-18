package com.example.mislugares;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UsuarioFragment extends Fragment {
    @Override public View onCreateView(LayoutInflater inflador, ViewGroup contenedor, Bundle savedInstanceState) {
        View vista = inflador.inflate(R.layout.fragment_usuario, contenedor, false);
        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        TextView nombre = (TextView) vista.findViewById(R.id.nombre);
        nombre.setText(null==usuario.getDisplayName()?"":usuario.getDisplayName());

        TextView correo = (TextView) vista.findViewById(R.id.correo);
        correo.setText(null==usuario.getEmail()?"":usuario.getEmail());

        TextView proveedor = (TextView) vista.findViewById(R.id.proveedor);
        proveedor.setText(null==usuario.getProviders()?"":usuario.getProviders().toString());

        TextView telefono = (TextView) vista.findViewById(R.id.telefono);
        telefono.setText(null==usuario.getPhoneNumber()?"":usuario.getPhoneNumber());

        TextView uid = (TextView) vista.findViewById(R.id.uid);
        uid.setText(null==usuario.getUid()?"":usuario.getUid());

        Button cerrarSesion =(Button) vista.findViewById(R.id.btn_cerrar_sesion);
        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AuthUI.getInstance().signOut(getActivity()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                        public void onComplete(@NonNull Task<Void> task) {
                        Intent i = new Intent(getActivity(),LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        getActivity().finish();
                    }
                });
            }
        });

    return vista;
    }
}
