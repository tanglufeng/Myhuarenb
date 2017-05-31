package com.abcs.haiwaigou.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abcs.haiwaigou.broadcast.MyUpdateUI;
import com.abcs.haiwaigou.model.Goods;
import com.abcs.haiwaigou.utils.LoadPicture;
import com.abcs.huaqiaobang.MyApplication;
import com.abcs.huaqiaobang.dialog.ProgressDlgUtil;
import com.abcs.huaqiaobang.model.BaseActivity;
import com.abcs.huaqiaobang.util.HttpRequest;
import com.abcs.huaqiaobang.util.HttpRevMsg;
import com.thinksns.sociax.thinksnsbase.utils.TLUrl;
import com.abcs.sociax.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.abcs.sociax.api.Api.mContext;

public class CommentActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.tljr_txt_comment_title)
    TextView tljrTxtCommentTitle;
    @InjectView(R.id.tljr_hqss_news_titlebelow)
    TextView tljrHqssNewsTitlebelow;
    @InjectView(R.id.relative_back)
    RelativeLayout relativeBack;
    @InjectView(R.id.tljr_grp_goods_title)
    RelativeLayout tljrGrpGoodsTitle;
    //    @InjectView(R.id.img_goods)
//    ImageView imgGoods;
//    @InjectView(R.id.t_goods_name)
//    TextView tGoodsName;
//    @InjectView(R.id.t_goods_price)
//    TextView tGoodsPrice;
//    @InjectView(R.id.t_num)
//    TextView tNum;
//    @InjectView(R.id.linearLayout1)
//    LinearLayout linearLayout1;
//    @InjectView(R.id.ratingBar)
//    RatingBar ratingBar;
//    @InjectView(R.id.linearLayout2)
//    LinearLayout linearLayout2;
//    @InjectView(R.id.relative_goods)
//    RelativeLayout relativeGoods;
//    @InjectView(R.id.ed_comment)
//    EditText edComment;
    @InjectView(R.id.ratingBar_description)
    RatingBar ratingBarDescription;
    @InjectView(R.id.ratingBar_service)
    RatingBar ratingBarService;
    @InjectView(R.id.ratingBar_deliver)
    RatingBar ratingBarDeliver;
    @InjectView(R.id.btn_commit)
    Button btnCommit;
    @InjectView(R.id.radio_check)
    CheckBox radioCheck;
    @InjectView(R.id.linear_goods)
    LinearLayout linearGoods;
    String order_id;
    //    String goods_id;
//    String goods_name;
//    String goods_img;
//    String goods_num;
//    String goods_price;
    int goods_point = 0;
    int descripte_point = 0;
    int service_point = 0;
    int deliver_point = 0;
    int anony = 0;
    @InjectView(R.id.radio_same)
    CheckBox radioSame;
    @InjectView(R.id.ratingBar_same)
    RatingBar ratingBarSame;
    @InjectView(R.id.ed_same)
    EditText edSame;
    @InjectView(R.id.linear_same)
    LinearLayout linearSame;
    @InjectView(R.id.btn_all_commit)
    Button btnAllCommit;


    private Handler handler = new Handler();
    public static ArrayList<Goods> commentList = new ArrayList<Goods>();

    String comments[];
    String hasComment[];
    String scores[];
    String point[];
    private boolean isSame;//是否统一评论
    String same_point;//统一评分
    String same_point_temp = "";//统一评分判断
    String same_comment;//统一评论
    String same_comment_temp = "";//统一评论判断

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hwg_activity_comment);
        ButterKnife.inject(this);
        order_id = (String) getIntent().getSerializableExtra("order_id");
//        goods_id = (String) getIntent().getSerializableExtra("goods_id");
//        goods_name = (String) getIntent().getSerializableExtra("goods_name");
//        goods_img = (String) getIntent().getSerializableExtra("goods_img");
//        goods_num = (String) getIntent().getSerializableExtra("goods_num");
//        goods_price = (String) getIntent().getSerializableExtra("goods_price");
        Log.i("zjz", "order_id=" + order_id);
