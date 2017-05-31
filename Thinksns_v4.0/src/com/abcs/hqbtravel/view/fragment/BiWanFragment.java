package com.abcs.hqbtravel.view.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.abcs.hqbtravel.adapter.FuJinWanAdapter;
import com.abcs.hqbtravel.donghua.JazzyHelper;
import com.abcs.hqbtravel.donghua.JazzyRecyclerViewScrollListener;
import com.abcs.hqbtravel.donghua.Utils;
import com.abcs.hqbtravel.entity.FujinSanBi;
import com.abcs.hqbtravel.entity.TouristAttractionsBean;
import com.abcs.hqbtravel.view.activity.BiWanDetialsActivity;
import com.abcs.sociax.android.R;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by zjz on 2016/9/6.
 */
public class BiWanFragment extends BaseFragment {

    private EasyRecyclerView rvBiWan;
    
    private FuJinWanAdapter adapter;
    private View view;
    private Activity activity;
    private String objectName;
    private String cityId;
    private FujinSanBi fujinSanBi;
    JazzyRecyclerViewScrollListener jazzyScrollListener;
    private static final String KEY_TRANSITION_EFFECT = "transition_effect";

    private Map<String, Integer> mEffectMap;
    private int mCurrentTransitionEffect = JazzyHelper.GROW;

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        mEffectMap = Utils.buildEffectMap(activity);
        Utils.populateEffectMenu(menu, new ArrayList<>(mEffectMap.keySet()), activity);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String strEffect = item.getTitle().toString();
        Toast.makeText(activity, strEffect, Toast.LENGTH_SHORT).show();
        setupJazziness(mEffectMap.get(strEffect));
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_TRANSITION_EFFECT, mCurrentTransitionEffect);
    }

    private void setupJazziness(int effect) {
        mCurrentTransitionEffect = effect;
        jazzyScrollListener.setTransitionEffect(mCurrentTransitionEffect);
    }

    public static BiWanFragment newInstance(String cityId, FujinSanBi fujinSanBi) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("cityId", cityId);
        bundle.putSerializable("objectName", fujinSanBi);
        BiWanFragment countryFragment = new BiWanFragment();
        countryFragment.setArguments(bundle);
        return countryFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        if (view == null) {
            view = activity.getLayoutInflater().inflate(R.layout.biwan_fragment, null);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup p = (ViewGroup) view.getParent();
        if (p != null)
            p.removeView(view);

        Bundle bundle = getArguments();
        if (bundle != null) {
            cityId = (String)bundle.getSerializable("cityId");
            fujinSanBi = (FujinSanBi) bundle.getSerializable("objectName");
            Log.i("zjz", "local_object=" + objectName);
        }

        rvBiWan=(EasyRecyclerView)view.findViewById(R.id.rv_biwan);

        rvBiWan.setOnScrollListener(jazzyScrollListener=new JazzyRecyclerViewScrollListener());
        if (savedInstanceState != null) {
            mCurrentTransitionEffect = savedInstanceState.getInt(KEY_TRANSITION_EFFECT, JazzyHelper.GROW);
            setupJazziness(mCurrentTransitionEffect);
        }

        rvBiWan.setLayoutManager(new LinearLayoutManager(activity));
        rvBiWan.setAdapter(adapter = new FuJinWanAdapter(activity));
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                TouristAttractionsBean itemsEntity = adapter.getAllData().get(position);
                Intent it = new Intent(getActivity(), BiWanDetialsActivity.class);
                it.putExtra("bwanId", itemsEntity.id);
                it.putExtra("cityId", cityId);
                it.putExtra("photo", itemsEntity.photo);
                startActivity(it);

            }
        });

        adapter.setError(R.layout.view_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.resumeMore();
            }
        });
        

        initAllDates();
        return view;
    }


    @Override
    protected void lazyLoad() {
    }



    private void initAllDates(){
        
        if(fujinSanBi!=null){
            if(fujinSanBi.body.touristAttractions!=null&&fujinSanBi.body.touristAttractions.size()>0){
                    adapter.addAll(fujinSanBi.body.touristAttractions);
                    adapter.notifyDataSetChanged();
            }
            }
    }

  


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
