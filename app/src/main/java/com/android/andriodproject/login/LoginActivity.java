package com.android.andriodproject.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.andriodproject.MainActivity;
import com.android.andriodproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;           // 파이어베이스 인증
    private DatabaseReference mDatabaseReference; // 실시간 데이터 베이스
    private EditText join_password, join_email; // 회원가입 입력필드

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("petproject");

        join_email = findViewById(R.id.login_email);
        join_password = findViewById(R.id.login_pwd);


        Button btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                // 로그인 요청
                String strEmail = join_email.getText().toString();
                String strPwd = join_password.getText().toString();

                mFirebaseAuth.signInWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful()){
                            // 성공 시점
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });


        TextView join_sign = findViewById(R.id.join_sign);
        join_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // 회원가입 화면으로 이동
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

//        TextView find_pwd = findViewById(R.id.find_pwd);
//        find_pwd.setOnClickListener(new View.OnClickListener()
//        {
//
//            @Override
//            public void onClick(View v)
//            {
//                // 아이디 찾기 화면 이동
//
//            }
//        });
    }
}