package com.example.motorshop.activity.product;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.motorshop.activity.R;
import com.example.motorshop.datasrc.Motor;
import com.example.motorshop.datasrc.MotorDetail;

import java.util.ArrayList;

public class ChiTietXeActivity extends AppCompatActivity {

    //DanhSachChiTietXeAdapter danhSachChiTietXeAdapter;
    TextView tvTenSP, tvGia, tvMaXe;
    ImageView ivPhoTo;
    ListView lvHienThiChiTietXe;
    Button btnSua, btnCapNhat, btnThem;
    ArrayList<MotorDetail> data = new ArrayList<>();
    String ma;

    Motor motor;
    MotorDetail motorDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_xe);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setControl();
        setEvent();
    }

    private void setEvent() {

        //danhSachChiTietXeAdapter = new DanhSachChiTietXeAdapter(this, R.layout.item_chi_tiet_xe, data);
        //lvHienThiChiTietXe.setAdapter(danhSachChiTietXeAdapter);


        ChonXe(motor);
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

    public void ChonXe(Motor motor) {
        this.motor = motor;
        Intent intent = getIntent();
        String tenXe = intent.getStringExtra("tenXe");
        tvTenSP.setText(tenXe);
        int donGia = intent.getIntExtra("donGia", 0);
        tvGia.setText(Integer.toString(donGia) + "VND");
        String maXe = intent.getStringExtra("maXe");
        tvMaXe.setText(maXe);

        byte[] hinhAnh = intent.getByteArrayExtra("hinhAnh");
        //chuyển byte [] -> bitmap
        Bitmap bitmap = BitmapFactory.decodeByteArray(hinhAnh, 0, hinhAnh.length);
        ivPhoTo.setImageBitmap(bitmap);


    }

}