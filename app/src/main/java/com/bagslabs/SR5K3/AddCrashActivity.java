package com.bagslabs.SR5K3;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bagslabs.SR5K3.models.AddCrash;
import com.bagslabs.SR5K3.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddCrashActivity extends BaseActivity {

    private static final String TAG = "AddAccidentsActivity";
    private static final String REQUIRED = "Required";

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    private EditText mAreaField;
    private EditText mProblemField;
    private EditText mCauseField;
    private EditText mActionField;
    private FloatingActionButton mSubmitButton;

    TimePickerDialog timePickerDialog;
    private EditText tvDateResult;
    private EditText tvTimeResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_crash);

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        mAreaField = findViewById(R.id.field_area);
        mProblemField = findViewById(R.id.field_problem);
        mCauseField = findViewById(R.id.field_cause);
        mActionField = findViewById(R.id.field_action);
        mSubmitButton = findViewById(R.id.fab_submit_post);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });

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
                tvTimeResult.setText(+hourOfDay+":"+minute);
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
                int realMonth = monthofyear+1;
                tvDateResult.setText(dayofmonth + "/" + realMonth + "/" + year);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void submitPost() {
        final String tanggal = tvDateResult.getText().toString();
        final String pukul = tvTimeResult.getText().toString();
        final String area = mAreaField.getText().toString();
        final String problem = mProblemField.getText().toString();
        final String cause = mCauseField.getText().toString();
        final String action = mActionField.getText().toString();

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
        if (TextUtils.isEmpty(area)) {
            mAreaField.setError(REQUIRED);
            return;
        }

        // Nik is required
        if (TextUtils.isEmpty(problem)) {
            mProblemField.setError(REQUIRED);
            return;
        }

        // Bagian is required
        if (TextUtils.isEmpty(cause)) {
            mCauseField.setError(REQUIRED);
            return;
        }

        // Krono is required
        if (TextUtils.isEmpty(action)) {
            mActionField.setError(REQUIRED);
            return;
        }

        // Disable button so there are no multi-posts
        setEditingEnabled(false);
        Toast.makeText(this, "Posting...", Toast.LENGTH_SHORT).show();

        // [START single_value_read]
        final String userId = getUid();
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        // [START_EXCLUDE]
                        if (user == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(AddCrashActivity.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Write new post
                            writeNewPost(userId, user.username, area, problem, cause, action, tanggal, pukul);
                        }

                        // Finish this Activity, back to the stream
                        setEditingEnabled(true);
                        finish();
                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        // [START_EXCLUDE]
                        setEditingEnabled(true);
                        // [END_EXCLUDE]
                    }
                });
        // [END single_value_read]
    }

    private void setEditingEnabled(boolean enabled) {
        tvDateResult.setEnabled(enabled);
        tvTimeResult.setEnabled(enabled);
        mAreaField.setEnabled(enabled);
        mProblemField.setEnabled(enabled);
        mCauseField.setEnabled(enabled);
        mActionField.setEnabled(enabled);
        if (enabled) {
            mSubmitButton.setVisibility(View.VISIBLE);
        } else {
            mSubmitButton.setVisibility(View.GONE);
        }
    }

    // [START write_fan_out]
    private void writeNewPost(String userId, String username, String area, String problem, String cause, String action, String tanggal, String pukul) {
        // Create new addAccidents at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("crash").push().getKey();
        AddCrash addCrash = new AddCrash(userId, username, area, problem, cause, action, tanggal, pukul);
        Map<String, Object> postValues = addCrash.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/crash/" + key, postValues);

        mDatabase.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }
    // [END write_fan_out]
}
