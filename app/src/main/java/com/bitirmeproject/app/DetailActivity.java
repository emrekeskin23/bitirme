package com.bitirmeproject.app;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.Chip;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.bitirmeproject.app.R;
import com.squareup.picasso.Picasso;

public class DetailActivity extends Fragment {
    private TextView urun_adi;
    private TextView urun_aciklama;
    private TextView urun_fiyat;
    private ImageView urun_fotograf;
    private Chip chipLocation, chipDate;
    private Button buttonDetayBuy;


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_layout, container, false);

        String urun_id = getArguments().getString("urun_id");
        String strurun_adi = getArguments().getString("urun_adi");
        String strurun_aciklama = getArguments().getString("urun_aciklama");
        String strurun_fiyat = getArguments().getString("urun_fiyat");

        urun_adi = view.findViewById(R.id.detayTextUrunAdi);
        urun_aciklama = view.findViewById(R.id.detayTextUrunAciklama);
        urun_fiyat = view.findViewById(R.id.detayTextUrunFiyat);
        urun_fotograf = view.findViewById(R.id.imageView1);
        chipLocation = view.findViewById(R.id.chipLocation);
        chipDate = view.findViewById(R.id.chipDate);
        buttonDetayBuy = view.findViewById(R.id.buttonDetayBuy);

        urun_adi.setText(strurun_adi);
        urun_aciklama.setText(strurun_aciklama);
        urun_fiyat.setText(strurun_fiyat);


        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("urun");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    String key = d.getKey();
                    Urun u5 = d.getValue(Urun.class);
                    if (urun_id.equals(u5.getUrun_id())) {
                        Picasso.get().load(u5.getUrun_fotograf()).into(urun_fotograf);
                        chipLocation.setText(u5.getUrun_lokasyon());
                        chipDate.setText(u5.getUrun_yuklenme_tarih());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Veri hatası: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        buttonDetayBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference reference = db.getReference("urun");

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            Urun urun = d.getValue(Urun.class);
                            if (urun.getUrun_id().equals(urun_id)) {
                                String productOwner = urun.getUrun_sahibi_id();
                                FirebaseDatabase db2 = FirebaseDatabase.getInstance();
                                DatabaseReference reference2 = db2.getReference("kullanici");

                                reference2.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                                            Kullanici k = d.getValue(Kullanici.class);
                                            if (k.getKullanici_id().equals(productOwner)) {
                                                String tokisi = k.getKullanici_adi();
                                                FirebaseDatabase db3 = FirebaseDatabase.getInstance();
                                                DatabaseReference reference3 = db3.getReference("mesaj");

                                                reference3.addValueEventListener(new ValueEventListener() {
                                                    Boolean unique = true;

                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (unique) {
                                                            String username = getArguments().getString("loginusername");
                                                            Mesaj m = new Mesaj();
                                                            m.setToKisi(tokisi);
                                                            m.setMesajicerik("URUN HALEN SATILIKSA ALIYORUM");
                                                            m.setZaman("12 Ocak");
                                                            m.setFromKisi(username);

                                                            Mesaj m1 = new Mesaj();
                                                            m1.setToKisi(username);
                                                            m1.setMesajicerik("İçerik görüntülendi");
                                                            m1.setFromKisi(tokisi);
                                                            m1.setZaman("12 Ocak");
                                                            reference3.push().setValue(m);
                                                            reference3.push().setValue(m1);
                                                            unique = false;
                                                            buttonDetayBuy.setText("MSJ GONDERILDI");
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                                        Toast.makeText(getContext(), "Mesaj verisi hatası: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(getContext(), "Kullanıcı verisi hatası: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), "Veri hatası: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return view;
    }
}
