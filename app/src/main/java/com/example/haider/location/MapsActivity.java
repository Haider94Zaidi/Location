package com.example.haider.location;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap,gMap1;
    RadioButton radiobtnselected;
    double d=2;
    RadioGroup neargroup;
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters

    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in Milliseconds
    double longi,lat;
    protected LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);




        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(

                LocationManager.GPS_PROVIDER,

                MINIMUM_TIME_BETWEEN_UPDATES,

                MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,

                new MyLocationListener()

        );


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);




    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        GPSTracker gps = new GPSTracker(MapsActivity.this);
        Location loc;
        loc = gps.getLocation();

        String jsonstr = "[{\"lat\" : 24.896037,\"long\":67.081433,\"place\":\"National Stadium\"},\n" +
                "{\"lat\" : 24.882451,\"long\":67.069389,\"place\":\"Bahadurabad\"},\n" +
                "{\"lat\" : 24.899405,\"long\":67.072995,\"place\":\"Civic Center\"},\n" +
                "{\"lat\" : 24.887244,\"long\":67.076858,\"place\":\"Dhoraji\"},\n" +
                "{\"lat\" : 24.917570,\"long\":67.097010,\"place\":\"Nepa Chowrangi\"}]";

        try {
            JSONArray jarr = new JSONArray(jsonstr);
            int len = jarr.length();
            JSONObject obj;
            double dd;
            for (int i=0;i<len;i++) {
                obj = jarr.getJSONObject(i);
                double lat = obj.getDouble("lat");
                double longi = obj.getDouble("long");
                dd = getDistanceFromLatLonInKm(loc.getLatitude(), loc.getLongitude(), lat, longi);
                if (dd <= d) {


                    mMap = googleMap;
                    LatLng sydney1 = new LatLng(lat, longi);
                    mMap.addMarker(new MarkerOptions().position(sydney1).title(obj.getString("place")).snippet("haider"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney1));

                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


//        mMap = googleMap;
//        LatLng sydney1 = new LatLng(24.896037,67.081433);
//        mMap.addMarker(new MarkerOptions().position(sydney1).title("Stadium"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney1));

//        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.user_current_location_icon);

        LatLng sydney = new LatLng(loc.getLatitude(), loc.getLongitude());
        mMap.addMarker(new MarkerOptions().position(sydney).title("You are Here").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14.0f));


        mMap.setOnMarkerClickListener(this);

//        Double d = getDistanceFromLatLonInKm(loc.getLatitude(), loc.getLongitude(), 24.879520, 67.162636);
//        if (d <= 9) {
//
//            LatLng sydney1 = new LatLng(24.8914778, 67.0742797);
//            mMap.addMarker(new MarkerOptions().position(sydney1).title("Agha Khan"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney1));
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(14.0f));
//
//            LatLng sydney2 = new LatLng(24.879520, 67.162636);
//            mMap.addMarker(new MarkerOptions().position(sydney2).title("Shah Faisal"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney2));
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(14.0f));
//
//            LatLng sydney = new LatLng(loc.getLatitude(), loc.getLongitude());
//            mMap.addMarker(new MarkerOptions().position(sydney).title("Mashriq Center"));
//
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(14.0f));
//
//        } else {
////        mMap.animateCamera(CameraUpdateFactory.zoomTo(14.0f));
//
//            LatLng sydney1 = new LatLng(24.8914778, 67.0742797);
//            mMap.addMarker(new MarkerOptions().position(sydney1).title("Agha Khan"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney1));
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(14.0f));
//
//        }
    }


    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }
    private double getDistanceFromLatLonInKm(double lat1,double lon1,double lat2,double lon2) {
        double R = 6371; // Radius of the earth in km
        double dLat = deg2rad(lat2-lat1);  // deg2rad below
        double dLon = deg2rad(lon2-lon1);
        double a =
                Math.sin(dLat/2) * Math.sin(dLat/2) +
                        Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
                                Math.sin(dLon/2) * Math.sin(dLon/2)
                ;
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c; // Distance in km
        return d;
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
                LatLng ln;
                ln = marker.getPosition();
                String name = marker.getTitle();
        String snippet = marker.getSnippet();
//                Toast.makeText(this,"Latitude "+Double.toString(ln.latitude)+" Longitude "+Double.toString(ln.longitude), Toast.LENGTH_SHORT).show();
                Toast.makeText(MapsActivity.this, name+""+snippet, Toast.LENGTH_SHORT).show();
                return true;
    }

    private class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location location) {

            String message = String.format(

                    "New Location \n Longitude: %1$s \n Latitude: %2$s",

                    location.getLongitude(), location.getLatitude()

            );

//            Toast.makeText(MapsActivity.this, message, Toast.LENGTH_LONG).show();
            longi = location.getLongitude(); lat = location.getLatitude();
        }

        public void onStatusChanged(String s, int i, Bundle b) {

            Toast.makeText(MapsActivity.this, "Provider status changed",

                    Toast.LENGTH_LONG).show();

        }



        public void onProviderDisabled(String s) {

            Toast.makeText(MapsActivity.this,

                    "Provider disabled by the user. GPS turned off",

                    Toast.LENGTH_LONG).show();

        }



        public void onProviderEnabled(String s) {

            Toast.makeText(MapsActivity.this,

                    "Provider enabled by the user. GPS turned on",

                    Toast.LENGTH_LONG).show();

        }



    }

}

