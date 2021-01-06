package com.rmproduct.locknote.MobileNote;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rmproduct.locknote.R;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder> {

    private Context context;
    private List<Model> modelList;

    public NoteAdapter(Context context, List<Model> modelList) {
        this.context = context;
        this.modelList = modelList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView dateTv, timeTv, titleTv, bodyTv;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);

            dateTv = itemView.findViewById(R.id.noteDate);
            timeTv = itemView.findViewById(R.id.noteTime);
            titleTv = itemView.findViewById(R.id.noteTitle);
            bodyTv = itemView.findViewById(R.id.noteBody);

        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final Model model = modelList.get(position);

        holder.dateTv.setText(model.getDate());
        holder.timeTv.setText(model.getTime());
        holder.titleTv.setText(model.getTitle());
        holder.bodyTv.setText(model.getBody());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewNote.class);
                intent.putExtra("note_id", model.getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }


}
