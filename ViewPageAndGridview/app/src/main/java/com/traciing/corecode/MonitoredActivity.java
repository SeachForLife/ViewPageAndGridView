package com.traciing.corecode;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.traciing.domain.TemInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonitoredActivity extends Activity {

    public static MonitoredActivity instance = null;

    private ViewPager mTabPager;//声明对象
    private ImageView mTabImg;// 动画图片
    private ImageView mTab_1, mTab_2, mTab_3;
    private int zero = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int one;// 单个水平动画位移
    private int two;

    //温度页面控件
    private GridView main_gridView;
    private ListView lc_tempture;
    private ProgramAdapter adapter;
    private ProgramAdapternew newadapter;
    private TextView gridview_titile_name;

    //功能模块控件
    private LinearLayout traciing_up_layout;
    private LinearLayout traciing_alltem_layout;

    private String getResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitored);

        //接收参数值
        getResult = getIntent().getStringExtra("traciingParam");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);// 启动activity时不自动弹出软键盘
        instance = this;
        initUI();
        intiViewPage();
    }

    /**
     * 控件定义
     */
    private void initUI(){
        mTabPager = (ViewPager) findViewById(R.id.monitor_tabpager);
        mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());

        mTab_1 = (ImageView) findViewById(R.id.monitor_temmessage);
        mTab_2 = (ImageView) findViewById(R.id.monitor_function);
        mTab_3 = (ImageView) findViewById(R.id.monitor_exception);

        mTabImg = (ImageView) findViewById(R.id.monitor_tab);//动画图片

        mTab_1.setOnClickListener(new MyOnClickListener(0));
        mTab_2.setOnClickListener(new MyOnClickListener(1));
        mTab_3.setOnClickListener(new MyOnClickListener(2));

        //默认选择项
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
        two = one * 2;

        // 将要分页显示的View装入数组中
        LayoutInflater mLi = LayoutInflater.from(this);
        View view1 = mLi.inflate(R.layout.gridview_tem, null);
        View view2 = mLi.inflate(R.layout.traciing_function, null);
        View view4 = mLi.inflate(R.layout.exception_case, null);

        // 每个页面的view数据
        final ArrayList<View> views = new ArrayList<View>();
        views.add(view1);
        views.add(view2);
        views.add(view4);

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
                if (position == 0) {
                    gridview_titile_name = (TextView) findViewById(R.id.gridview_titile_name);
                    if (getResult.equals("1")) {
                        gridview_titile_name.setText("收货");
                    } else if (getResult.equals("2")) {
                        gridview_titile_name.setText("中转运输");
                    } else if (getResult.equals("3")) {
                        gridview_titile_name.setText("送货");
                    }

                    main_gridView = (GridView) findViewById(R.id.main_gridView);
                    lc_tempture = (ListView) findViewById(R.id.lc_tempture);
                    List<Map<String, Object>> item = getData();
                    adapter = new ProgramAdapter(item);
//        SimpleAdapter simpleAdapter = new SimpleAdapter(this, item, R.layout.gridviewitem, new String[] { "itemImage", "itemName" }, new int[] { R.id.temValue, R.id.boxid });
                    main_gridView.setAdapter(adapter);

                    // 添加点击事件
                    main_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                            int index = arg2 + 1;// id是从0开始的，所以需要+1
                            System.out.println("你按下了选项:" + index);
                        }
                    });
                    //虚拟数据
                    List<TemInfo> list_cartem = new ArrayList<TemInfo>();
                    TemInfo tem = new TemInfo();
                    tem.setBoxid("厢前");
                    tem.setTemValue("33");
                    tem.setLoss_time("09:00");
                    list_cartem.add(tem);
                    TemInfo tem1 = new TemInfo();
                    tem1.setBoxid("厢后");
                    tem1.setTemValue("23");
                    tem1.setLoss_time("06:00");
                    list_cartem.add(tem1);
                    newadapter = new ProgramAdapternew(list_cartem);
                    lc_tempture.setAdapter(newadapter);
                }
                if (position == 1) {
                    traciing_up_layout = (LinearLayout) findViewById(R.id.traciing_up_layout);
                    traciing_alltem_layout = (LinearLayout) findViewById(R.id.traciing_alltem_layout);
                    if (getResult.equals("3")) {
                        traciing_alltem_layout.setVisibility(View.VISIBLE);
                        traciing_up_layout.setVisibility(View.VISIBLE);
                    } else {
                        traciing_up_layout.setVisibility(View.GONE);
                        traciing_alltem_layout.setVisibility(View.GONE);
                    }
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

    /**
   * 页卡切换监听
   */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int i, float v, int i2) {

        }

        public void onPageSelected(int arg0) {
            System.out.println("-----------");
            Animation animation = null;
            switch (arg0) {
                case 0:
                    mTab_1.setImageDrawable(getResources().getDrawable(
                            R.drawable.de1));
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(one, 0, 0, 0);
                        mTab_2.setImageDrawable(getResources().getDrawable(
                                R.drawable.de));
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, 0, 0, 0);
                        mTab_3.setImageDrawable(getResources().getDrawable(
                                R.drawable.de));
                    }
                    break;
                case 1:
                    mTab_2.setImageDrawable(getResources().getDrawable(
                            R.drawable.de1));
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(zero, one, 0, 0);
                        mTab_1.setImageDrawable(getResources().getDrawable(
                                R.drawable.de));
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, one, 0, 0);
                        mTab_3.setImageDrawable(getResources().getDrawable(
                                R.drawable.de));
                    }
                    break;
                case 2:
                    mTab_3.setImageDrawable(getResources().getDrawable(
                            R.drawable.de1));
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(zero, two, 0, 0);
                        mTab_1.setImageDrawable(getResources().getDrawable(
                                R.drawable.de));
                    } else if (currIndex == 1) {
                        animation = new TranslateAnimation(one, two, 0, 0);
                        mTab_2.setImageDrawable(getResources().getDrawable(
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

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();

        String[] listTem = new String[]{"11.2", "22.2", "11.2", "22.2", "11.2", "22.2", "11.2", "22.2"};

        String[] listBoxid = new String[]{"001", "002", "003", "004", "005", "006", "007", "008"};
        for (int i = 0; i < listTem.length; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("itemImage", listTem[i]);
            item.put("itemName", listBoxid[i]);
            items.add(item);
        }
        return items;

    }

    static class ViewHolder {
        TextView tv_id;
        TextView tv_value;
        TextView tv_time;
    }

    class ProgramAdapter extends BaseAdapter {

        private List<Map<String, Object>> itemlist = new ArrayList<Map<String, Object>>();

        public ProgramAdapter(List<Map<String, Object>> item) {
            System.out.println("------------");
            for (int i = 0; i < item.size(); i++) {
                itemlist.add(item.get(i));
            }
        }

        @Override
        public int getCount() {
            return itemlist.size();
        }

        @Override
        public Object getItem(int position) {
            if (position < 0 || position >= itemlist.size()) {
                return null;
            }
            return itemlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            View view = null;
            ViewHolder holder = null;
            try {
                if (convertView == null) {
                    view = View.inflate(MonitoredActivity.this,
                            R.layout.gridviewitem, null);
                    holder = new ViewHolder();
                    holder.tv_value = (TextView) view.findViewById(R.id.temValue);
                    holder.tv_id = (TextView) view.findViewById(R.id.boxid);
                    view.setTag(holder);
                } else {
                    view = convertView;
                    holder = (ViewHolder) view.getTag();
                }
                Map<String, Object> ne_map = (Map<String, Object>) getItem(position);
//                System.out.println("当前获取的值："+ne_map.get("itemImage").toString()+":::"+position);
                double temv = Double.parseDouble(ne_map.get("itemImage").toString());
                holder.tv_value.setText(ne_map.get("itemImage").toString());
                if (temv > 20) {
                    holder.tv_value.setTextColor(Color.RED);
                } else {
                    holder.tv_value.setTextColor(Color.BLACK);
                }
                holder.tv_id.setText(ne_map.get("itemName").toString());
            } catch (Exception e) {
                System.out.println("异常捕获:" + e);
            }
            return view;
        }
    }

    //车厢内传感器显示apter
    class ProgramAdapternew extends BaseAdapter {

        private List<TemInfo> temInfoList = new ArrayList<TemInfo>();

        public ProgramAdapternew(List<TemInfo> temlist) {
            for (TemInfo tem : temlist) {
                temInfoList.add(tem);
            }
        }

        @Override
        public int getCount() {
            return temInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            // fixme
            // 判断下， 防止数组越界
            if (position < 0 || position >= temInfoList.size()) {
                return null;
            }
            return temInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            ViewHolder holder = null;
            try {
                if (convertView == null) {
                    view = View.inflate(MonitoredActivity.this,
                            R.layout.tempture_item1, null);
                    holder = new ViewHolder();
                    holder.tv_id = (TextView) view.findViewById(R.id.tem_id1);
                    holder.tv_value = (TextView) view.findViewById(R.id.tem_value1);
                    holder.tv_time = (TextView) view
                            .findViewById(R.id.tem_carnum1);
                    view.setTag(holder);
                } else {
                    view = convertView;
                    holder = (ViewHolder) view.getTag();
                }
                TemInfo lctem = (TemInfo) getItem(position);
                holder.tv_id.setText(lctem.getBoxid().toString());
                holder.tv_value.setText(lctem.getTemValue().toString());
                holder.tv_time.setText(lctem.getLoss_time().toString());

            } catch (Exception e) {
                System.out.println("异常:" + e);
            }
            return view;
        }

    }

    public void onClick_traciing(View v) {

        switch (v.getId()) {
            case R.id.traciing_loading:
                System.out.println("装车");
                break;
            case R.id.traciing_unloading:
                System.out.println("卸车");
                break;
            case R.id.traciing_alltem:
                System.out.println("运单全程温度");
                break;
            case R.id.traciing_orderup:
                System.out.println("订单提交");
                break;
            case R.id.traciing_printnew:
                System.out.println("新添运单打印");
                break;
        }
    }

    public void onClick_exception(View v) {

        switch (v.getId()) {
            case R.id.exception_car:
                System.out.println("冷藏车异常");
                break;
            case R.id.exception_box:
                System.out.println("冷藏箱异常");
                break;
            case R.id.exception_Customer:
                System.out.println("货主或客户异常");
                break;
            case R.id.exception_other:
                System.out.println("其他");
                break;
        }
    }


}
