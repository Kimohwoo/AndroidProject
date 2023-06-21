package com.android.andriodproject

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.andriodproject.Model.Contacts
import com.android.andriodproject.databinding.ActivityMain2Binding
import com.android.andriodproject.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class MainActivity2 : AppCompatActivity() {


    private lateinit var binding: ActivityMain2Binding
    private lateinit var firebaseRef: DatabaseReference
    private lateinit var storageRef: StorageReference
    private val PERMISSION_REQUEST_CODE = 123

    private var mFirebaseAuth: FirebaseAuth? = null
    private var user: FirebaseUser? = null
    private var mMainBinding: ActivityMain2Binding? = null


    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        mFirebaseAuth = FirebaseAuth.getInstance()
        user = FirebaseAuth.getInstance().currentUser

        mMainBinding = ActivityMain2Binding.inflate(layoutInflater)

        if (checkReadWritePermission()) {
            Log.d("ksj", "이미 권환이 부여되어있음")
        } else {
            requestReadWritePermission()
        }

        firebaseRef = FirebaseDatabase.getInstance().getReference("contacts")
        storageRef = FirebaseStorage.getInstance().getReference("Images")

        binding.btnSend.setOnClickListener {
            saveData()
        }

        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
            binding.imgAdd.setImageURI(it)
            if (it != null) {
                uri = it
            }
        }

        binding.btnPickImage.setOnClickListener {
            pickImage.launch("image/*")
        }
    }

    private fun checkReadWritePermission(): Boolean {
        val readPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_MEDIA_IMAGES
        )
        val writePermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        return readPermission == PackageManager.PERMISSION_GRANTED && writePermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestReadWritePermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("ksj", "권한이 승인됨")
                // 예: 외부 저장소에서 파일 읽기 또는 쓰기
            } else {
                Log.d("ksj", "권한이 거부됨")
                // 예: 권한이 거부되어 기능을 ga3사용할 수 없음을 알리는 메시지 표시
            }
        }
    }







    private fun saveData() {
        val emailId = binding.edtEmailId.text.toString()
        val pw = binding.edtPw.text.toString()

        if (emailId.isEmpty()) {
            binding.edtEmailId.error = "write a emailId"
            return
        }
        if (pw.isEmpty()) {
            binding.edtPw.error = "write a pw number"
            return
        }

        val idToken = firebaseRef.push().key ?: ""
        var contacts: Contacts

        uri?.let {
            storageRef.child(idToken).putFile(it)
                .addOnSuccessListener { task ->
                    task.metadata!!.reference!!.downloadUrl
                        .addOnSuccessListener { url ->
                            val imgUrl = url.toString()

                            contacts = Contacts(idToken , emailId, pw, imgUrl)

                            firebaseRef.child(idToken).setValue(contacts)
                                .addOnCompleteListener {
                                    // 성공적으로 데이터 저장됨
                                }
                                .addOnFailureListener { _ ->
                                   Log.d("ksj", "데이터저장 실패")
                                }
                        }
                }
        }
    }
}
