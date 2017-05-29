package mymapstut.com.example.admin.mapboy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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

        ///instansiate markers
        mp = new MarkerOptions()
                .position(new LatLng(43.656729, -79.377162)).title("Home");

        md = new MarkerOptions()
                .position(new LatLng(43.733092, -79.264254)).title("Dad's");

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
        CameraPosition target = CameraPosition.builder().target(latLngToronto).zoom(10).tilt(65).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(target));
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //add markers and
        //instantiate

        mMap.addMarker(mp);
        mMap.addMarker(md);


    }
}
