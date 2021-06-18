package com.example.motorshop.activity.product;

import android.app.SearchManager;
import android.content.Intent;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.motorshop.activity.R;
import com.example.motorshop.activity.product.dialog.LuaChonPhuTung;
import com.example.motorshop.activity.product.dialog.LuaChonThemPhuTungDialog;
import com.example.motorshop.datasrc.Accessory;
import com.example.motorshop.datasrc.Image;
import com.example.motorshop.datasrc.Motor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QuanLyPhuTungActivity extends AppCompatActivity {

    ListView lvHienThiPhuTung;
    List<Accessory> accessoryList;
    List<Accessory> accessories;
    @Nullable
    List<Image> images;
    SearchView searchTenPT;
    DanhSachPhuTungAdapter danhSachPhuTungAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_phu_tung);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setControl();

        accessoryList = new ArrayList<>();
        accessories = new ArrayList<>();
        images = new ArrayList<>();

        extractAccessories();
        extractImages();

        setEvent();
        setClick();
    }

    private void setEvent() {

        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchTenPT.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchTenPT.setSubmitButtonEnabled(true);
        searchTenPT.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchAccessory(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0) {
                    accessoryList.clear();
                    searchAccessory(newText);

                } else {

                    accessoryList.clear();

                    extractAccessories();
                    extractImages();

                }
                return false;
            }
        });

    }

    private void setControl() {
        searchTenPT = (SearchView) findViewById(R.id.searchTenPT);
        lvHienThiPhuTung = (ListView) findViewById(R.id.lvHienThiPhuTung);
    }

    private void extractAccessories() {
        String url = "http://192.168.1.44:8080/api/motorshop/accessories";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println("Response is: " + response.toString());

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Accessory accessory = new Accessory();
                        accessory.setId(jsonObject.getInt("id"));
                        accessory.setName(jsonObject.getString("name").toString());
                        accessory.setAmount(jsonObject.getInt("amount"));
                        accessory.setPrice(jsonObject.getInt("price"));
                        accessory.setWarrantyPeriod(jsonObject.getInt("warrantyPeriod"));
                        accessory.setBrandId(jsonObject.getString("brandId"));
                        Log.d("deserialize", jsonObject.getString("name"));
                        accessoryList.add(accessory);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                danhSachPhuTungAdapter = new DanhSachPhuTungAdapter(getApplicationContext(), R.layout.item_phu_tung, (ArrayList) accessoryList, (ArrayList) images);
                danhSachPhuTungAdapter.notifyDataSetChanged();
                lvHienThiPhuTung.setAdapter(danhSachPhuTungAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        });

        requestQueue.add(jsonArrayRequest);

    }

    private void extractImages() {
        String url = "http://192.168.1.44:8080/api/motorshop/images";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println("Response is: " + response.toString());

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Image image = new Image();
                        image.setMotorId(jsonObject.getInt("motorId"));
                        image.setImage(jsonObject.getString("image").getBytes());
                        Log.d("deserialize", jsonObject.getString("image"));
                        images.add(image);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                danhSachPhuTungAdapter = new DanhSachPhuTungAdapter(getApplicationContext(), R.layout.item_phu_tung, (ArrayList) accessoryList, (ArrayList) images);
                danhSachPhuTungAdapter.notifyDataSetChanged();
                lvHienThiPhuTung.setAdapter(danhSachPhuTungAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        });

        requestQueue.add(jsonArrayRequest);

    }

    private void searchAccessory(String keyword) {
        ArrayList<Accessory> accessories = searchPT(keyword);
        if (accessories != null) {
            lvHienThiPhuTung.setAdapter(new DanhSachPhuTungAdapter(getApplicationContext(), R.layout.item_phu_tung, (ArrayList) accessoryList, (ArrayList) images));
        }
    }

    private ArrayList<Accessory> searchPT(String keyword) {
        String url = "http://192.168.1.44:8080/api/motorshop/accessories/name?name=" + keyword;
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println("Response is: " + response.toString());
                accessories = null;
                accessories = new ArrayList<Accessory>();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Accessory accessory = new Accessory();
                        accessory.setId(jsonObject.getInt("id"));
                        accessory.setName(jsonObject.getString("name"));
                        accessory.setAmount(jsonObject.getInt("amount"));
                        accessory.setPrice(jsonObject.getInt("price"));
                        accessory.setWarrantyPeriod(jsonObject.getInt("warrantyPeriod"));
                        accessory.setBrandId(jsonObject.getString("brandId"));
                        Log.d("deserialize", jsonObject.getString("name"));
                        accessories.add(accessory);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                danhSachPhuTungAdapter = new DanhSachPhuTungAdapter(getApplicationContext(), R.layout.item_phu_tung, (ArrayList) accessories, (ArrayList) images);
                danhSachPhuTungAdapter.notifyDataSetChanged();
                lvHienThiPhuTung.setAdapter(danhSachPhuTungAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        });

        requestQueue.add(jsonArrayRequest);
        return (ArrayList<Accessory>) accessories;
    }

    private void setClick() {

        lvHienThiPhuTung.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                new LuaChonPhuTung(QuanLyPhuTungActivity.this, danhSachPhuTungAdapter.data.get(position)).show();

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
                Toast.makeText(QuanLyPhuTungActivity.this, "Xe selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.accessories:
                Intent intent2 = new Intent(this, QuanLyPhuTungActivity.class);
                startActivity(intent2);
                Toast.makeText(QuanLyPhuTungActivity.this, "Phu Tung selected", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void luaChonThem(View view) {
        new LuaChonThemPhuTungDialog(this).show();
    }

    public void chuyenDenManThemXe() {
        Intent i = new Intent(this, ThemXeActivity.class);
        startActivity(i);
    }

    public void chuyenDenManThemPhuTung() {
        Intent i = new Intent(this, ThemPhuTungActivity.class);
        startActivity(i);
    }

}