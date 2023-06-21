package com.android.andriodproject.login;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.andriodproject.R;
import com.android.andriodproject.databinding.ActivityRegisterBinding;
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

    private ActivityRegisterBinding mRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("petproject");

        mRegister = ActivityRegisterBinding.inflate(getLayoutInflater());

        findViewById(R.id.btn_pwd_check);
        findViewById(R.id.btn_join);
        findViewById(R.id.btn_cancel);


    }

    View.OnClickListener onClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if(v.getId() == R.id.btn_pwd_check)
            {
                checkPwd();
            }

            else if(v.getId() == R.id.btn_join)
            {
                startRegister();
            }

            else if(v.getId() == R.id.btn_cancel)
            {
                moveLogin();
            }
        }
    };

        private void startRegister()
        {
            // 회원가입 처리시작
            String strEmail = mRegister.joinEmail.getText().toString();
            String strPwd = mRegister.joinPwd.getText().toString();
            String strPwdCheck = mRegister.btnPwdCheck.getText().toString();

            if(strEmail.length()>0 && strEmail.length()>0)
            {
                if (strEmail.length()>0 && strPwdCheck.length()>0)
                {
                    mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                                UserAccount account = new UserAccount();
                                account.setIdToken(firebaseUser.getUid());
                                account.setEmailId(firebaseUser.getEmail());
                                account.setPassword(strPwd);

                                // setValue : database에 insert 하는 방식
                                mDatabaseReference.child("UserAccount").child(firebaseUser.getUid()).setValue(account);

                                Toast.makeText(RegisterActivity.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                if (task.getException() != null) {
                                    Toast.makeText(RegisterActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        }

                    });
                }
            }
            else
                {
                    Toast.makeText(RegisterActivity.this, "회원가입에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
                }
        }

        private void checkPwd()
        {
            String strPwd = mRegister.joinPwd.getText().toString();
            String strPwdCheck = mRegister.btnPwdCheck.getText().toString();

            if(strPwd.equals(strPwdCheck))
                {
                    Toast.makeText(RegisterActivity.this, "비밀번호가 일치합니다.", Toast.LENGTH_SHORT).show();
                }
            else
            {
                Toast.makeText(RegisterActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        }




    private void moveLogin()
    {
        //로그인 화면으로 이동
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);

        finish();
    }

}