package com.example.arekr.dumptrace;

import android.*;
import android.Manifest;
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
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
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
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
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

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import java.util.UUID;


public class MainActivity extends AppCompatActivity implements AsyncResponse,GoogleApiClient.ConnectionCallbacks, View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private static final String CLOUD_VISION_API_KEY = "AIzaSyAKOO0uCKphUyBtK3vBq1Hgoty-q0k4iro";
    public static final String FILE_NAME = "temp.jpg";
    private static final String ANDROID_CERT_HEADER = "X-Android-Cert";
    private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";
    private static final String GOOGLE_API_KEY = "AIzaSyAw57QanU7FzXTTieEXfthB0m_B9qvViJc";
    public static final String TAG = MainActivity.class.getSimpleName();
    private static final int GALLERY_PERMISSIONS_REQUEST = 0;
    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    public static final int CAMERA_IMAGE_REQUEST = 3;
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
    boolean validate=false;
    String provider;
    Object[] toPass;
    //String googlePlacesData = null;
    String uuid;
    int randomNum;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                Intent i = new Intent(MainActivity.this, StartActivity.class);

                startActivity(i);
            }
        });

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
        ivImage = (ImageView) findViewById(R.id.ivImage);
        ivCamera = (ImageView) findViewById(R.id.ivCamera);
        ivGallery = (ImageView) findViewById(R.id.ivGallery);
        ivUpload = (ImageView) findViewById(R.id.ivUpload);
        ivUpload.setOnClickListener(this);
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
                startCamera();
//                Log.d("camera", "latitude");
//                System.out.println("camera");
//                System.out.println("camera");
//                System.out.println("camera");
//                if (PermissionUtils.requestPermission(
//                        MainActivity.this,
//                        CAMERA_PERMISSIONS_REQUEST,
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.CAMERA)) {
//
//                    try {
//
//                        startActivityForResult(cameraPhoto.takePhotoIntent(), CAMERA_REQUEST);
//                        cameraPhoto.addToGallery();
//                    } catch (IOException e) {
//                        Toast.makeText(getApplicationContext(),
//                                "Something Wrong while taking photos", Toast.LENGTH_SHORT).show();
//                    }
//                }
            }
        });

        ivGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uuid = UUID.randomUUID().toString();
                startGalleryChooser();
//                System.out.println("Gallery");
//                System.out.println("Gallery");
//                Log.d("gallery", "latitude");
//                if (PermissionUtils.requestPermission(MainActivity.this, GALLERY_PERMISSIONS_REQUEST, Manifest.permission.READ_EXTERNAL_STORAGE)) {
//
//                startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY_REQUEST);}
            }
        });