//        Log.i("zjz", "goods_id=" + goods_id);
        Log.i("zjz", "commentList=" + commentList.size());
        comments = new String[commentList.size()];
        scores = new String[commentList.size()];
        hasComment = new String[commentList.size()];
        point = new String[commentList.size()];
        setOnListener();
        if (commentList.size() > 1) {
            radioSame.setVisibility(View.VISIBLE);
            radioSame.setChecked(true);
            initGoods(true);
            isSame = true;
        } else {
            radioSame.setVisibility(View.GONE);
            initGoods(false);
            isSame = false;
        }
        initRating();
    }

    private CompoundButton.OnCheckedChangeListener checkAllListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            isSame = isChecked;
            radioSame.setChecked(isChecked);
            initGoods(isChecked);
            if (isChecked) {
                linearSame.setVisibility(View.VISIBLE);

            } else {
                linearSame.setVisibility(View.GONE);
            }
        }

    };

    //输入表情前的光标位置
    private int cursorPos;
    //输入表情前EditText中的文本
    private String inputAfterText;
    //是否重置了EditText的内容
    private boolean resetText;


    private void initGoods(boolean isCheck) {

        linearGoods.removeAllViews();
        for (int i = 0; i < commentList.size(); i++) {
            View view = getLayoutInflater().inflate(R.layout.hwg_item_comment_goods, null);
            ImageView img_goods = (ImageView) view.findViewById(R.id.img_goods);
            LoadPicture loadPicture = new LoadPicture();
            loadPicture.initPicture(img_goods, commentList.get(i).getPicarr());
            TextView t_goods_name = (TextView) view.findViewById(R.id.t_goods_name);
            t_goods_name.setText(commentList.get(i).getTitle());
            TextView t_goods_price = (TextView) view.findViewById(R.id.t_goods_price);
            t_goods_price.setText("¥" + commentList.get(i).getMoney());
            TextView t_num = (TextView) view.findViewById(R.id.t_num);
            t_num.setText(commentList.get(i).getGoods_num() + "");
            RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
            LinearLayout linear2 = (LinearLayout) view.findViewById(R.id.linearLayout2);
            linear2.setVisibility(isCheck ? View.GONE : View.VISIBLE);
            final int finalI = i;
            point[i] = "";
            hasComment[i] = "";
            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    goods_point = (int) rating;
                    point[finalI] = String.valueOf(rating);
                    scores[finalI] = "&goods[" + commentList.get(finalI).getGoods_id() + "][score]=" + (int) rating;
                }
            });
//            final EditText ed_comment = (EditText) view.findViewById(R.id.ed_comment);
            final EditText ed_comment = (EditText) view.findViewById(R.id.ed_comment);
            ed_comment.setVisibility(isCheck ? View.GONE : View.VISIBLE);

            ed_comment.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int before, int count) {
                    if (!resetText) {
                        cursorPos = ed_comment.getSelectionEnd();
                        //这里用s.toString()而不直接用s是因为如果用s，
                        // 那么，inputAfterText和s在内存中指向的是同一个地址，s改变了，
                        // inputAfterText也就改变了，那么表情过滤就失败了
                        inputAfterText = s.toString();
                    }

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!resetText) {
                        if (count >= 2) {//表情符号的字符长度最小为2
                            CharSequence input = s.subSequence(cursorPos, cursorPos + count);
                            if (containsEmoji(input.toString())) {
                                resetText = true;
                                Toast.makeText(mContext, "不支持输入Emoji表情符号", Toast.LENGTH_SHORT).show();
                                //是表情符号就将文本还原为输入表情符号之前的内容
                                ed_comment.setText(inputAfterText);
                                CharSequence text = ed_comment.getText();
                                if (text instanceof Spannable) {
                                    Spannable spanText = (Spannable) text;
                                    Selection.setSelection(spanText, text.length());
                                }
                            }
                        }
                    } else {
                        resetText = false;
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    hasComment[finalI] = s.toString();
                    comments[finalI] = "&goods[" + commentList.get(finalI).getGoods_id() + "][comment]=" + s.toString();
//                    CheckOrderActivity.params[position] = "&pay_message[" + checkOrders.get(position).getStore_id() + "]=" + s.toString();
                }
            });
