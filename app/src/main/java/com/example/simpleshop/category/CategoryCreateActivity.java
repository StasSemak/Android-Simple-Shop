package com.example.simpleshop.category;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.simpleshop.BaseActivity;
import com.example.simpleshop.ChangeImageActivity;
import com.example.simpleshop.MainActivity;
import com.example.simpleshop.R;
import com.example.simpleshop.dto.category.CategoryCreateDTO;
import com.example.simpleshop.dto.category.CategoryItemDTO;
import com.example.simpleshop.service.CategoryNetwork;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryCreateActivity extends BaseActivity {
    private static int SELECT_IMAGE_RESULT = 300;
    Uri uri = null;
    ImageView imgSelectImage;

    TextInputEditText txtCategoryName;
    TextInputEditText txtCategoryDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_create);
        imgSelectImage = findViewById(R.id.imgSelectImage);
        txtCategoryName = findViewById(R.id.txtCategoryName);
        txtCategoryDescription = findViewById(R.id.txtCategoryDescription);
    }

    public void onClickSelectImage(View view) {
        Intent intent = new Intent(this, ChangeImageActivity.class);
        startActivityForResult(intent, SELECT_IMAGE_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_IMAGE_RESULT && data != null) {
            uri = (Uri) data.getParcelableExtra("croppedUri");
            imgSelectImage.setImageURI(uri);
        }
    }

    public void onClickSave(View view) {
        CategoryCreateDTO model = new CategoryCreateDTO();
        if(!ValidateFields()) return;

        model.setName(txtCategoryName.getText().toString());
        model.setDescription(txtCategoryDescription.getText().toString());
        model.setImageBase64(uriGetBase64(uri));

        CategoryNetwork.getInstance()
                .getJsonApi()
                .create(model)
                .enqueue(new Callback<CategoryItemDTO>() {
                    @Override
                    public void onResponse(Call<CategoryItemDTO> call, Response<CategoryItemDTO> response) {
                        Intent intent = new Intent(CategoryCreateActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<CategoryItemDTO> call, Throwable t) {

                    }
                });
    }

    public boolean ValidateFields()
    {
        if(txtCategoryName.getText().toString().matches("")) {
            Toast
                .makeText(getApplicationContext(), "Name can't be empty!", Toast.LENGTH_SHORT)
                .show();
            return false;
        }
        else if(txtCategoryDescription.getText().toString().matches("")) {
            Toast
                .makeText(getApplicationContext(), "Description can't be empty!", Toast.LENGTH_SHORT)
                .show();
            return false;
        }
        else if (uri == null) {
            Toast
                .makeText(getApplicationContext(), "You must add an image!", Toast.LENGTH_SHORT)
                .show();
            return false;
        }
        return true;
    }

    public String uriGetBase64(Uri uri) {
        try {
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            }
            catch(IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            byte[] byteArr = bytes.toByteArray();
            return Base64.encodeToString(byteArr, Base64.DEFAULT);
        }
        catch(Exception ex) {
            return null;
        }
    }
}