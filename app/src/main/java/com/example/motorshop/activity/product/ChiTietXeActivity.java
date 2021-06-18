package com.example.motorshop.activity.product;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.motorshop.activity.R;
import com.example.motorshop.datasrc.Motor;
import com.example.motorshop.datasrc.MotorDetail;
import com.example.motorshop.datasrc.MotorInfo;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChiTietXeActivity extends AppCompatActivity {

    DanhSachChiTietXeAdapter danhSachChiTietXeAdapter;
    TextView tvTenSP, tvGia, tvMaXe;
    ImageView ivPhoTo;
    ListView lvHienThiChiTietXe;
    Button btnSua, btnCapNhat, btnThem;
    List<MotorDetail> motorDetailList;
    List<Motor> motorList;
    List<MotorInfo> motorInfoList;

    String ma;

    Motor motor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_xe);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setControl();

        motorList = new ArrayList<>();
        motorDetailList = new ArrayList<>();
        motorInfoList = new ArrayList<>();

        ChonXe(motor);

        extractMoTorDetails();

        setEvent();
    }

    private void setEvent() {

    }

    private void setControl() {
        tvTenSP = (TextView) findViewById(R.id.tvTenSP);
        tvMaXe = (TextView) findViewById(R.id.tvMaXe);
        tvGia = (TextView) findViewById(R.id.tvGia);
        ivPhoTo = (ImageView) findViewById(R.id.ivPhoto);
        btnSua = (Button) findViewById(R.id.btnSua);
        btnCapNhat = (Button) findViewById(R.id.btnCapNhat);
        btnThem = (Button) findViewById(R.id.btnThem);
        lvHienThiChiTietXe = (ListView) findViewById(R.id.lvHienThiChiTietXe);
    }

    private void extractMoTorDetails() {
        String url = "http://192.168.1.44:8080/api/motorshop/motorDetails/clear/motorName?motorName=" + tvTenSP.getText().toString();
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println("Response is: " + response.toString());

                for (int i = 0; i < response.length(); i++) {
                    try {

                        JSONArray jsonArray = response.getJSONArray(i);
                        MotorDetail motorDetail = new MotorDetail();
                        MotorInfo motorInfo = new MotorInfo();
                        motorDetail.setId(jsonArray.getInt(0));
                        motorDetail.setMotorId(Integer.parseInt(tvMaXe.getText().toString()));

                        if (jsonArray.getString(2).equals("Số Khung")){
                            motorInfo.setId(1);
                            motorInfo.setName("Số Khung");
                        }
                        if (jsonArray.getString(2).equals("Số Sườn")){
                            motorInfo.setId(2);
                            motorInfo.setName("Số Sườn");
                        }
                        if (jsonArray.getString(2).equals("Khối Lượng")){
                            motorInfo.setId(3);
                            motorInfo.setName("Khối Lượng");
                        }
                        if (jsonArray.getString(2).equals("Dài X Rộng X Cao")){
                            motorInfo.setId(4);
                            motorInfo.setName("Dài X Rộng X Cao");
                        }
                        if (jsonArray.getString(2).equals("Dung Tích Bình Xăng")){
                            motorInfo.setId(7);
                            motorInfo.setName("Dung Tích Bình Xăng");
                        }

                        motorDetail.setContent(jsonArray.getString(3));

                        Log.d("deserialize", jsonArray.getString(3));
                        motorDetailList.add(motorDetail);
                        motorInfoList.add(motorInfo);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                danhSachChiTietXeAdapter = new DanhSachChiTietXeAdapter(getApplicationContext(), R.layout.item_chi_tiet_xe, (ArrayList) motorDetailList, (ArrayList) motorInfoList);
                danhSachChiTietXeAdapter.notifyDataSetChanged();
                lvHienThiChiTietXe.setAdapter(danhSachChiTietXeAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        });

        requestQueue.add(jsonArrayRequest);

    }

    public void ChonXe(Motor motor) {
        this.motor = motor;
        Intent intent = getIntent();
        String tenXe = intent.getStringExtra("tenXe");
        tvTenSP.setText(tenXe);
        int donGia = intent.getIntExtra("donGia", 0);
        tvGia.setText(Integer.toString(donGia) + "VND");
        int maXe = intent.getIntExtra("maXe", 0);
        tvMaXe.setText(Integer.toString(maXe));

        //byte[] hinhAnh = intent.getByteArrayExtra("hinhAnh");
        //chuyển byte [] -> bitmap
        //Bitmap bitmap = BitmapFactory.decodeByteArray(hinhAnh, 0, hinhAnh.length);
        //ivPhoTo.setImageBitmap(bitmap);


    }

}