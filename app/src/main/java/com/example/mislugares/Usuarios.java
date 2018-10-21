package com.example.mislugares;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Usuarios {
    static void guardarUsuario(final FirebaseUser user) {
        Usuario usuario = new Usuario(user.getDisplayName(), user.getEmail());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("usuarios/" + user.getUid()).setValue(usuario);
    }
}
