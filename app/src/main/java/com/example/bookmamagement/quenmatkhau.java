package com.example.bookmamagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class quenmatkhau extends AppCompatActivity {
    Button btnQuenMatKhau;
    EditText edtEmail;
    TextView tvChuyenTrangDangNhap;

    protected FirebaseAuth firebaseAuth;
    protected FirebaseUser firebaseUser;
    protected DatabaseReference databaseReference;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quenmatkhau);

        //Ánh xạ
        tvChuyenTrangDangNhap = findViewById(R.id.tvChuyenTrangDangNhap);
        edtEmail = findViewById(R.id.edtEmail);
        btnQuenMatKhau = findViewById(R.id.btnQuenMatKhau);

        firebaseAuth = FirebaseAuth.getInstance();

        //Xử lý các sự kiện
        btnQuenMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForgotPassword();
            }
        });
        tvChuyenTrangDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToLoginActivity();
            }
        });
    }

    public void ForgotPassword() {
        String email = edtEmail.getText().toString().trim();
        if(!email.isEmpty()){

            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(quenmatkhau.this, "Đã gửi email khôi phục mật khẩu.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(quenmatkhau.this, "Chưa gửi được email khôi phục mật khẩu.", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }
        else{
            Toast.makeText(quenmatkhau.this, "Vui lòng nhập đúng Email.", Toast.LENGTH_SHORT).show();
        }

    }

    public void SendUserToLoginActivity(){
        Intent intent = new Intent(this, dangnhap.class);
        startActivity(intent);
    }
}
