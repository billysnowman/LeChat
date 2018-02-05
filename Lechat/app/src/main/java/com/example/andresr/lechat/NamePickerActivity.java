package com.example.andresr.lechat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Let the user choose a name & email before continuing the app.
 * Created by Hugo Gresse on 03/12/2017.
 */

public class NamePickerActivity extends AppCompatActivity {

    public static final int PERMISSION_ACCOUNT = 2;

    EditText mNameEditText;
    EditText mEmailEditText;
    Button   mSubmitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_namepicker);

        mNameEditText = (EditText) findViewById(R.id.nameEditText);
        mEmailEditText = (EditText) findViewById(R.id.emailEditText);
        mSubmitButton = (Button) findViewById(R.id.sendButton);

        fillDefaultEmail();

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (saveUserData()) {
                    goToMainActivity();
                }
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCOUNT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    fillDefaultEmail();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    /**
     * Save user info if info are correct, if so return true.
     */
    private boolean saveUserData() {
        String name  = mNameEditText.getText().toString();
        String email = mEmailEditText.getText().toString();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email)) {
            return false;
        }

        UserStorage.saveUserInfo(this, name, email);
        return true;
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }


    private void fillDefaultEmail(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED) {


            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.GET_ACCOUNTS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.GET_ACCOUNTS},
                        PERMISSION_ACCOUNT);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            mEmailEditText.setText(Utils.getDefaultUserEmail(this));
        }
    }

}