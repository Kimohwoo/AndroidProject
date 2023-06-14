package com.android.andriodproject

import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.android.andriodproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.root

    }
    //어떻게 하면 push 가 될까요?
    // 1. 코멘트를 추가했습니다.
    // 2. 오우 씨가 현재 파일에 변경 사항을 push 했습니다.
    // 3. 성호가 현재 파일을 git push를 했는데 2번 변경사항 때문에 reject되었습니다.  -- nsh 병합 테스트 (메인 액티비티 변경)1
    // 4. 예상대로 리젝트 되고 nsh 브랜치로 체크아웃
    // 5. check 브랜치를 병합하고 (백업합니다.)
    // 6. check 브랜치를 패치로 받고 체크아웃 하면 아래와 같이 나옵니다.
    // 7. 원격지를 리베이스 하거나 commit을 드랍해라고 합니다.
    // 8. commit을 드랍하고,
    // 9. 백업되어있던 nsh 브랜치를 다시 병합합니다.
    // 10. 이제 모두 수정된 파일을 다시 원격지에 푸시합니다.

}