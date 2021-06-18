package com.example.motorshop.activity.product;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.motorshop.activity.R;
import com.example.motorshop.datasrc.MotorDetail;
import com.example.motorshop.datasrc.MotorInfo;

import org.json.JSONException;
import org.json.JSONObject;

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
            Button btnCapNhat = convertView.findViewById(R.id.btnCapNhat);
            Button btnSua = convertView.findViewById(R.id.btnSua);

            MotorDetail motorDetail = data.get(position);
            MotorInfo motorInfo = motorInfos.get(position);

            edtChiTietXe.setEnabled(false);
            edtThongSoXe.setEnabled(false);

            edtThongSoXe.setText(motorInfo.getName());
            edtChiTietXe.setText(motorDetail.getContent());

            btnSua.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    edtChiTietXe.setEnabled(true);
                    edtThongSoXe.setEnabled(true);
                }
            });

            btnCapNhat.setOnClickListener(new View.OnClickListener() {
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

                    RequestQueue requestQueue = Volley.newRequestQueue(chiTietXeActivity.getApplicationContext());
                    JSONObject object = new JSONObject();
                    try {
                        //input your API parameters
                        object.put("name", edtThongSoXe.getText().toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String url = "http://192.168.1.44:8080/api/motorshop/motorInfos";
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, object,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    System.out.println("Response is: " + response.toString());
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("tag", "onErrorResponse: " + error.getMessage());
                        }
                    });

                    JSONObject object1 = new JSONObject();
                    try {
                        //input your API parameters
                        object1.put("motorId", motorDetail.getMotorId());
                        object1.put("motorInfoId", motorDetail.getMotorInfoId());
                        object1.put("content", edtChiTietXe.getText().toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String url1 = "http://192.168.1.44:8080/api/motorshop/motorDetails";
                    JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.PUT, url1, object1,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    System.out.println("Response is: " + response.toString());
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("tag", "onErrorResponse: " + error.getMessage());
                        }
                    });

                    requestQueue.add(jsonObjectRequest);
                    requestQueue.add(jsonObjectRequest1);

                    Toast.makeText(chiTietXeActivity, "Cập Nhật Thành Công", Toast.LENGTH_SHORT).show();
                    //finish();
                    Intent intent = new Intent(chiTietXeActivity, ChiTietXeActivity.class);
                    chiTietXeActivity.startActivity(intent);

                }
            });

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

    private boolean checkNullInfo(EditText e) {
        String s = "" + e.getText();
        if (s.length() == 0) {
            return false;
        } else {
            return true;
        }
    }

    private void thongBao(String s) {
        Toast.makeText(((ChiTietXeActivity) context), s, Toast.LENGTH_SHORT).show();
    }


}
