package com.example.bookmamagement;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class thongtincanhan extends AppCompatActivity {
    protected FirebaseAuth firebaseAuth;
    protected FirebaseUser firebaseUser;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users");
    StorageReference storageReference2;
    FirebaseStorage storage;


    TextView tvHoTenTTCN, tvNgaySinhTTCN, tvSDTTTCN, tvDiaChiTTCN;
    ImageView imgAvatarTTCN;
    Button btnCapNhat,btnCapNhatMatKhau;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thongtincanhan);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageReference2 = storage.getReference("FileAva");


        if(firebaseAuth.getCurrentUser() == null){
            finish();
            SendUserToLoginActivity();
        }
        imgAvatarTTCN = findViewById(R.id.imgAvatarTTCN);
        tvHoTenTTCN = findViewById(R.id.tvHoTenTTCN);
        tvNgaySinhTTCN = findViewById(R.id.tvNgaySinhTTCN);
        tvSDTTTCN = findViewById(R.id.tvSDTTTCN);
        tvDiaChiTTCN = findViewById(R.id.tvDiaChiTTCN);
        getOldInformation();
        btnCapNhat = findViewById(R.id.btnCapNhat);
        btnCapNhatMatKhau = findViewById(R.id.btnCapNhatMatKhau);

        btnCapNhatMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                SendUserToChangePassword();
            }
        });

        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                SendUserToUpdateProfile();
            }
        });
    }

    //Menu
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
                            Toast.makeText(thongtincanhan.this, "Bạn không phải ADMIN.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
            case R.id.dangxuat:
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
        Toast.makeText(this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
        SendUserToLoginActivity();
    }


    public void getOldInformation() {
        myRef.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    String diachi = snapshot.child("diachi").getValue().toString();
                    String hoten = snapshot.child("hoten").getValue().toString();
                    String ngaysinh = snapshot.child("ngaysinh").getValue().toString();
                    String sdt = snapshot.child("sdt").getValue().toString();
                    String fileNameAvatar = snapshot.child("fileNameAvatar").getValue().toString();
                    tvDiaChiTTCN.setText(diachi);
                    tvHoTenTTCN.setText(hoten);
                    tvNgaySinhTTCN.setText(ngaysinh);
                    tvSDTTTCN.setText(sdt);
                    storageReference2 = FirebaseStorage.getInstance().getReference().child("FileAva/" + fileNameAvatar);
//                    storageReference.child(fileNameAvatar);
                    try {
                        final File localFile = File.createTempFile("temp", "png");
                        storageReference2.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                imgAvatarTTCN.setImageBitmap(bitmap);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(capnhatthongtincanhan.this, "Error: " + e, Toast.LENGTH_SHORT).show();

                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                else{

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void SendUserToUpdateProfile(){
        Intent intent = new Intent(this, capnhatthongtincanhan.class);
        startActivity(intent);
    }

    public void SendUserToChangePassword(){
        Intent intent = new Intent(this, doimatkhau.class);
        startActivity(intent);
    }
//
//    public void SendUserToLoginActivity(){
//        Intent intent = new Intent(this, dangnhap.class);
//        startActivity(intent);
//    }
}
