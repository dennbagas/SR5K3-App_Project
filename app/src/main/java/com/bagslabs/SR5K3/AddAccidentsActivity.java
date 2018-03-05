package com.bagslabs.SR5K3;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.bagslabs.SR5K3.models.AddAccidents;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddAccidentsActivity extends BaseActivity {

    private static final String TAG = "AddAccidentsActivity", REQUIRED = "Required";
    private Uri imageUri;

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    private StorageReference mStorage;
    // [END declare_database_ref]

    private EditText mNamaField, mNikField, mBagianField, mKronoField, mPenangananField, mAtasan;
    private FloatingActionButton mSubmitButton;
    SimpleDateFormat dateFormatter;
    TimePickerDialog timePickerDialog;
    private TextView tvDateResult, tvTimeResult;
    Button mAddPhoto;
    ImageView mPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_accidents);

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
        // [END initialize_database_ref]

        mNamaField = findViewById(R.id.field_nama);
        mNikField = findViewById(R.id.field_nik);
        mBagianField = findViewById(R.id.field_bagian);
        mKronoField = findViewById(R.id.field_krono);
        mPenangananField = findViewById(R.id.field_penanganan);
        mAtasan = findViewById(R.id.field_atasan);
        mSubmitButton = findViewById(R.id.fab_submit_post);
        mAddPhoto = findViewById(R.id.addPhoto);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });
        mAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLaunchCamera();
            }
        });

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        tvDateResult = findViewById(R.id.tv_dateresult);
        tvDateResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });

        tvTimeResult = findViewById(R.id.tv_timeresult);
        tvTimeResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog();
            }
        });
    }

    public void showTimeDialog() {

        Calendar calendar = Calendar.getInstance();

        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                tvTimeResult.setText(+hourOfDay + ":" + minute);
            }
        },
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }

    public void showDateDialog() {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthofyear, int dayofmonth) {
                tvDateResult.setText(dayofmonth + "-" + monthofyear + "-" + year);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void onLaunchCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 0);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                mPhoto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getActualImage(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void submitPost() {

        final String tanggal = tvDateResult.getText().toString();
        final String pukul = tvTimeResult.getText().toString();
        final String nama = mNamaField.getText().toString();
        final String nik = mNikField.getText().toString();
        final String bagian = mBagianField.getText().toString();
        final String krono = mKronoField.getText().toString();
        final String penanganan = mPenangananField.getText().toString();
        final String atasan = mAtasan.getText().toString();

        // Tanggal is required
        if (TextUtils.isEmpty(tanggal)) {
            tvDateResult.setError(REQUIRED);
            return;
        }
        // Pukul is required
        if (TextUtils.isEmpty(pukul)) {
            tvTimeResult.setError(REQUIRED);
            return;
        }
        // Nama is required
        if (TextUtils.isEmpty(nama)) {
            mNamaField.setError(REQUIRED);
            return;
        }
        // Nik is required
        if (TextUtils.isEmpty(nik)) {
            mNikField.setError(REQUIRED);
            return;
        }
        // Bagian is required
        if (TextUtils.isEmpty(bagian)) {
            mBagianField.setError(REQUIRED);
            return;
        }
        // Krono is required
        if (TextUtils.isEmpty(krono)) {
            mKronoField.setError(REQUIRED);
            return;
        }
        // Penanganan is required
        if (TextUtils.isEmpty(penanganan)) {
            mPenangananField.setError(REQUIRED);
            return;
        }
        // Atasan is required
        if (TextUtils.isEmpty((atasan))) {
            mAtasan.setError(REQUIRED);
            return;
        }

        // Disable button so there are no multi-posts
        setEditingEnabled(false);

        // [START single_value_read]

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.show();

        if (imageUri == null) {
            // show message
            Toast.makeText(getApplicationContext(), "Tambahkan Foto Terlebih Dahulu", Toast.LENGTH_LONG).show();
        } else {

            StorageReference reference = mStorage.child("accident" + System.currentTimeMillis() + "." + getActualImage(imageUri));
            reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    // Write new post
                    //noinspection ConstantConditions
                    writeNewPost(nama, nik, bagian, krono, penanganan, tanggal, pukul, taskSnapshot.getDownloadUrl().toString(), atasan);
                    Log.e(TAG, "Success Posting");

                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Success Posting.", Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })

                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double totalProgress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded" + totalProgress + "%");
                        }
                    });


            // Finish this Activity, back to the stream
            setEditingEnabled(true);

            finish();
            // [END_EXCLUDE]
        }
        // [END single_value_read]
    }

    // [START write_fan_out]
    private void writeNewPost(String nama, String nik, String bagian, String kronologi, String penanganan, String tanggal
            , String pukul, String imageUrl, String atasan) {
        // Create new addAccidents at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("accidents").push().getKey();
        AddAccidents addAccidents = new AddAccidents(nama, nik, bagian, kronologi, penanganan, tanggal, pukul, imageUrl, atasan);
        Map<String, Object> postValues = addAccidents.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/accidents/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }
    // [END write_fan_out]

    private void setEditingEnabled(boolean enabled) {
        mNamaField.setEnabled(enabled);
        mNikField.setEnabled(enabled);
        mBagianField.setEnabled(enabled);
        mKronoField.setEnabled(enabled);
        mPenangananField.setEnabled(enabled);
        mAtasan.setEnabled(enabled);
        if (enabled) {
            mSubmitButton.setVisibility(View.VISIBLE);
        } else {
            mSubmitButton.setVisibility(View.GONE);
        }
    }
}
