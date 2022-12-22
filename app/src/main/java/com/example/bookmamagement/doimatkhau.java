package com.example.bookmamagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class doimatkhau extends AppCompatActivity {
    protected FirebaseAuth firebaseAuth;
    protected FirebaseUser firebaseUser;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users");
    StorageReference storageReference2;
    FirebaseStorage storage;
    EditText  edtMatKhauMoi, edtNhapLaiMatKhauMoi;
    Button btnDoiMatKhau;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doimatkhau);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        //Ánh xạ
        edtMatKhauMoi = findViewById(R.id.edtMatKhauMoi);
        edtNhapLaiMatKhauMoi = findViewById(R.id.edtNhapLaiMatKhauMoi);
        btnDoiMatKhau = findViewById(R.id.btnDoiMatKhau);

        btnDoiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePassword();
            }
        });

    }

    private void ChangePassword(){
        String newPwd = edtMatKhauMoi.getText().toString().trim();
        String reTypePwd = edtNhapLaiMatKhauMoi.getText().toString().trim();

        if(!newPwd.equals(reTypePwd)){
            Toast.makeText(this, "Mật khẩu không khớp, hãy thử lại.", Toast.LENGTH_SHORT).show();
        }
        else if(newPwd.length()<6){
            Toast.makeText(this, "Mật khẩu phải có độ dài từ 6 kí tự trở lên.", Toast.LENGTH_SHORT).show();
        }
        else{
            firebaseUser.updatePassword(newPwd).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(doimatkhau.this, "Đổi mật khẩu thành công.", Toast.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().signOut();
                            SendUserToLoginActivity();
                    }
                    else{
                        Toast.makeText(doimatkhau.this, "Đổi mật khẩu không thành công.", Toast.LENGTH_SHORT).show();

                    }
                }
            });
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
                            Toast.makeText(doimatkhau.this, "Bạn không phải ADMIN.", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(doimatkhau.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
        SendUserToLoginActivity();
    }

}
