package login;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.android.andriodproject.R;
import com.android.andriodproject.databinding.ActivityMyPageBinding;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MyPage extends AppCompatActivity {

    private FirebaseUser user;
    private FirebaseAuth userAuth;
    private ActivityMyPageBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userAuth = FirebaseAuth.getInstance();

        mBinding = ActivityMyPageBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);




        mBinding.btnPrivCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {


            }
        });


        return ;
    }
}