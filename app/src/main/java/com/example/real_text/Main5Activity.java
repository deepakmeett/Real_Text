package com.example.real_text;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
public class Main5Activity extends AppCompatActivity {

    ImageView imageView;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    ProgressBar progressBar;
    Button button, buttonNext;
    Uri uri;
    private StorageTask upload_task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main5 );
        imageView = findViewById( R.id.img_take_button );
        button = findViewById( R.id.upload_image );
        buttonNext = findViewById( R.id.nextBtn );
        progressBar = findViewById( R.id.progressBar );
        storageReference = FirebaseStorage.getInstance().getReference( "uploads" );
        databaseReference = FirebaseDatabase.getInstance().getReference( "uploads" );
        imageView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType( "image/*" );
                intent.setAction( Intent.ACTION_GET_CONTENT );
                startActivityForResult( intent, 1 );
            }
        } );
        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (upload_task!=null&&upload_task.isInProgress()){
                    Toast.makeText( Main5Activity.this, "File is being uploaded. Please wait a moment!", Toast.LENGTH_SHORT ).show();
                }else {
                    uploadfile();
                }
            }
        } );
        
        buttonNext.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main5Activity.this, Main6Activity.class);
                startActivity( intent );
            }
        } );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (resultCode == RESULT_OK && requestCode == 1 && data != null && data.getData() != null) {
            uri = data.getData();
//            Picasso.get().load( uri ).into( imageView ); //this and 
            imageView.setImageURI( uri );                 // this work same.
        }
    }

    private String getFileExtention(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType( contentResolver.getType( uri ) );
    }

    private void uploadfile() {
        if (uri != null) {
            StorageReference fileRefernce = storageReference.child( System.currentTimeMillis()
                                                                    + "." + getFileExtention( uri ) );
            upload_task = fileRefernce.putFile( uri ).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler(  );
                    handler.postDelayed( new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress( 0 );
                        }
                    }, 500 );
                    Toast.makeText( Main5Activity.this, "upload successful", Toast.LENGTH_SHORT ).show();
                }
            } ).addOnFailureListener( new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText( Main5Activity.this, e.getMessage(), Toast.LENGTH_SHORT ).show();
                }
            } ).addOnProgressListener( new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressBar.setProgress( (int) progress );
                }
            } );
        } else {
            Toast.makeText( this, "No file selected", Toast.LENGTH_SHORT ).show();
        }

    }
}
