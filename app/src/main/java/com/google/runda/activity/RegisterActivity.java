package com.google.runda.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.runda.R;
import com.google.runda.bll.Region;
import com.google.runda.event.PullProvincesSucceedEvent;
import com.google.runda.model.Province;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

public class RegisterActivity extends Activity implements View.OnClickListener,AdapterView.OnItemClickListener{

    EditText etUserName;
    EditText etLinkPhoneNumber;
    EditText etPassword;
    EditText etPasswordEnsure;
    Spinner spSex;
    Spinner spProvince;
    Spinner spCity;
    Spinner spCounty;
    EditText etDetailsAddress;

    Button btnRegister;
    Button btnReset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        EventBus.getDefault().register(this);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadProvinces();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void init(){
        etUserName=(EditText)this.findViewById(R.id.etUserName);
        etDetailsAddress= (EditText) this.findViewById(R.id.etDetailsAddress);
        etLinkPhoneNumber=(EditText)this.findViewById(R.id.etLinkPhoneNumber);
        etPassword=(EditText)this.findViewById(R.id.etPassword);
        etPasswordEnsure=(EditText)this.findViewById(R.id.etPasswordEnsure);
        spSex= (Spinner) this.findViewById(R.id.spSex);
        spProvince= (Spinner) this.findViewById(R.id.spProvince);
        spCity= (Spinner) this.findViewById(R.id.spCity);
        spCounty= (Spinner) this.findViewById(R.id.spCounty);

        btnRegister= (Button) findViewById(R.id.btnRegister);
        btnReset= (Button) findViewById(R.id.btnReset);

        btnRegister.setOnClickListener(this);
        btnReset.setOnClickListener(this);

        spProvince.setOnItemClickListener(this);
        spCity.setOnItemClickListener(this);
        spCounty.setOnItemClickListener(this);
    }

    //加载省
    private void loadProvinces(){
        com.google.runda.bll.Region region=new Region();
        region.beginPullProvinces();

    }
    public void onEventMainThread(PullProvincesSucceedEvent event){
        Log.e("wukaikai","RegisterActivity收到省份加载成功消息");
        List<LinkedHashMap<String,String>> data=event.getData();
        ArrayList<String> provinces=new ArrayList<String>();
        for(LinkedHashMap p : data){
            provinces.add((String) p.get("name"));
        }
        String[] strings= (String[]) provinces.toArray(new String[provinces.size()]);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,strings);
        spProvince.setAdapter(adapter);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnRegister:

                break;
            case R.id.btnReset:

                break;
        }
    }


    List<LinkedHashMap<String,String>> provinceList;
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (view.getId()){
            case R.id.spProvince:

                break;
            case R.id.spCity:
                break;
            case R.id.spCounty:
                break;
            case R.id.spSex:
                break;
        }
    }
}
