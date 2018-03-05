package com.bagslabs.SR5K3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bagslabs.SR5K3.models.AddAccidents;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.IOException;

public class AccidentsDetailActivity extends BaseActivity {

    private static final String TAG = "AccidentsDetailActivity";
    public static final String EXTRA_POST_KEY = "post_key";

    private DatabaseReference mPostReference;
    private ValueEventListener mPostListener;
    String mPostKey;

    public TextView namaView;
    public TextView nikView;
    public TextView bagianView;
    public TextView kronologiView;
    public TextView penangananView;
    public ImageView imageUrl;
    Bitmap imageBitmap;
    String imageString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accidents_post_detail);

        // Get post key from intent
        mPostKey = getIntent().getStringExtra(EXTRA_POST_KEY);
        if (mPostKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
        }

        // Initialize Database
        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("accidents").child(mPostKey);

        // Initialize Views
        namaView = findViewById(R.id.post_nama);
        nikView = findViewById(R.id.post_nik);
        bagianView = findViewById(R.id.post_bagian);
        kronologiView = findViewById(R.id.post_kronologi);
        penangananView = findViewById(R.id.post_penanganan);
        imageUrl = findViewById(R.id.post_photo);

    }

    @Override
    public void onStart() {
        super.onStart();

        // Add value event listener to the post
        // [START post_value_event_listener]
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get AddAccidents object and use the values to update the UI
                AddAccidents addAccidents = dataSnapshot.getValue(AddAccidents.class);
                // [START_EXCLUDE]
                if (addAccidents != null) {
                    namaView.setText(addAccidents.nama);
                    nikView.setText(addAccidents.nik);
                    bagianView.setText(addAccidents.bagian);
                    kronologiView.setText(addAccidents.kronologi);
                    penangananView.setText(addAccidents.penanganan);
                    imageString = addAccidents.imageEncoded;

                    try {
                        imageBitmap = decodeFromFirebaseBase64(imageString);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                imageUrl.setImageBitmap(imageBitmap);
                // [END_EXCLUDE]
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting AddAccidents failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(AccidentsDetailActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        mPostReference.addValueEventListener(postListener);
        // [END post_value_event_listener]

        // Keep copy of post listener so we can remove it when app stops
        mPostListener = postListener;
    }

    public static Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }

    @Override
    public void onStop() {
        super.onStop();

        // Remove post value event listener
        if (mPostListener != null) {
            mPostReference.removeEventListener(mPostListener);
        }
    }
}
