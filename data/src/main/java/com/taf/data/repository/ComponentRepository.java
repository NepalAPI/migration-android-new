package com.taf.data.repository;

import com.taf.data.BuildConfig;
import com.taf.data.entity.mapper.DataMapper;
import com.taf.data.repository.datasource.DataStoreFactory;
import com.taf.data.utils.AppPreferences;
import com.taf.data.utils.DateUtils;
import com.taf.data.utils.Logger;
import com.taf.model.CountryWidgetData;
import com.taf.repository.IWidgetComponentRepository;

import java.util.Calendar;
import java.util.List;

import rx.Observable;

/**
 * Created by rakeeb on 10/20/16.
 */

public class ComponentRepository implements IWidgetComponentRepository {

    private final DataStoreFactory mDataStoreFactory;
    private final DataMapper mDataMapper;
    AppPreferences mAppPreferences;
    String unit = "metric";

    private static final String TAG = "ComponentRepository";

    public ComponentRepository(DataStoreFactory factory, DataMapper mapper, AppPreferences appPreferences) {
        mDataStoreFactory = factory;
        mDataMapper = mapper;
        mAppPreferences = appPreferences;
    }

    @Override
    public Observable<CountryWidgetData.Component> getComponent(int type) {
        switch (type) {
            case CountryWidgetData.COMPONENT_CALENDAR:
                // TODO: 10/20/16 add calendar implementation
                Logger.e(TAG, ">>> component calendar");
                CountryWidgetData.CalendarComponent component = new CountryWidgetData.CalendarComponent();
                Calendar today = Calendar.getInstance();
                String nepaliDate = new DateUtils.NepaliDateConverter().englishToNepali(today);
                component.setToday(today);
                component.setNepaliDate(nepaliDate);

                return Observable.just(component);
            case CountryWidgetData.COMPONENT_FOREX:
            case CountryWidgetData.COMPONENT_WEATHER:
                Logger.e(TAG, ">>> component " + type);
                return mDataStoreFactory.createRestDataStore(BuildConfig.OPEN_WEATHER_URL)
                        .getWeatherInfo(mAppPreferences.getLocation(),unit)
                        .map(jsonElement -> mDataMapper.transformWeaherInfo(jsonElement));
        }
        return null;
    }
}
