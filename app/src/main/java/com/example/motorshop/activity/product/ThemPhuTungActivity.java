package com.example.motorshop.activity.product;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;

public class ThemPhuTungActivity extends AppCompatActivity {
    EditText edtTenPhuTung, edtSoLuong, edtDonGia, edtHanBaoHanh;
    Spinner spnHangPhuTung;
    ImageView ivPhoTo;
    Button btnChonAnh;

    Accessory accessory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_phu_tung);

        setControl();
        setEvent();
    }

    private void setEvent() {
        ArrayAdapter adapterHang = ArrayAdapter.createFromResource(this, R.array.HangPT, R.layout.item_spinner);
        spnHangPhuTung.setAdapter(adapterHang);

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
                Toast.makeText(ThemPhuTungActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(ThemPhuTungActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    private void openImagePicker() {
        TedBottomPicker.with(ThemPhuTungActivity.this)
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

    private void setControl() {
        edtSoLuong = (EditText) findViewById(R.id.edtSoLuong);
        edtHanBaoHanh = (EditText) findViewById(R.id.edtHanBaoHanh);
        edtTenPhuTung = (EditText) findViewById(R.id.edtTenPhuTung);
        edtDonGia = (EditText) findViewById(R.id.edtDonGia);
        spnHangPhuTung = (Spinner) findViewById(R.id.spnHangPhuTung);
        btnChonAnh = (Button) findViewById(R.id.btnChonAnh);
        ivPhoTo = (ImageView) findViewById(R.id.ivPhoto);
    }



    private Image getImage(){
        Image item = new Image();

        //chuyển data imageview -> byte[]
        BitmapDrawable bitmapDrawable = (BitmapDrawable) ivPhoTo.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        bitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
        byte[] hinhAnh = byteArray.toByteArray();

        item.setImage(hinhAnh);
        return item;
    }

    public void postData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("name",edtTenPhuTung.getText().toString());
            object.put("amount",Integer.parseInt(edtSoLuong.getText().toString()));
            object.put("price",Integer.parseInt(edtDonGia.getText().toString()));
            object.put("warrantyPeriod",Integer.parseInt(edtHanBaoHanh.getText().toString()));
            if (spnHangPhuTung.getSelectedItem().toString().equals("Ohlins"))
                object.put("brandId","BR04");
            if (spnHangPhuTung.getSelectedItem().toString().equals("Akrapovic"))
                object.put("brandId","BR05");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*JSONObject object1 = new JSONObject();
        try {
            //input your API parameters

            //chuyển data imageview -> byte[]
            BitmapDrawable bitmapDrawable = (BitmapDrawable) ivPhoTo.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();
            bitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
            byte[] hinhAnh = byteArray.toByteArray();
            String string = new String(hinhAnh);

            //object1.put("image", Arrays.toString(hinhAnh).trim());
            object1.put("image", hinhAnh);

        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        String url = "http://192.168.1.44:8080/api/motorshop/accessories";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Response is: "+ response.toString().substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        });

        requestQueue.add(jsonObjectRequest);
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

    public void themPhuTung(View view) {

        if (!checkNullInfo(edtTenPhuTung)) {
            thongBao("Bị thiếu tên phụ tùng");
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

        postData();
        Toast.makeText(ThemPhuTungActivity.this, "Thêm Phụ Tùng Thành Công", Toast.LENGTH_SHORT).show();
        //finish();
        Intent intent = new Intent(this, QuanLyPhuTungActivity.class);
        startActivity(intent);
    }

    public void quayLaiPhuTung(View view) {
        Intent intent = new Intent(ThemPhuTungActivity.this, QuanLyPhuTungActivity.class);
        startActivity(intent);
    }

}