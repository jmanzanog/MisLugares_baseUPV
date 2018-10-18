package com.example.mislugares;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    return vista;
    }
}
