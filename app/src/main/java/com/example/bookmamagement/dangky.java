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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;

public class dangky extends AppCompatActivity {
    private String email;
    private String matKhau;
    private String nhapLaiMatKhau;

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
            Toast.makeText(dangky.this, "Email không hợp lệ.", Toast.LENGTH_SHORT).show();
        }else if(matKhau.isEmpty() || matKhau.length()<6){
            Toast.makeText(dangky.this, "Mật khẩu không được trống và phải lớn hơn 6 kí tự.", Toast.LENGTH_SHORT).show();
        }else if(!matKhau.equals(nhapLaiMatKhau)){
            Toast.makeText(dangky.this, "Mật khẩu không khớp.", Toast.LENGTH_SHORT).show();
        }
        else{
            firebaseAuth.createUserWithEmailAndPassword(email, matKhau).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(dangky.this, "Đăng ký thành công.", Toast.LENGTH_SHORT).show();
                        SendUserToLoginActivity();
                    }
                    else{
                        Toast.makeText(dangky.this, "Đăng ký thất bại. " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }

    public void SetDataUserProfile(){

    }

    public void SendUserToLoginActivity(){
        Intent intent = new Intent(dangky.this, dangnhap.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}
