package com.example.motorshop.activity.product;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

    String ma;

    Motor motor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_xe);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setControl();
        motorDetailList = new ArrayList<>();

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
                        JSONObject jsonObject = response.getJSONObject(i);
                        MotorDetail motorDetail = new MotorDetail();
                        motorDetail.setId(jsonObject.getInt("id"));
                        motorDetail.setMotorId(Integer.parseInt(tvMaXe.getText().toString()));

                        if (jsonObject.getString("name").equals("Số Khung")){
                            motorDetail.setMotorInfoId(1);
                        }
                        if (jsonObject.getString("name").equals("Số Sườn")){
                            motorDetail.setMotorInfoId(2);
                        }
                        if (jsonObject.getString("name").equals("Khối Lượng")){
                            motorDetail.setMotorInfoId(3);
                        }
                        if (jsonObject.getString("name").equals("Dài x Rộng x Cao")){
                            motorDetail.setMotorInfoId(4);
                        }
                        if (jsonObject.getString("name").equals("Dung tích bình xăng")){
                            motorDetail.setMotorInfoId(7);
                        }

                        motorDetail.setContent(jsonObject.getString("content"));
                        Log.d("deserialize", jsonObject.getString("content"));
                        motorDetailList.add(motorDetail);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                danhSachChiTietXeAdapter = new DanhSachChiTietXeAdapter(getApplicationContext(), R.layout.item_chi_tiet_xe, (ArrayList) motorDetailList);
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
        String maXe = intent.getStringExtra("maXe");
        tvMaXe.setText(maXe);

        //byte[] hinhAnh = intent.getByteArrayExtra("hinhAnh");
        //chuyển byte [] -> bitmap
        //Bitmap bitmap = BitmapFactory.decodeByteArray(hinhAnh, 0, hinhAnh.length);
        //ivPhoTo.setImageBitmap(bitmap);


    }

}