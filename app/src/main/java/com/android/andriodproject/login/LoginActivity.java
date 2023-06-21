package com.android.andriodproject.login;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.andriodproject.MainActivity;
import com.android.andriodproject.R;
import com.android.andriodproject.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.view.Change;

public class LoginActivity extends AppCompatActivity
{
    private FirebaseAuth mFirebaseAuth;           // 파이어베이스 인증

    private ActivityLoginBinding mBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseApp.initializeApp(LoginActivity.this);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        mBinding.getRoot().findViewById(R.id.btn_login).setOnClickListener(onClickListener);
        mBinding.getRoot().findViewById(R.id.join_sign).setOnClickListener(onClickListener);
        mBinding.getRoot().findViewById(R.id.find_pwd).setOnClickListener(onClickListener);



    }

        View.OnClickListener onClickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(view.getId() == R.id.btn_login)
                {
                    login();
                }

                else if(view.getId() == R.id.join_sign)
                {
                    startLogin();
                }

                else if(view.getId() == R.id.find_pwd)
                {
                    startFindID();
                }
            }
        };





        private void login ()
        {
            {
                // 로그인 요청
                String strEmail = mBinding.loginEmail.getText().toString();
                String strPwd = mBinding.loginPwd.getText().toString();

                mFirebaseAuth.signInWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 성공 시점
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        }

            private void startLogin()
            {
                // 회원가입 화면으로 이동
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }


            private void startFindID()
            {
                // 아이디 찾기 화면 이동
                Intent intent = new Intent(LoginActivity.this, FindPass.class);
                startActivity(intent);

            }


}