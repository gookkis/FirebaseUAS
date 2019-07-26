package com.gookkis.firebaseuas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FormActivity extends AppCompatActivity {

    Spinner spnProdi;
    EditText etNama, etURLGambar;
    Button btnSimpan;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference databaseMahasiswa = database.getReference("mahasiswa");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        spnProdi = findViewById(R.id.spn_prodi);
        etNama = findViewById(R.id.et_nama);
        etURLGambar = findViewById(R.id.et_url_gambar);
        btnSimpan = findViewById(R.id.btn_simpan);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.prodi_option, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnProdi.setAdapter(adapter);

        if (!Bantuan.isNew) {
            etNama.setText(Bantuan.nama);
            etURLGambar.setText(Bantuan.urlGambar);

            int spinnerPosition = adapter.getPosition(Bantuan.prodi);
            spnProdi.setSelection(spinnerPosition);
        }


        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etNama.getText().toString().isEmpty() && !etURLGambar.getText().toString().isEmpty()) {
                    if (!Bantuan.isNew) {

                        MahasiswaModel mahasiswaModel = new MahasiswaModel(Bantuan.id, etNama.getText().toString(), spnProdi.getSelectedItem().toString(),
                                etURLGambar.getText().toString());

                        databaseMahasiswa.child(Bantuan.id).setValue(mahasiswaModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(FormActivity.this, "Berhasil diupdate!", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(FormActivity.this, "Gagal Tersimpan! " + e.getLocalizedMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        });


                    } else {

                        String id = databaseMahasiswa.push().getKey();
                        MahasiswaModel mahasiswaModel = new MahasiswaModel(id, etNama.getText().toString(), spnProdi.getSelectedItem().toString(),
                                etURLGambar.getText().toString());
                        databaseMahasiswa.child(id).setValue(mahasiswaModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(FormActivity.this, "Berhasil tersimpan!", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(FormActivity.this, "Gagal Tersimpan! " + e.getLocalizedMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } else {
                    Toast.makeText(FormActivity.this, "Form tidak boleh kosong!", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
