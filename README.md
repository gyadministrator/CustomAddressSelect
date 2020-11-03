引入module
allprojects {
		repositories {
			maven { url 'https://jitpack.io' }
		}
	}
implementation 'com.github.gyadministrator:CustomAddressSelect:1.0'

主要代码
package com.android.custom.address;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.custom.pickview.entity.PickerData;
import com.android.custom.pickview.entity.ProvinceBean;
import com.android.custom.pickview.entity.SecondBean;
import com.android.custom.pickview.entity.ThirdBean;
import com.android.custom.pickview.listener.OnPickerClickListener;
import com.android.custom.pickview.util.JsonArrayUtil;
import com.android.custom.pickview.util.JsonUtil;
import com.android.custom.pickview.view.CustomPickerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private List<String> mProvinceData = new ArrayList<>();
    private Map<String, List<String>> mSecondData = new HashMap<>();
    private Map<String, List<String>> mThirdData = new HashMap<>();
    private Button button;
    private CustomPickerView pickerView;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        PickerData pickerData = readJson();
        initData(pickerData);
        //initPickerView();
    }

    private void initData(PickerData pickerData) {
        pickerView = new CustomPickerView(this, pickerData);
        pickerView.setScreenH(3)
                .setDiscolourHook(true)
                .setRadius(0)
                .setContentLine(true)
                .setContentText(16,Color.RED)
                .setListText(16,Color.RED)
                .setRadius(0)
                .build();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示选择器
                pickerView.show(button);
            }
        });

        //选择器点击事件
        pickerView.setOnPickerClickListener(new OnPickerClickListener() {
            @Override
            public void OnPickerClick(PickerData pickerData) {
                Toast.makeText(MainActivity.this, pickerData.getFirstText() + "," + pickerData.getSecondText() + "," + pickerData.getThirdText(), Toast.LENGTH_SHORT).show();
                pickerView.dismiss();//关闭选择器
            }
        });
    }

    private PickerData readJson() {
        List<String> mCityData = new ArrayList<>();
        Map<String, List<String>> mDistrictMap = new HashMap<>();
        Map<String, List<String>> mVillageMap = new HashMap<>();
        String json = JsonUtil.getJson("address.json", this);
        try {
            JSONObject cityJsonObject = new JSONObject(json);
            Iterator<String> cityIterator = cityJsonObject.keys();
            while (cityIterator.hasNext()) {
                List<String> mDistrictData = new ArrayList<>();
                //获取城市
                String city = cityIterator.next();
                Log.e(TAG, "readJson:    城市：" + city);
                mCityData.add(city);
                //获取城市下的区
                JSONObject districtJsonObject = cityJsonObject.getJSONObject(city);
                Iterator<String> districtIterator = districtJsonObject.keys();
                while (districtIterator.hasNext()) {
                    List<String> mVillageData = new ArrayList<>();
                    //获取城市下面的区
                    String district = districtIterator.next();
                    Log.e(TAG, "readJson:    区：" + district);
                    mDistrictData.add(district);
                    //获取村
                    JSONArray villageJsonArray = districtJsonObject.getJSONArray(district);
                    for (int i = 0; i < villageJsonArray.length(); i++) {
                        //获取村
                        String village = villageJsonArray.getString(i);
                        Log.e(TAG, "readJson:    村：" + village);
                        mVillageData.add(village);
                    }
                    //村和区对应
                    mVillageMap.put(district, mVillageData);
                }
                //区和城市对应
                mDistrictMap.put(city, mDistrictData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        PickerData pickerData = new PickerData();
        pickerData.setFirstData(mCityData);
        pickerData.setSecondData(mDistrictMap);
        pickerData.setThirdData(mVillageMap);
        pickerData.setInitSelectText("请选择");
        Log.e(TAG, "readJson:    数据：" + pickerData.toString());
        return pickerData;
    }

    private void initView() {
        button = findViewById(R.id.mButton);
    }

    private void initPickerView() {
        //一级列表
        ProvinceBean provinceBean = new ProvinceBean();
        mProvinceData.addAll(provinceBean.getRepData().getProvince());

        //二级列表
        SecondBean secondBean = new SecondBean();
        mSecondData.putAll(secondBean.getRepData().getSecond());

        //三级列表
        ThirdBean thirdBean = new ThirdBean();
        mThirdData.putAll(thirdBean.getRepData().getThird());

        Log.i("json", JsonArrayUtil.toJson(mProvinceData));
        Log.i("json", JsonArrayUtil.toJson(mSecondData));
        Log.i("json", JsonArrayUtil.toJson(mThirdData));

        //设置数据有多少层级
        PickerData data = new PickerData();
        data.setFirstData(mProvinceData);//json: ["广东","江西"]
        data.setSecondData(mSecondData);//json: {"江西":["南昌","赣州"],"广东":["广州","深圳","佛山","东莞"]}
        data.setThirdData(mThirdData);//json: {"广州":["天河区","白云区","番禹区","花都区"],"赣州":["章贡区","黄金开发区"],"东莞":["东城","南城"],"深圳":["南山区","宝安区","龙华区"],"佛山":["禅城区","顺德区"],"南昌":["东湖区","青云谱区","青山湖区"]}

        data.setInitSelectText("请选择");

        pickerView = new CustomPickerView(this, data);
        pickerView.setScreenH(3)
                .setDiscolourHook(true)
                .setRadius(0)
                .setContentLine(true)
                .setRadius(0)
                .build();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示选择器
                pickerView.show(button);
            }
        });

        //选择器点击事件
        pickerView.setOnPickerClickListener(new OnPickerClickListener() {
            @Override
            public void OnPickerClick(PickerData pickerData) {
                Toast.makeText(MainActivity.this, pickerData.getFirstText() + "," + pickerData.getSecondText() + "," + pickerData.getThirdText(), Toast.LENGTH_SHORT).show();
                pickerView.dismiss();//关闭选择器
            }
        });
    }
}


