package com.bitirmeproject.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddProduct extends Fragment {
    private EditText textCityname, textProductName, textProductPrice, textProductDescription;
    private Button buttonProductAdd, buttonProductPhotoPicker;
    private ImageView imageProductPhoto;
    private FirebaseFirestore db;
    private Uri imguri;
    private StorageReference mStorageRef;
    private final static int IMG_REQUEST_ID = 10;
    private final static int RESULT_OK = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Get Firestore instance from MyApplication
        db = MyApplication.getFirestoreDB();

        // View oluşturma
        View view = inflater.inflate(R.layout.addproduct_layout, container, false);

        // Elementlere erişim
        textCityname = view.findViewById(R.id.textCityname);
        textProductName = view.findViewById(R.id.textProductName);
        textProductPrice = view.findViewById(R.id.textProductPrice);
        textProductDescription = view.findViewById(R.id.textProductDescription);
        buttonProductAdd = view.findViewById(R.id.buttonProductAdd);
        buttonProductPhotoPicker = view.findViewById(R.id.buttonProductPhotoPicker);
        imageProductPhoto = view.findViewById(R.id.imageProductPhoto);
        buttonProductAdd.setEnabled(false);

        // Firebase Storage referansı
        mStorageRef = FirebaseStorage.getInstance().getReference("Images/" + UUID.randomUUID().toString());

        // Ürün ekleme butonuna tıklanması
        buttonProductAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonProductAdd.setEnabled(false);
                buttonProductAdd.setText("Urun Ekleniyor.");
                String strCityname = textCityname.getText().toString();
                String strProductname = textProductName.getText().toString();
                String strProductprice = textProductPrice.getText().toString();
                String strProductDescription = textProductDescription.getText().toString();

                // Fotoğraf seçilmemişse hata mesajı
                if (imguri == null) {
                    Toast.makeText(getContext(), "Lütfen bir ürün fotoğrafı seçin", Toast.LENGTH_SHORT).show();
                    buttonProductAdd.setEnabled(true);
                    buttonProductAdd.setText("Ürün Ekle");
                    return;
                }

                // Fotoğraf yükleme işlemi
                mStorageRef.putFile(imguri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                final Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                                firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String mDownloadUrl = uri.toString();

                                        Bundle bundle = getArguments();
                                        if (bundle != null) {
                                            String userfullname = getArguments().getString("userfullname");
                                            String username = getArguments().getString("username");
                                            String userkey = getArguments().getString("userkey");
                                            Date d = new Date();
                                            CharSequence s = DateFormat.format("MMMM d, yyyy ", d.getTime());

                                            // Ürün verilerini hazırlama
                                            Map<String, Object> urunData = new HashMap<>();
                                            urunData.put("urun_adi", strProductname);
                                            urunData.put("urun_aciklama", strProductDescription);
                                            urunData.put("urun_fiyat", Integer.parseInt(strProductprice));
                                            urunData.put("urun_fotograf", mDownloadUrl);
                                            urunData.put("urun_sahibi_id", userkey);
                                            urunData.put("urun_lokasyon", strCityname);
                                            urunData.put("urun_yuklenme_tarih", s.toString());

                                            // Firestore'a ürün ekleme
                                            db.collection("urun")
                                                .add(urunData)
                                                .addOnSuccessListener(documentReference -> {
                                                    buttonProductAdd.setText("Urun Eklendi.");
                                                    textCityname.setText("");
                                                    textProductName.setText("");
                                                    textProductPrice.setText("");
                                                    textProductDescription.setText("");
                                                    Toast.makeText(getActivity(), "URUN EKLENDİ !", Toast.LENGTH_SHORT).show();
                                                    buttonProductAdd.setEnabled(true);
                                                })
                                                .addOnFailureListener(e -> {
                                                    Toast.makeText(getContext(), "Ürün ekleme hatası: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    buttonProductAdd.setEnabled(true);
                                                    buttonProductAdd.setText("Ürün Ekle");
                                                });
                                        }
                                    }
                                }).addOnFailureListener(e -> {
                                    Toast.makeText(getContext(), "Fotoğraf yükleme hatası: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    buttonProductAdd.setEnabled(true);
                                    buttonProductAdd.setText("Ürün Ekle");
                                });
                            }
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getContext(), "Fotoğraf yükleme hatası: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            buttonProductAdd.setEnabled(true);
                            buttonProductAdd.setText("Ürün Ekle");
                        });
            }
        });

        // Fotoğraf seçme butonuna tıklanması
        buttonProductPhotoPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonProductAdd.setEnabled(true);
                requestImage();
            }
        });

        return view;
    }

    // Fotoğraf seçme işlemi
    private void requestImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "SELECT PICTURE"), IMG_REQUEST_ID);
    }

    // Fotoğraf seçimi sonrası işlem
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQUEST_ID && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imguri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imguri);
                imageProductPhoto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
