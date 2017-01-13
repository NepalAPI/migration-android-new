package com.taf.shuvayatra.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.taf.data.utils.Logger;
import com.taf.interactor.UseCaseData;
import com.taf.model.BaseModel;
import com.taf.model.Podcast;
import com.taf.model.PodcastResponse;
import com.taf.shuvayatra.MyApplication;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.PlayerFragmentActivity;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.PodcastListPresenter;
import com.taf.shuvayatra.ui.adapter.ListAdapter;
import com.taf.shuvayatra.ui.custom.EmptyStateRecyclerView;
import com.taf.shuvayatra.ui.fragment.MiniPlayerFragment;
import com.taf.shuvayatra.ui.interfaces.ListItemClickListener;
import com.taf.shuvayatra.ui.views.PodcastListView;
import com.taf.shuvayatra.util.AnalyticsUtil;
import com.taf.util.MyConstants;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class PodcastsActivity extends PlayerFragmentActivity implements
        PodcastListView,
        ListItemClickListener,
        SwipeRefreshLayout.OnRefreshListener {
    public static final String TAG = "PodcastsActivity";
    public static final String STATE_PODCASTS = "state-podcasts";

    public static final Integer PAGE_LIMIT = 15;
    public static final Integer INITIAL_OFFSET = 1;

    @Inject
    PodcastListPresenter mPresenter;

    @BindView(R.id.recycler_view)
    EmptyStateRecyclerView mRecyclerView;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeContainer;
    @BindView(R.id.empty_view)
    View mEmptyView;

    int mPage = INITIAL_OFFSET;
    int mTotalDataCount = 0;
    int listItemSelection;
    boolean mIsLoading = false;
    boolean mIsLastPage = false;
    UseCaseData mUseCaseData = new UseCaseData();

    ListAdapter<Podcast> mAdapter;

    Long mId;
    String mTitle;
    private boolean playingFromHere;
    private LinearLayoutManager mLayoutManager;

    @Override
    public int getLayout() {
        return R.layout.activity_podcasts;
    }

    @Override
    public boolean isDataBindingEnabled() {
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle data = getIntent().getExtras();
        mId = data.getLong(MyConstants.Extras.KEY_ID, -1);
        mTitle = data.getString(MyConstants.Extras.KEY_TITLE, "");
        setupAdapter();
        initialize();
        playingFromHere = false;

        getSupportActionBar().setTitle(mTitle);
        if (savedInstanceState != null) {
            AnalyticsUtil.logViewEvent(getAnalytics(), mId, mTitle, "podcast-channel");
            List<Podcast> podcasts = (List<Podcast>) savedInstanceState.get(STATE_PODCASTS);
//            mAdapter.setDataCollection(podcasts);
        } else {
            loadPodcasts(INITIAL_OFFSET);
        }
    }

    private void setupAdapter(){
        mAdapter = new ListAdapter(getContext(), this);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mSwipeContainer.setOnRefreshListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setEmptyView(mEmptyView);
        mRecyclerView.setEmptyMessage(getResources().getString(R.string.default_empty_message));
        mSwipeContainer.setOnRefreshListener(this);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

                if (!mIsLoading && !mIsLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        loadPodcasts(mPage + 1);
                    }
                }
            }
        });
    }

    public void loadPodcasts(int page){
//        mPage = page;
        mSwipeContainer.setRefreshing(true);
        mUseCaseData.clearAll();
        mUseCaseData.putInteger(UseCaseData.OFFSET, page);
        mIsLoading = true;
        Logger.e(TAG,"loading Post: "+ page);
        mPresenter.initialize(mUseCaseData);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean alwaysShowPlayer() {
        return true;
    }

    @Override
    public void onListItemSelected(BaseModel pModel, int pIndex) {
        if(playingFromHere) {
            ((MyApplication) getApplicationContext()).mService.changeCurrentPodcast(pIndex);
        } else {
            ((MyApplication) getApplicationContext()).mService.setPodcasts(mAdapter.getDataCollection());
            playingFromHere = true;
        }
    }

    @Override
    public void onRefresh() {
        mPage = INITIAL_OFFSET;
        loadPodcasts(INITIAL_OFFSET);

    }

    @Override
    public void renderPodcasts(PodcastResponse podcasts) {
        if(podcasts.isFromCache()){
            if(mAdapter.getDataCollection().isEmpty()){
                mAdapter.setDataCollection(podcasts.getData().getData());
                mPage = podcasts.getData().getCurrentPage();
                mIsLastPage = podcasts.getData().getTotal() == podcasts.getData().getData().size();
                Logger.e(TAG,"cache: page "+ mPage);
                Logger.e(TAG,"cache: page "+ mAdapter.getDataCollection().size());
            }
            return;
        }

        Logger.e(TAG," ============================ start ==================================");
        Logger.e(TAG,"current page / total page"+ podcasts.getData().getCurrentPage() +" / " + podcasts.getData().getLastPage());
        Logger.e(TAG,"total items: "+ podcasts.getData().getTotal());
        Logger.e(TAG,"prevoius item: "+ mAdapter.getItemCount() );
        Logger.e(TAG,"add items:  "+ podcasts.getData().getData().size());
        mPage = podcasts.getData().getCurrentPage();

        if (mPage == INITIAL_OFFSET) {
            mAdapter.setDataCollection(podcasts.getData().getData());
        } else {
            mAdapter.addDataCollection(podcasts.getData().getData());
        }
//        mTotalDataCount = podcasts.getData().getTotal();
        mIsLastPage = (mPage == podcasts.getData().getLastPage());
        Logger.e(TAG, "new items " + mAdapter.getItemCount());
        Logger.e(TAG," ============================ end ================================== \n");

        if (!podcasts.getData().getData().isEmpty()) {
            if(!((MyApplication) getApplicationContext()).mService.getPlayStatus()) {
                ((MyApplication) getApplicationContext()).mService.setPodcasts(podcasts.getData().getData());
                playingFromHere = true;
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_player, MiniPlayerFragment.newInstance(this), MiniPlayerFragment.TAG)
                    .commit();
        }

    }

    @Override
    public void showLoadingView() {
        mSwipeContainer.setRefreshing(true);
    }

    @Override
    public void hideLoadingView() {
        mSwipeContainer.setRefreshing(false);
        mIsLoading = false;
    }

    @Override
    public void showErrorView(String errorMessage) {
        Snackbar.make(mSwipeContainer, errorMessage, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public Context getContext() {
        return this;
    }

    private void initialize() {
        DaggerDataComponent.builder()
                .activityModule(getActivityModule())
                .applicationComponent(getApplicationComponent())
                .dataModule(new DataModule(mId))
                .build()
                .inject(this);

        mPresenter.attachView(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mAdapter != null) {
            outState.putSerializable(STATE_PODCASTS, (Serializable) mAdapter.getDataCollection());
        }
    }
}
