package com.abbott.wheelcircle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.abbott.libcircle.utils.PickUtils;
import com.abbott.libcircle.wheel.CommonPickPopWinLoop;
import com.abbott.wheelcircle.bean.Type;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_common;
    private Button btn_time;
    private String chooseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_common = (Button) findViewById(R.id.btn_common);
        btn_time = (Button) findViewById(R.id.btn_time);

        btn_common.setOnClickListener(this);
        btn_time.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v == btn_common) {
            PickUtils.showCommonPicker(this, chooseId, getListTypes(), new CommonPickPopWinLoop.OnPickCompletedListener<Type>() {


                @Override
                public void onPickCompleted(String id, String name,Type type) {
                    chooseId = id;
                    btn_common.setText("类型选择：" + type.getText());
                }
            });

        } else if (v == btn_time) {

        }
    }


    public List<Type> getListTypes() {
        List<Type> types = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            Type type = new Type();
            type.setId((i + 1) + "");
            type.setName("类型" + (i + 1));

            types.add(type);
        }

        return types;
    }
}
