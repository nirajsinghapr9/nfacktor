package com.example.nfacktor;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.android.material.snackbar.Snackbar;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    GoogleMap mGoogleMap;
    SupportMapFragment mapFragment;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;
    private static ProgressDialog progressDialog;
    SearchView searchView;
    RelativeLayout layout;
    Snackbar snackbar;
    Polygon polygon;
    boolean gridSet = false;
    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        searchView = findViewById(R.id.idSearchView);
        layout=findViewById(R.id.snack);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;

                // checking if the entered location is null or not.
                if (location != null || location.equals("")) {
                    // on below line we are creating and initializing a geo coder.
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try {
                        // on below line we are getting location from the
                        // location name and adding that location to address list.
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // on below line we are getting the location
                    // from our list a first position.
                    Address address = addressList.get(0);

                    // on below line we are creating a variable for our location
                    // where we will add our locations latitude and longitude.
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                    // on below line we are adding marker to that position.
                    mGoogleMap.addMarker(new MarkerOptions().position(latLng).title(location).draggable(true));

                    // below line is to animate camera to that position.
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    public void fetchLocation() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Fetching location..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.show();

        new CountDownTimer(10000, 10000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                progressDialog.dismiss();
            }
        }.start();


        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if ((ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);

        } else {
            progressDialog.dismiss();
            Toast.makeText(this, "Provide GPS permission", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        if(snackbar!=null){
            snackbar.dismiss();
        }
        fetchLocation();
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        progressDialog.dismiss();
        snackbar = Snackbar.make(layout, "GPS is disabled",Snackbar.LENGTH_INDEFINITE);
        snackbar.setBackgroundTint(Color.parseColor("#FF000000"));
        snackbar.setActionTextColor(Color.parseColor("#cc3300") );
        snackbar.setTextColor(Color.parseColor("#FFFFFFFF") );
              snackbar.setAction("Enable",new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        snackbar.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!isGooglePlayServicesAvailable(MapsActivity.this)) {
            showAlert();
        }
        fetchLocation();
    }

    private void showAlert() {
        new AlertDialog.Builder(this)
                .setTitle("Failure")
                .setMessage("No Play Service Installed")
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000); // two minute interval
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }

                //Place current location marker
               LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
               /*  MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                markerOptions.draggable(true);
                mGoogleMap.addMarker(markerOptions);*/

                //move map camera
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));

                what3words(location.getLatitude(), location.getLongitude());
            }
        }

    };


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    public boolean isGooglePlayServicesAvailable(Activity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(activity, status, 2404).show();
            }
            return false;
        }
        return true;
    }


    public void what3words(double latitude, double longitude) {

        LatLng pt = new LatLng(latitude, longitude);

        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        CameraUpdate cu = CameraUpdateFactory.newLatLng(pt);
        mGoogleMap.moveCamera(cu);
        cu = CameraUpdateFactory.zoomTo(14);
        mGoogleMap.moveCamera(cu);

        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
               // mGoogleMap.addCircle(new CircleOptions().radius(4).strokeColor(Color.BLUE).center(latLng));
            }
        });

        mGoogleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                ((GridView) findViewById(R.id.grid_any)).setAlignment(
                        null, mGoogleMap.getProjection(), mGoogleMap.getProjection().getVisibleRegion());

            }
        });

        // on idle fetch the grid using the screen center point
        mGoogleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                final LatLng centerOfGridCell = mGoogleMap.getCameraPosition().target;
            Log.e("Zoom", String.valueOf(mGoogleMap.getCameraPosition().zoom));
                if (mGoogleMap.getCameraPosition().zoom > 18) {
                    getGrid(centerOfGridCell, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("lines");
                                JSONObject firstList = jsonArray.getJSONObject(1);
                                JSONObject firstPt = firstList.getJSONObject("start");
                                String lat = firstPt.getString("lat");
                                String lng = firstPt.getString("lng");

                                LatLng alignmentPt = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                                Projection p = mGoogleMap.getProjection();
                                VisibleRegion vr = p.getVisibleRegion();

                                ((GridView) findViewById(R.id.grid_any)).setVisibility(View.VISIBLE);
                                ((GridView) findViewById(R.id.grid_any)).setAlignment(centerOfGridCell, p, vr);

                                if (polygon != null) {
                                    polygon.remove();
                                }

                                // take alignment point and draw 3 meter square polygon
                                LatLng pt1 = SphericalUtil.computeOffset(centerOfGridCell, 3, 90);
                                LatLng pt2 = SphericalUtil.computeOffset(pt1, 3, 180);
                                LatLng pt3 = SphericalUtil.computeOffset(pt2, 3, 270);

                                polygon = mGoogleMap.addPolygon(new PolygonOptions().add(centerOfGridCell,
                                        pt1, pt2, pt3, centerOfGridCell)
                                        .strokeColor(Color.parseColor("#cccccc")).strokeWidth(4).fillColor(Color.parseColor("#cccccc")));


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    });
                    gridSet = true;
                }else{
                    ((GridView) findViewById(R.id.grid_any)).setVisibility(View.GONE);
                }

            }
        });
    }

    LatLng getCenterPointInPolygon(Polygon polygon) {
        LatLngBounds.Builder latLngBounds = LatLngBounds.builder();
        for (LatLng latLng : polygon.getPoints()) {
            latLngBounds.include(latLng);
        }
        return latLngBounds.build().getCenter();
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    // Issue request to w3w - REMEMBER TO REPLACE **YOURKEY** ...
    private void getGrid(LatLng pt, Response.Listener<String> listener) {

        // something 9 meters to east
        LatLng otherPt = SphericalUtil.computeOffset(pt, 6.0, 225);
        String bboxStr = Double.toString(pt.latitude)+","+
                Double.toString(pt.longitude)+","+
                Double.toString(otherPt.latitude)+","+
                Double.toString(otherPt.longitude);
        RequestQueue rq = Volley.newRequestQueue(this);
       // String url = "https://api.what3words.com/v3/grid?bbox="+bboxStr+"&format=json&key=7C1Z3CQK";
        String url="https://api.what3words.com/v3/grid-section?key=7C1Z3CQK&bounding-box="+bboxStr+"&format=json";

        StringRequest req = new StringRequest(Request.Method.GET, url, listener, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", "volley error: "+error);
            }
        });

        rq.add(req);
    }

}