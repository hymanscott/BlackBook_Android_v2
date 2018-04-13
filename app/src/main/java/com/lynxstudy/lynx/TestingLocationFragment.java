package com.lynxstudy.lynx;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.lynx.LynxManager;
import com.lynxstudy.lynx.R;
import com.lynxstudy.model.LocationsDistance;
import com.lynxstudy.model.TestingLocations;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestingLocationFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
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
    String[] filters = {"Filters", "PrEP", "HIV Testing",
            "STI Testing","Under 18"};
    String[] miles = {"10 miles", "20 miles", "50 miles",
            "100 miles"};
    ImageView search,swipe_arrow;
    EditText zipcode;
    Spinner filterSpinner,milesSpinner;
    private BottomSheetBehavior mBottomSheetBehavior;
    private Tracker tracker;
    Typeface tf;
    TextView infoWindow_Title,infoWindow_Address,infoWindow_Phone,infoWindow_url,infoWindow_distance,infoWindow_url_title,infoWindow_hours_title,infoWindow_hours,infoWindow_ins_title,infoWindow_insurance,infoWindow_ages_title,infoWindow_ages;
    public TestingLocationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lynx_prep_map, container, false);
        setUpGClient();
        db = new DatabaseHelper(getActivity());
        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        // Piwik Analytics //
        tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
		tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/Lynxtesting/Locations").title("Lynxtesting/Locations").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
        //Type face
        tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        mMapView.onResume(); // needed to get the map to display immediately
        // Moving MyLocation Button to bottom //
        if(mMapView.findViewById(1) != null){
            View btnMyLocation = ((View) mMapView.findViewById(1).getParent()).findViewById(2);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(80,80); // size of button in dp
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            params.setMargins(0, 0, 15, 15);
            btnMyLocation.setLayoutParams(params);
        }
        mlocManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*Bottom Sheet*/
        View bottomSheet = view.findViewById( R.id.bottom_sheet );
        final ImageView swipe_arrow = (ImageView) view.findViewById( R.id.swipe_arrow);
        infoWindow_Title = (TextView)view.findViewById(R.id.infoWindow_Title);
        infoWindow_Title.setTypeface(tf);
        infoWindow_Address = (TextView)view.findViewById(R.id.infoWindow_Address);
        infoWindow_Address.setTypeface(tf);
        infoWindow_Phone = (TextView)view.findViewById(R.id.infoWindow_Phone);
        infoWindow_Phone.setTypeface(tf);
        infoWindow_url = (TextView)view.findViewById(R.id.infoWindow_url);
        infoWindow_url.setTypeface(tf);
        infoWindow_distance = (TextView)view.findViewById(R.id.infoWindow_distance);
        infoWindow_distance.setTypeface(tf);
        infoWindow_url_title = (TextView)view.findViewById(R.id.infoWindow_url_title);
        infoWindow_url_title.setTypeface(tf);
        infoWindow_hours_title = (TextView)view.findViewById(R.id.infoWindow_hours_title);
        infoWindow_hours_title.setTypeface(tf);
        infoWindow_hours = (TextView)view.findViewById(R.id.infoWindow_hours);
        infoWindow_hours.setTypeface(tf);
        infoWindow_ins_title = (TextView)view.findViewById(R.id.infoWindow_ins_title);
        infoWindow_ins_title.setTypeface(tf);
        infoWindow_insurance = (TextView)view.findViewById(R.id.infoWindow_insurance);
        infoWindow_insurance.setTypeface(tf);
        infoWindow_ages_title = (TextView)view.findViewById(R.id.infoWindow_ages_title);
        infoWindow_ages_title.setTypeface(tf);
        infoWindow_ages = (TextView)view.findViewById(R.id.infoWindow_ages);
        infoWindow_ages.setTypeface(tf);

        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN); // Hiding Bottom Sheet by default
        mBottomSheetBehavior.setPeekHeight(0);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    mBottomSheetBehavior.setPeekHeight(0);
                    swipe_arrow.setImageDrawable(getResources().getDrawable(R.drawable.swipe_up));
                }else if(newState == BottomSheetBehavior.STATE_DRAGGING){
                    swipe_arrow.setImageDrawable(getResources().getDrawable(R.drawable.swipe_up));
                }else if(newState == BottomSheetBehavior.STATE_EXPANDED){
                    swipe_arrow.setImageDrawable(getResources().getDrawable(R.drawable.swipe_down));
                }
            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {
            }
        });

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
                googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {
                        //Log.v("onMyLocationButtonClick","yes");
                        String filter = null;
                        if(!filterSpinner.getSelectedItem().toString().equals("Filters"))
                            filter = filterSpinner.getSelectedItem().toString();

                        Location ll = googleMap.getMyLocation();
                        LatLng yourLoc = new LatLng(ll.getLatitude(), ll.getLongitude());
                        showFilteredMarker(yourLoc,filter,milesSpinner.getSelectedItem().toString());
                        return false;
                    }
                });
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        mBottomSheetBehavior.setPeekHeight(0);
                    }
                });
                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        if (marker.getTitle() != null) {
                            int id = Integer.parseInt(marker.getTitle());
                            TestingLocations testingLocations = db.getTestingLocationbyID(id);
                            TrackHelper.track().event("Testing Location Marker","Click").name(testingLocations.getName()).with(tracker);
                            infoWindow_Title.setText(testingLocations.getName());
                            infoWindow_Address.setText(testingLocations.getAddress());
                            infoWindow_Phone.setText("Phone:" + testingLocations.getPhone_number());
                            if (testingLocations.getUrl().isEmpty()) {
                                infoWindow_url.setVisibility(View.GONE);
                            }else{
                                infoWindow_url.setVisibility(View.VISIBLE);
                                infoWindow_url.setText(testingLocations.getUrl());
                            }
                            infoWindow_hours.setText(Html.fromHtml(testingLocations.getOperation_hours()));
                            infoWindow_insurance.setText(testingLocations.getInsurance());
                            infoWindow_ages.setText(testingLocations.getAges());
                            /*Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(testingLocations.getUrl()));
                            startActivity(browserIntent);*/
                            infoWindow_distance.setText(marker.getSnippet());

                            mBottomSheetBehavior.setPeekHeight(200);
                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }
                        return true;
                    }
                });
            }
        });

        // Spinner Items //
        filterSpinner = (Spinner)view.findViewById(R.id.filters);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_row, R.id.txtView, filters);
        filterSpinner.setAdapter(adapter);
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LatLng searchLatLng = null;
                String filter = null;
                if(!zipcode.getText().toString().isEmpty())
                {
                    searchLatLng = getLatLngFromZip(zipcode.getText().toString());
                }
                if(position!=0)
                    filter = filterSpinner.getSelectedItem().toString();
                if(googleMap !=null)
                    showFilteredMarker(searchLatLng,filter,milesSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        milesSpinner = (Spinner)view.findViewById(R.id.miles);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_row, R.id.txtView, miles);
        milesSpinner.setAdapter(adapter1);
        milesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LatLng searchLatLng = null;
                String filter = null;
                if(!filterSpinner.getSelectedItem().toString().equals("Filters"))
                    filter = filterSpinner.getSelectedItem().toString();
                if(!zipcode.getText().toString().isEmpty())
                    searchLatLng = getLatLngFromZip(zipcode.getText().toString());
                if(googleMap !=null)
                    showFilteredMarker(searchLatLng,filter,milesSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        search = (ImageView)view.findViewById(R.id.search);
        zipcode = (EditText) view.findViewById(R.id.zipCode);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!zipcode.getText().toString().isEmpty()){
                    String filter=null;
                    if(!filterSpinner.getSelectedItem().toString().equals("Filters"))
                        filter = filterSpinner.getSelectedItem().toString();
                    if(googleMap !=null)
                        showFilteredMarker(getLatLngFromZip(zipcode.getText().toString()),filter,milesSpinner.getSelectedItem().toString());
                }
            }
        });
        return view;
    }

    // Getting latlng from zip code //
    public LatLng getLatLngFromZip(String zip){

        try {
            BigDecimal n = new BigDecimal(zip);
            final Geocoder geocoder = new Geocoder(getActivity());
            LatLng latLng = null;

            try {
                List<Address> addresses = geocoder.getFromLocationName(zip, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    // Use the address as needed
                    latLng = new LatLng(address.getLatitude(), address.getLongitude());
                } else {
                    // Display appropriate message when Geocoder services are not available
                    latLng = null;
                }
            } catch (IOException e) {
                // handle exception
            }
            return latLng;
        } catch (Exception e) {
            LatLng latLng = null;
            if(Geocoder.isPresent()){
                try {
                    Geocoder gc = new Geocoder(getActivity());
                    List<Address> addresses= gc.getFromLocationName(zip, 5); // get the found Address Objects

                    List<LatLng> ll = new ArrayList<LatLng>(addresses.size()); // A list to save the coordinates if they are available
                    for(Address a : addresses){
                        if(a.hasLatitude() && a.hasLongitude()){
                            latLng = new LatLng(a.getLatitude(), a.getLongitude());
                        }
                    }
                } catch (IOException ex) {
                    // handle the exception
                }
            }
            return latLng;
        }
    }

    private void showFilteredMarker(LatLng latLng, String filter,String miles){
        // Set Location //
        Location l = new Location("");
        if(latLng!=null){
            l.setLatitude(latLng.latitude);
            l.setLongitude(latLng.longitude);
        }
        // set miles //
        String m[] = miles.split(" ");
        int maxDist = Integer.parseInt(m[0]);
        getTestingLocations(l,filter);
        Collections.sort(Locations_DistanceArray, LocationsDistance.LocDist);
        googleMap.clear();

        // Add Marker on the Location found based on search Result //
        LatLng yourLoc = new LatLng(l.getLatitude(), l.getLongitude());
        MarkerOptions yourLocMarker = new MarkerOptions().position(yourLoc).title(null).snippet(null);
        yourLocMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker));
        googleMap.addMarker(yourLocMarker);

        int marker_count = 0;
        bounds = new LatLngBounds.Builder();
        for (LocationsDistance locationsDistance : Locations_DistanceArray) {
            if (locationsDistance.getDistance() < maxDist) {
                LatLng firstLatLng =null;
                LatLng latlng = new LatLng(locationsDistance.getLatitude(), locationsDistance.getLongitude());
                MarkerOptions marker = new MarkerOptions().position(latlng).title(String.valueOf(locationsDistance.getLocation_distance_id())).snippet("Distance in Miles : " + locationsDistance.getDistance());
                if(marker_count==0)
                    firstLatLng = latlng;
                if(filter==null || filter.equals("null")){
                    googleMap.addMarker(getMarkerIcon(locationsDistance.getType(),marker));
                    bounds.include(new LatLng(locationsDistance.getLatitude(), locationsDistance.getLongitude()));
                //}else if(filter.equals(locationsDistance.getType())){
                }else {
                    googleMap.addMarker(getMarkerIcon(filter,marker));
                    bounds.include(new LatLng(locationsDistance.getLatitude(), locationsDistance.getLongitude()));
                }
                marker_count++;
                // Move Camera to very first location //
                if(firstLatLng!=null){
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(firstLatLng));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(firstLatLng, (float) 7.6));
                }
            }
        }

        if (marker_count == 0) {
            for (LocationsDistance locationsDistance : Locations_DistanceArray) {
                LatLng latlng = new LatLng(locationsDistance.getLatitude(), locationsDistance.getLongitude());
                MarkerOptions marker = new MarkerOptions().position(latlng).title(String.valueOf(locationsDistance.getLocation_distance_id())).snippet("Distance in Miles : " + locationsDistance.getDistance());
                googleMap.addMarker(getMarkerIcon(locationsDistance.getType(),marker));
                bounds.include(new LatLng(locationsDistance.getLatitude(), locationsDistance.getLongitude()));
                // Move Camera to very first location //
                LatLng firstLatLng = new LatLng(Locations_DistanceArray.get(0).getLatitude(),Locations_DistanceArray.get(0).getLongitude());
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(firstLatLng));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(firstLatLng, (float) 7.6));
            }
        }
    }
    public MarkerOptions getMarkerIcon(String filter,MarkerOptions marker){
        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.cityiconblue));
        return marker;
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
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.stopAutoManage(getActivity());
            googleApiClient.disconnect();
        }
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
        }
        /**/
        LatLng latlng = new LatLng(mylocation.getLatitude(), mylocation.getLongitude());// This methods gets the users current longitude and latitude.
        if(count==0){
            addDraggableMarker(latlng);
            count++;
        }
        //showFilteredMarker(latlng,filterSpinner.getSelectedItem().toString(),milesSpinner.getSelectedItem().toString());
        /*getTestingLocations(mylocation);

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
        }*/
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
        marker_yourLoc.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker));
        googleMap.addMarker(marker_yourLoc);
        //Log.v("MarkerLocation",marker.getPosition().latitude+"--"+marker.getPosition().longitude);
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
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.stopAutoManage(getActivity());
            googleApiClient.disconnect();
        }
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), 0, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        //googleApiClient.connect();
    }

    public void getTestingLocations(Location currentlocation,String type) {
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
            if(type==null){
                LocationsDistance location_distance = new LocationsDistance(location.getTesting_location_id(), Double.parseDouble(location.getLatitude()), Double.parseDouble(location.getLongitude()), distance_inMiles, location.getName(),location.getType());
                Locations_DistanceArray.add(location_distance);
            }else{
                switch (type){
                    case "PrEP":
                        if(location.getPrep_clinic().equals("Yes")){
                            LocationsDistance location_distance = new LocationsDistance(location.getTesting_location_id(), Double.parseDouble(location.getLatitude()), Double.parseDouble(location.getLongitude()), distance_inMiles, location.getName(),location.getType());
                            Locations_DistanceArray.add(location_distance);
                        }
                        break;
                    case "HIV Testing":
                        if(location.getHiv_clinic().equals("Yes")){
                            LocationsDistance location_distance = new LocationsDistance(location.getTesting_location_id(), Double.parseDouble(location.getLatitude()), Double.parseDouble(location.getLongitude()), distance_inMiles, location.getName(),location.getType());
                            Locations_DistanceArray.add(location_distance);
                        }
                        break;
                    case "STI Testing":
                        if(location.getSti_clinic().equals("Yes")){
                            LocationsDistance location_distance = new LocationsDistance(location.getTesting_location_id(), Double.parseDouble(location.getLatitude()), Double.parseDouble(location.getLongitude()), distance_inMiles, location.getName(),location.getType());
                            Locations_DistanceArray.add(location_distance);
                        }
                        break;
                        case "Under 18":
                        if(location.getUnder_eighteen().equals("Yes")){
                            LocationsDistance location_distance = new LocationsDistance(location.getTesting_location_id(), Double.parseDouble(location.getLatitude()), Double.parseDouble(location.getLongitude()), distance_inMiles, location.getName(),location.getType());
                            Locations_DistanceArray.add(location_distance);
                            Log.v("Under18",location.getName());
                        }
                        break;
                    default:
                        LocationsDistance location_distance = new LocationsDistance(location.getTesting_location_id(), Double.parseDouble(location.getLatitude()), Double.parseDouble(location.getLongitude()), distance_inMiles, location.getName(),location.getType());
                        Locations_DistanceArray.add(location_distance);
                }
            }
        }

        //custom info window
        /*if(googleMap!=null) {
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
        }*/

    }
}

