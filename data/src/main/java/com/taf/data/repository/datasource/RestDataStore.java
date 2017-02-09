package com.taf.data.repository.datasource;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.JsonElement;
import com.taf.data.api.ApiRequest;
import com.taf.data.cache.CacheImpl;
import com.taf.data.database.DatabaseHelper;
import com.taf.data.entity.BlockEntity;
import com.taf.data.entity.ChannelEntity;
import com.taf.data.entity.CountryEntity;
import com.taf.data.entity.DeletedContentDataEntity;
import com.taf.data.entity.InfoEntity;
import com.taf.data.entity.LatestContentEntity;
import com.taf.data.entity.PodcastResponseEntity;
import com.taf.data.entity.PostEntity;
import com.taf.data.entity.PostResponseEntity;
import com.taf.data.entity.ScreenBlockEntity;
import com.taf.data.entity.ScreenEntity;
import com.taf.data.entity.ScreenFeedEntity;
import com.taf.data.entity.SyncDataEntity;
import com.taf.data.entity.UpdateResponseEntity;
import com.taf.data.entity.UserInfoEntity;
import com.taf.data.entity.UserInfoResponse;
import com.taf.data.exception.NetworkConnectionException;
import com.taf.data.utils.Logger;
import com.taf.model.CountryWidgetData;
import com.taf.model.base.ApiQueryParams;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.taf.util.MyConstants.Adapter.TYPE_ABOUT;
import static com.taf.util.MyConstants.Adapter.TYPE_CONTACT_US;
import static com.taf.util.MyConstants.Extras.KEY_ABOUT;
import static com.taf.util.MyConstants.Extras.KEY_CONTACT_US;


public class RestDataStore implements IDataStore {

    public static final String TAG = "RestDataStore";
    private final Context mContext;
    private final ApiRequest mApiRequest;
    private final DatabaseHelper mDBHelper;
    private final CacheImpl mCache;


    public RestDataStore(Context pContext,
                         ApiRequest pApiRequest,
                         DatabaseHelper pDBHelper,
                         CacheImpl cache) {
        mApiRequest = pApiRequest;
        mDBHelper = pDBHelper;
        mContext = pContext;
        mCache = cache;
    }

