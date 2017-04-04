package com.aptmobility.lynx;


import android.*;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aptmobility.helper.DatabaseHelper;
import com.aptmobility.model.LocationsDistance;
import com.aptmobility.model.TestingLocations;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class LYNXTestingLocation extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,GoogleMap.OnMarkerDragListener,
        LocationListener {

    MapView mMapView;
    private GoogleMap googleMap;
    private Location mylocation;
    private GoogleApiClient googleApiClient;
    private final static int REQUEST_CHECK_SETTINGS_GPS=0x3;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS=0x4;
    List<LocationsDistance> Locations_DistanceArray = new ArrayList<LocationsDistance>();
    LatLngBounds.Builder bounds;
    DatabaseHelper db;
    LocationManager mlocManager;
    public LYNXTestingLocation() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lynxtesting_location, container, false);
        setUpGClient();
        db = new DatabaseHelper(getActivity());
        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately
        mlocManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.setMyLocationEnabled(true);

                /*// For dropping a marker at a point on the Map
                LatLng sydney = new LatLng(12.968123, 77.654530);
                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/
            }
        });
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        checkPermissions();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    int count = 0;
    @Override
    public void onLocationChanged(Location location) {
        mylocation = location;
        if (mylocation != null) {
            Double latitude=mylocation.getLatitude();
            Double longitude=mylocation.getLongitude();
            //Or Do whatever you want with your location
            Log.v("LocationChanged",latitude+"--"+longitude);
        }
        /**/
        LatLng latlng = new LatLng(mylocation.getLatitude(), mylocation.getLongitude());// This methods gets the users current longitude and latitude.
        if(count==0){
            addDraggableMarker(latlng);
            count++;
        }

        getTestingLocations(mylocation);

        Collections.sort(Locations_DistanceArray, LocationsDistance.LocDist);

        int marker_count = 0;
        bounds = new LatLngBounds.Builder();
        for (LocationsDistance locationsDistance : Locations_DistanceArray) {
            if (marker_count < LynxManager.maxMarker && locationsDistance.getDistance() < LynxManager.maxDistance) {
                latlng = new LatLng(locationsDistance.getLatitude(), locationsDistance.getLongitude());
                MarkerOptions marker = new MarkerOptions().position(latlng).title(String.valueOf(locationsDistance.getLocation_distance_id())).snippet("Distance in Miles : " + locationsDistance.getDistance());
                googleMap.addMarker(marker);
                bounds.include(new LatLng(locationsDistance.getLatitude(), locationsDistance.getLongitude()));
                marker_count++;
            }
        }

        if (marker_count == 0) {
            for (int i = 0; i < LynxManager.minMarker; i++) {
                latlng = new LatLng(Locations_DistanceArray.get(i).getLatitude(), Locations_DistanceArray.get(i).getLongitude());
                MarkerOptions marker = new MarkerOptions().position(latlng).title(String.valueOf(Locations_DistanceArray.get(i).getLocation_distance_id())).snippet("Distance in Miles : " + Locations_DistanceArray.get(i).getDistance());
                googleMap.addMarker(marker);
                bounds.include(new LatLng(Locations_DistanceArray.get(i).getLatitude(), Locations_DistanceArray.get(i).getLongitude()));
            }
        }
        /**/
    }

    public void addDraggableMarker(LatLng latlng){
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));//Moves the camera to users current longitude and latitude
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, (float) 7.6));//Animates camera and zooms to preferred state on the user's current location.
        MarkerOptions marker_yourLoc = new MarkerOptions().position(latlng).draggable(true);
        marker_yourLoc.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker));
        googleMap.addMarker(marker_yourLoc);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), (float) 7.6));
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        //googleMap.clear();
        Toast.makeText(getActivity(), "draglistner", Toast.LENGTH_SHORT).show();
        MarkerOptions marker_yourLoc = new MarkerOptions().position(marker.getPosition()).draggable(true);
        marker_yourLoc.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
        googleMap.addMarker(marker_yourLoc);
        Log.v("MarkerLocation",marker.getPosition().latitude+"--"+marker.getPosition().longitude);
        Location currentlocation = new Location("ss");
        currentlocation.setLatitude(marker.getPosition().latitude);
        currentlocation.setLongitude(marker.getPosition().longitude);
        onLocationChanged(currentlocation);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), (float) 7.6));
    }
    private void checkPermissions(){
        int permissionLocation = ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(getActivity(),
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        }else{
            getMyLocation();
        }

    }
    private void getMyLocation(){
        if(googleApiClient!=null) {
            if (googleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(getActivity(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    mylocation =                     LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setInterval(3000);
                    locationRequest.setFastestInterval(3000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);
                    builder.setAlwaysShow(true);
                    LocationServices.FusedLocationApi
                            .requestLocationUpdates(googleApiClient, locationRequest, this);
                    PendingResult<LocationSettingsResult> result =
                            LocationServices.SettingsApi
                                    .checkLocationSettings(googleApiClient, builder.build());
                    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

                        @Override
                        public void onResult(LocationSettingsResult result) {
                            final Status status = result.getStatus();
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.SUCCESS:
                                    // All location settings are satisfied.
                                    // You can initialize location requests here.
                                    int permissionLocation = ContextCompat
                                            .checkSelfPermission(getActivity(),
                                                    android.Manifest.permission.ACCESS_FINE_LOCATION);
                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                        mylocation = LocationServices.FusedLocationApi
                                                .getLastLocation(googleApiClient);
                                    }
                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    // Location settings are not satisfied.
                                    // But could be fixed by showing the user a dialog.
                                    try {
                                        // Show the dialog by calling startResolutionForResult(),
                                        // and check the result in onActivityResult().
                                        // Ask to turn on GPS automatically
                                        status.startResolutionForResult(getActivity(),
                                                REQUEST_CHECK_SETTINGS_GPS);
                                    } catch (IntentSender.SendIntentException e) {
                                        // Ignore the error.
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    // Location settings are not satisfied.
                                    // However, we have no way
                                    // to fix the
                                    // settings so we won't show the dialog.
                                    // finish();
                                    break;
                            }
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS_GPS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        getMyLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                }
                break;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        int permissionLocation = ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            getMyLocation();
        }
    }
    private synchronized void setUpGClient() {
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), 0, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }
    public void getTestingLocations(Location currentlocation) {
        Locations_DistanceArray.clear();
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
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {


                // Getting view from the layout file info_window_layout
                View v = getLayoutInflater(Bundle.EMPTY).inflate(R.layout.info_window, null);

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
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
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
}
