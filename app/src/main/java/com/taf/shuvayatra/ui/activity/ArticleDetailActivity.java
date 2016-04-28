package com.taf.shuvayatra.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.content.Intent;
import android.view.MenuItem;

import com.taf.model.Post;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.FacebookActivity;
import com.taf.shuvayatra.databinding.ArticleDetailDataBinding;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.PostFavouritePresenter;
import com.taf.shuvayatra.ui.interfaces.PostDetailView;
import com.taf.util.MyConstants;

import javax.inject.Inject;

public class ArticleDetailActivity extends FacebookActivity implements PostDetailView {

    @Inject
    PostFavouritePresenter mFavouritePresenter;

    private boolean mOldFavouriteState;

    @Override
    public int getLayout() {
        return R.layout.activity_article_detail;
    }

    @Override
    public boolean isDataBindingEnabled() {
        return true;
    }

    @Override
    public boolean containsFavouriteOption() {
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mPost = (Post) bundle.getSerializable(MyConstants.Extras.KEY_ARTICLE);
        }
        ((ArticleDetailDataBinding) mBinding).setArticle(mPost);
        mOldFavouriteState = mPost.isFavourite() != null ? mPost.isFavourite() : false;

        initialize();

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void updateFavouriteState() {
        super.updateFavouriteState();
    }

    private void initialize() {
        DaggerDataComponent.builder()
                .activityModule(getActivityModule())
                .applicationComponent(getApplicationComponent())
                .dataModule(new DataModule(mPost.getId()))
                .build()
                .inject(this);
        mFavouritePresenter.attachView(this);
    }

    @Override
    public void onPostFavouriteStateUpdated(Boolean status) {
        mPost.setIsFavourite(status ? !mOldFavouriteState : mOldFavouriteState);
        invalidateOptionsMenu();
    }

    @Override
    public void showErrorView(String pErrorMessage) {
        Snackbar.make(mBinding.getRoot(), pErrorMessage, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean containsShareOption() {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }else if (item.getItemId() == R.id.action_share) {
            showShareDialog(mPost);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
