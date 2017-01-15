package com.taf.shuvayatra.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.taf.interactor.UseCaseData;
import com.taf.model.BaseModel;
import com.taf.model.Block;
import com.taf.model.ScreenDataModel;
import com.taf.model.ScreenModel;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseFragment;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.ScreenDataPresenter;
import com.taf.shuvayatra.ui.adapter.BlocksAdapter;
import com.taf.shuvayatra.ui.views.ScreenDataView;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by umesh on 1/13/17.
 */

public class BlockScreenFragment extends BaseFragment implements ScreenDataView, SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = "BlockScreenFragment";
    public static final String STATE_SCREEN = "screen";
    public static final String STATE_BLOCKS = "blocks";

    private ScreenModel mScreen;
    BlocksAdapter mAdapter;

    @Inject
    ScreenDataPresenter presenter;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;


    ScreenDataModel<Block> mScreenModel;

    public static BlockScreenFragment newInstance(ScreenModel screen) {

        BlockScreenFragment fragment = new BlockScreenFragment();
        fragment.mScreen = screen;
        return fragment;
    }
    @Override
    public int getLayout() {
        return R.layout.item_empty_recycler_view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpAdapter();

        if (savedInstanceState != null){
            mScreen = (ScreenModel) savedInstanceState.get(STATE_SCREEN);
            initialize();
            mAdapter.setBlocks((List<BaseModel>) savedInstanceState.get(STATE_BLOCKS));
        } else {
            initialize();
            loadData();
        }
    }

    private void setUpAdapter(){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new BlocksAdapter(getContext(), getChildFragmentManager());
        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    private void initialize(){
        DaggerDataComponent.builder().applicationComponent(getTypedActivity().getApplicationComponent())
                .activityModule(getTypedActivity().getActivityModule())
                .dataModule(new DataModule(mScreen.getId()))
                .build()
                .inject(this);

        presenter.attachView(this);
    }

    private void loadData(){
        UseCaseData useCaseData = new UseCaseData();
        useCaseData.putString(UseCaseData.END_POINT, mScreen.getEndPOint());
        useCaseData.putString(UseCaseData.SCREEN_DATA_TYPE, mScreen.getType());
        presenter.initialize(useCaseData);
    }

    @Override
    public void showErrorView(String pErrorMessage) {
        Snackbar.make(mRecyclerView, pErrorMessage, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showLoadingView() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoadingView() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void renderScreenData(ScreenDataModel model) {

        this.mScreenModel = model;
        mAdapter.setBlocks(model.getData());
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(STATE_BLOCKS, (Serializable) mAdapter.getBlocks());
        outState.putSerializable(STATE_SCREEN, mScreen);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.destroy();
    }
}