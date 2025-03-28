package com.bitirmeproject.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
import com.bitirmeproject.app.R;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends Fragment {
    private TextView textName, textAdres;
    private Button buttonProfileAddProduct;
    List<Urun> urunList;

    private CustomAdapter customAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_layout, container, false);

        textName = view.findViewById(R.id.textName);
        buttonProfileAddProduct = view.findViewById(R.id.buttonProfileAddProduct);

        if (getArguments() != null) {
            String username = this.getArguments().getString("username");
            String userkey = this.getArguments().getString("userkey");
            textName.setText(username);
        }

        buttonProfileAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getArguments() != null) {
                    String username = getArguments().getString("username");
                    String userfullname = getArguments().getString("userfullname");
                    String userkey = getArguments().getString("userkey");


                    AddProduct ap = new AddProduct();
                    Bundle bundle = new Bundle();
                    bundle.putString("username", username);
                    bundle.putString("userfullname", userfullname);
                    bundle.putString("userkey", userkey);
                    ap.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, ap).commit();
                }
            }
        });


        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myref = db.getReference("urun");
        urunList = new ArrayList<>();
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    if (getArguments() != null) {
                        String username = getArguments().getString("username");
                        String userfullname = getArguments().getString("userfullname");
                        String userkey = getArguments().getString("userkey");


                        Urun u1 = d.getValue(Urun.class);
                        String dburunkey = u1.getUrun_sahibi_id().toString();

                        if (dburunkey.equals(userkey)) {
                            urunList.add(u1);
                        }
                    }
                }
                    RecyclerView rv = view.findViewById(R.id.profileRv);
                    customAdapter = new CustomAdapter(urunList, getContext());
                    rv.setAdapter(customAdapter);
                    rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }


}
