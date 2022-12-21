package com.example.bookmamagement;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class UserApdater extends ArrayAdapter<UserProfile> {
    Activity context;
    int resource;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference2;

    public UserApdater(Activity context, int resource){
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = this.context.getLayoutInflater();
        View customView = inflater.inflate(this.resource, null);

        ImageView imgUser = customView.findViewById(R.id.imgUser);
        ImageView imgDelete = customView.findViewById(R.id.imgDelete);
        ImageView imgEdit = customView.findViewById(R.id.imgEdit);
        TextView tvEmail = customView.findViewById(R.id.tvEmail);
        TextView tvHoTen = customView.findViewById(R.id.tvHoTen);
        TextView tvSDT = customView.findViewById(R.id.tvSDT);
        TextView tvNgaySinh = customView.findViewById(R.id.tvNgaySinh);

        UserProfile userProfile = getItem(position);

        tvEmail.setText(userProfile.getEmail());
        tvHoTen.setText(userProfile.getHoten());
        tvSDT.setText(userProfile.getSdt());
        tvNgaySinh.setText(userProfile.getNgaysinh());

        storageReference2 = FirebaseStorage.getInstance().getReference().child("FileAva/" + userProfile.getFileNameAvatar());
        try {
            final File localFile = File.createTempFile("temp", "png");
            storageReference2.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    imgUser.setImageBitmap(bitmap);
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

        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Xóa tài khoản " + getItem(position).getEmail(), Toast.LENGTH_SHORT).show();
                ((quanlytaikhoan) context).showXoaAccountDialog(userProfile);
            }
        });

        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((quanlytaikhoan) context).showCapQuyenAdmin(userProfile);
            }
        });
        return customView;
    }

}
