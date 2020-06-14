package com.example.real_text;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class Main2Activity extends AppCompatActivity {

    TextView textView;
    Button button, button1;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main2 );
        textView = findViewById( R.id.edit_retrieve );
        button = findViewById( R.id.button_Retrieve );
        button1 = findViewById( R.id.retrieve_next );
        databaseReference = FirebaseDatabase.getInstance().getReference().child( "Data" );
       
        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.child( "name" ).getValue().toString();
                        textView.setText( name );
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText( Main2Activity.this, "User didn't press the Text retrieve Button", Toast.LENGTH_SHORT ).show();
                    }
                } );
            }
        } );
        button1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( Main2Activity.this, Main3Activity.class );
                startActivity( intent );
            }
        } );
    }
}