package com.example.bookmamagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    protected FirebaseAuth firebaseAuth;
    protected FirebaseUser firebaseUser;
    protected DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            finish();
            SendUserToLoginActivity();
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference =  FirebaseDatabase.getInstance().getReference();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu:
                showMenuDialog();
        }
        return super.onOptionsItemSelected(item);
    }


    public void showMenuDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_menu);
        dialog.setCanceledOnTouchOutside(false);

        //Trang chu
        ImageView imgTrangChu = dialog.findViewById(R.id.imgTrangChu);
        TextView tvTrangChu = dialog.findViewById(R.id.tvTrangChu);

        //Dang xuat
        ImageView imgLogout = dialog.findViewById(R.id.imgLogout);
        TextView tvDangXuat = dialog.findViewById(R.id.tvDangXuat);

        //Cap nhat thong tin
        ImageView imgCapNhatThongTin = dialog.findViewById(R.id.imgCapNhatThongTin);
        TextView tvCapNhatThongTin = dialog.findViewById(R.id.tvCapNhatThongTin);


        //Quan ly tai khoan
        ImageView imgQuanLyTaiKhoan = dialog.findViewById(R.id.imgQuanLyTaiKhoan);
        TextView tvQuanLyTaiKhoan = dialog.findViewById(R.id.tvQuanLyTaiKhoan);

        tvDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                Toast.makeText(MainActivity.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                SendUserToLoginActivity();

            }
        });
        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                Toast.makeText(MainActivity.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                SendUserToLoginActivity();
            }
        });

        tvTrangChu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToMainActivity();
            }
        });
        imgTrangChu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToMainActivity();
            }
        });

        tvCapNhatThongTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToUpdateUserProfile();
            }
        });
        imgCapNhatThongTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToUpdateUserProfile();
            }
        });

        tvQuanLyTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToManagerUser();
            }
        });
        imgQuanLyTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToManagerUser();
            }
        });


        dialog.show();
    }

    public void SendUserToLoginActivity(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, dangnhap.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void SendUserToMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void SendUserToUpdateUserProfile(){
        Intent intent = new Intent(this, capnhatthongtincanhan.class);
        startActivity(intent);
    }

    public void SendUserToManagerUser(){
        Intent intent = new Intent(this, quanlytaikhoan.class);
        startActivity(intent);
    }
}