TeaPickerView属性大全
方法名	属性
setHeights(int mHeight)	显示具体的高度(dp),设置0是自适应(高度没有默认值，需要主动设置)
setScreenH(int num)	显示的高度占屏幕的百分比
setBackground(int color)	设置整体的背景颜色 默认是#ffffff
setRadius(int mRadius)	设置圆角，默认0
setContentBackground(int color)	内容栏的背景颜色 默认是#ffffff
setContentHeight(int mHeight)	内容栏的高度(dp) 默认是50dp
setContentText(int size,int color)	内容栏字体的大小和颜色, 默认是16sp,#0aa666，用此方法会固定颜色
setContentText(ColorStateList drawable)	自定义内容栏字体颜色变换器 在res目录下创建color文件夹用selector 默认颜色#555 选中颜色#0aa666
setContentLine(boolean bl)	内容栏选中是否有下划线 默认不开启
setContentLineColor(Drawable drawable)	自定义内容栏下划线用layer-list 默认是下边框描边 颜色#0fbc72 高度1dp
setLine(int mHeight,int color)	分割线的高度和颜色 默认是0.5dp #e5e5e5
setitemHeight(int mHeight)	设置list的item的高度(dp) 默认是40dp
setListText(int size,int color)	设置list的字体大小和颜色 默认是15 #555
setScrollBal(boolean bl)	设置list是否显示滚动条,默认false
setAlpha(float mFloat)	设置阴影层的透明度 默认是0.5f
setDiscolour(boolean bl)	设置选中项是否加色，默认true
setDiscolourColor(int color)	设置选中项加色的颜色值，默认#0aa666
setDiscolourHook(boolean bl)	设置选中项是否有√图标，默认false
setCustomHook(Drawable drawable)	自定义√图标
build()	参数设置完毕，一定要build一下
方法名	属性
setInitSelectText(String firstText)	初始文字
setFirstDatas(List mFirstDatas)	设置一级数据
setSecondDatas(Map<String, List> mSecondDatas)	设置二级数据
setThirdDatas(Map<String, List> mThirdDatas)	设置三级数据
setFourthDatas(Map<String, List> mFourthDatas)	设置四级数据
给出参考bean地址
一级ProvinceBean 二级SecondBean 三级ThirdBean

默认内容栏字体颜色变换器
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
	<item android:state_selected="true" android:color="@color/picker_select_text_color"/>
	<item android:state_pressed="true" android:color="@color/picker_select_text_color"/>
	<item android:state_checked="true" android:color="@color/picker_select_text_color"/>
	<item android:state_focused="true" android:color="@color/picker_select_text_color"/>
	<item android:color="@color/picker_text_color"/>
</selector>
默认内容栏下划线
<?xml version="1.0" encoding="UTF-8"?>
<layer-list xmlns:android="http://schemas.android.com/apk/res/android" >
    <!-- 边框颜色值 -->
    <item>
        <shape>
            <solid android:color="@color/station_average" />
        </shape>
    </item>
    <item android:bottom="1dp"> <!--设置只有底部有边框-->
        <shape>
            <solid android:color="#ffffff" />
        </shape>
    </item>
</layer-list>