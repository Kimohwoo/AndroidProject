package com.android.andriodproject.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.andriodproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class Change extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;

    private Button btn_reset_check, btn_reset_cancel;

    private EditText check_reset_email, check_rest_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);

        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        findViewById(R.id.btn_reset_check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (user != null)
                {
                    // Name, email address, and profile photo Url
                    String email = user.getEmail();

                    // Check if user's email is verified
                    boolean emailVerified = user.isEmailVerified();

                    // The user's ID, unique to the Firebase project. Do NOT use this value to
                    // authenticate with your backend server, if you have one. Use
                    // FirebaseUser.getIdToken() instead.
                    String uid = user.getUid();

                    Toast.makeText(Change.this, "아이디 비밀번호를 다시 확인해주세요", Toast.LENGTH_SHORT).show();

                }
                else if(user != null){

                    for (UserInfo profile : user.getProviderData()) {
                        // Id of the provider (ex: google.com)
                        String providerId = profile.getProviderId();

                        // UID specific to the provider
                        String uid = profile.getUid();

                        // Name, email address, and profile photo Url
                        String name = profile.getDisplayName();
                        String email = profile.getEmail();



                    }
                }


                else {
                    Toast.makeText(Change.this, "회원가입을 해주세요", Toast.LENGTH_SHORT).show();
                }

            }

        });


    }


}