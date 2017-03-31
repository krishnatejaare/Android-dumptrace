package com.example.arekr.dumptrace;

import android.*;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.nfc.Tag;
import android.provider.MediaStore;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.design.widget.Snackbar;

import android.support.v4.app.ActivityCompat;

import com.firebase.client.Firebase;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.android.photoutil.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import java.util.UUID;


public class MainActivity extends AppCompatActivity implements AsyncResponse,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    //private static final String GOOGLE_API_KEY = "AIzaSyB0xxmOEjeMEp4ym20pO1NyInvL4cdl2Ko";

    // private static final String GOOGLE_API_KEY = "AIzaSyCtuMR0XBODO2ugxyeq8UQHv5y2oNE5tFE";

    private static final String GOOGLE_API_KEY = "AIzaSyAw57QanU7FzXTTieEXfthB0m_B9qvViJc";
    public static final String TAG = MainActivity.class.getSimpleName();
    Uri S;
    GPSTracker gpsTracker;
    Bitmap photo;
    StorageReference filepath;
    private StorageReference mstorage;
    private Firebase mref;
    ImageView ivCamera, ivGallery, ivUpload, ivImage;
    final int CAMERA_REQUEST = 13323;
    final int GALLERY_REQUEST = 22131;
    CameraPhoto cameraPhoto;
    GalleryPhoto galleryPhoto;
    String selectedPhoto;
    String a;
    private ProgressDialog mProgress;
    // private GoogleApiClient mGoogleApiClient;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private DatabaseReference mFirebaseDatabaseReference;
    public LocationManager manager;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    double latitude;
    double longitude;
    String provider;
    Object[] toPass;
    //String googlePlacesData = null;
    String uuid;

    // nextInt is normally exclusive of the top value,
// so add 1 to make it inclusive
    int randomNum;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkInternetConenction();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("Dump Trace");

        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        mstorage = FirebaseStorage.getInstance().getReference();
        gpsTracker = new GPSTracker(this);

//        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/geocode/json?");
//        googlePlacesUrl.append("latlng=" + gpsTracker.getLatitude() + "," + gpsTracker.getLongitude());
//        googlePlacesUrl.append("&location_type=ROOFTOP&result_type=street_address");
//        googlePlacesUrl.append("&sensor=true");
//        googlePlacesUrl.append("&key=" + GOOGLE_API_KEY);
//        System.out.println("googlePlacesUrl" + googlePlacesUrl);
//        toPass = new Object[1];
//        Log.d("TAG", googlePlacesUrl.toString());
//
//        toPass[0] = googlePlacesUrl.toString();
//
//        Http http = new Http();
//        http.delegate = this;
//        http.execute(toPass);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        ivCamera = (ImageView) findViewById(R.id.ivCamera);
        ivGallery = (ImageView) findViewById(R.id.ivGallery);
        ivUpload = (ImageView) findViewById(R.id.ivUpload);
        mProgress = new ProgressDialog(this);
        cameraPhoto = new CameraPhoto(getApplicationContext());
        galleryPhoto = new GalleryPhoto(getApplicationContext());
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uuid = UUID.randomUUID().toString();
                Log.d("camera", "latitude");
                System.out.println("camera");
                System.out.println("camera");
                System.out.println("camera");

                try {
                    startActivityForResult(cameraPhoto.takePhotoIntent(), CAMERA_REQUEST);
                    cameraPhoto.addToGallery();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(),
                            "Something Wrong while taking photos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ivGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uuid = UUID.randomUUID().toString();
                System.out.println("Gallery");
                System.out.println("Gallery");
                Log.d("gallery", "latitude");

                startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY_REQUEST);
            }
        });

        ivUpload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mProgress.setMessage("Uploading Image");
                mProgress.show();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
                DatabaseReference usersref = mFirebaseDatabaseReference.child("Address of the Dump spots").child(uuid);
                usersref.child("Address").setValue(a);
                usersref.child("Latitude").setValue(gpsTracker.getLatitude());
                usersref.child("Longitude").setValue(gpsTracker.getLongitude());
                usersref.child("Time").setValue(sdf.format(new Date()));
                try {
                    Bitmap bitmap = ImageLoader.init().from(selectedPhoto).requestSize(1024, 1024).getBitmap();
                    String encodedImage = ImageBase64.encode(bitmap);
                    Uri u = S;

                    filepath.putFile(u).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mProgress.dismiss();
                            Toast.makeText(MainActivity.this, "Upload Completed", Toast.LENGTH_LONG).show();
                        }
                    });
                    //System.out.println("image="+encodedImage);
                } catch (FileNotFoundException e) {

                    Toast.makeText(getApplicationContext(), "error in encoding photos", Toast.LENGTH_SHORT).show();
                }
            }

        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }





    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private boolean checkInternetConenction() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);


        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||

                connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content), "Internet connected", Snackbar.LENGTH_SHORT);
            snackbar.show();
            return true;
        } else if (
                connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content), "No internet connection!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            checkInternetConenction();
                        }
                    });

// Changing message text color
            snackbar.setActionTextColor(Color.RED);


// Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
            return false;
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();



        //randomNum = ThreadLocalRandom.current().nextInt(100, 10000000 + 1);
        //uuid=Integer.toString(randomNum);
       gpsTracker.getLocation();
       // mGoogleApiClient.connect();
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/geocode/json?");
        googlePlacesUrl.append("latlng=" + gpsTracker.getLatitude() + "," + gpsTracker.getLongitude());
       // googlePlacesUrl.append("latlng=" + latitude + "," + longitude);
        //googlePlacesUrl.append("&location_type=ROOFTOP&result_type=street_address");
        googlePlacesUrl.append("&result_type=street_address");
       //googlePlacesUrl.append("&location_type=GEOMETRIC_CENTER|APPROXIMATE");
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + GOOGLE_API_KEY);
        System.out.println("googlePlacesUrl" + googlePlacesUrl);
        toPass = new Object[1];
        Log.d("TAG", googlePlacesUrl.toString());

        toPass[0] = googlePlacesUrl.toString();

        Http http = new Http();
        http.delegate = this;
        http.execute(toPass);


    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //txtSpeechInput.setText(result.get(0));
                    if(result.get(0).equals("open camera")){
                        uuid = UUID.randomUUID().toString();
                        try {
                            startActivityForResult(cameraPhoto.takePhotoIntent(), CAMERA_REQUEST);
                            cameraPhoto.addToGallery();
                        } catch (IOException e) {
                            Toast.makeText(getApplicationContext(),
                                    "Something Wrong while taking photos", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if(result.get(0).equals("open gallery")){
                        uuid = UUID.randomUUID().toString();
                        startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY_REQUEST);
                    }

                    if(result.get(0).equals("upload")){
                        mProgress.setMessage("Uploading Image");
                        mProgress.show();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference usersref = mFirebaseDatabaseReference.child("Address of the Dump spots").child(uuid);
                        usersref.child("Address").setValue(a);
                        usersref.child("Latitude").setValue(gpsTracker.getLatitude());
                        usersref.child("Longitude").setValue(gpsTracker.getLongitude());
                        usersref.child("Time").setValue(sdf.format(new Date()));
                        try {
                            Bitmap bitmap = ImageLoader.init().from(selectedPhoto).requestSize(1024, 1024).getBitmap();
                            String encodedImage = ImageBase64.encode(bitmap);
                            Uri u = S;

                            filepath.putFile(u).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    mProgress.dismiss();
                                    Toast.makeText(MainActivity.this, "Upload Completed", Toast.LENGTH_LONG).show();
                                }
                            });
                            //System.out.println("image="+encodedImage);
                        } catch (FileNotFoundException e) {

                            Toast.makeText(getApplicationContext(), "error in encoding photos", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                break;
            }

        }

        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {

                String photoPath = cameraPhoto.getPhotoPath();
                selectedPhoto = photoPath;
                try {
                    Bitmap bitmap = ImageLoader.init().from(photoPath).requestSize(1024, 1024).getBitmap();
                    photo = bitmap;
                    ivImage.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(),
                            "Something Wrong while loading photos", Toast.LENGTH_SHORT).show();
                }

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), photo, "image", null);
                S = Uri.parse(path);

               // filepath = mstorage.child("photos").child(uuid).child(S.getLastPathSegment());
                filepath = mstorage.child("photos").child(uuid);
                //}
            } else if (requestCode == GALLERY_REQUEST) {
                Uri uri = data.getData();
                S = data.getData();
                String name=S.getLastPathSegment();
//                FormBodyPart userFile = new FormBodyPart("userfile", new FileBody(myImageFile));
//                userFile.addField("filename","NEWNAMEOFILE.jpg");
//                multipartEntity.addPart(userFile);

                //filepath = mstorage.child("photos").child(uuid).child(S.getLastPathSegment());
                filepath = mstorage.child("photos").child(uuid);

                galleryPhoto.setPhotoUri(uri);
                String photoPath = galleryPhoto.getPath();
                selectedPhoto = photoPath;
                try {
                    Bitmap bitmap = ImageLoader.init().from(photoPath).requestSize(512, 512).getBitmap();
                    ivImage.setImageBitmap(bitmap);

                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(),
                            "Something Wrong while choosing photos", Toast.LENGTH_SHORT).show();
                }


            }
        }
    }


    @Override
    public void processFinish(String output) throws JSONException {
        String address = output;
        Object[] O = new Object[1];
        O[0] = address;
        JSONObject googlePlacesJson;
        googlePlacesJson = new JSONObject((String) O[0]);
        System.out.println(googlePlacesJson);
        JSONArray arr = googlePlacesJson.getJSONArray("results");
        JSONObject json_data = arr.getJSONObject(0);
        a = json_data.getString("formatted_address");
        System.out.println(a);


    }

    @Override
    public void onConnected(Bundle bundle) {

        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
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
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else {
            handleNewLocation(location);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        handleNewLocation(location);

    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());

    latitude = location.getLatitude();
    longitude = location.getLongitude();

//        else{
//    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//}




    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }

    }


}
