package com.abcs.huaqiaobang.tljr.data;

import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.main.MainActivity;
import com.abcs.huaqiaobang.tljr.news.adapter.NewsAdapter;
import com.abcs.huaqiaobang.tljr.news.bean.Tag;
import com.abcs.huaqiaobang.tljr.news.channel.bean.ChannelItem;
import com.abcs.huaqiaobang.util.Complete;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.abcs.huaqiaobang.util.LogUtil;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.huaqiaobang.util.Util;
import com.abcs.sociax.t4.android.ThinksnsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 获取数据类
 *
 * @author Administrator
 */
public class InitData {
    private ThinksnsActivity activity;
    private MainActivity content;

    public InitData(ThinksnsActivity activity) {
        // TODO Auto-generated constructor stub
        this.activity = activity;
        initPerfrence();
    }


    public InitData(MainActivity activity) {
        // TODO Auto-generated constructor stub
        content = activity;
        Log.i("tga", "InitData()");
        initNewsType(new Complete() {

            @Override
            public void complete() {
                // TODO Auto-generated method stub

            }

        });
    }


    private void initPerfrence() {

        initNewsType(new Complete() {

            @Override
            public void complete() {
            }
        });
    }

    public static void getUserLevel(final Complete complete) {
        if (MyApplication.getInstance().self != null) {
            HttpRequest.sendHttpsGet(TLUrl.getInstance().URL_LEVEL + "/" + MyApplication.getInstance().self.getId(), "",
                    new HttpRevMsg() {
                        @Override
                        public void revMsg(String msg) {
                            try {
                                JSONObject object = new JSONObject(msg).getJSONObject("result");
                                MyApplication.getInstance().self.setLevel(object.optInt("level"));
                                MyApplication.getInstance().self.setLevelNeed((float) object.optDouble("need", 0));
                                MyApplication.getInstance().self.setLevelNeedTotal((float) object.optDouble("needtotal", 0));
                                MyApplication.getInstance().self.setLevelTotal((float) object.optDouble("total", 0));
                                MyApplication.getInstance().self.setLevelUnit(object.optString("unit"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                complete.complete();
                            }
                        }
                    });
        } else {
        }
    }

    public static void getUserEvent(final Complete complete) {
        if (MyApplication.getInstance().self != null) {
            HttpRequest.sendHttpsGet(TLUrl.getInstance().URL_LEVEL + "/" + MyApplication.getInstance().self.getId()
                    + "/event", "", new HttpRevMsg() {
                @Override
                public void revMsg(String msg) {
                    try {
                        JSONObject object = new JSONObject(msg).getJSONObject("result");
                        MyApplication.getInstance().self.setIntegral(object.getInt("number"));

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        complete.complete();
                    }
                }
            });
        }
    }



    /**
     * 远程获取新闻类型
     */
    private void initNewsType(final Complete complete) {

        String newtype2 = Util.getFromAssets("news_type1.properties");
        Log.i("tga", newtype2 + "====");
//        if (newtype2 != "") {
        initDate(newtype2, new Complete() {
            @Override
            public void complete() {

            }
        }); // 先拿本地的实例化
//        }





        final String newsChannel = Constent.preference.getString("NEWS_TYPE1",Util.getFromAssets("news_type1.properties"));
        LogUtil.i("initData", newsChannel);



        initDate(newsChannel, new Complete() {
            @Override
            public void complete() {
                if ( Constent.netType.equals("未知")) {
                    complete.complete();
                } else {
                    String pId = Constent.preference.getString("news_p_id", "0");
                    String url = TLUrl.getInstance().URL_new + "api/utc/get";
                    String params = "platform=2&uid=" + pId;
                    LogUtil.i("initData", url + "?" + params);
                    HttpRequest.sendPost(url, params, new HttpRevMsg() {

                        @Override
                        public void revMsg(final String msg) {

                            try {
                                JSONObject obj = new JSONObject(msg);
                                String status = obj.getString("status");
                                if (status != null) {
                                    if (status.equals("1")) {


                                        Constent.preference.edit().putString(msg,newsChannel).commit();
                                        initDate(msg, complete);
                                    } else {
                                        LogUtil.i("initData", "获取用户配置失败 ");
                                        complete.complete();
                                    }
                                } else {
                                    complete.complete();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                complete.complete();
                            }
                            LogUtil.i("initData", msg);
                        }
                    } );
                }
            }
        });
    }

    private static void initDate(String msg, Complete complete) {


        ArrayList<ChannelItem> D_userChannelList = new ArrayList<ChannelItem>();
        try {
            JSONObject obj = new JSONObject(msg);
            JSONObject jsonObject = obj.getJSONObject("joData");

            JSONArray tags = jsonObject.getJSONArray("tags");
            for (int i = 0; i < tags.length(); i++) {
                JSONObject rs = tags.getJSONObject(i);

                Tag tag = new Tag();
                tag.setId(rs.getString("id"));
                tag.setColor(rs.getString("color"));
                tag.setText(rs.getString("text"));
                NewsAdapter.tagsMap.put(rs.getInt("id"), tag);
                LogUtil.i("initTest", rs.getString("text"));
            }

            JSONArray newChannel_Selected = jsonObject.getJSONArray("selected");
            for (int w = 0; w < newChannel_Selected.length(); w++) {
                JSONObject rs = newChannel_Selected.getJSONObject(w);
                ChannelItem selected = new ChannelItem();
                selected.setId(w);
                selected.setOrderId(w);
                selected.setName(rs.getString("name"));
                selected.setContentPictureUrl(
                        rs.has("contentPictureUrl") ? rs.getString("contentPictureUrl") : "default");
                selected.setSpecies(rs.getString("species"));
                selected.setSelected(1);
                selected.setChannelType(rs.getInt("channelType"));

                D_userChannelList.add(selected);
            }
            MainActivity.userChannelList = D_userChannelList;

            complete.complete();
        } catch (JSONException e) {
            e.printStackTrace();
            complete.complete();
        }





    }

    public static boolean isrefreshByUser = false;

    public static void refreshByUser(final MainActivity activity) {
        if (activity.viewpager.getCurrentItem() == 2) {
            activity.viewpager.setCurrentItem(0);
        }

        Log.i("tga", "refreshBuUser===");

        System.out.println("refreshByUser-----");
        isrefreshByUser = true;
 		String pId = Constent.preference.getString("news_p_id", "0");
        HttpRequest.sendGet(TLUrl.getInstance().URL_new+"api/utc/get","platform=2&uid=" + pId, new HttpRevMsg() {

            @Override
            public void revMsg(String msg) {
                System.out.println("refreshByUser msg:" + msg);
                String json = msg == null ? Util.getFromAssets("news_type1.properties") : msg;
                initDate(json, new Complete() {
                    @Override
                    public void complete() {
                    }
                });

                Editor editor = Constent.preference.edit();
                editor.putString("NEWS_TYPE1", json);
                editor.commit();
            }
        }, 3000);

    }

}
