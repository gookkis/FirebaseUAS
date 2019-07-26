package com.gookkis.firebaseuas;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MahasiswaAdapter extends RecyclerView.Adapter<MahasiswaAdapter.MyHolder> {
    Context context;
    List<MahasiswaModel> mahasiswaModel;
    EditDataListener editDataListener;
    DeleteDataListener deleteDataListener;
    DetailDataListener detailDataListener;


    public MahasiswaAdapter(Context context,
                            List<MahasiswaModel> mahasiswaModel,
                            EditDataListener editDataListener,
                            DeleteDataListener deleteDataListener,
                            DetailDataListener detailDataListener) {
        this.context = context;
        this.mahasiswaModel = mahasiswaModel;
        this.editDataListener = editDataListener;
        this.deleteDataListener = deleteDataListener;
        this.detailDataListener = detailDataListener;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mahasiswa, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {

        final MahasiswaModel mahasiswa = mahasiswaModel.get(position);

        holder.tvNama.setText(mahasiswa.getNama());
        holder.tvProdi.setText(mahasiswa.getProdi());
        Glide.with(context).load(mahasiswa.getUrlGambar()).into(holder.fotoMahasiswa);

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDataListener.onEditData(mahasiswa, position);
            }
        });

        holder.btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDataListener.onDeleteData(mahasiswa, position);
            }
        });

        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailDataListener.onDetailData(mahasiswa, position);
            }
        });

        holder.fotoMahasiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ZoomActivity.class);
                intent.putExtra("url", mahasiswa.getUrlGambar());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mahasiswaModel.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        CircleImageView fotoMahasiswa;
        TextView tvNama, tvProdi;
        Button btnEdit, btnHapus, btnDetail;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            fotoMahasiswa = itemView.findViewById(R.id.profile_image);
            tvProdi = itemView.findViewById(R.id.tv_prodi);
            tvNama = itemView.findViewById(R.id.tv_nama);
            btnDetail = itemView.findViewById(R.id.btn_detail);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnHapus = itemView.findViewById(R.id.btn_hapus);
        }
    }

    public interface EditDataListener {
        void onEditData(MahasiswaModel mahasiswaModel, int pos);
    }

    public interface DeleteDataListener {
        void onDeleteData(MahasiswaModel mahasiswaModel, int pos);
    }

    public interface DetailDataListener {
        void onDetailData(MahasiswaModel mahasiswaModel, int pos);
    }
}
