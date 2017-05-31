package com.abcs.haiwaigou.local.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.abcs.haiwaigou.local.activity.HireAndFindActivity;
import com.abcs.haiwaigou.local.activity.HireAndFindActivityOthers;
import com.abcs.haiwaigou.local.beans.Menu;
import com.abcs.haiwaigou.local.evenbus.CityChange;
import com.abcs.haiwaigou.utils.ACache;
import com.abcs.haiwaigou.utils.MyString;
import com.abcs.haiwaigou.view.BaseFragment;
import com.abcs.haiwaigou.view.MyGridView;
import com.abcs.hqbtravel.view.activity.BiChiActivity;
import com.abcs.hqbtravel.view.activity.BiMaiActivity;
import com.abcs.hqbtravel.view.activity.BiWanActivity2;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.adapter.CommonAdapter;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.view.HqbViewHolder;
import com.abcs.huaqiaobang.wxapi.WXEntryActivity;
import com.abcs.sociax.android.R;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class LocalMenuFragment extends BaseFragment {


    @InjectView(R.id.grid_brand)
    MyGridView gridBrand;
    @InjectView(R.id.grid_brand_big)
    MyGridView grid_brand_big;
    private Activity activity;
    ACache aCache;
    private View view;
    String menuId;
    Handler handler=new Handler();

    public static LocalMenuFragment newInstance(String cityId)  {
        Bundle bundle = new Bundle();
        bundle.putString("cityId", cityId);
        LocalMenuFragment countryFragment = new LocalMenuFragment();
        countryFragment.setArguments(bundle);
        return countryFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.local_menu_fragment, container,false);
        activity = getActivity();
        aCache = ACache.get(activity);
       /* ViewGroup p = (ViewGroup) view.getParent();
        if (p != null)
            p.removeView(view);*/

        ButterKnife.inject(this, view);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String cityId = bundle.getString("cityId");
            if(aCache.getAsJSONArray("local_munus")!=null&&aCache.getAsJSONArray("local_munus").length()>0){
                try {
                    initDatasWithAcha(aCache.getAsJSONArray("local_munus"),cityId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                initDatas(cityId);
            }
        }

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void getPic(final ArrayList<Menu> menuList,final String cityId){
        if (gridBrand != null) {

            Log.d("zds", "lLoad: "+menuList.size());
            gridBrand.setVisibility(View.VISIBLE);
            gridBrand.setAdapter(new CommonAdapter<Menu>(activity, menuList, R.layout.fragment_shopping_store_item) {
                @Override
                public void convert(HqbViewHolder helper, Menu item, int position) {

                    if (menuList.get(position).getMenuId().equals(MyString.LOCAL_MENU8)) {
                        helper.setReLativeVisiBiLity(R.id.relati_tishi, false);
                        helper.setText(R.id.tishi_numbers_s, 178 + "");
                    } else {
                        helper.setReLativeVisiBiLity(R.id.relati_tishi, false);
                    }

                    helper.setText(R.id.tv_name, item.getMenuName());
                    helper.setImageByUrl(R.id.img_icon, item.getImgUrl(), 0);

                }
            });

            Log.i("zds", "cityId========: "+cityId);
            gridBrand.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (menuList.get(position).getMenuId()) {
                        case MyString.LOCAL_MENU1:
                        case MyString.LOCAL_MENU2:
                        case MyString.LOCAL_MENU3:
                        case MyString.LOCAL_MENU4:
                        case MyString.LOCAL_MENU5:
                        case MyString.LOCAL_MENU7:
                            Intent tt = null;
                            tt = new Intent(activity, HireAndFindActivityOthers.class);
//                            tt = new Intent(activity, HireAndFindActivity2.class);  //  原来的
                            tt.putExtra("title", menuList.get(position).getMenuName());
                            tt.putExtra("cityId", cityId);
                            tt.putExtra("menuId", menuList.get(position).getMenuId());
                            menuId = menuList.get(position).getMenuId();
                            activity.startActivity(tt);
                            break;
                        case MyString.LOCAL_MENU_CHI:
                            Intent irw=new Intent(activity,BiChiActivity.class);
                            irw.putExtra("cityId",cityId);
                            irw.putExtra("current_lat", MyApplication.my_current_lat);
                            irw.putExtra("current_lng", MyApplication.my_current_lng);
                            startActivity(irw);
                            break;
                        case MyString.LOCAL_MENU_WAN:
                            Intent ir=new Intent(activity,BiWanActivity2.class);
                            ir.putExtra("cityId",cityId);
                            ir.putExtra("current_lat", MyApplication.my_current_lat);
                            ir.putExtra("current_lng", MyApplication.my_current_lng);
                            startActivity(ir);
                            break;
                        case MyString.LOCAL_MENU_MAI:
                            Intent irq=new Intent(activity,BiMaiActivity.class);
                            irq.putExtra("cityId",cityId);
                            irq.putExtra("current_lat", MyApplication.my_current_lat);
                            irq.putExtra("current_lng", MyApplication.my_current_lng);
                            startActivity(irq);
                            break;
                        case MyString.LOCAL_MENU8:

                            Intent intent = null;
                            intent = new Intent(activity, HireAndFindActivity.class);  // 通讯录
                            intent.putExtra("title", menuList.get(position).getMenuName());
                            intent.putExtra("cityId", cityId);
                            intent.putExtra("menuId", menuList.get(position).getMenuId());
                            menuId = menuList.get(position).getMenuId();
                            activity.startActivity(intent);

                            break;
                        case MyString.LOCAL_MENU9:
                            if(MyApplication.getInstance().getMykey()==null){
                                Intent ty=new Intent(activity,WXEntryActivity.class);
                                ty.putExtra("isthome",true);
                                startActivity(ty);
                            }else {
//                                checkIsInto();
                            }
                            break;
                        default:
                            Toast.makeText(getContext(), "敬请期待", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
        }
    }
    private void getBigPic(final ArrayList<Menu> menuList,final String cityId){

        if (grid_brand_big != null) {

            Log.d("zds", "lLoad: "+menuList_big.size());
            grid_brand_big.setVisibility(View.VISIBLE);
            grid_brand_big.setAdapter(new CommonAdapter<Menu>(activity, menuList, R.layout.fragment_shopping_store_item_big) {
                @Override
                public void convert(HqbViewHolder helper, Menu item, int position) {

                    if (menuList.get(position).getMenuId().equals(MyString.LOCAL_MENU8)) {
                        helper.setReLativeVisiBiLity(R.id.relati_tishi, false);
                        helper.setText(R.id.tishi_numbers_s, 178 + "");
                    } else {
                        helper.setReLativeVisiBiLity(R.id.relati_tishi, false);
                    }

                    helper.setText(R.id.tv_name, item.getMenuName());
                    helper.setImageByUrl(R.id.img_icon, item.getImgUrl(), 0);

                }
            });

            grid_brand_big.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (menuList.get(position).getMenuId()) {
                        case MyString.LOCAL_MENU1:
                        case MyString.LOCAL_MENU2:
                        case MyString.LOCAL_MENU3:
                        case MyString.LOCAL_MENU4:
                        case MyString.LOCAL_MENU5:
                        case MyString.LOCAL_MENU7:
                            Intent tt = null;
                            tt = new Intent(activity, HireAndFindActivityOthers.class);
//                            tt = new Intent(activity, HireAndFindActivity2.class);
                            tt.putExtra("title", menuList.get(position).getMenuName());
                            tt.putExtra("cityId", cityId);
                            tt.putExtra("menuId", menuList.get(position).getMenuId());
                            menuId = menuList.get(position).getMenuId();
                            activity.startActivity(tt);
                            break;
                        case MyString.LOCAL_MENU_CHI:
                            Intent irw=new Intent(activity,BiChiActivity.class);
                            irw.putExtra("cityId",cityId);
                            irw.putExtra("current_lat", MyApplication.my_current_lat);
                            irw.putExtra("current_lng", MyApplication.my_current_lng);
                            startActivity(irw);
                            break;
                        case MyString.LOCAL_MENU_WAN:
                            Intent ir=new Intent(activity,BiWanActivity2.class);
                            ir.putExtra("cityId",cityId);
                            ir.putExtra("current_lat", MyApplication.my_current_lat);
                            ir.putExtra("current_lng", MyApplication.my_current_lng);
                            startActivity(ir);
                            break;
                        case MyString.LOCAL_MENU_MAI:
                            Intent irq=new Intent(activity,BiMaiActivity.class);
                            irq.putExtra("cityId",cityId);
                            irq.putExtra("current_lat", MyApplication.my_current_lat);
                            irq.putExtra("current_lng", MyApplication.my_current_lng);
                            startActivity(irq);
                            break;
                        case MyString.LOCAL_MENU8:  //通讯录

                            Intent intent = null;
                            intent = new Intent(activity, HireAndFindActivity.class);
                            intent.putExtra("title", menuList.get(position).getMenuName());
                            intent.putExtra("cityId", cityId);
                            intent.putExtra("menuId", menuList.get(position).getMenuId());
                            menuId = menuList.get(position).getMenuId();
                            activity.startActivity(intent);

                            break;
                        case MyString.LOCAL_MENU9:
                            if(MyApplication.getInstance().getMykey()==null){
                                Intent ty=new Intent(activity,WXEntryActivity.class);
                                ty.putExtra("isthome",true);
                                startActivity(ty);
                            }else {
//                                checkIsInto();
                            }
                            break;
                        default:
                            Toast.makeText(getContext(), "敬请期待", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
        }
    }
    private ArrayList<Menu> menuList_big= new ArrayList<Menu>();
    private ArrayList<Menu> menuList= new ArrayList<Menu>();

    public void initMenuList(JSONArray menuArray,final String cityId) throws JSONException {
        menuList_big.clear();
        menuList.clear();
        for (int i = 0; i < menuArray.length(); i++) {
            JSONObject menuObj = menuArray.getJSONObject(i);
            if(i<4){
                Menu menu = new Menu();
                menu.setImgUrl(menuObj.optString("imgUrl"));
                menu.setMenuId(menuObj.optString("menuId"));
                menu.setMenuName(menuObj.optString("menuName"));
                menu.setMenuParentId(menuObj.optInt("menuParentId"));
                menu.setIs_show(menuObj.optInt("is_show"));
                menu.setIs_click(menuObj.optInt("is_click"));
                menuList_big.add(menu);
            }else {
                Menu menu = new Menu();
                menu.setImgUrl(menuObj.optString("imgUrl"));
                menu.setMenuId(menuObj.optString("menuId"));
                menu.setMenuName(menuObj.optString("menuName"));
                menu.setMenuParentId(menuObj.optInt("menuParentId"));
                menu.setIs_show(menuObj.optInt("is_show"));
                menu.setIs_click(menuObj.optInt("is_click"));
                menuList.add(menu);
            }
        }

        getBigPic(menuList_big,cityId);
        getPic(menuList,cityId);

    }


    private void initDatasWithAcha(JSONArray menuArray,String cityId) throws JSONException{
        initMenuList(menuArray,cityId);
    }


    private void initDatas(final String cityId){

        HttpRequest.sendGet(TLUrl.getInstance().URL_local_main_fragment,"cityId=" + cityId+"&version=2", new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("zjz", "local_msg=" + msg);

                        try {
                            JSONObject mainObj = new JSONObject(msg);
                            if (mainObj.optString("status").equals("1")) {
                                JSONObject msgObj = mainObj.optJSONObject("msg");
                                JSONArray menuArray = msgObj.optJSONArray("menu");
                                if (menuArray != null) {
                                    initMenuList(menuArray,cityId);
                                    aCache.remove("local_munus");
                                    aCache.put("local_munus",menuArray);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMenuListChange(final CityChange cityChange){

        Log.d("zds", "onMenuListChange: cityChange.city"+cityChange.city);

        initDatas(cityChange.city);
    }
    @Override
    protected void lazyLoad() {
    }

/*    private void checkIsInto() {
//        http://www.huaqiaobang.com/mobile/index.php?act=native&op=verify_member_native&key=939f6c2c1ad7199187be733cc714955a
        HttpRequest.sendGet(TLUrl.getInstance().URL_hwg_base + "/mobile/index.php", "act=native&op=verify_member_native&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        if (TextUtils.isEmpty(msg)) {
                            return;
                        }
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.optInt("state") == 1) {  // 去首页
                        *//*        "id": "2132",
                                        "store_name": "天赋打呢",
                                        "address_id": "3624",
                                        "district_id": "1",
                                        "district_name": " 仟惠仁（维也纳）",
                                        "member_id": "21",
                                        "member_name": "17875505332",
                                        "add_time": "1491805590",
                                        "text": null,
                                        "is_default": "1"*//*

                                JSONObject bean = object.optJSONObject("datas");
                                Intent intent = new Intent(activity, BenDiPeiSongActivity3.class);
                                intent.putExtra("address_id", bean.optString("address_id"));
                                intent.putExtra("district_id", bean.optString("district_id"));
                                intent.putExtra("store_name", bean.optString("store_name"));
                                intent.putExtra("district_name", bean.optString("district_name"));
                                activity.startActivity(intent);
                            }
                            else if (object.optInt("state") == -1) {  //
                               *//* Intent intent = new Intent(activity, HuoHangEditAddressActivity.class);
                                intent.putExtra("isAdd", true);
                                activity.startActivity(intent);*//*
                                Intent intent = new Intent(activity, BenDiPeiSongActivity3.class);
                                intent.putExtra("address_id", "");
                                intent.putExtra("district_id", "");
                                intent.putExtra("store_name", "");
                                intent.putExtra("district_name", "");
                                activity.startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }*/
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
