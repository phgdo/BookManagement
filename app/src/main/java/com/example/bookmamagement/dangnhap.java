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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class dangnhap extends AppCompatActivity {
    EditText edtEmail;
    EditText edtMatKhau;
    Button btnDangNhap;
    TextView tvQuenMatKhau;


    String emailPattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dangnhap);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");

        //Kiểm tra đã đăng nhập chưa, nếu đăng nhập rồi thì chuyển đến trang chủ
//        if(firebaseAuth.getCurrentUser()!=null){
//            finish();
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//            //prpfile activity.
//        }
//        else{
//            SetDataUserProfile();
//
//        }


        edtEmail = findViewById(R.id.edtEmail);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        tvQuenMatKhau = findViewById(R.id.tvQuenMatKhau);

        tvQuenMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToForgotPasswordActivity();
            }
        });

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PerforLogin();
            }

        });
    }



    public void PerforLogin(){
        String email = edtEmail.getText().toString().trim();
        String matKhau = edtMatKhau.getText().toString().trim();

        if(!email.matches(emailPattern)){
            Toast.makeText(dangnhap.this, "Email không hợp lệ.", Toast.LENGTH_SHORT).show();
        }else if(matKhau.isEmpty()  || matKhau.length()<6){
            Toast.makeText(dangnhap.this, "Mật khẩu không được trống và phải lớn hơn 6 kí tự.", Toast.LENGTH_SHORT).show();
        }
        else{
            firebaseAuth.signInWithEmailAndPassword(email, matKhau).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(dangnhap.this, "Đăng nhập thành công.", Toast.LENGTH_SHORT).show();
                        SendUserToMainActivity();
                    }
                    else{
                        Toast.makeText(dangnhap.this, "Đăng nhập thất bại. " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    public void SendUserToMainActivity(){
        Intent intent = new Intent(dangnhap.this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void SendUserToForgotPasswordActivity(){
        Intent intent = new Intent(dangnhap.this, quenmatkhau.class);
        startActivity(intent);
    }
}
