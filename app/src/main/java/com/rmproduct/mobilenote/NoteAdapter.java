package com.rmproduct.mobilenote;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

    /*public class MyViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener {

        TextView dateTv, timeTv, titleTv, bodyTv;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);

            dateTv=itemView.findViewById(R.id.noteDate);
            timeTv=itemView.findViewById(R.id.noteTime);
            titleTv=itemView.findViewById(R.id.noteTitle);
            bodyTv=itemView.findViewById(R.id.noteBody);

        }

        @Override
        public void onClick(View view) {

            Intent intent=new Intent(context, ViewNote.class);

            int mPosition = getLayoutPosition();

            Model model=modelList.get(mPosition);

            intent.putExtra("note_id", model.getId());


        }
    }*/

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final Model model=modelList.get(position);

        holder.dateTv.setText(model.getDate());
        holder.timeTv.setText(model.getTime());
        holder.titleTv.setText(model.getTitle());
        holder.bodyTv.setText(model.getBody());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ViewNote.class);
                intent.putExtra("note_id", model.getId());
                intent.putExtra("note_title", model.getTitle());
                intent.putExtra("note_body", model.getBody());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }


}
