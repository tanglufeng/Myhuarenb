package com.abcs.sociax.t4.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.abcs.sociax.android.R;
import com.abcs.sociax.api.Api;
import com.abcs.sociax.t4.android.fragment.FragmentSociax;
import com.abcs.sociax.t4.component.HolderSociax;
import com.abcs.sociax.t4.exception.VerifyErrorException;
import com.abcs.sociax.t4.model.ModelSearchUser;
import com.thinksns.sociax.thinksnsbase.bean.ListData;
import com.thinksns.sociax.thinksnsbase.bean.SociaxItem;
import com.thinksns.sociax.thinksnsbase.exception.ApiException;
import com.thinksns.sociax.thinksnsbase.exception.DataInvalidException;
import com.thinksns.sociax.thinksnsbase.exception.ListAreEmptyException;
import com.thinksns.sociax.thinksnsbase.utils.UnitSociax;

/**
 * Created by hedong on 16/5/19.
 */
public class AdapterNearByGridUser extends AdapterSociaxList {
    private static final  int column = 3;

    private double latitude, longitude;
    private int page = 1;
    private boolean isRecommend = false;    //是否推荐

    @Override
    public int getMaxid() {
        return getLast().getUid();
    }

    @Override
    public ModelSearchUser getLast() {
        return (ModelSearchUser)super.getLast();
    }

    public AdapterNearByGridUser(FragmentSociax fragment, ListData<SociaxItem> list) {
        super(fragment, list);
    }

    /**
     * 设置地理坐标
     * @param latitude
     * @param longitude
     */
    public void setLatitudeLngitude(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HolderSociax viewHolder = null;
        if (convertView == null || convertView.getTag(R.id.tag_viewholder) == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            viewHolder = new HolderSociax();
            convertView = inflater.inflate(R.layout.griditem_user, null);
            viewHolder.tv_user_photo = (ImageView) convertView
                    .findViewById(R.id.iv_one);
            viewHolder.tv_user_content = (TextView)convertView.findViewById(R.id.tv_distance1);
            viewHolder.ll_part = (LinearLayout)convertView.findViewById(R.id.ll_distance);
            convertView.setTag(R.id.tag_viewholder, viewHolder);
        } else {
            viewHolder = (HolderSociax) convertView.getTag(R.id.tag_viewholder);
        }

        ModelSearchUser user = (ModelSearchUser)getItem(position);
        Glide.with(parent.getContext()).load(getItem(position).getUserface())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(viewHolder.tv_user_photo);

        if(!TextUtils.isEmpty(user.getDistinct())) {
            viewHolder.ll_part.setVisibility(View.VISIBLE);
            viewHolder.tv_user_content.setText(user.getDistinct() + "m");
        }else {
            viewHolder.ll_part.setVisibility(View.GONE);
        }

        int width = UnitSociax.getWindowWidth(parent.getContext()) / column;
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(width, width);
        convertView.setLayoutParams(params);
        return convertView;
    }

    @Override
    public ListData<SociaxItem> refreshNew(int count) throws VerifyErrorException, ApiException, ListAreEmptyException, DataInvalidException {
        page = 1;
        return new Api.Users().getNearByUser(String.valueOf(latitude),
                    String.valueOf(longitude), page, httpListener);
    }


    @Override
    public ListData<SociaxItem> refreshHeader(SociaxItem obj) throws VerifyErrorException, ApiException, ListAreEmptyException,
            DataInvalidException{
        return refreshNew(1);
    }

    @Override
    public ListData<SociaxItem> refreshFooter(SociaxItem obj) throws VerifyErrorException, ApiException, ListAreEmptyException, DataInvalidException {
        page++;
        return new Api.Users().getNearByUser(String.valueOf(latitude),
                    String.valueOf(longitude), page, httpListener);
    }
}
