package com.example.motorshop.activity.product;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.motorshop.activity.R;
import com.example.motorshop.datasrc.Image;
import com.example.motorshop.datasrc.Motor;

import java.util.ArrayList;

public class DanhSachXeAdapter extends ArrayAdapter<Motor> {
    Context context;
    int resource;
    public ArrayList<Motor> data;
    @Nullable
    public ArrayList<Image> images;

    public DanhSachXeAdapter(Context context, int resource, ArrayList data, @Nullable ArrayList images) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.data = data;
        this.images = images;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Motor getItem(int position) {
        return data.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item_xe, null);


        if (data.size() > 0) {

            TextView tvTenXe = convertView.findViewById(R.id.tvTenXe);
            TextView tvTenHang = convertView.findViewById(R.id.tvTenHang);
            TextView tvMaXe = convertView.findViewById(R.id.tvMaXe);
            TextView tvSoLuong = convertView.findViewById(R.id.tvSoLuong);
            TextView tvDonGia = convertView.findViewById(R.id.tvDonGia);
            TextView tvHanBaoHanh = convertView.findViewById(R.id.tvHanBaoHanh);

            ImageView ivXe = convertView.findViewById(R.id.ivXe);

            Motor motor = data.get(position);
            //Image image = images.get(position);

            tvMaXe.setText(Integer.toString(motor.getId()));
            tvTenXe.setText(motor.getName());

            if (motor.getBrandId().toString().equals("BR01"))
                tvTenHang.setText("Honda");
            if (motor.getBrandId().toString().equals("BR02"))
                tvTenHang.setText("Yamaha");
            if (motor.getBrandId().toString().equals("BR03"))
                tvTenHang.setText("SYM");
            tvSoLuong.setText(Integer.toString(motor.getAmount()));
            tvDonGia.setText(Integer.toString(motor.getPrice()));
            tvHanBaoHanh.setText(motor.getWarrantyPeriod() + " tháng");


            //byte[] hinhAnh = image.getImage();

            //chuyển byte [] -> bitmap
            //Bitmap bitmap = BitmapFactory.decodeByteArray(hinhAnh, 0, hinhAnh.length);
            //ivXe.setImageBitmap(bitmap);
        }

        return convertView;
    }
}
