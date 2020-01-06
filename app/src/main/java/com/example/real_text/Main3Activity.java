package com.example.real_text;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
public class Main3Activity extends AppCompatActivity {
    Button button, button1;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    Real_Adaptor real_adaptor;
    List<Real_Model> modelList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main3 );
        button = findViewById( R.id.button_recycler );
        button1 = findViewById( R.id.button_recycler_next );
        recyclerView = findViewById( R.id.f_recycle );
        recyclerView.setHasFixedSize( true );
        recyclerView.setLayoutManager( new LinearLayoutManager( this ) );
        modelList = new ArrayList<>(  );
        databaseReference = FirebaseDatabase.getInstance().getReference();
        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            Real_Model model = ds.getValue( Real_Model.class );
                            modelList.add( model );
                        }
                        real_adaptor = new Real_Adaptor( Main3Activity.this, modelList );
                        recyclerView.setAdapter( real_adaptor );
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText( getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT ).show();
                    }
                } );
            }
        } );
        
        button1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( Main3Activity.this, Main4Activity.class );
                startActivity( intent );
            }
        } );
    }
}
