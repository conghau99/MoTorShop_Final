package com.example.motorshop.activity.product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.motorshop.activity.R;
import com.example.motorshop.datasrc.MotorDetail;
import com.example.motorshop.datasrc.MotorInfo;

import java.util.ArrayList;

public class DanhSachChiTietXeAdapter extends ArrayAdapter<MotorDetail> {
    Context context;
    int resource;
    public ArrayList<MotorDetail> data;
    public ArrayList<MotorInfo> motorInfos;
    ChiTietXeActivity chiTietXeActivity;

    public DanhSachChiTietXeAdapter(Context context, int resource, ArrayList data, ArrayList motorInfos) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.data = data;
        this.motorInfos = motorInfos;
    }

    @Override
    public int getCount() {
        return data.size() & motorInfos.size();
    }

    @Override
    public MotorDetail getItem(int position) {
        return data.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item_chi_tiet_xe, null);

        if (data.size() > 0) {

            EditText edtThongSoXe = convertView.findViewById(R.id.edtThongSoXe);
            EditText edtChiTietXe = convertView.findViewById(R.id.edtChiTietXe);

            MotorDetail motorDetail = data.get(position);
            MotorInfo motorInfo = motorInfos.get(position);

            edtChiTietXe.setEnabled(false);
            edtThongSoXe.setEnabled(false);

            /*if (motorDetail.getMotorInfoId().equals(1)){
                edtThongSoXe.setText("Số Khung");
            }
            if (motorDetail.getMotorInfoId().equals(2)){
                edtThongSoXe.setText("Số Sườn");
            }
            if (motorDetail.getMotorInfoId().equals(3)){
                edtThongSoXe.setText("Khối Lượng");
            }
            if (motorDetail.getMotorInfoId().equals(4)){
                edtThongSoXe.setText("Dài x Rộng x Cao");
            }
            if (motorDetail.getMotorInfoId().equals(7)){
                edtThongSoXe.setText("Dung tích bình xăng");
            }*/

            edtThongSoXe.setText(motorInfo.getName());
            edtChiTietXe.setText(motorDetail.getContent());

            /*((ChiTietXeActivity) context).btnSua.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
                    //edtChiTietXe.setEnabled(true);
                    //edtThongSoXe.setEnabled(true);
                }
            });*/


            /*chiTietXeActivity.btnCapNhat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!checkNullInfo(edtThongSoXe)) {
                        thongBao("Bị thiếu thông số xe");
                        return;
                    }

                    if (!checkNullInfo(edtChiTietXe)) {
                        thongBao("Bị thiếu chi tiết xe");
                        return;
                    }


                    DBManager database = new DBManager(chiTietXeActivity);
                    //Xe xe = getXe();
                    ChiTietThongSoXe item = new ChiTietThongSoXe();
                    item.setNoiDungTS(edtChiTietXe.getText().toString());


                    if (edtThongSoXe.getText().toString().equals("Trọng lượng xe : "))
                        item.setMaTS(1);

                    database.updateCTTSX(chiTietThongSoXe);
                    Toast.makeText(chiTietXeActivity, "Cập Nhật Thành Công", Toast.LENGTH_SHORT).show();
                    edtChiTietXe.setEnabled(false);
                    edtThongSoXe.setEnabled(false);
                    Intent intent = new Intent(chiTietXeActivity, ChiTietXeActivity.class);
                    chiTietXeActivity.startActivity(intent);
                }
            });*/

        }

        return convertView;
    }

    /*private boolean checkNullInfo(EditText e) {
        String s = "" + e.getText();
        if (s.length() == 0) {
            return false;
        } else {
            return true;
        }
    }

    private void thongBao(String s) {
        Toast.makeText(((ChiTietXeActivity) context), s, Toast.LENGTH_SHORT).show();
    }*/


}
