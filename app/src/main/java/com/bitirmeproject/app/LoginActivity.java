package com.bitirmeproject.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class LoginActivity extends AppCompatActivity {
    private Button buttonKayitGecisYap, buttonLoginGirisYap;
    private TextInputEditText textLoginMail, textLoginPassword;
    private ConstraintLayout log;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        // Initialize FirestoreDB
        var db = MyApplication.getFirestoreDB();

        buttonKayitGecisYap = findViewById(R.id.buttonKayıtGecisYap);
        buttonLoginGirisYap = findViewById(R.id.buttonLoginGirisYap);
        textLoginMail = findViewById(R.id.textLoginMail);
        textLoginPassword = findViewById(R.id.textLoginPassword);
        log = findViewById(R.id.containerx);

        buttonLoginGirisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strMail = textLoginMail.getText().toString();
                String strPassword = textLoginPassword.getText().toString();

                // Query FirestoreDB for user
                db.collection("kullanici")
                    .whereEqualTo("kullanici_adi", strMail)
                    .whereEqualTo("kullanici_sifre", strPassword)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        boolean isAuthenticated = false;
                        
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            isAuthenticated = true;
                            String key = document.getId();
                            String strFullName = document.getString("kullanici_fullname");
                            String dbUsername = document.getString("kullanici_adi");

                            // Ana ekrana yönlendirme
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("userfullname", strFullName);
                            intent.putExtra("username", dbUsername);
                            intent.putExtra("userkey", key);
                            startActivity(intent);
                            finish();
                            break;
                        }

                        // Eğer kullanıcı bulunamazsa hata mesajı göster
                        if (!isAuthenticated) {
                            Snackbar.make(log, "Kullanıcı Adı Veya Şifre Yanlış", Snackbar.LENGTH_LONG)
                                    .setAction("ACTION", null)
                                    .show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Snackbar.make(log, "Veri hatası: " + e.getMessage(), Snackbar.LENGTH_LONG)
                                .setAction("ACTION", null)
                                .show();
                    });
            }
        });

        buttonKayitGecisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SingUpActivity.class);
                startActivity(intent);
            }
        });
    }
}
