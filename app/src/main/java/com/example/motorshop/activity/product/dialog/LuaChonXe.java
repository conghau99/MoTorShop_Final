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
import com.example.motorshop.activity.product.QuanLyXeActivity;
import com.example.motorshop.activity.product.SuaXeActivity;
import com.example.motorshop.datasrc.Image;
import com.example.motorshop.datasrc.Motor;

import java.util.ArrayList;

public class LuaChonXe extends Dialog {

    Motor motor;
    Image image;
    QuanLyXeActivity quanLyXeActivity;
    ArrayList<Motor> data = new ArrayList<>();
    ArrayList<Image> images = new ArrayList<>();

    public LuaChonXe(Context context, Motor motor) {
        super(context);
        this.motor = motor;
        this.quanLyXeActivity = (QuanLyXeActivity) context;

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dg_nua_chon_xe);

        //ánh xạ
        TextView tvTenXe = findViewById(R.id.tvTenXe);
        TextView tvSuaThongTinXe = findViewById(R.id.tvSuaThongTinXe);
        TextView tvXoaXe = findViewById(R.id.tvXoaXe);
        TextView tvChiTietXe = findViewById(R.id.tvChiTietXe);

        //set thông tin
        tvTenXe.setText(motor.getName());
        tvSuaThongTinXe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(quanLyXeActivity, SuaXeActivity.class);
                intent.putExtra("maXe", motor.getId());
                intent.putExtra("tenXe", motor.getName().trim());
                intent.putExtra("soLuong", motor.getAmount());
                intent.putExtra("donGia", motor.getPrice());
                intent.putExtra("hanBH", motor.getWarrantyPeriod());
                //intent.putExtra("hinhAnh",image.getImage());
                quanLyXeActivity.startActivity(intent);
                dismiss();
            }
        });

        tvXoaXe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue requestQueue = Volley.newRequestQueue(quanLyXeActivity.getApplicationContext());
                String url = "http://192.168.1.44:8080/api/motorshop/motors?id=" + motor.getId();

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

                Toast.makeText(quanLyXeActivity, "Xóa Xe Thành Công", Toast.LENGTH_SHORT).show();
                dismiss();
                quanLyXeActivity.startActivity(new Intent(quanLyXeActivity, QuanLyXeActivity.class));
            }
        });


        tvChiTietXe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(quanLyXeActivity, ChiTietXeActivity.class);
                intent.putExtra("maXe", motor.getId());
                intent.putExtra("tenXe", motor.getName().trim());
                intent.putExtra("donGia", motor.getPrice());
                //intent.putExtra("hinhAnh",image.getImage());
                quanLyXeActivity.startActivity(intent);
                dismiss();
            }
        });
    }
}
