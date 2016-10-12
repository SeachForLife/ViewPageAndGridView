package com.traciing.corecode;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.traciing.Manager.UpdateAppManager;
import com.traciing.common.Common;
import com.traciing.common.HttpService;

import java.util.ArrayList;

public class ViewPageActivity extends Activity {

    public static ViewPageActivity instance = null;

    //ViewPage相关
    private ViewPager mTabPager;//声明对象
    private ImageView mTabImg;// 动画图片
    private ImageView mTab_1, mTab_2;
    private int zero = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int one;// 单个水平动画位移

    //本机信息元素
    private TextView message_simi;
    private TextView version;
    private LinearLayout version_layout;

    private UpdateAppManager updateManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);// 启动activity时不自动弹出软键盘
        instance = this;
        initUI();
        intiViewPage();
    }

    /**
     * 定义当前activity元素
     */
    private void initUI() {
        mTabPager = (ViewPager) findViewById(R.id.tabpager);
        mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());
        mTab_1 = (ImageView) findViewById(R.id.img_address);
        mTab_2 = (ImageView) findViewById(R.id.img_settings);
        mTabImg = (ImageView) findViewById(R.id.img_tab_now);//动画图片
        mTab_1.setOnClickListener(new MyOnClickListener(0));
        mTab_2.setOnClickListener(new MyOnClickListener(1));
        //图标默认选择项
        mTab_1.setImageDrawable(getResources().getDrawable(
                R.drawable.de1));
    }

    /**
     * 设置viewpage相关属性及操作
     */
    private void intiViewPage() {
        Display currDisplay = getWindowManager().getDefaultDisplay();// 获取屏幕当前分辨率
        int displayWidth = currDisplay.getWidth();
        one = displayWidth / 4; // 设置水平动画平移大小

        // 将要分页显示的View装入数组中
        LayoutInflater mLi = LayoutInflater.from(this);
        View function_view = mLi.inflate(R.layout.function_module, null);
        View persion_view = mLi.inflate(R.layout.personal_message, null);

        // 每个页面的view数据
        final ArrayList<View> views = new ArrayList<View>();
        views.add(function_view);
        views.add(persion_view);

        // 填充ViewPager的数据适配器
        PagerAdapter mPagerAdapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public void destroyItem(View container, int position, Object object) {
                ((ViewPager) container).removeView(views.get(position));
            }

            @Override
            public Object instantiateItem(View container, int position) {
                ((ViewPager) container).addView(views.get(position));

                System.out.println("postino+:" + position);
                if (position == 1) {
                    message_simi = (TextView) findViewById(R.id.message_simi);
                    version = (TextView) findViewById(R.id.version);
                    version_layout = (LinearLayout) findViewById(R.id.version_layout);

                    message_simi.setText(Common.getIMEI());
                    version.setText(Common.getVersionName());
                    version_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            System.out.println("开始检测APP版本.");
                            new AsyncTask<Void, Void, String>() {
                                @Override
                                protected String doInBackground(Void... voids) {
                                    String str = null;
                                    try {
                                        // 网络不好，重试3次
                                        for (int i = 0; i < 3; i++) {
                                            str = HttpService.CheckVersion("3.0");
                                            if (!str.equals("11")) {
                                                break;
                                            }
                                        }
                                    } catch (Exception E) {
                                        System.out.print(E);
                                    }
                                    return str;
                                }

                                protected void onPostExecute(String str) {
                                    if (str.equals("11")) {
                                        Toast.makeText(getApplicationContext(), "请确认下网络情况！", Toast.LENGTH_LONG)
                                                .show();
                                    } else if (str.equals("0")) {
                                        System.out.println("无需更新");
                                        Toast.makeText(getApplicationContext(), "当前系统已是最新版本！", Toast.LENGTH_LONG)
                                                .show();
                                    } else {
                                        updateManager = new UpdateAppManager(ViewPageActivity.this);
                                        updateManager.checkUpdateInfo(str);
                                    }
                                }
                            }.execute();
                        }
                    });
                }

                return views.get(position);
            }
        };

        mTabPager.setAdapter(mPagerAdapter);
    }

    /**
     * 图标点击监听
     */
    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        public void onClick(View v) {
            mTabPager.setCurrentItem(index);
        }
    }

    ;

    /*
   * 页卡切换监听
   */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int i, float v, int i2) {

        }

        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
                case 0:
                    mTab_1.setImageDrawable(getResources().getDrawable(
                            R.drawable.de1));
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(zero, one, 0, 0);
                        mTab_2.setImageDrawable(getResources().getDrawable(
                                R.drawable.de));
                    }
                    break;
                case 1:
                    System.out.println("-----111------");
                    mTab_2.setImageDrawable(getResources().getDrawable(
                            R.drawable.de1));
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(one, zero, 0, 0);
                        mTab_1.setImageDrawable(getResources().getDrawable(
                                R.drawable.de));
                    }
                    break;
                default:
                    break;
            }
            currIndex = arg0;
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(150);// 动画持续时间
            mTabImg.startAnimation(animation);// 开始动画
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.matter_enter:
                System.out.println("物料入库");
                Intent materin_intent = new Intent(ViewPageActivity.this, MaterManagerActivity.class);
                materin_intent.putExtra("actionvalue", "1");
                startActivity(materin_intent);
                break;
            case R.id.matter_out:
                System.out.println("物料出库");
                Intent materout_intent = new Intent(ViewPageActivity.this, MaterManagerActivity.class);
                materout_intent.putExtra("actionvalue", "2");
                startActivity(materout_intent);
                break;
            case R.id.function_pickup:
                System.out.println("取       货");
                Intent matertem_intent = new Intent(ViewPageActivity.this, MonitoredActivity.class);
                matertem_intent.putExtra("traciingParam", "1");
                startActivity(matertem_intent);
                break;
            case R.id.function_traciing:
                System.out.println("中转运输");
                Intent matertraciing_intent = new Intent(ViewPageActivity.this, MonitoredActivity.class);
                matertraciing_intent.putExtra("traciingParam", "2");
                startActivity(matertraciing_intent);
                break;
            case R.id.function_send:
                System.out.println("送       货");
                Intent matertsend_intent = new Intent(ViewPageActivity.this, MonitoredActivity.class);
                matertsend_intent.putExtra("traciingParam", "3");
                startActivity(matertsend_intent);
                break;
        }
    }

}
