package com.example.bookmamagement;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.google.firebase.database.Logger;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class quanlytaikhoan extends AppCompatActivity {
    protected FirebaseAuth firebaseAuth;
    protected FirebaseUser firebaseUser;
    FirebaseDatabase database;
    DatabaseReference myRef;

    ListView lvAccs;
    List<UserProfile> listU;
    UserApdater userApdater;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quanlytaikhoan);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");
        lvAccs = findViewById(R.id.lvAccs);
        listU =new ArrayList<>();
        userApdater =new UserApdater(this, R.layout.user_item);
        listU.clear();
        userApdater.clear();
        lvAccs.setAdapter(null);
        lvAccs.setAdapter(userApdater);
        //Lấy ra tất cả tài khoản nhân viên
        loadAccounts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.themtaikhoanmoi, menu);
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
                SendUserToManagerUser();
                break;
            case R.id.dangxuat:
                logout();
                break;
            case R.id.themtaikhoan:
                SendUserToRegister();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadAccounts() {
        listU.clear();
//        userApdater.clear();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds: snapshot.getChildren()){
                        String id = ds.getKey().toString();
                        String hoten = "", email= "", Sdt= "", ngaysinh= "", diachi= "", imgAva= "";
                        for (DataSnapshot ds2: ds.getChildren()){
                            if(ds2.getKey().toString().equals("hoten")){
                                hoten = ds2.getValue().toString();
                            }
                            if(ds2.getKey().toString().equals("email")){
                                email = ds2.getValue().toString();
                            }
                            if(ds2.getKey().toString().equals("sdt")){
                                Sdt = ds2.getValue().toString();
                            }
                            if(ds2.getKey().toString().equals("ngaysinh")){
                                ngaysinh = ds2.getValue().toString();
                            }
                            if(ds2.getKey().toString().equals("diachi")){
                                diachi = ds2.getValue().toString();
                            }
                            if(ds2.getKey().toString().equals("imgAva")){
                                imgAva = ds2.getValue().toString();
                            }

                        }
                        UserProfile u = new UserProfile(id, hoten, Sdt, email, diachi,ngaysinh, imgAva);
                        userApdater.add(u);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        userApdater.notifyDataSetChanged();
    }


//    private void loadAccounts() {
//        listU.clear();
//        userApdater.clear();
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    for(DataSnapshot ds: snapshot.getChildren()){
//                        String id = ds.getKey().toString();
//                        String hoten = "", email= "", Sdt= "", ngaysinh= "", diachi= "", imgAva= "";
//                        for (DataSnapshot ds2: ds.getChildren()){
//                            if(ds2.getKey().toString().equals("hoten")){
//                                hoten = ds2.getValue().toString();
//                            }
//                            if(ds2.getKey().toString().equals("email")){
//                                 email = ds2.getValue().toString();
//                            }
//                            if(ds2.getKey().toString().equals("sdt")){
//                                 Sdt = ds2.getValue().toString();
//                            }
//                            if(ds2.getKey().toString().equals("ngaysinh")){
//                                 ngaysinh = ds2.getValue().toString();
//                            }
//                            if(ds2.getKey().toString().equals("diachi")){
//                                 diachi = ds2.getValue().toString();
//                            }
//                            if(ds2.getKey().toString().equals("imgAva")){
//                                 imgAva = ds2.getValue().toString();
//                            }
//
//                        }
//                        UserProfile u = new UserProfile(id, hoten, Sdt, email, diachi,ngaysinh, imgAva);
//                        userApdater.add(u);
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        userApdater.notifyDataSetChanged();
//    }

    //Xoa san pham
    public void showXoaAccountDialog(UserProfile userProfile){
        AlertDialog.Builder dialogDeleteUser = new AlertDialog.Builder(this);
        dialogDeleteUser.setMessage("Ban co muon xoa " + userProfile.toString() + "?");
        dialogDeleteUser.setCancelable(false);
        dialogDeleteUser.setPositiveButton("Co", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                loadAccounts();
            }
        });
        dialogDeleteUser.setNegativeButton("Khong", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialogDeleteUser.show();
    }

    public void showThemTaiKhoan(){
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dangky);
        dialog.setCanceledOnTouchOutside(false);

        EditText edtEmail = dialog.findViewById(R.id.edtEmail);
        EditText edtMatKhau = dialog.findViewById(R.id.edtMatKhau);
        EditText edtNhapLaiMatKhau = dialog.findViewById(R.id.edtNhapLaiMatKhau);
        Button btnDangKy = dialog.findViewById(R.id.btnDangKy);

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString().trim();
                String matkhau = edtMatKhau.getText().toString().trim();
                String nhaplaimatkhau = edtNhapLaiMatKhau.getText().toString().trim();
                PerforAuth(email, matkhau, nhaplaimatkhau);
                dialog.dismiss();
            }
        });
    dialog.show();
    }



    private void PerforAuth(String email, String matKhau, String nhapLaiMatKhau){
        String emailPattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

        if(!email.matches(emailPattern)){
            Toast.makeText(this, "Email không hợp lệ.", Toast.LENGTH_SHORT).show();
        }else if(matKhau.isEmpty() || matKhau.length()<6){
            Toast.makeText(this, "Mật khẩu không được trống và phải lớn hơn 6 kí tự.", Toast.LENGTH_SHORT).show();
        }else if(!matKhau.equals(nhapLaiMatKhau)){
            Toast.makeText(this, "Mật khẩu không khớp.", Toast.LENGTH_SHORT).show();
        }
        else{
            firebaseAuth.createUserWithEmailAndPassword(email, matKhau).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(quanlytaikhoan.this, "Đăng ký thành công.", Toast.LENGTH_SHORT).show();
                        SetDataUserProfile(email, matKhau);
                    }
                    else{
                        Toast.makeText(quanlytaikhoan.this, "Đăng ký thất bại. " + task.getException(), Toast.LENGTH_SHORT).show();
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
                }
            }
        });

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
//                Toast.makeText(MainActivity.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                SendUserToLoginActivity();

            }
        });
        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
//                Toast.makeText(MainActivity.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
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

    public void SendUserToRegister(){
        Intent intent = new Intent(this, dangky.class);
        startActivity(intent);
    }

    public void logout(){
        firebaseAuth.signOut();
        Toast.makeText(this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
        SendUserToLoginActivity();
    }
}
