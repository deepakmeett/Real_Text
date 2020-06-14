package com.example.real_text;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
public class MainActivity extends AppCompatActivity implements LocationListener {

    EditText editText1;
    Button button, button1, button2;
    DatabaseReference databaseReference;
    HashMap<String, String> take_data = new HashMap<>();
    List<Address> address = new ArrayList<>();
    ProgressBar progressBar;
    int sec = 1;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        editText1 = findViewById( R.id.edit_One );
        button = findViewById( R.id.buttonPanel );
        button1 = findViewById( R.id.button_next );
        button2 = findViewById( R.id.get_address );
        progressBar = findViewById( R.id.progress_horizontal );
        databaseReference = FirebaseDatabase.getInstance().getReference().child( "Data" );
        button.setOnClickListener( new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                runnable.run();
            }
        } );
        
        button1.setOnLongClickListener( new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent( getApplicationContext(), Main2Activity.class );
                startActivity( intent );
                return false;
            }
        } );
        button2.setOnClickListener( new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                handler.removeCallbacks( runnable );

            }
        } );
        button1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( MainActivity.this, Main6Activity.class );
                startActivity( intent );
            }
        } );
    }

    private Runnable runnable = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void run() {
            if (sec <= 10) {
                address.clear();
                getLocation();
                //this 
                take_data.put( "name", editText1.getText().toString() );
                take_data.put( "email", "deepak@gmail.com" );
                take_data.put( "imageUrl", "https://firebasestorage.googleapis.com/v0/b/realtext-ea479.appspot.com/o/uploads%2F1577884762876%2Cjpg?alt=media&token=26eeeeb8-d4e2-47bd-b8aa-4c67aeed1c89" );
//                try {
                    add_data();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                //to this code for real time database text send on firebase
                double progess = (10.0 * sec);
                progressBar.setProgress( (int) progess );
                sec++;
                handler.postDelayed( this, 10000 );
            } else {
                progressBar.setProgress( 0 );
                Toast.makeText( MainActivity.this, "handler is done", Toast.LENGTH_SHORT ).show();
            }

        }
    };

    public void add_data()/*throws Exception*/ {
        databaseReference.setValue( take_data ).addOnCompleteListener( new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText( MainActivity.this, "Text sent on Firebase", Toast.LENGTH_SHORT ).show();
                } else {
                    Toast.makeText( getApplicationContext(), "Text not sent on Firebase", Toast.LENGTH_SHORT ).show();
                }
            }
        } );
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getLocation() {
        LocationManager locationManager = (LocationManager) getSystemService( LOCATION_SERVICE );
        if (checkSelfPermission( Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && checkSelfPermission( Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 5000, 5, this );
    }

    @Override
    public void onLocationChanged(Location location) {
        Geocoder geocoder = new Geocoder( this, Locale.getDefault() );
        try {
            address = geocoder.getFromLocation( location.getLatitude(), location.getLongitude(), 1 );
        } catch (IOException e) {
            e.printStackTrace();
        }
        String traced_actual_location = address.get( 0 ).getAddressLine( 0 );
        String traced_actual_latitude = String.valueOf( location.getLatitude() );
        String traced_actual_longitude = String.valueOf( location.getLongitude() );
        final String locate = traced_actual_latitude + "° N," + " " + traced_actual_longitude + "° E";
        final String locatio = traced_actual_location + "\n" + locate;
        editText1.setText( locatio );
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText( getApplicationContext()
                , "Location and Internet connection should be enabled to view location"
                , Toast.LENGTH_SHORT ).show();
    }
}