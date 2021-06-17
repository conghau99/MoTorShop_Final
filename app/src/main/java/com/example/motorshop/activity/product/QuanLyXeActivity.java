package com.example.motorshop.activity.product;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.motorshop.activity.R;
import com.example.motorshop.activity.product.dialog.LuaChonThemXeDialog;
import com.example.motorshop.activity.product.dialog.LuaChonXe;
import com.example.motorshop.datasrc.Accessory;
import com.example.motorshop.datasrc.Image;
import com.example.motorshop.datasrc.Motor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class QuanLyXeActivity extends AppCompatActivity {
    ListView lvHienThiXe;
    SearchView searchTenXe, searchHang;
    List<Motor> motorList;
    List<Image> images;
    DanhSachXeAdapter danhSachXeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_xe);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setControl();
        motorList = new ArrayList<>();
        images = new ArrayList<>();

        extractMoTors();

        setEvent();
        setClick();
    }

    private void setEvent() {

        /*SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchTenXe.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchTenXe.setSubmitButtonEnabled(true);
        searchTenXe.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchXe(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchXe(newText);
                return false;
            }
        });

        SearchManager searchManager1 = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchHang.setSearchableInfo(searchManager1.getSearchableInfo(getComponentName()));
        searchHang.setSubmitButtonEnabled(true);
        searchHang.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchNCC(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchNCC(newText);
                return false;
            }
        });*/

    }


    private void setControl() {
        lvHienThiXe = (ListView) findViewById(R.id.lvHienThiXe);
        searchTenXe = (SearchView) findViewById(R.id.searchTenXe);
        searchHang = (SearchView) findViewById(R.id.searchHang);
    }

    private void extractMoTors() {
        String url = "http://192.168.1.44:8080/api/motorshop/motors";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println("Response is: " + response.toString());

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Motor motor = new Motor();
                        motor.setId(jsonObject.getInt("id"));
                        motor.setName(jsonObject.getString("name").toString());
                        motor.setAmount(jsonObject.getInt("amount"));
                        motor.setPrice(jsonObject.getInt("price"));
                        motor.setWarrantyPeriod(jsonObject.getInt("warrantyPeriod"));
                        motor.setBrandId(jsonObject.getString("brandId"));
                        Log.d("deserialize", jsonObject.getString("name"));
                        motorList.add(motor);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                danhSachXeAdapter = new DanhSachXeAdapter(getApplicationContext(), R.layout.item_xe, (ArrayList) motorList);
                danhSachXeAdapter.notifyDataSetChanged();
                lvHienThiXe.setAdapter(danhSachXeAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        });

        requestQueue.add(jsonArrayRequest);

    }


    /*private void searchXe(String keyword){
        DBManager dbManager = new DBManager(getApplicationContext());
        ArrayList<Xe> xes = dbManager.searchXe(keyword);
        if (xes != null){
            lvHienThiXe.setAdapter(new DanhSachXeAdapter(getApplicationContext(), R.layout.item_xe, xes));
        }
    }

    private void searchNCC(String keyword){
        DBManager dbManager = new DBManager(getApplicationContext());
        ArrayList<Xe> xes = dbManager.searchNCC(keyword);
        if (xes != null){
            lvHienThiXe.setAdapter(new DanhSachXeAdapter(getApplicationContext(), R.layout.item_xe, xes));
        }
    }*/

    private void setClick() {

        lvHienThiXe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                new LuaChonXe(QuanLyXeActivity.this, danhSachXeAdapter.data.get(position)).show();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_product, menu);

        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.motors:
                Intent intent = new Intent(this, QuanLyXeActivity.class);
                startActivity(intent);
                Toast.makeText(QuanLyXeActivity.this, "Xe selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.accessories:
                Intent intent2 = new Intent(this, QuanLyPhuTungActivity.class);
                startActivity(intent2);
                Toast.makeText(QuanLyXeActivity.this, "Phu Tung selected", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void luaChonThem(View view) {
        new LuaChonThemXeDialog(this).show();
    }

    public void chuyenDenManThemXe() {
        Intent i = new Intent(this, ThemXeActivity.class);
        startActivity(i);
    }

    public void chuyenDenManThemPhuTung() {
        Intent intent = new Intent(this, ThemPhuTungActivity.class);
        startActivity(intent);
    }

}