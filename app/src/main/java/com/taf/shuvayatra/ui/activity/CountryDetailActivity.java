package com.taf.shuvayatra.ui.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;

import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.CategoryDetailActivity;
import com.taf.shuvayatra.databinding.CountryDetailDataBinding;
import com.taf.shuvayatra.util.AnalyticsUtil;
import com.taf.util.MyConstants;

import butterknife.Bind;

public class CountryDetailActivity extends CategoryDetailActivity {

    @Bind(R.id.app_bar)
    AppBarLayout mAppBar;

    @Override
    public int getLayout() {
        return R.layout.activity_country_detail;
    }

    @Override
    public MyConstants.DataParent getDataParent() {
        return MyConstants.DataParent.COUNTRY;
    }

    @Override
    public void expandAppBar() {
        mAppBar.setExpanded(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((CountryDetailDataBinding) mBinding).setCountry(mCategory);
        if (savedInstanceState == null) {
            AnalyticsUtil.logViewEvent(getAnalytics(), mCategory.getId(), mCategory.getTitle(),
                    "section_country");
        }
    }
}
