package com.gookkis.firebaseuas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView rvMahasiswa;
    Button btnTambahMahasiswa;
    List<MahasiswaModel> mahasiswaModelList = new ArrayList<>();

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference databaseMahasiswa = database.getReference("mahasiswa");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvMahasiswa = findViewById(R.id.rv_mahasiswa);
        btnTambahMahasiswa = findViewById(R.id.btn_tmbh_mahasiswa);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvMahasiswa.addItemDecoration(new DividerItemDecoration(rvMahasiswa.getContext(),
                ((LinearLayoutManager) layoutManager).getOrientation()));
        rvMahasiswa.setLayoutManager(layoutManager);

        btnTambahMahasiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FormActivity.class);
                Bantuan.isNew = true;
                startActivity(intent);
            }
        });

        databaseMahasiswa.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!mahasiswaModelList.isEmpty())
                    mahasiswaModelList.clear();

                for (DataSnapshot dataMahasiswa : dataSnapshot.getChildren()) {
                    mahasiswaModelList.add(dataMahasiswa.getValue(MahasiswaModel.class));
                }

                MahasiswaAdapter mahasiswaAdapter = new MahasiswaAdapter(MainActivity.this, mahasiswaModelList,
                        new MahasiswaAdapter.EditDataListener() {
                            @Override
                            public void onEditData(MahasiswaModel mahasiswaModel, int pos) {
                                Intent intent = new Intent(MainActivity.this, FormActivity.class);
                                Bantuan.isNew = false;
                                Bantuan.id = mahasiswaModel.getId();
                                Bantuan.nama = mahasiswaModel.getNama();
                                Bantuan.urlGambar = mahasiswaModel.getUrlGambar();
                                Bantuan.prodi = mahasiswaModel.getProdi();
                                startActivity(intent);
                            }
                        },
                        new MahasiswaAdapter.DeleteDataListener() {
                            @Override
                            public void onDeleteData(final MahasiswaModel mahasiswaModel, int pos) {

                                database.getReference("nilai").child(mahasiswaModel.getId()).removeValue().addOnSuccessListener(
                                        new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                databaseMahasiswa.child(mahasiswaModel.getId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(MainActivity.this, "Data berhasil dihapus.", Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }
                                        }
                                );
                            }
                        },
                        new MahasiswaAdapter.DetailDataListener() {
                            @Override
                            public void onDetailData(MahasiswaModel mahasiswaModel, int pos) {
                                Intent intent = new Intent(MainActivity.this, NilaiActivity.class);
                                Bantuan.id = mahasiswaModel.getId();
                                Bantuan.nama = mahasiswaModel.getNama();
                                Bantuan.urlGambar = mahasiswaModel.getUrlGambar();
                                Bantuan.prodi = mahasiswaModel.getProdi();
                                startActivity(intent);
                            }
                        });

                rvMahasiswa.setAdapter(mahasiswaAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
