package com.example.crypto_tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.crypto_tracker.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ArrayList<crypto> list;
    currency_adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        list=new ArrayList<>();
        adapter=new currency_adapter(list,this);
        binding.rcv.setLayoutManager(new LinearLayoutManager(this));
        binding.rcv.setAdapter(adapter);
        getdata();

        binding.textinputfield.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filetercurren(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }



    private  void filetercurren(String currency){
        ArrayList<crypto> filter_list=new ArrayList<>();
        for (crypto item:list)
        {
            if(item.getName().toLowerCase().contains(currency.toLowerCase())){
                filter_list.add(item);
            }

            else{
                adapter.filterList(filter_list);
            }
        }
    }


    private  void getdata(){
        binding.progressCircular.setVisibility(View.VISIBLE);
        String url="https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
        RequestQueue requestQueue= Volley.newRequestQueue(this);
                JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(JSONObject response)
            {

                try {
                    JSONArray  data=response.getJSONArray("data");
                    for(int i=0;i<data.length();i++){

                        JSONObject dataobj=data.getJSONObject(i);
                        String name=dataobj.getString("name");
                        String symbol=dataobj.getString("symbol");

                       JSONObject temp=dataobj.getJSONObject("quote");
                       JSONObject usd=temp.getJSONObject("USD");
                       double price=usd.getDouble("price");
                       list.add(new crypto(name,symbol,price));

                       adapter.notifyDataSetChanged();
                        binding.progressCircular.setVisibility(View.GONE);
                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Fail to extract json data", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("crypto123", error.getMessage().toString());
                binding.progressCircular.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        })
                {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {

                        HashMap<String,String > headers=new HashMap<>();
                        headers.put("X-CMC_PRO_API_KEY","7303f0e3-21f6-4d1f-8b02-a0cfbcbaf90f");
                        return headers;
                    }
                };
        requestQueue.add(jsonObjectRequest);
    }

}