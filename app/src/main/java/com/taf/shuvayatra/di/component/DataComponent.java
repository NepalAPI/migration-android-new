package com.taf.shuvayatra.di.component;

import com.taf.data.di.PerActivity;
import com.taf.shuvayatra.base.CategoryDetailActivity;
import com.taf.shuvayatra.di.module.ActivityModule;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.gcm.MyFcmListenerService;
import com.taf.shuvayatra.receivers.DownloadStateReceiver;
import com.taf.shuvayatra.ui.activity.ArticleDetailActivity;
import com.taf.shuvayatra.ui.activity.AudioDetailActivity;
import com.taf.shuvayatra.ui.activity.FacebookParseActivity;
import com.taf.shuvayatra.ui.activity.PlacesDetailActivity;
import com.taf.shuvayatra.ui.activity.SearchListActivity;
import com.taf.shuvayatra.ui.activity.SplashScreenActivity;
import com.taf.shuvayatra.ui.activity.TagListActivity;
import com.taf.shuvayatra.ui.activity.VideoDetailActivity;
import com.taf.shuvayatra.ui.fragment.DestinationFragment;
import com.taf.shuvayatra.ui.fragment.FeedFragment;
import com.taf.shuvayatra.ui.fragment.InfoFragment;
import com.taf.shuvayatra.ui.fragment.JourneyFragment;
import com.taf.shuvayatra.ui.fragment.NotificationsFragment;

import dagger.Component;

@PerActivity
@Component(
        dependencies = ApplicationComponent.class,
        modules = {ActivityModule.class, DataModule.class}
)
public interface DataComponent extends ActivityComponent {
    void inject(SplashScreenActivity pActivity);
    void inject(PlacesDetailActivity pActivity);
    void inject(AudioDetailActivity pActivity);
    void inject(ArticleDetailActivity pActivity);
    void inject(VideoDetailActivity pActivity);
    void inject(TagListActivity pActivity);
    void inject(FeedFragment pFragment);
    void inject(JourneyFragment pFragment);
    void inject(DestinationFragment pFragment);
    void inject(DownloadStateReceiver pReceiver);
    void inject(CategoryDetailActivity pActivity);
    void inject(MyFcmListenerService pService);
    void inject(NotificationsFragment pFragment);
    void inject(InfoFragment pFragment);
    void inject(FacebookParseActivity pActivity);
    void inject(SearchListActivity pActivity);
}
