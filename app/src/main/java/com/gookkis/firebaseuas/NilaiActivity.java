package com.gookkis.firebaseuas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NilaiActivity extends AppCompatActivity {

    Spinner spnMataKuliah;
    EditText etNilai;
    TextView tvNama;
    Button btnSimpan;
    CircleImageView fotoProfile;
    boolean isNew = true;
    String idNilaiEdit = "";

    RecyclerView rvNilai;
    List<NilaiModel> nilaiModelList = new ArrayList<>();
    ArrayAdapter<CharSequence> spinnerAdapter;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference databaseNilai = database.getReference("nilai").child(Bantuan.id);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nilai);

        fotoProfile = findViewById(R.id.profile_image);
        spnMataKuliah = findViewById(R.id.spn_mata_kuliah);
        tvNama = findViewById(R.id.tv_nama);
        etNilai = findViewById(R.id.et_nilai);
        btnSimpan = findViewById(R.id.btn_simpan);
        rvNilai = findViewById(R.id.rv_nilai);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvNilai.addItemDecoration(new DividerItemDecoration(rvNilai.getContext(),
                ((LinearLayoutManager) layoutManager).getOrientation()));
        rvNilai.setLayoutManager(layoutManager);

        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.mata_kuliah, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnMataKuliah.setAdapter(spinnerAdapter);

        tvNama.setText(Bantuan.nama);
        Glide.with(this).load(Bantuan.urlGambar).into(fotoProfile);

        fotoProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NilaiActivity.this, ZoomActivity.class);
                intent.putExtra("url", Bantuan.urlGambar);
                startActivity(intent);
            }
        });


        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!etNilai.getText().toString().isEmpty()) {

                    if (!isNew) {

                        NilaiModel nilaiModel = new NilaiModel(idNilaiEdit, spnMataKuliah.getSelectedItem().toString(),
                                etNilai.getText().toString());

                        databaseNilai.child(idNilaiEdit).setValue(nilaiModel).addOnSuccessListener(
                                new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        isNew = true;
                                        etNilai.setText("");
                                        spnMataKuliah.setSelection(0);
                                        Toast.makeText(NilaiActivity.this, "Berhasil diupdate!", Toast.LENGTH_LONG).show();
                                    }
                                }).addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(NilaiActivity.this, "Gagal Tersimpan! " + e.getLocalizedMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                });
                    } else {
                        String id = databaseNilai.push().getKey();

                        NilaiModel nilaiModel = new NilaiModel(id, spnMataKuliah.getSelectedItem().toString(),
                                etNilai.getText().toString());

                        databaseNilai.child(id).setValue(nilaiModel).addOnSuccessListener(
                                new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        isNew = true;
                                        etNilai.setText("");
                                        spnMataKuliah.setSelection(0);
                                        Toast.makeText(NilaiActivity.this, "Berhasil tersimpan!", Toast.LENGTH_LONG).show();
                                    }
                                }).addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(NilaiActivity.this, "Gagal Tersimpan! " + e.getLocalizedMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                } else {
                    Toast.makeText(NilaiActivity.this, "Form tidak boleh kosong!", Toast.LENGTH_LONG).show();
                }
            }
        });

        databaseNilai.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!nilaiModelList.isEmpty())
                    nilaiModelList.clear();

                for (DataSnapshot dataNilai : dataSnapshot.getChildren()) {
                    nilaiModelList.add(dataNilai.getValue(NilaiModel.class));
                }

                NilaiAdapter nilaiAdapter = new NilaiAdapter(NilaiActivity.this, nilaiModelList,
                        new NilaiAdapter.EditDataListener() {
                            @Override
                            public void onEditData(NilaiModel nilaiModel, int pos) {
                                isNew = false;
                                idNilaiEdit = nilaiModel.getId();

                                etNilai.setText(nilaiModel.getNilai());
                                int spinnerPosition = spinnerAdapter.getPosition(nilaiModel.getMatkul());
                                spnMataKuliah.setSelection(spinnerPosition);

                            }
                        },
                        new NilaiAdapter.DeleteDataListener() {
                            @Override
                            public void onDeleteData(NilaiModel nilaiModel, int pos) {
                                databaseNilai.child(nilaiModel.getId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(NilaiActivity.this, "Data berhasil dihapus.", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        });
                rvNilai.setAdapter(nilaiAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
