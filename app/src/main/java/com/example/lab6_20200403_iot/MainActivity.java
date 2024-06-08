package com.example.lab6_20200403_iot;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lab6_20200403_iot.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    EditText correo;
    EditText contrasena;
    Button btnLogin;
    private final static String TAG = "msg-test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }

    private void login() {
        String correoLogin = correo.getText().toString().trim();
        String pswdLogin = contrasena.getText().toString().trim();
        String TAG = "msg-auth";

        if (correoLogin.isEmpty() || pswdLogin.isEmpty()) {
            Toast.makeText(MainActivity.this, "Ingrese correo y contraseña.", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(correoLogin, pswdLogin)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null && user.isEmailVerified()) {
                                // Se obtiene el rol del usuario
                                FirebaseFirestore db = FirebaseFirestore.getInstance();

                            } else if (user != null) {
                                user.sendEmailVerification().addOnCompleteListener(task2 -> {
                                    Toast.makeText(MainActivity.this, "Se le ha enviado un correo para validar su cuenta", Toast.LENGTH_SHORT).show();
                                });
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Autenticación fallida.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}