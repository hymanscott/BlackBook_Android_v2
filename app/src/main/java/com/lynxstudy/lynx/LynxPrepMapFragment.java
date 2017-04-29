package com.lynxstudy.lynx;


import android.*;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
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
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lynxstudy.helper.DatabaseHelper;
import com.google.android.gms.location.LocationListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class LynxPrepMapFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
            GoogleApiClient.OnConnectionFailedListener,GoogleMap.OnMarkerDragListener, LocationListener {


        public LynxPrepMapFragment() {
            // Required empty public constructor
        }

        MapView mMapView;
        private GoogleMap googleMap;
        private Location mylocation;
        private GoogleApiClient googleApiClient;
        private final static int REQUEST_CHECK_SETTINGS_GPS=0x1;
        private final static int REQUEST_ID_MULTIPLE_PERMISSIONS=0x2;
        DatabaseHelper db;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View rootView = inflater.inflate(R.layout.fragment_lynx_prep_map, container, false);
            setUpGClient();
            db = new DatabaseHelper(getActivity());
            mMapView = (MapView) rootView.findViewById(R.id.mapView);
            mMapView.onCreate(savedInstanceState);

            mMapView.onResume(); // needed to get the map to display immediately

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
            return rootView;
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

        @Override
        public void onLocationChanged(Location location) {
            mylocation = location;
            if (mylocation != null) {
                Double latitude=mylocation.getLatitude();
                Double longitude=mylocation.getLongitude();
                //Or Do whatever you want with your location
                Log.v("LocationChanged",latitude+"--"+longitude);
                LatLng latlng = new LatLng(mylocation.getLatitude(), mylocation.getLongitude());// This methods gets the users current longitude and latitude.
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));//Moves the camera to users current longitude and latitude
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, (float) 7.6));//Animates camera and zooms to preferred state on the user's current location.
                MarkerOptions marker_yourLoc = new MarkerOptions().position(latlng).draggable(true);
                marker_yourLoc.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker));
                googleMap.addMarker(marker_yourLoc);
            }

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

        @Override
        public void onMarkerDragStart(Marker marker) {

        }

        @Override
        public void onMarkerDrag(Marker marker) {

        }

        @Override
        public void onMarkerDragEnd(Marker marker) {

        }
    }
