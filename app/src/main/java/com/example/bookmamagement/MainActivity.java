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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    protected FirebaseAuth firebaseAuth;
    protected FirebaseUser firebaseUser;
    protected DatabaseReference databaseReference;
    FirebaseDatabase database;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference =  FirebaseDatabase.getInstance().getReference();
        if(firebaseUser == null){
            finish();
            SendUserToLoginActivity();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.trangchu:
                SendUserToMainActivity();
                break;
            case R.id.thongtintaikhoan:
                SendUserToUpdateUserProfile();
                break;
            case R.id.quanly:
                myRef.child(firebaseUser.getUid()).child("isAdmin").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getValue().toString().equals("1")){
                            SendUserToManagerUser();
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Bạn không phải ADMIN.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
            case R.id.dangxuat:
                finish();
                logout();
                break;
        }


        return super.onOptionsItemSelected(item);
    }


    public void SendUserToLoginActivity(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, dangnhap.class);
        startActivity(intent);
    }

    public void SendUserToMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void SendUserToUpdateUserProfile(){
        Intent intent = new Intent(this, thongtincanhan.class);
        startActivity(intent);
    }

    public void SendUserToManagerUser(){
        Intent intent = new Intent(this, quanlytaikhoan.class);
        startActivity(intent);
    }

    public void logout(){
        firebaseAuth.signOut();
        Toast.makeText(MainActivity.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
        SendUserToLoginActivity();
    }


//    public void showMenuDialog() {
//        Dialog dialog = new Dialog(this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.dialog_menu);
//        dialog.setCanceledOnTouchOutside(false);
//
//        //Trang chu
//        ImageView imgTrangChu = dialog.findViewById(R.id.imgTrangChu);
//        TextView tvTrangChu = dialog.findViewById(R.id.tvTrangChu);
//
//        //Dang xuat
//        ImageView imgLogout = dialog.findViewById(R.id.imgLogout);
//        TextView tvDangXuat = dialog.findViewById(R.id.tvDangXuat);
//
//        //Cap nhat thong tin
//        ImageView imgCapNhatThongTin = dialog.findViewById(R.id.imgCapNhatThongTin);
//        TextView tvCapNhatThongTin = dialog.findViewById(R.id.tvCapNhatThongTin);
//
//
//        //Quan ly tai khoan
//        ImageView imgQuanLyTaiKhoan = dialog.findViewById(R.id.imgQuanLyTaiKhoan);
//        TextView tvQuanLyTaiKhoan = dialog.findViewById(R.id.tvQuanLyTaiKhoan);
//
//        tvDangXuat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                firebaseAuth.signOut();
//                Toast.makeText(MainActivity.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
//                SendUserToLoginActivity();
//
//            }
//        });
//        imgLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                firebaseAuth.signOut();
//                Toast.makeText(MainActivity.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
//                SendUserToLoginActivity();
//            }
//        });
//
//        tvTrangChu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SendUserToMainActivity();
//            }
//        });
//        imgTrangChu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SendUserToMainActivity();
//            }
//        });
//
//        tvCapNhatThongTin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SendUserToUpdateUserProfile();
//            }
//        });
//        imgCapNhatThongTin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SendUserToUpdateUserProfile();
//            }
//        });
//
//        tvQuanLyTaiKhoan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SendUserToManagerUser();
//            }
//        });
//        imgQuanLyTaiKhoan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SendUserToManagerUser();
//            }
//        });
//
//
//        dialog.show();
//    }
}



