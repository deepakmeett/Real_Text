package com.example.real_text;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
public class Main4Activity extends AppCompatActivity {
    ImageView imageView;
    DatabaseReference databaseReference;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main4 );
        imageView = findViewById( R.id.img_button );
        button = findViewById( R.id.image_to_next );
        databaseReference = FirebaseDatabase.getInstance().getReference().child( "Data" );

        imageView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String image_from_firebase = dataSnapshot.child( "imageUrl" ).getValue().toString();
                        Picasso.get().load( image_from_firebase).into( imageView );
                        Toast.makeText( getApplicationContext(), "Image has been set", Toast.LENGTH_SHORT ).show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText( Main4Activity.this, databaseError.getMessage(), Toast.LENGTH_SHORT ).show();
                    }
                } );
            }
        } );
        
        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( Main4Activity.this, Main5Activity.class);
                startActivity( intent );
            }
        } );
    }
}