    public Observable<LatestContentEntity> getLatestContents(Long pLastUpdatedStamp) {
        if (isThereInternetConnection()) {
            return mApiRequest.getLatestContents(pLastUpdatedStamp)
                    .doOnNext(pLatestContentEntity -> {
                        Logger.d("RestDataStore_getLatestContents", "insert/update");
                        Observable.create(pSubscriber -> {
                            mDBHelper.insertUpdate(pLatestContentEntity);
                            Logger.e("RestDataStore", "completed insert");
                            pSubscriber.onCompleted();
                        }).subscribe();
                    });
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    public Observable<DeletedContentDataEntity> getDeletedContent(long pLastUpdatedStamp) {
        if (isThereInternetConnection()) {
            return mApiRequest.getDeletedContent(pLastUpdatedStamp)
                    .doOnNext(pDeletedContentDataEntity -> mDBHelper.deleteContents
                            (pDeletedContentDataEntity));
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    public Observable<Boolean> syncFavourites(List<SyncDataEntity> pSyncData) {
        if (isThereInternetConnection()) {
            return mApiRequest.updateFavouriteState(pSyncData)
                    .doOnNext(pResponseEntity -> {
                        Observable.create(pSubscriber -> {
                            mDBHelper.updateFavouriteState(pResponseEntity.getSuccessIdList(),
                                    true);
                            mDBHelper.updateFavouriteState(pResponseEntity.getFailedIdList(),
                                    false);
                            pSubscriber.onCompleted();
                        }).subscribeOn(Schedulers.computation()).subscribe();
                    })
                    .map(pResponseEntity -> true);
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    public Observable<List<BlockEntity>> getHomeBlocks(ApiQueryParams params) {
        if (isThereInternetConnection()) {
            return mApiRequest.getHomeBlocks(params)
                    .doOnNext(blockEntities -> mCache.saveHomeBlocks(blockEntities));
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    public Observable<PodcastResponseEntity> getPodcasts(int offset, Long channelId) {
        if (isThereInternetConnection()) {
            return mApiRequest.getPodcasts(offset, channelId)
                    .doOnNext(entity -> {
                        if (entity.getData() != null)
                            mCache.savePodcastsByChannelId(entity, channelId);
                    });
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    public Observable<PostResponseEntity> getPosts(int feedType, int limit, int offset, String filterParams, long id) {

        if (isThereInternetConnection()) {

            if (feedType == 0) {
                return mApiRequest.getPosts(limit, offset, filterParams, id)
                        .doOnNext(responseEntity -> mCache.savePosts(feedType, filterParams,
                                responseEntity.getData(), (offset != 1)));
            }

            return mApiRequest.getNewsList(limit, offset)
                    .doOnNext(responseEntity -> mCache.savePosts(feedType, filterParams,
                            responseEntity.getData(), (offset != 1)));
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    public Observable<PostEntity> getPost(Long id) {
        if (isThereInternetConnection()) {
            return mApiRequest.getPost(id)
                    .doOnNext(entity -> {
                        mCache.savePost(entity);
                    });
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    public Observable<UpdateResponseEntity> updateFavoriteCount(Long id, boolean status) {
        if (isThereInternetConnection()) {
            return mApiRequest.updateFavoriteCount(id, status)
                    .doOnNext(entity -> {
                        if (!entity.getStatus().equals("success")) {
                            mDBHelper.updateUnSyncedPost(id, status, false);
                        }
                    });
        } else {
            mDBHelper.updateUnSyncedPost(id, status, false);
            return Observable.error(new NetworkConnectionException());
        }
    }

    public Observable<UpdateResponseEntity> updateShareCount(Long id) {
        if (isThereInternetConnection()) {
            return mApiRequest.updateShareCount(id)
                    .doOnNext(entity -> {
                        if (!entity.getStatus().equals("success")) {
                            mDBHelper.updateUnSyncedPost(id, null, true);
                        }
                    });
        } else {
            mDBHelper.updateUnSyncedPost(id, null, true);
            return Observable.error(new NetworkConnectionException());
        }
    }

    public Observable<CountryWidgetData.Component> getComponent(int componentType) {
        if (isThereInternetConnection()) {
            return mApiRequest.getComponent(componentType);
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    public Observable<JsonElement> getWeatherInfo(String place, String unit) {
        Logger.e(TAG,"weather component from api");
        if (isThereInternetConnection()) {
            return mApiRequest.getWeather(place, unit)
                    .doOnNext(mCache::saveWeather);
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    public Observable<List<CountryEntity>> getCountryList() {
        if (isThereInternetConnection()) {
            return mApiRequest.getCountryList().doOnNext(
                    countryEntities -> {
                        mCache.saveCountryList(countryEntities);
                    }
            );
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    public Observable<List<ChannelEntity>> getChannelList() {
        if (isThereInternetConnection()) {
            return mApiRequest.getChannelList().doOnNext(
                    channelEntityList -> {
                        mCache.saveChannelList(channelEntityList);
                    }
            );
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    public Observable<List<BlockEntity>> getJourneyContents(ApiQueryParams params) {
        if (isThereInternetConnection()) {
            return mApiRequest.getJourneyContent(params)
                    .doOnNext(blockEntities -> {
                        mCache.saveJourneyBlocks(blockEntities);
                    });
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    public Observable<JsonElement> getForexInfo() {
        Logger.e(TAG,"forex component from api");

        if (isThereInternetConnection())
            return mApiRequest.getForex()
                    .doOnNext(mCache::saveForex);
        else
            return Observable.error(new NetworkConnectionException());
    }

    public Observable<List<BlockEntity>> getDestinationBlocks(long id, ApiQueryParams params) {
        if (isThereInternetConnection()) {
            return mApiRequest.getDestinationBlock(id, params)
                    .doOnNext(blockEntities -> {
                        mCache.saveDestinationBlocks(id, blockEntities);
                    });
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }


    public Observable<Boolean> syncUserActions(List<SyncDataEntity> entities) {
        if (isThereInternetConnection()) {
            return mApiRequest.syncUserAtions(entities)
                    .doOnNext(responseEntity -> {
                        Observable.create(pSubscriber -> {
                            mDBHelper.deleteSyncedPosts(responseEntity.getSuccessIdList());
                            pSubscriber.onCompleted();
                        }).subscribeOn(Schedulers.computation()).subscribe();
                    })
                    .map(responseEntity -> responseEntity.getFailedIdList().isEmpty());
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    public Observable<PostResponseEntity> getSearchPosts(int limit, int offset, String query, String type) {
        if (isThereInternetConnection()) {
            return mApiRequest.getSearchPosts(limit, offset, query, type);
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    public Observable<UserInfoResponse> saveUserInfo(UserInfoEntity entity) {
        if (isThereInternetConnection()) {
            return mApiRequest.saveUserInfo(entity);
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    public Observable<List<ScreenEntity>> getScreenEntity(ApiQueryParams params) {
        if (isThereInternetConnection()) {
            return mApiRequest.getScreens(params)
                    .doOnNext(screenEntities -> mCache.saveScreens(screenEntities));
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    public Observable<ScreenBlockEntity> getScreenBlockData(long id, ApiQueryParams params) {
        if (isThereInternetConnection()) {
            return mApiRequest.getScreenBlockData(id, params)
                    .doOnNext(screenDataEntities -> {
                        Logger.e(TAG, "screenENtities: " + screenDataEntities);
                        mCache.saveScreenBlockData(id, screenDataEntities);
                    });
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    public Observable<ScreenFeedEntity> getScreenFeedData(long id, int page, ApiQueryParams params) {
        if (isThereInternetConnection()) {
            return mApiRequest.getScreenFeedData(id, page, params)
                    .doOnNext(screenDataEntities -> {
                        mCache.saveScreenFeedData(id, screenDataEntities);
                    });
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    private boolean isThereInternetConnection() {
        boolean isConnected;

        ConnectivityManager connectivityManager =
                (ConnectivityManager) this.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = (networkInfo != null && networkInfo.isConnectedOrConnecting());

        return isConnected;
    }

//    public Observable<PostResponseEntity> getNewsList(int page) {
//        if (isThereInternetConnection()) {
//            return mApiRequest.getNewsList(page)
//                    .doOnNext(blockEntities -> {
//                        mCache.saveNewsBlocks(blockEntities);
//                    });
//        } else {
//            return Observable.error(new NetworkConnectionException());
//        }
//    }

    public Observable<InfoEntity> getInfo(String key) {
        if (isThereInternetConnection()) {
            switch (key) {
                case KEY_ABOUT:
                    return mApiRequest.getInfo(TYPE_ABOUT).doOnNext(infoEntity ->
                            mCache.saveInfo(key, infoEntity));

                case KEY_CONTACT_US:
                    return mApiRequest.getInfo(TYPE_CONTACT_US).doOnNext(infoEntity ->
                            mCache.saveInfo(key, infoEntity));
            }
        }
        return Observable.error(new NetworkConnectionException());
    }
}
