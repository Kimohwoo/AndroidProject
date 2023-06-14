package com.android.andriodproject.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.andriodproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;           // 파이어베이스 인증
    private DatabaseReference mDatabaseReference; // 실시간 데이터 베이스
    private EditText join_email, join_pwd; // 회원가입 입력필드
    private Button btn_join, btn_cancel; // 회원가입 이메일확인, 가입 확인, 가입 취소 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("petproject");

        join_email = findViewById(R.id.join_email);
        join_pwd = findViewById(R.id.join_pwd);


        btn_join = findViewById(R.id.btn_join);
        btn_cancel = findViewById(R.id.btn_cancel); //


        btn_join.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                // 회원가입 처리시작
                String strEmail = join_email.getText().toString();
                String strPwd = join_pwd.getText().toString();

                mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                            UserAccount account = new UserAccount();
                            account.setIdToken(firebaseUser.getUid());
                            account.setEmailId(firebaseUser.getEmail());
                            account.setPassword(strPwd);

                            // setValue : database에 insert 하는 방식
                            mDatabaseReference.child("UserAccount").child(firebaseUser.getUid()).setValue(account);

                            Toast.makeText(RegisterActivity.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(RegisterActivity.this, "회원가입에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

        Button btn_cancel = findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //로그인 화면으로 이동
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);

                finish();
            }
        });


    }
}