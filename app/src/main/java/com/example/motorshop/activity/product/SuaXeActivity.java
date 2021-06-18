package com.example.motorshop.activity.product;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.motorshop.activity.R;
import com.example.motorshop.datasrc.Accessory;
import com.example.motorshop.datasrc.Image;
import com.example.motorshop.datasrc.Motor;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;

public class SuaXeActivity extends AppCompatActivity {
    EditText edtTenXe, edtSoLuong, edtDonGia, edtHanBaoHanh;
    Spinner spnHangXe;
    ImageView ivPhoTo;
    Button btnChonAnh;
    int ma;

    Motor motor;
    @Nullable Image image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_xe);

        setControl();
        setEvent();
    }

    private void setControl() {
        edtSoLuong = (EditText) findViewById(R.id.edtSoLuong);
        edtHanBaoHanh = (EditText) findViewById(R.id.edtHanBaoHanh);
        edtTenXe = (EditText) findViewById(R.id.edtTenXe);
        edtDonGia = (EditText) findViewById(R.id.edtDonGia);
        spnHangXe = (Spinner) findViewById(R.id.spnHangXe);
        ivPhoTo = (ImageView) findViewById(R.id.ivPhoto);
        btnChonAnh = (Button) findViewById(R.id.btnChonAnh);
    }

    private void setEvent() {
        ArrayAdapter adapterHang = ArrayAdapter.createFromResource(this, R.array.Hang, R.layout.item_spinner);
        spnHangXe.setAdapter(adapterHang);

        ChonXe(motor);
        //ChonImage(image);

        btnChonAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });
    }

    private void requestPermission() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                openImagePicker();
                Toast.makeText(SuaXeActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(SuaXeActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    private void openImagePicker() {
        TedBottomPicker.with(SuaXeActivity.this)
                .showGalleryTile(false)
                .showCameraTile(false)
                .show(new TedBottomSheetDialogFragment.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {
                        // here is selected image uri
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            ivPhoTo.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void editData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("id",ma);
            object.put("name",edtTenXe.getText().toString());
            object.put("amount",Integer.parseInt(edtSoLuong.getText().toString()));
            object.put("price",Integer.parseInt(edtDonGia.getText().toString()));
            object.put("warrantyPeriod",Integer.parseInt(edtHanBaoHanh.getText().toString()));
            if (spnHangXe.getSelectedItem().toString().equals("Honda"))
                object.put("brandId","BR01");
            if (spnHangXe.getSelectedItem().toString().equals("Yamaha"))
                object.put("brandId","BR02");
            if (spnHangXe.getSelectedItem().toString().equals("SYM"))
                object.put("brandId","BR03");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = "http://192.168.1.44:8080/api/motorshop/motors";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Response is: "+ response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void editImage() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        try {
            //input your API parameters

            //chuyển data imageview -> byte[]
            BitmapDrawable bitmapDrawable = (BitmapDrawable) ivPhoTo.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();
            bitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
            byte[] hinhAnh = byteArray.toByteArray();

            object.put("image", Arrays.toString(hinhAnh).trim());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = "http://192.168.1.44:8080/api/motorshop/images";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Response is: "+ response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void ChonImage(Image image) {
        this.image = image;
        Intent intent = getIntent();

        byte[] hinhAnh = intent.getByteArrayExtra("hinhAnh");

        //chuyển byte [] -> bitmap
        Bitmap bitmap = BitmapFactory.decodeByteArray(hinhAnh, 0, hinhAnh.length);
        ivPhoTo.setImageBitmap(bitmap);
    }

    public void ChonXe(Motor motor) {
        this.motor = motor;
        Intent intent = getIntent();
        int maXe = intent.getIntExtra("maXe",0);
        ma = maXe;
        String tenXe = intent.getStringExtra("tenXe");
        edtTenXe.setText(tenXe);
        int soLuong = (intent.getIntExtra("soLuong", 0));
        edtSoLuong.setText(Integer.toString(soLuong));
        int donGia = intent.getIntExtra("donGia",0);
        edtDonGia.setText(Integer.toString(donGia));
        int hanBH = intent.getIntExtra("hanBH",0);
        edtHanBaoHanh.setText(Integer.toString(hanBH));

    }

    private boolean checkNullInfo(EditText e) {
        String s = "" + e.getText();
        if (s.length() == 0) {
            return false;
        } else {
            return true;
        }
    }

    private void thongBao(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public void suaXe(View view) {

        if (!checkNullInfo(edtTenXe)) {
            thongBao("Bị thiếu tên xe");
            return;
        }

        if (!checkNullInfo(edtSoLuong)) {
            thongBao("Bị thiếu số lượng");
            return;
        }

        if (!checkNullInfo(edtDonGia)) {
            thongBao("Bị thiếu đơn giá");
            return;
        }


        if (!checkNullInfo(edtHanBaoHanh)) {
            thongBao("Bị thiếu hạn bảo hành");
            return;
        }


        editData();
        editImage();
        Toast.makeText(SuaXeActivity.this, "Sửa Xe Thành Công", Toast.LENGTH_SHORT).show();
        //finish();
        Intent intent = new Intent(this, QuanLyXeActivity.class);
        startActivity(intent);

    }

    public void quayLai(View view) {
        Intent intent = new Intent(SuaXeActivity.this, QuanLyXeActivity.class);
        startActivity(intent);
    }

}