//        ivUpload.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                mProgress.setMessage("Uploading Image");
//                mProgress.show();
//                if(validate==true) {
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                    mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
//                    DatabaseReference usersref = mFirebaseDatabaseReference.child("Address of the Dump spots").child(uuid);
//                    usersref.child("Address").setValue(a);
//                    usersref.child("Latitude").setValue(gpsTracker.getLatitude());
//                    usersref.child("Longitude").setValue(gpsTracker.getLongitude());
//                    usersref.child("Time").setValue(sdf.format(new Date()));
//                // Bitmap bitmap = ImageLoader.init().from(selectedPhoto).requestSize(1024, 1024).getBitmap();
//                // String encodedImage = ImageBase64.encode(bitmap);
//
//                Uri u = S;
//                filepath.putFile(u).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        mProgress.dismiss();
//                        Toast.makeText(MainActivity.this, "Upload Completed", Toast.LENGTH_LONG).show();
//                    }
//                });
//                //System.out.println("image="+encodedImage);
//                 }
//                else{
//
//                    mProgress.dismiss();
//                    Toast.makeText(MainActivity.this, "Not a valid image for Reporting ", Toast.LENGTH_LONG).show();
//                }
//            }
//
//        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    public void startGalleryChooser() {
        if (PermissionUtils.requestPermission(this, GALLERY_PERMISSIONS_REQUEST, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY_REQUEST);}
    }


    public void startCamera() {
        if (PermissionUtils.requestPermission(
                this,
                CAMERA_PERMISSIONS_REQUEST,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri uri = FileProvider.getUriForFile(MainActivity.this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
           intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


                startActivityForResult(intent, CAMERA_IMAGE_REQUEST);
               // cameraPhoto.addToGallery();

        }
    }
    public File getCameraFile() {
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(dir, FILE_NAME);
    }

    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, CAMERA_PERMISSIONS_REQUEST, grantResults)) {
                    startCamera();
                }
                break;
            case GALLERY_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, GALLERY_PERMISSIONS_REQUEST, grantResults)) {
                    startGalleryChooser();
                }
                break;
        }
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

                    if(result.get(0).equals("upload")) {
                        if (validate == true) {


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
                        else{
                            if(validate==false) {
                                mProgress.dismiss();
                                Toast.makeText(MainActivity.this, "Not a valid image for Reporting ", Toast.LENGTH_LONG).show();
                            }
                            }
                    }

                }
                break;
            }

        }

        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_IMAGE_REQUEST ) {

                String photoPath = cameraPhoto.getPhotoPath();

                selectedPhoto = photoPath;
                try {
                    Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
                    Bitmap bitmap =
                            scaleBitmapDown(
                                    MediaStore.Images.Media.getBitmap(getContentResolver(), uri),
                                    1200);
                    //Bitmap bit = ImageLoader.init().from(photoPath).requestSize(1024, 1024).getBitmap();
                    callCloudVision(bitmap);

                    ivImage.setImageBitmap(bitmap);
                    mProgress.setMessage("Validating Image");
                    mProgress.show();
                    photo = bitmap;
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(),
                            "Something Wrong while loading photos", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Log.d(TAG, "Image picking failed because " + e.getMessage());
                }

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), photo, "image", null);

                //S = Uri.parse(path);
                Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
                S=uri;

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
                    callCloudVision(bitmap);
                    ivImage.setImageBitmap(bitmap);
                    mProgress.setMessage("Validating Image");
                    mProgress.show();
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(),
                            "Something Wrong while choosing photos", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Log.d(TAG, "Image picking failed because " + e.getMessage());
                    //Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
                }


            }
        }
    }
    public Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
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

    private void callCloudVision(final Bitmap bitmap) throws IOException {
        // Switch text to loading


        // Do the real work in an async task, because we need to use the network anyway
        new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object... params) {
                try {
                    HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
                    JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

                    VisionRequestInitializer requestInitializer =
                            new VisionRequestInitializer(CLOUD_VISION_API_KEY) {
                                /**
                                 * We override this so we can inject important identifying fields into the HTTP
                                 * headers. This enables use of a restricted cloud platform API key.
                                 */
                                @Override
                                protected void initializeVisionRequest(VisionRequest<?> visionRequest)
                                        throws IOException {
                                    super.initializeVisionRequest(visionRequest);

                                    String packageName = getPackageName();
                                    visionRequest.getRequestHeaders().set(ANDROID_PACKAGE_HEADER, packageName);

                                    String sig = PackageManagerUtils.getSignature(getPackageManager(), packageName);

                                    visionRequest.getRequestHeaders().set(ANDROID_CERT_HEADER, sig);
                                }
                            };

                    Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
                    builder.setVisionRequestInitializer(requestInitializer);

                    Vision vision = builder.build();

                    BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                            new BatchAnnotateImagesRequest();
                    batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
                        AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();

                        // Add the image
                        Image base64EncodedImage = new Image();
                        // Convert the bitmap to a JPEG
                        // Just in case it's a format that Android understands but Cloud Vision
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                        byte[] imageBytes = byteArrayOutputStream.toByteArray();

                        // Base64 encode the JPEG
                        base64EncodedImage.encodeContent(imageBytes);
                        annotateImageRequest.setImage(base64EncodedImage);

                        // add the features we want
                        annotateImageRequest.setFeatures(new ArrayList<Feature>() {{
                            Feature labelDetection = new Feature();
                            labelDetection.setType("LABEL_DETECTION");
                            labelDetection.setMaxResults(10);
                            add(labelDetection);
                        }});

                        // Add the list of one thing to the request
                        add(annotateImageRequest);
                    }});

                    Vision.Images.Annotate annotateRequest =
                            vision.images().annotate(batchAnnotateImagesRequest);
                    // Due to a bug: requests to Vision API containing large images fail when GZipped.
                    annotateRequest.setDisableGZipContent(true);
                    Log.d(TAG, "created Cloud Vision request object, sending request");

                    BatchAnnotateImagesResponse response = annotateRequest.execute();
                    return convertResponseToString(response);

                } catch (GoogleJsonResponseException e) {
                    Log.d(TAG, "failed to make API request because " + e.getContent());
                } catch (IOException e) {
                    Log.d(TAG, "failed to make API request because of other IOException " +
                            e.getMessage());
                }
                return "Cloud Vision API request failed. Check logs for details.";
            }

            protected void onPostExecute(String result) {

                if(validate==true){
                    mProgress.dismiss();
                    Toast.makeText(MainActivity.this, "You can now Upload ", Toast.LENGTH_LONG).show();
                }
                else{
                    mProgress.dismiss();
                    Toast.makeText(MainActivity.this, "Not a valid image, Please select another ", Toast.LENGTH_LONG).show();}
                System.out.println(" cloud vision result is  "+ result);
                //mImageDetails.setText(result);
            }
        }.execute();
    }
    private String convertResponseToString(BatchAnnotateImagesResponse response) {
        String message = "I found these things:\n\n";

        List<EntityAnnotation> labels = response.getResponses().get(0).getLabelAnnotations();
        System.out.println("labels are"+labels);
        // System.out.println("labels Description are"+labels);

        if (labels != null) {
            for (EntityAnnotation label : labels) {
                // if(label.getDescription()=="waste||pollution||scrap||furniture||rubble||mattress||bed||yard||litter||patio||backyard||asphalt||lawn||material||flora||tree||soil||geological phenomenon||road"){
                if(label.getDescription().matches("waste||pollution||scrap||furniture||rubble||mattress||bed||yard||litter||patio||backyard||asphalt||lawn||material||flora||tree||soil||geological phenomenon||jungle||garden||forest||natural environment||grass||field")){
                    System.out.println("label Detected..................................................................");
                    validate=true;
                }
                message += String.format(Locale.US, "%.3f: %s", label.getScore(), label.getDescription());
                message += "\n";
            }
        } else {
            message += "nothing";
        }

        return message;
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


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivUpload) {
            mProgress.setMessage("Uploading Image");
            mProgress.show();
            if(validate==true) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
                DatabaseReference usersref = mFirebaseDatabaseReference.child("Address of the Dump spots").child(uuid);
                usersref.child("Address").setValue(a);
                usersref.child("Latitude").setValue(gpsTracker.getLatitude());
                usersref.child("Longitude").setValue(gpsTracker.getLongitude());
                usersref.child("Time").setValue(sdf.format(new Date()));
                // Bitmap bitmap = ImageLoader.init().from(selectedPhoto).requestSize(1024, 1024).getBitmap();
                // String encodedImage = ImageBase64.encode(bitmap);

                Uri u = S;
                filepath.putFile(u).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mProgress.dismiss();
                        Toast.makeText(MainActivity.this, "Upload Completed", Toast.LENGTH_LONG).show();
                    }
                });
                //System.out.println("image="+encodedImage);
                validate=false;
            }
            else{
                if(validate==false) {
                    mProgress.dismiss();
                    Toast.makeText(MainActivity.this, "Not a valid image for Reporting ", Toast.LENGTH_LONG).show();
                }
                }

        }
    }
}
