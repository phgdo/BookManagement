package com.example.bookmamagement;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.Map;

public class capnhatthongtincanhan extends AppCompatActivity {

    protected FirebaseAuth firebaseAuth;
    protected FirebaseUser firebaseUser;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users");
    FirebaseStorage storage;
    StorageReference storageReference;
    StorageReference storageReference2;
    StorageReference ref;



    StorageReference storageRef;


    Button btnCapNhatThongTin;
    EditText edtHoVaTen, edtNgaySinh, edtSoDienThoai, edtDiaChi, edtEmail;
    ImageView imgAvatar;
    private Uri imgUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.capnhatthongtincanhan);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("FileAva");
        storageReference2 = storage.getReference("FileAva");
        storageRef = storage.getReference();


        if(firebaseAuth.getCurrentUser() == null){
            finish();
            SendUserToLoginActivity();
        }
        else{
            getOldInformation();
            imgAvatar = findViewById(R.id.imgAvatar);
            edtHoVaTen = findViewById(R.id.edtHoVaTen);
            edtSoDienThoai = findViewById(R.id.edtSoDienThoai);
            edtNgaySinh = findViewById(R.id.edtNgaySinh);
            edtDiaChi = findViewById(R.id.edtDiaChi);
//            edtEmail = findViewById(R.id.edtEmail);
            btnCapNhatThongTin = findViewById(R.id.btnCapNhatThongTin);

            imgAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    xuLyLayAnh();
                }
            });
            //Xử lý nút Cập nhật.
            btnCapNhatThongTin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UpdateProfile();
                }
            });



        }

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
                    edtDiaChi.setText(diachi);
                    edtHoVaTen.setText(hoten);
                    edtNgaySinh.setText(ngaysinh);
                    edtSoDienThoai.setText(sdt);
                    storageReference2 = FirebaseStorage.getInstance().getReference().child("FileAva/" + fileNameAvatar);
//                    storageReference.child(fileNameAvatar);
                    try {
                        final File localFile = File.createTempFile("temp", "png");
                        storageReference2.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                imgAvatar.setImageBitmap(bitmap);
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
        myRef.child(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                Toast.makeText(capnhatthongtincanhan.this, "Lay tin thành công.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtensionFromString(String name) {
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf);
    }

    public void UpdateProfile() {
        UserProfile u = new UserProfile();
        Map<String, Object> newUserValues = u.toMap();
        String hoten = edtHoVaTen.getText().toString().trim();
        String sdt = edtSoDienThoai.getText().toString().trim();
        String ngaysinh = edtNgaySinh.getText().toString().trim();
        String diachi = edtDiaChi.getText().toString().trim();
//        String email = edtEmail.getText().toString().trim();
        myRef.child(firebaseUser.getUid()).child("diachi").setValue(diachi);
        myRef.child(firebaseUser.getUid()).child("hoten").setValue(hoten);
        myRef.child(firebaseUser.getUid()).child("sdt").setValue(sdt);
        myRef.child(firebaseUser.getUid()).child("ngaysinh").setValue(ngaysinh);
        myRef.child(firebaseUser.getUid()).child("email").setValue(diachi);
        u.setDiachi(diachi);
//        u.setEmail(email);
        u.setHoten(hoten);
        u.setNgaysinh(ngaysinh);
//        u.setUri(imgUri);
        u.setSdt(sdt);
        u.setEmail(firebaseUser.getEmail().toString());

        if(imgUri == null || imgUri.equals("")){
            myRef.child(firebaseUser.getUid()).child("fileNameAvatar").setValue(firebaseUser.getUid());
            u.setFileNameAvatar(firebaseUser.getUid());
        }
        else{
            String filename = getFileName(imgUri);
            ref = storageReference.child(firebaseUser.getUid());
            ref.putFile(imgUri);
            myRef.child(firebaseUser.getUid()).child("fileNameAvatar").setValue(firebaseUser.getUid());
        }
        Toast.makeText(capnhatthongtincanhan.this, "Cập nhật thông tin thành công.", Toast.LENGTH_SHORT).show();


//        myRef.child(firebaseUser.getUid()).setValue(u).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if(task.isSuccessful()){
//                    Toast.makeText(capnhatthongtincanhan.this, "Cập nhật thông tin thành công.", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    Toast.makeText(capnhatthongtincanhan.this, "Cập nhật thông tin thất bại.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        myRef.child(firebaseUser.getUid()).child("isAdmin").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    myRef.child(firebaseUser.getUid()).child("isAdmin").setValue(snapshot);
//                }
//                if(!snapshot.exists()){
//                    myRef.child(firebaseUser.getUid()).child("isAdmin").setValue("0");
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    public void setLevel(){
        myRef.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    myRef.child(firebaseUser.getUid()).child("isAdmin").setValue("0").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(capnhatthongtincanhan.this, "Cập nhật thông tin thành công.", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(capnhatthongtincanhan.this, "Cập nhật thông tin thất bại.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void xuLyLayAnh() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent.createChooser(intent, "Chọn ảnh"), 113);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==113 && resultCode==RESULT_OK){
            try {

                imgUri = data.getData();
                Bitmap hinh = MediaStore.Images.Media.getBitmap(getContentResolver(),imgUri);
                imgAvatar.setImageBitmap(hinh);

            }
            catch (Exception ex){
                Log.e("Khong the hien thi anh", ex.toString());
            }
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @SuppressLint("Range")
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
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
                            Toast.makeText(capnhatthongtincanhan.this, "Bạn không phải ADMIN.", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(capnhatthongtincanhan.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
        SendUserToLoginActivity();
    }


}
