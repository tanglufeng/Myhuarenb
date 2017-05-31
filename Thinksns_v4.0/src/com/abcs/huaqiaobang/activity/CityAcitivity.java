package com.abcs.huaqiaobang.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.abcs.sociax.android.R;
import com.abcs.huaqiaobang.adapter.PopAdapter;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.huaqiaobang.tljr.news.adapter.CharacterParser;
import com.abcs.huaqiaobang.tljr.news.view.SideBar;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/1/19.
 */
public class CityAcitivity extends BaseActivity {

    @InjectView(R.id.listview_pop)
    ListView listviewPop;
    @InjectView(R.id.pop_sidebar)
    SideBar popSidebar;
    @InjectView(R.id.gridview)
    GridView gridview;
    private CharacterParser characterParser;
    private PopAdapter adapter;
    private ArrayList<PopBean> SourceDateList;
    private ArrayList<String> list_city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_popup_dialog);
        ButterKnife.inject(this);

        String cityhistory = Util.preference.getString("cityhistory", "");

        String[] citySplit = cityhistory.split(",");


        if (citySplit.length > 0 && !"".equals(cityhistory)) {
            final String[] city = new String[citySplit.length];
            final String[] num = new String[citySplit.length];
            for (int i = 0; i < citySplit.length; i++) {
                String[] split = citySplit[i].split("-");
                city[i] = split[1];
                num[i] = split[0];

            }
            gridview.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, city));

            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent();
                    PopBean popBean = new PopBean();
                    popBean.setName(city[position]);
                    popBean.setId(num[position]);
                    intent.putExtra("city", popBean);
                    setResult(1, intent);
                    finish();
                }
            });
        } else {
            gridview.setVisibility(View.GONE);
        }


        characterParser = CharacterParser.getInstance();

        popSidebar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                if (adapter != null) {
                    int position = adapter.getPositionForSection(s.charAt(0));
                    if (position != -1) {//listview头，
                        listviewPop.setSelection(position);
                    }
                }

            }
        });
        findViewById(R.id.tljr_btn_lfanhui).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        finish();
                    }
                });
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                list_city = new ArrayList<String>();
                AssetManager assets = CityAcitivity.this.getAssets();

                BufferedReader reader = null;
                try {

                    reader = new BufferedReader(new InputStreamReader(assets.open("city.txt"), "GBK"));

                    String city = null;
                    while ((city = reader.readLine()) != null) {
                        list_city.add(city.trim());

                    }


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                SourceDateList = filledData(list_city);
                Collections.sort(SourceDateList, new Comparator<PopBean>() {
                    @Override
                    public int compare(PopBean o1, PopBean o2) {
                        if (o1.getSortLetters().equals("@")
                                || o2.getSortLetters().equals("#")) {
                            return -1;
                        } else if (o1.getSortLetters().equals("#")
                                || o2.getSortLetters().equals("@")) {
                            return 1;
                        } else {
                            return o1.getSortLetters().compareTo(o2.getSortLetters());
                        }
                    }
                });
                adapter = new PopAdapter(CityAcitivity.this, SourceDateList);
                listviewPop.setAdapter(adapter);
                listviewPop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Intent intent = new Intent();
                        intent.putExtra("city", SourceDateList.get(position));
                        setResult(1, intent);
                        SharedPreferences.Editor edit = Util.preference.edit();
                        PopBean popBean = SourceDateList.get(position);
                        String str_city = popBean.getId() + "-" + popBean.getName();
                        String longhistory = Util.preference.getString("cityhistory", "");

                        if (!longhistory.contains(str_city + ",")) {
                            StringBuilder sb = new StringBuilder(longhistory);
                            sb.insert(0, str_city + ",");
                            edit.putString("cityhistory", sb.toString());
                            edit.commit();
                        }
                        finish();
                    }
                });
            }
        };
        task.execute();
    }


    private ArrayList<PopBean> filledData(ArrayList<String> date) {
        ArrayList<PopBean> mSortList = new ArrayList<>();
        ArrayList<String> indexString = new ArrayList<>();

        for (int i = 0; i < date.size(); i++) {
            PopBean sortModel = new PopBean();
            String[] split = date.get(i).split("-");
            sortModel.setId(split[0]);
            sortModel.setName(split[1]);
            String pinyin = characterParser.getSelling(split[1]);
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {

                //对重庆多音字做特殊处理
                if (pinyin.startsWith("zhongqing")) {
                    sortString = "C";
                    sortModel.setSortLetters("C");
                } else {
                    sortModel.setSortLetters(sortString.toUpperCase());
                }

                if (!indexString.contains(sortString)) {
                    indexString.add(sortString);
                }
            }

            mSortList.add(sortModel);
        }
        Collections.sort(indexString);
//        sideBar.setIndexText(indexString);
        return mSortList;
    }


    public static class PopBean implements Parcelable {


        private String id;
        private String name;
        private String sortLetters;

        public String getSortLetters() {
            return sortLetters;
        }

        public void setSortLetters(String sortLetters) {
            this.sortLetters = sortLetters;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.name);
            dest.writeString(this.sortLetters);
        }

        public PopBean() {
        }

        protected PopBean(Parcel in) {
            this.id = in.readString();
            this.name = in.readString();
            this.sortLetters = in.readString();
        }

        public static final Creator<PopBean> CREATOR = new Creator<PopBean>() {
            public PopBean createFromParcel(Parcel source) {
                return new PopBean(source);
            }

            public PopBean[] newArray(int size) {
                return new PopBean[size];
            }
        };
    }

    private OnclickItem listener;

    public interface OnclickItem {
        public void onClickItem(PopBean popBean);
    }

    public void setOnclickItem(OnclickItem listener) {
        this.listener = listener;
    }
}
