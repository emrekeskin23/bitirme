package com.bitirmeproject.app;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.bitirmeproject.app.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SearchActivity extends Fragment {
    private Button detailButton;
    List<Urun> mList = new ArrayList<>();
    private CustomAdapter customAdapter;
    private RecyclerView rv;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_layout, container, false);
        ImageView img = view.findViewById(R.id.imageView2);

        FirebaseFirestore db = MyApplication.getFirestoreDB();
        StorageReference uploadref = FirebaseStorage.getInstance().getReference("Images");

        CollectionReference myref = db.collection("urun");
        myref.addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.w("SearchActivity", "Listen failed.", error);
                return;
            }
            for (QueryDocumentSnapshot document : value) {
                Map<String, Object> data = document.getData();
                String urun_id = document.getId();
                String urun_adi = (String) data.get("urun_adi");
                long urun_fiyat = Long.parseLong(String.valueOf(data.get("urun_fiyat")));
                String urun_fotograf = (String) data.get("urun_fotograf");
                String urun_sahibi_id = (String) data.get("urun_sahibi_id");
                String urun_lokasyon = (String) data.get("urun_lokasyon");
                String urun_yuklenme_tarih = (String) data.get("urun_yuklenme_tarih");
                String urun_aciklama = (String) data.get("urun_aciklama");

                Urun urun = new Urun(urun_id, urun_adi, urun_fotograf, urun_aciklama, urun_fiyat, urun_sahibi_id, urun_lokasyon, urun_yuklenme_tarih);
                mList.add(urun);
            }
        });

        // myref.addValueEventListener(new ValueEventListener() {
        //     @Override
        //     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        //         if (getArguments() != null) {
        //             for (DataSnapshot d : dataSnapshot.getChildren()) {
        //                 Urun u2 = d.getValue(Urun.class);
        //                 String key = d.getKey();
        //                 mList.add(u2);
        //                 Bundle b = new Bundle();
        //                 b.putString("productowner", u2.getUrun_sahibi_id());

        //                 String userfullname = getArguments().getString("userfullname");
        //                 String username = getArguments().getString("username");
        //                 String userkey = getArguments().getString("userkey");

        //                 rv = view.findViewById(R.id.rv);
        //                 customAdapter = new CustomAdapter(mList, getContext());
        //                 customAdapter.setUserkey(userkey);
        //                 customAdapter.setUserfullname(userfullname);
        //                 customAdapter.setUsername(username);
        //                 rv.setAdapter(customAdapter);
        //                 rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        //             }
        //         }
        //     }

        //     @Override
        //     public void onCancelled(@NonNull DatabaseError databaseError) {
        //     }
        // });
        rv = view.findViewById(R.id.rv);
        customAdapter = new CustomAdapter(mList, getContext());
        rv.setAdapter(customAdapter);
        rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        return view;
    }
}
