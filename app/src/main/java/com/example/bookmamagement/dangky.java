package com.example.bookmamagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

public class dangky extends AppCompatActivity {
    private String email;
    private String matKhau;
    private String nhapLaiMatKhau;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users");

    protected FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    protected FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    EditText edtEmail, edtMatKhau, edtNhapLaiMatKhau;
    Button btnDangKy;
    String emailPattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dangky);

        edtEmail = findViewById(R.id.edtEmail);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        edtNhapLaiMatKhau = findViewById(R.id.edtNhapLaiMatKhau);

        btnDangKy = findViewById(R.id.btnDangKy);

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PerforAuth();
            }
        });

    }

    public void PerforAuth(){
        String email = edtEmail.getText().toString().trim();
        String matKhau = edtMatKhau.getText().toString().trim();
        String nhapLaiMatKhau = edtNhapLaiMatKhau.getText().toString().trim();

        if(!email.matches(emailPattern)){
            Toast.makeText(dangky.this, "Email kh??ng h???p l???.", Toast.LENGTH_SHORT).show();
        }else if(matKhau.isEmpty() || matKhau.length()<6){
            Toast.makeText(dangky.this, "M???t kh???u kh??ng ???????c tr???ng v?? ph???i l???n h??n 6 k?? t???.", Toast.LENGTH_SHORT).show();
        }else if(!matKhau.equals(nhapLaiMatKhau)){
            Toast.makeText(dangky.this, "M???t kh???u kh??ng kh???p.", Toast.LENGTH_SHORT).show();
        }
        else{
            firebaseAuth.createUserWithEmailAndPassword(email, matKhau).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(dangky.this, "????ng k?? th??nh c??ng.", Toast.LENGTH_SHORT).show();
                        SetDataUserProfile(email, matKhau);
                        SendUserToLoginActivity();
                    }
                    else{
                        Toast.makeText(dangky.this, "????ng k?? th???t b???i. " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }

    public void SetDataUserProfile(String email, String password){
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    myRef.child(user.getUid()).child("isAdmin").setValue("0");
                    myRef.child(user.getUid()).child("email").setValue(email);
                    myRef.child(user.getUid()).child("diachi").setValue("");
                    myRef.child(user.getUid()).child("fileNameAvatar").setValue("");
                    myRef.child(user.getUid()).child("hoten").setValue("");
                    myRef.child(user.getUid()).child("id").setValue(user.getUid());
                    myRef.child(user.getUid()).child("ngaysinh").setValue("");
                    myRef.child(user.getUid()).child("sdt").setValue("");
//                    UserProfile userProfile = new UserProfile("", "", "", email, "", "", "");
//                    myRef.child(user.getUid()).setValue(userProfile);
                }
            }
        });

    }

    public void SendUserToManagerUser(){
        Intent intent = new Intent(this, quanlytaikhoan.class);
        startActivity(intent);
    }
    public void SendUserToLoginActivity(){
        firebaseAuth.signOut();
        Intent intent = new Intent(this, dangnhap.class);
        startActivity(intent);
    }

}
