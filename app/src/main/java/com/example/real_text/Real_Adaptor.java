package com.example.real_text;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
public class Real_Adaptor extends RecyclerView.Adapter<Real_Adaptor.ViewHolder> {
    
    private Context context;
    private List<Real_Model> list ;

    public Real_Adaptor(Context context, List<Real_Model> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from( context );
        View view = layoutInflater.inflate( R.layout.data, parent, false );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Real_Model realModel = list.get( position );
        holder.textView1.setText( realModel.getName() );
        holder.textView2.setText( realModel.getEmail() );
        Glide.with( context ).load( realModel.getImageUrl() ).into( holder.imageView);
        holder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        } );
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            textView1 = itemView.findViewById( R.id.edit_One );
            textView2 = itemView.findViewById( R.id.edit_Two );
            imageView = itemView.findViewById( R.id.image_one );
        }
    }
}
