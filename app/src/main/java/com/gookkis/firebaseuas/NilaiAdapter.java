package com.gookkis.firebaseuas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NilaiAdapter extends RecyclerView.Adapter<NilaiAdapter.MyHolder> {
    Context context;
    List<NilaiModel> nilaiModel;
    EditDataListener editDataListener;
    DeleteDataListener deleteDataListener;

    public NilaiAdapter(Context context,
                        List<NilaiModel> nilaiModel,
                        EditDataListener editDataListener,
                        DeleteDataListener deleteDataListener) {
        this.context = context;
        this.nilaiModel = nilaiModel;
        this.editDataListener = editDataListener;
        this.deleteDataListener = deleteDataListener;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_nilai, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {

        final NilaiModel nilai = nilaiModel.get(position);

        holder.tvNilai.setText(nilai.getNilai());
        holder.tvMatkul.setText(nilai.getMatkul());

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDataListener.onEditData(nilai, position);
            }
        });

        holder.btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDataListener.onDeleteData(nilai, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return nilaiModel.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView tvMatkul, tvNilai;
        Button btnEdit, btnHapus;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            tvNilai = itemView.findViewById(R.id.tv_nilai);
            tvMatkul = itemView.findViewById(R.id.tv_matkul);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnHapus = itemView.findViewById(R.id.btn_hapus);
        }
    }

    public interface EditDataListener {
        void onEditData(NilaiModel nilaiModel, int pos);
    }

    public interface DeleteDataListener {
        void onDeleteData(NilaiModel nilaiModel, int pos);
    }

}
