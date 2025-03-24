package com.bitirmeproject.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class SingUpActivity extends AppCompatActivity {
    private TextInputEditText textFullName, textEmail, textPassword;
    private MaterialButton buttonCreateAccount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singup_layout);

        // Initialize FirestoreDB
        var db = MyApplication.getFirestoreDB();

        textFullName = findViewById(R.id.textFullName);
        textEmail = findViewById(R.id.textEmail);
        textPassword = findViewById(R.id.textPassword);
        buttonCreateAccount = findViewById(R.id.buttonCreateAccount);
        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strFullName = textFullName.getText().toString().trim();
                String strEmail = textEmail.getText().toString().trim();
                String strPassword = textPassword.getText().toString().trim();

                if (strFullName.isEmpty() || strEmail.isEmpty() || strPassword.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Lütfen tüm alanları doldurun!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // if collection is not exist, create it

                CollectionReference collectionReference = db.collection("kullanici");

                collectionReference.get().addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        collectionReference.document("kullanici").set(new HashMap<String, Object>());
                    }
                });

                // Check if user already exists
                db.collection("kullanici")
                    .whereEqualTo("kullanici_adi", strEmail)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Bu kullanıcı adı zaten kullanılıyor!", Toast.LENGTH_SHORT).show();
                        } else {
                            // Create new user
                            Map<String, Object> yeniKullanici = new HashMap<>();
                            yeniKullanici.put("kullanici_adi", strEmail);
                            yeniKullanici.put("kullanici_sifre", strPassword);
                            yeniKullanici.put("kullanici_fullname", strFullName);

                            db.collection("kullanici")
                                .add(yeniKullanici)
                                .addOnSuccessListener(documentReference -> {
                                    Toast.makeText(getApplicationContext(), "Hesap başarıyla oluşturuldu!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getApplicationContext(), "Kayıt başarısız: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getApplicationContext(), "Veri hatası: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
            }
        });
    }
}
