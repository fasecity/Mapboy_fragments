package mymapstut.com.example.admin.mapboy;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    //permisission vars
    public static final int MY_LOCATION_PERMMISSION = 101;
    private boolean permissionIsGranted=false;
    //map vars
    GoogleMap mMap;
    boolean mapReady = false;
    MarkerOptions mp;
    MarkerOptions md;
    //loctaion services vars////////////////////////
    private final String LOG_TAG = "Moes_testapp";
    private TextView txtoutput;
    private GoogleApiClient mGoogleapiClient;
    private LocationRequest mLocationRequest;
    /////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //PERMISION SETTINGGS
        Button pembtn = (Button) findViewById(R.id.permbtn);
        pembtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String [] {Manifest.permission.ACCESS_FINE_LOCATION},MY_LOCATION_PERMMISSION );
                }
                else{
                    permissionIsGranted = true;
                }

            }
        });

        // instatiate mylocation client needs this context and a builder()
        mGoogleapiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        txtoutput = (TextView) findViewById(R.id.txtOutput);

        // mGoogleapiClient.connect();


        ///instansiate markers add icon method to make custom markers
        mp = new MarkerOptions()
                .position(new LatLng(43.656729, -79.377162)).title("Home")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.worker));

        md = new MarkerOptions()
                .position(new LatLng(43.733092, -79.264254)).title("Dad's");


        //button for search & input

        Button searchBtn = (Button) findViewById(R.id.searchbtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mapReady) {
                    try {
                        geoLocate(v);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        ///////////////////////////

        //buttons
        Button satBtn = (Button) findViewById(R.id.Satilitebtn);
        satBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mapReady) {
                     mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

                }
            }
        });
        Button roadBtn = (Button) findViewById(R.id.RoadBtn);
        roadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mapReady) {

                    LatLng latLngToronto = new LatLng(43.733092, -79.264254);
                    CameraPosition target = CameraPosition.builder().target(latLngToronto).zoom(17).tilt(65).build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(target), 5000, null);
                    // mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

                }
            }
        });

        Button hybridBtn = (Button) findViewById(R.id.HybridBtn);
        hybridBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mapReady) {
                    LatLng latLngToronto = new LatLng(43.656729, -79.377162);
                    CameraPosition target = CameraPosition.builder().target(latLngToronto).zoom(17).tilt(65).build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(target), 5000, null);
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleapiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleapiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapReady = true;
        mMap = googleMap;
        LatLng latLngToronto = new LatLng(43.733092, -79.264254);
        LatLng latLnghome = new LatLng(43.656729, -79.377162);

        CameraPosition target = CameraPosition.builder().target(latLngToronto).zoom(5).tilt(65).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(target));
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //add markers and
        //instantiate
        mMap.addMarker(mp);
        mMap.addMarker(md);

        //add polyline
        googleMap.addPolyline(new PolylineOptions().geodesic(true).add(latLngToronto)
                .add(latLnghome));

        //add circle with color
        googleMap.addCircle(new CircleOptions().center(latLngToronto)
                .radius(10000)
                .strokeColor(Color.green(6)).fillColor(Color.argb(64, 0, 255, 0)));

    }


    //go to location method
    private void goToLocation(double lat, double lng, float zoom) {
        mapReady = true;
        LatLng latlng = new LatLng(lat, lng);
        CameraPosition target = CameraPosition.builder().target(latlng).zoom(zoom).tilt(40).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(target));
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

    }

    //hides keyoard
    private void hideSoftKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    //geocoder method
    public void geoLocate(View view) throws IOException {
        hideSoftKeyboard(view);
        EditText inputTxt = (EditText) findViewById(R.id.editText);

        String searchString = inputTxt.getText().toString();

        Geocoder gc = new Geocoder(this);//thows exeption change method signiture
        List<android.location.Address> list = gc.getFromLocationName(searchString, 1);

        if (list.size() > 0) {
            //get address from list index 0
            android.location.Address address = list.get(0);

            //get name of adress
            String locality = address.getLocality();

            Toast.makeText(this, "You searched: " + locality, Toast.LENGTH_SHORT).show();

            //get lang ang lng from adtress
            double lat = address.getLatitude();
            double lng = address.getLongitude();

            goToLocation(lat, lng, 15);


        }


    }

    //My location methods overides
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(this, "onconnected", Toast.LENGTH_SHORT).show();
        mLocationRequest = LocationRequest.create();//create
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);//high acuracy
        mLocationRequest.setInterval(1000);//update evrey second

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String [] {Manifest.permission.ACCESS_FINE_LOCATION},MY_LOCATION_PERMMISSION );
            }

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleapiClient, mLocationRequest, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_LOCATION_PERMMISSION:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    permissionIsGranted = true;
                    Toast.makeText(this, "Loacation permission Granted", Toast.LENGTH_SHORT).show();
                }
                else {
                    permissionIsGranted=false;
                    Toast.makeText(this, "Loacation permission NOT! Granted", Toast.LENGTH_SHORT).show();
                    txtoutput.setText("Permsission not granted");

                }
                break;
        }




    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "suspended", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onLocationChanged(Location location) {

    double lat = location.getLatitude();
     double lng = location.getLongitude();
        txtoutput.setText(lat + "," + lng);
    }
}