//
//            //////////////////////////////////
//            ed_comment.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//                    hasComment[finalI] = s.toString();
//                    comments[finalI] = "&goods[" + commentList.get(finalI).getGoods_id() + "][comment]=" + s.toString();
////                    CheckOrderActivity.params[position] = "&pay_message[" + checkOrders.get(position).getStore_id() + "]=" + s.toString();
//                }
//            });
            linearGoods.addView(view);
        }
        ratingBarSame.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                StringBuffer buffer = new StringBuffer();
                same_point_temp = String.valueOf(rating);
                for (int i = 0; i < commentList.size(); i++) {
                    buffer.append("&goods[" + commentList.get(i).getGoods_id() + "][score]=" + (int) rating);
                }
                same_point = buffer.toString();
                Log.i("zjz", "same_point=" + same_point);
            }
        });
        edSame.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                same_comment_temp = s.toString();
                StringBuffer buffer = new StringBuffer();
                for (int i = 0; i < commentList.size(); i++) {
                    buffer.append("&goods[" + commentList.get(i).getGoods_id() + "][comment]=" + s.toString());
                }
                same_comment = buffer.toString();
                Log.i("zjz", "same_comment=" + same_comment);
            }
        });
//        LoadPicture loadPicture = new LoadPicture();
//        loadPicture.initPicture(imgGoods, goods_img);
//        tGoodsName.setText(goods_name);
//        tGoodsPrice.setText("yen" + goods_price);
//        tNum.setText(goods_num);
    }


    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }

    private void initRating() {
//        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//            @Override
//            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
//                goods_point = (int) rating;
//            }
//        });
        ratingBarDeliver.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                deliver_point = (int) rating;
            }
        });
        ratingBarDescription.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                descripte_point = (int) rating;
            }
        });
        ratingBarService.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                service_point = (int) rating;
            }
        });
    }

    private void setOnListener() {
        radioSame.setOnCheckedChangeListener(checkAllListener);
        btnCommit.setOnClickListener(this);
        relativeBack.setOnClickListener(this);
        radioCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                anony = isChecked ? 1 : 0;
            }
        });
        btnAllCommit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        switch (v.getId()) {
            case R.id.relative_back:
                imm.hideSoftInputFromInputMethod(v.getWindowToken(), 0);
                finish();
                break;
            case R.id.btn_commit:
                if (isSame) {
                    Log.i("zjz", "same_point_temp=" + same_point_temp);
                    Log.i("zjz", "same_comment_temp=" + same_comment_temp);
                    if (same_point_temp.equals("")) {
                        showToast("您还没有对商品进行评分哦！");
                        return;
                    } else if (TextUtils.isEmpty(same_comment_temp)) {
                        showToast("您还没有对商品进行评论哦！");
                        return;
                    }
                } else {
                    for (int i = 0; i < commentList.size(); i++) {
                        Log.i("zjz", "hascomment[" + i + "]=" + hasComment[i]);
                        Log.i("zjz", "point[" + i + "]=" + point[i]);
                        if (point[i].equals("")) {
                            showToast("您还没有对商品进行评分哦！");
                            return;
                        }
                        if (hasComment[i].equals("") || hasComment[i].length() == 0) {
                            showToast("您还没有对商品进行评论哦！");
                            return;
                        }

                    }
                }

                if (descripte_point == 0 || service_point == 0 || deliver_point == 0) {
                    showToast("您还没有对店铺的服务进行评分哦！");
                } else {
                    commit(false);
                }
                imm.hideSoftInputFromInputMethod(v.getWindowToken(), 0);
                break;
            case R.id.btn_all_commit:

                commit(true);
                break;
        }
    }

    String[] default_comments = {"华人邦客服服务态度真好，发货很快，商品质量也很好。店家很讲信誉，而且很不错哦，在这家买东东，我很满意哦！",
            "货好，人好，简单就好！", "一个字好，两个字很好，三个字太好啦；哈哈哈！", "不错的卖家，相信华人邦，所以又光顾咯！",
            "东西收到了，很满意，真的是超级好的卖家，解答疑问不厌其烦，细致认真，关键是东西好，而且发货超快，包装仔细，值得信赖!",
            "华人邦不错的，好卖家，果然一流的，非常会为买家着想，赞赞赞!", "好好好，一切尽在不言中!", "诚信第一，服务第一，绝对相信！！！"};

    private void commit(boolean isAll) {
        String goodScores = "";
        String goodComment = "";
        if (isAll) {
            descripte_point = 5;
            service_point = 5;
            deliver_point = 5;
            StringBuffer buffer_point = new StringBuffer();
            for (int i = 0; i < commentList.size(); i++) {
                if (point[i].equals(""))
                    buffer_point.append("&goods[" + commentList.get(i).getGoods_id() + "][score]=5");
                else
                    buffer_point.append(scores[i]);
            }
            goodScores = buffer_point.toString();
            StringBuffer buffer_comment = new StringBuffer();
            for (int i = 0; i < commentList.size(); i++) {
                Random random = new Random();
                int ram = random.nextInt(8);
                Log.i("zjz", default_comments[ram]);
                if (hasComment[i].equals("") || hasComment[i].length() == 0)
                    buffer_comment.append("&goods[" + commentList.get(i).getGoods_id() + "][comment]=" + default_comments[ram]);
                else
                    buffer_comment.append(comments[i]);
            }
            goodComment = buffer_comment.toString();
        } else if (isSame) {
            goodScores = same_point;
            goodComment = same_comment;
        } else {
            StringBuffer bufferComments = new StringBuffer("");
            StringBuffer bufferScores = new StringBuffer("");
            for (int i = 0; i < commentList.size(); i++) {
                bufferScores.append(scores[i]);
                bufferComments.append(comments[i]);
            }
            goodScores = bufferScores.toString();
            goodComment = bufferComments.toString();
        }

//        showToast(goodScores);
        Log.i("zjz", "goodscores=" + goodScores);
//        showToast(goodComment);
        Log.i("zjz", "goodcomment=" + goodComment);
        ProgressDlgUtil.showProgressDlg("Loading...", this);
        HttpRequest.sendPost(TLUrl.getInstance().URL_hwg_comment + "&order_id=" + order_id, goodScores + goodComment +
                "&anony=" + anony + "&store_desccredit=" + descripte_point + "&store_servicecredit=" + service_point + "&store_deliverycredit=" + deliver_point + "&key=" + MyApplication.getInstance().getMykey(), new HttpRevMsg() {
            @Override
            public void revMsg(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(msg);
                            if (object.getInt("code") == 200) {
                                Log.i("zjz", "msg=" + msg);
                                showToast(object.optString("datas"));
                                if (object.optString("datas").contains("成功")) {
                                    MyUpdateUI.sendUpdateCollection(CommentActivity.this, MyUpdateUI.ORDER);
                                    MyUpdateUI.sendUpdateCollection(CommentActivity.this, MyUpdateUI.MYORDERNUM);
                                    finish();
                                }
                                ProgressDlgUtil.stopProgressDlg();
                            } else {
                                ProgressDlgUtil.stopProgressDlg();
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.i("zjz", e.toString());
                            Log.i("zjz", msg);
                            e.printStackTrace();
                            ProgressDlgUtil.stopProgressDlg();
                        }
                    }
                });

            }
        });
    }

    @Override
    protected void onDestroy() {
        ButterKnife.reset(this);
        super.onDestroy();
    }
}
