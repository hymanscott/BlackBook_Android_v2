package com.aptmobility.lynx;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.aptmobility.helper.DatabaseHelper;
import com.aptmobility.model.LocationsDistance;
import com.aptmobility.model.TestingLocations;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class testing_nearestTestingLocation extends Activity implements LocationListener, GoogleMap.OnMarkerDragListener {
    DatabaseHelper db;
    MapView mapView;
    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */

    List<LocationsDistance> Locations_DistanceArray = new ArrayList<LocationsDistance>();
    LatLngBounds.Builder bounds;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_nearest_testing_location);
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        // Requesting User Permission
        if(Build.VERSION.SDK_INT < 23 || (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)){
        setUpMapIfNeeded();
        mMap.setOnMarkerDragListener(this);
        }

        //marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.my_marker_icon)));


    }


    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        setUpMapIfNeeded();
        if (LynxManager.onPause == true) {
            Intent lockscreen = new Intent(this, PasscodeUnlockActivity.class);
            startActivity(lockscreen);
        }
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();
//        db.close();

    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {

            try {
                MapsInitializer.initialize(this);
            } catch (Exception e) {
                e.printStackTrace();
            }

            /*mMap = mapView.getMap();
            mMap.setMyLocationEnabled(true);*/
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }

            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {

                    //mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 30));
                    // To set New zoom location
                    /*Collections.sort(Locations_DistanceArray, LocationsDistance.LocDist);
                    Log.v("Loc_DistanceArrayLen", String.valueOf(Locations_DistanceArray.size()));
                    int i = 0;
                    LatLng latlng1= null;
                    LatLng latlng2= null;
                    for (LocationsDistance locationsDistance : Locations_DistanceArray) {
                            if(i==0){
                                latlng2 = new LatLng(locationsDistance.getLatitude(), locationsDistance.getLongitude());
                            } else if(i==LynxManager.minMarker){
                                latlng1 = new LatLng(locationsDistance.getLatitude(), locationsDistance.getLongitude());
                            }
                        i++;
                    }
                    LatLngBounds newZoomlocation = new LatLngBounds(
                            latlng1, latlng2);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newZoomlocation.getCenter(), 5));*/
                }
            });
        }
    }

    private void setUpMap() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);//use of location services by firstly defining location manager.
        String provider = lm.getBestProvider(new Criteria(), true);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location loc = lm.getLastKnownLocation(provider);
        Log.v("LocationEnables", String.valueOf(loc));

        GetLocation gt = new GetLocation(this);
        if (loc != null) {
            onLocationChanged(loc);
        }else{
            if(gt.getLocation()!=null){
                Log.v("LocationGetlocation", String.valueOf(gt.getLocation()));
                onLocationChanged(gt.getLocation());
            }
        }
    }

    @Override
    public void onLocationChanged(Location currentlocation) {
        LatLng latlng = new LatLng(currentlocation.getLatitude(), currentlocation.getLongitude());// This methods gets the users current longitude and latitude.
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));//Moves the camera to users current longitude and latitude
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, (float) 7.6));//Animates camera and zooms to preferred state on the user's current location.
        MarkerOptions marker_yourLoc = new MarkerOptions().position(latlng).draggable(true);
        marker_yourLoc.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
        mMap.addMarker(marker_yourLoc);
        getTestingLocations(currentlocation);

        Collections.sort(Locations_DistanceArray, LocationsDistance.LocDist);

        int marker_count = 0;
        bounds = new LatLngBounds.Builder();
        for (LocationsDistance locationsDistance : Locations_DistanceArray) {
            if (marker_count < LynxManager.maxMarker && locationsDistance.getDistance() < LynxManager.maxDistance) {
                latlng = new LatLng(locationsDistance.getLatitude(), locationsDistance.getLongitude());
                MarkerOptions marker = new MarkerOptions().position(latlng).title(String.valueOf(locationsDistance.getLocation_distance_id())).snippet("Distance in Miles : " + locationsDistance.getDistance());
                mMap.addMarker(marker);
                bounds.include(new LatLng(locationsDistance.getLatitude(), locationsDistance.getLongitude()));
                marker_count++;
            }
        }

        if (marker_count == 0) {
            for (int i = 0; i < LynxManager.minMarker; i++) {
                latlng = new LatLng(Locations_DistanceArray.get(i).getLatitude(), Locations_DistanceArray.get(i).getLongitude());
                MarkerOptions marker = new MarkerOptions().position(latlng).title(String.valueOf(Locations_DistanceArray.get(i).getLocation_distance_id())).snippet("Distance in Miles : " + Locations_DistanceArray.get(i).getDistance());
                mMap.addMarker(marker);
                bounds.include(new LatLng(Locations_DistanceArray.get(i).getLatitude(), Locations_DistanceArray.get(i).getLongitude()));
            }
        }
    }

    public void getTestingLocations(Location currentlocation) {
        Locations_DistanceArray.clear();
        db = new DatabaseHelper(this);
        List<TestingLocations> locationsList = db.getAllTestingLocations();
        for (TestingLocations location : locationsList) {
            Location testingLocation = new Location("Testing Location");
            testingLocation.setLatitude(Double.valueOf(location.getLatitude()));
            testingLocation.setLongitude(Double.valueOf(location.getLongitude()));

            //distance in meters
            double distance_inMeters = currentlocation.distanceTo(testingLocation);

            // converting into miles (1m = 0.000621371 mi)
            int distance_inMiles = (int) (distance_inMeters * 0.000621371);

            LocationsDistance location_distance = new LocationsDistance(location.getTesting_location_id(), Double.parseDouble(location.getLatitude()), Double.parseDouble(location.getLongitude()), distance_inMiles, location.getName());
            Locations_DistanceArray.add(location_distance);
        }

        //custom info window
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {


                // Getting view from the layout file info_window_layout
                View v = getLayoutInflater().inflate(R.layout.info_window, null);

                // Getting the position from the marker
                LatLng latLng = marker.getPosition();

                TextView tv_title = (TextView) v.findViewById(R.id.infoWindow_Title);
                TextView tv_address = (TextView) v.findViewById(R.id.infoWindow_Address);
                TextView tv_phone = (TextView) v.findViewById(R.id.infoWindow_Phone);
                TextView tv_url = (TextView) v.findViewById(R.id.infoWindow_url);
                TextView tv_distance = (TextView) v.findViewById(R.id.infoWindow_distance);

                if (marker.getTitle() != null) {
                    int id = Integer.parseInt(marker.getTitle());
                    TestingLocations testingLocations = db.getTestingLocationbyID(id);
                    tv_title.setText(testingLocations.getName());
                    tv_address.setText(testingLocations.getAddress());
                    tv_phone.setText("Phone:" + testingLocations.getPhone_number());
                    if (testingLocations.getUrl().isEmpty()) {
                        tv_url.setVisibility(View.GONE);
                    }
                    tv_url.setText(testingLocations.getUrl());
                    tv_distance.setText(marker.getSnippet());
                } else {
                    //tv_address.setText("Long press the marker to drag and find the nearest Testing Location");
                    return null;
                }
                // Returning the view containing InfoWindow contents

                return v;

            }
        });
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                int id = Integer.parseInt(marker.getTitle());
                TestingLocations testingLocations = db.getTestingLocationbyID(id);

                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(testingLocations.getUrl()));
                    startActivity(browserIntent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), (float) 7.6));
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        mMap.clear();
        MarkerOptions marker_yourLoc = new MarkerOptions().position(marker.getPosition()).draggable(true);
        marker_yourLoc.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
        mMap.addMarker(marker_yourLoc);
        Location currentlocation = new Location("ss");
        currentlocation.setLatitude(marker.getPosition().latitude);
        currentlocation.setLongitude(marker.getPosition().longitude);
        onLocationChanged(currentlocation);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), (float) 7.6));//Animates camera and zooms to preferred state on the user's current location.
    }


}
