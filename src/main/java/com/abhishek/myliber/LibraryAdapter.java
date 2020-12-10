package com.abhishek.myliber;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.MyViewHolder> {
    private ArrayList<Library1> library1ArrayList;
    public  LibraryAdapter(ArrayList<Library1> library1ArrayList){
        this.library1ArrayList=library1ArrayList;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView nametext;
        public MyViewHolder(final View view){
            super(view);
            nametext=view.findViewById(R.id.tv1);

        }
    }
    @NonNull
    @Override
    public LibraryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LibraryAdapter.MyViewHolder holder, int position) {
        String name=library1ArrayList.get(position).name;
        holder.nametext.setText(name);

    }

    @Override
    public int getItemCount() {
        return library1ArrayList.size();
    }
}
