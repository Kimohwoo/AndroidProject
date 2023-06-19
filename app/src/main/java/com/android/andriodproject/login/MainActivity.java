package com.android.andriodproject.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.android.andriodproject.R;
import com.android.andriodproject.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser user;

    private ActivityMainBinding mMainBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        mMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mMainBinding.getRoot();
        setContentView(view);



        mMainBinding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // 로그아웃
                mFirebaseAuth.signOut();

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);

                finish();
            }
        });


        mMainBinding.btnMypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {

                    /*String name = user.getDisplayName();*/
                    String email = user.getEmail();
                    /*Uri photoUrl = user.getPhotoUrl();*/


                    // The user's ID, unique to the Firebase project. Do NOT use this value to
                    // authenticate with your backend server, if you have one. Use
                    // FirebaseUser.getIdToken() instead.
                    String uid = user.getUid();

                    Intent intent = new Intent(MainActivity.this, MyPage.class);
                    intent.putExtra("uid", uid);
                    startActivity(intent);

                } else {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }



            }
        });

    }
}