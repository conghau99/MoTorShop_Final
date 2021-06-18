package com.example.motorshop.activity.product.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.motorshop.activity.R;
import com.example.motorshop.activity.product.ChiTietXeActivity;
import com.example.motorshop.activity.product.QuanLyPhuTungActivity;
import com.example.motorshop.activity.product.SuaPhuTungActivity;
import com.example.motorshop.datasrc.Accessory;
import com.example.motorshop.datasrc.Image;

import java.util.ArrayList;

public class LuaChonPhuTung extends Dialog {

    Accessory accessory;
    Image image;
    QuanLyPhuTungActivity quanLyPhuTungActivity;
    ArrayList<Accessory> data = new ArrayList<>();
    ArrayList<Image> images = new ArrayList<>();

    public LuaChonPhuTung(Context context, Accessory accessory) {
        super(context);
        this.accessory = accessory;
        this.quanLyPhuTungActivity = (QuanLyPhuTungActivity) context;

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dg_nua_chon_phu_tung);

        //ánh xạ
        TextView tvTenPhuTung = findViewById(R.id.tvTenPhuTung);
        TextView tvSuaThongTinPhuTung = findViewById(R.id.tvSuaThongTinPhuTung);
        TextView tvXoaPhuTung = findViewById(R.id.tvXoaPhuTung);
        TextView tvChiTietPhuTung = findViewById(R.id.tvChiTietPhuTung);

        //set thông tin
        tvTenPhuTung.setText(accessory.getName());
        tvSuaThongTinPhuTung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(quanLyPhuTungActivity, SuaPhuTungActivity.class);
                intent.putExtra("maPT",accessory.getId());
                intent.putExtra("tenPT",accessory.getName().trim());
                intent.putExtra("soLuong",accessory.getAmount());
                intent.putExtra("donGia",accessory.getPrice());
                intent.putExtra("hanBH",accessory.getWarrantyPeriod());
                //intent.putExtra("hinhAnh",image.getImage());
                quanLyPhuTungActivity.startActivity(intent);
                dismiss();
            }
        });

        tvXoaPhuTung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue requestQueue = Volley.newRequestQueue(quanLyPhuTungActivity.getApplicationContext());
                String url = "http://192.168.1.44:8080/api/motorshop/accessories?id=" + accessory.getId();

                StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Response is: " + response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("tag", "onErrorResponse: " + error.getMessage());
                    }
                });
                requestQueue.add(stringRequest);

                Toast.makeText(quanLyPhuTungActivity, "Xóa Phụ Tùng Thành Công", Toast.LENGTH_SHORT).show();
                dismiss();
                quanLyPhuTungActivity.startActivity(new Intent(quanLyPhuTungActivity, QuanLyPhuTungActivity.class));
            }
        });

        tvChiTietPhuTung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(quanLyPhuTungActivity, ChiTietXeActivity.class);
                intent.putExtra("maPT", accessory.getId());
                intent.putExtra("tenPT", accessory.getName().trim());
                intent.putExtra("donGia", accessory.getPrice());
                //intent.putExtra("hinhAnh",image.getImage());
                quanLyPhuTungActivity.startActivity(intent);
                dismiss();
            }
        });

    }
}
