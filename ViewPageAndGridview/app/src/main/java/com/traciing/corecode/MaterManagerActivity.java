package com.traciing.corecode;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MaterManagerActivity extends Activity {

    private Spinner mater_choose;
    private TextView mater_titile;
    private EditText mater_devicenum;
    private EditText mater_storenum;
    private LinearLayout mater_layout_storenum;

    private String action_value;

    private List<String> materlist=null;
    private static ArrayAdapter<String> adapter_spin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mater_manager);

        action_value=getIntent().getStringExtra("actionvalue");

        init();
    }

    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            String matername=mater_choose.getSelectedItem().toString();
            System.out.println("当前选择的是:"+matername);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    private void init(){
        mater_choose= (Spinner) findViewById(R.id.mater_choose);
        mater_titile= (TextView) findViewById(R.id.mater_titile);
        mater_storenum= (EditText) findViewById(R.id.mater_storenum);
        mater_devicenum= (EditText) findViewById(R.id.mater_devicenum);
        mater_layout_storenum= (LinearLayout) findViewById(R.id.mater_layout_storenum);

        if(action_value.equals("1")){
            mater_titile.setText("物料入库");
            mater_layout_storenum.setVisibility(View.VISIBLE);
        }else if(action_value.equals("2")){
            mater_titile.setText("物料出库");
            mater_layout_storenum.setVisibility(View.GONE);
        }

        materlist=new ArrayList<String>();
        materlist.add("冷藏箱");
        materlist.add("PDA");
        materlist.add("车辆");

        adapter_spin=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,materlist);
        adapter_spin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mater_choose.setAdapter(adapter_spin);
        mater_choose.setOnItemSelectedListener(new SpinnerSelectedListener());
        mater_choose.setVisibility(View.VISIBLE);
    }

    public void Click_mater(View v) {

        switch (v.getId()) {
            case R.id.mater_sure:
                System.out.println("执行确定操作");
                break;
        }
    }
}
