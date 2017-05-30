package mymapstut.com.example.admin.mapboy;

import android.graphics.Color;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.identity.intents.Address;
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

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    boolean mapReady= false;
    MarkerOptions mp;
    MarkerOptions md;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
                if(mapReady){
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                }
            }
        });
        Button roadBtn = (Button) findViewById(R.id.RoadBtn);
        roadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mapReady){

                    LatLng latLngToronto = new LatLng(43.733092, -79.264254);
                    CameraPosition target = CameraPosition.builder().target(latLngToronto).zoom(17).tilt(65).build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(target),5000,null);
                   // mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

                }
            }
        });

        Button hybridBtn = (Button) findViewById(R.id.HybridBtn);
        hybridBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mapReady){
                    LatLng latLngToronto = new LatLng(43.656729, -79.377162);
                    CameraPosition target = CameraPosition.builder().target(latLngToronto).zoom(17).tilt(65).build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(target),5000,null);
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

                }
            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapReady= true;
        mMap= googleMap;
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
                .strokeColor(Color.green(6)).fillColor(Color.argb(64,0,255,0)));

    }
    //go to location method
    private  void goToLocation(double lat,double lng,float zoom){
        mapReady= true;
        LatLng latlng = new LatLng(lat,lng);
        CameraPosition target = CameraPosition.builder().target(latlng).zoom(zoom).tilt(40).build();
         mMap.moveCamera(CameraUpdateFactory.newCameraPosition(target));
         mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

    }
    //hides keyoard
    private void hideSoftKeyboard(View v){
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(),0);
    }
    //geocoder method
    public void geoLocate(View view) throws IOException {
        hideSoftKeyboard(view);
        EditText inputTxt = (EditText) findViewById(R.id.editText);

        String searchString = inputTxt.getText().toString();

        Geocoder gc = new Geocoder(this);//thows exeption change method signiture
        List<android.location.Address> list = gc.getFromLocationName(searchString,1);

        if (list.size()>0){
            //get address from list index 0
            android.location.Address address = list.get(0);

            //get name of adress
            String locality = address.getLocality();

            Toast.makeText(this, "You searched: " + locality, Toast.LENGTH_SHORT).show();

            //get lang ang lng from adtress
            double lat = address.getLatitude();
            double lng = address.getLongitude();

            goToLocation(lat,lng,15);


        }



    }
}
