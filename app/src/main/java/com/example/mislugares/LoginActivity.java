package com.example.mislugares;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    List<AuthUI.IdpConfig> providers = null;
    private ProgressDialog progressDialog;
   
   
    @Override protected void onCreate(Bundle savedInst) {
        super.onCreate(savedInst);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build(), /*,*/
                new AuthUI.IdpConfig.TwitterBuilder().build());
                login(null);
    }

    private void login(String providerType) {
        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        if ("password".equals(providerType)&&!usuario.isEmailVerified()){
            Toast.makeText(this,"Debe validar su correo", Toast.LENGTH_LONG).show();
            FirebaseAuth.getInstance().getCurrentUser().reload();
            AuthUI.getInstance().signOut(getApplicationContext());
            usuario.sendEmailVerification();

            Intent i = new Intent(getCurrentActivity(),LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            getCurrentActivity().finish();

        }
        if ((usuario != null&&providerType!=null&&!"password".equals(providerType))||((usuario != null&&"password".equals(providerType)&&usuario.isEmailVerified()))) {
            Usuarios.guardarUsuario(usuario);
            Toast.makeText(this, "inicia sesión: " +
                    usuario.getDisplayName()+" - "+ usuario.getEmail()+" - "+
                    usuario.getProviders().get(0),Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
        if (usuario == null|| providerType ==null) {
            startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setLogo(R.mipmap.ic_launcher)
                    .setTheme(R.style.FirebaseUITema)
                    .setAvailableProviders(providers)
                    .setIsSmartLockEnabled(true).build(), RC_SIGN_IN);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response2 = IdpResponse.fromResultIntent(data);
            Log.d("getProviderType()", response2.getProviderType());

            Log.d("IdpResponse", new Gson().toJson(response2));
            if (resultCode == RESULT_OK) {
                login(response2.getProviderType());

                finish();
            } else {
                IdpResponse response = IdpResponse.fromResultIntent(data);
                if (response == null) {
                    Toast.makeText(this,"Cancelado",Toast.LENGTH_LONG).show();
                    return;
                } else if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this,"Sin conexión a Internet",
                            Toast.LENGTH_LONG).show();
                    return;
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this,"Error desconocido",
                            Toast.LENGTH_LONG).show();
                    return;
                }
            }

        }
    }

    public Activity getCurrentActivity() {
        return this;
    }
}
