package com.taf.shuvayatra.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.taf.data.utils.DateUtils;
import com.taf.data.utils.Logger;
import com.taf.interactor.UseCaseData;
import com.taf.model.CountryWidgetData;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.base.BaseFragment;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.CountryWidgetPresenter;
import com.taf.shuvayatra.ui.views.CountryWidgetView;
import com.taf.util.MyConstants;

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by rakeeb on 10/19/16.
 */
public class CountryWidgetFragment extends BaseFragment implements CountryWidgetView {

    public static final String TAG = "CountryWidgetFragment";

    @Inject
    CountryWidgetPresenter widgetPresenter;

    @BindView(R.id.nepali_date)
    TextView tvNepaliDate;
    @BindView(R.id.english_date)
    TextView tvEnglishDate;
    @BindView(R.id.textview_temperature)
    TextView tvTemperature;
    @BindView(R.id.imageview_weather)
    ImageView mImageViewWeather;
    @BindView(R.id.textview_country_name)
    TextView tvCountryName;

    // Different use cases for different components
    UseCaseData caseCalendar = new UseCaseData();
    UseCaseData caseForex = new UseCaseData();
    UseCaseData caseWeather = new UseCaseData();

    @Override
    public int getLayout() {
        return R.layout.list_item_country_widget;
    }

    public static CountryWidgetFragment newInstance(){
        CountryWidgetFragment fragment = new CountryWidgetFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initialize();

        String selectedCOuntry = ((BaseActivity) getActivity()).getPreferences().getLocation();
        String countryName = selectedCOuntry.substring(selectedCOuntry.indexOf(",")+1);
        tvCountryName.setText(countryName);
    }

    public void initialize() {

        DaggerDataComponent.builder()
                .dataModule(new DataModule())
                .activityModule(((BaseActivity) getActivity()).getActivityModule())
                .applicationComponent(((BaseActivity) getActivity()).getApplicationComponent())
                .build()
                .inject(this);

        widgetPresenter.attachView(this);

        caseCalendar.putInteger(UseCaseData.COMPONENT_TYPE, CountryWidgetData.COMPONENT_CALENDAR);
        caseWeather.putInteger(UseCaseData.COMPONENT_TYPE, CountryWidgetData.COMPONENT_WEATHER);
        caseForex.putInteger(UseCaseData.COMPONENT_TYPE, CountryWidgetData.COMPONENT_FOREX);

        // initialize each component for the country widget
        widgetPresenter.initialize(caseCalendar);
        widgetPresenter.initialize(caseWeather);
        widgetPresenter.initialize(caseForex);
    }

    @Override
    public void onComponentLoaded(CountryWidgetData.Component component) {
        switch (component.componentType()) {
            case CountryWidgetData.COMPONENT_CALENDAR:
                tvNepaliDate.setText(((CountryWidgetData.CalendarComponent) component).getNepaliDate());

                Calendar instance = ((CountryWidgetData.CalendarComponent) component).getToday();
                String date = DateUtils.getFormattedDate(DateUtils.DEFAULT_DATE_PATTERN, instance.getTime());
                String day = DateUtils.getEnglishDay(instance.get(Calendar.DAY_OF_WEEK));
                String englishDate = day + ",\n" + date;
                tvEnglishDate.setText(englishDate);
                break;
            case CountryWidgetData.COMPONENT_FOREX:
                // TODO: 10/24/16 adjust api for forex with preference
                String location = ((BaseActivity) getActivity()).getPreferences().getLocation();
                Logger.e(TAG, ">>> ");
//                tvForex.setText(component.toString());
                break;
            case CountryWidgetData.COMPONENT_WEATHER:
                tvTemperature.setText(((CountryWidgetData.WeatherComponent) component).getTemperature() +" "+ (char) 0x00B0 + "C");
                String pWeather = ((CountryWidgetData.WeatherComponent) component).getWeatherInfo();
                if (pWeather.toLowerCase().contains(MyConstants.WEATHER.TYPE_CLEAR_SKY)) {
                    Calendar cal = Calendar.getInstance();
                    if (cal.get(Calendar.HOUR_OF_DAY) < 19)
                        //// TODO: 6/29/2016 clear day
                        mImageViewWeather.setImageResource(R.drawable.ic_clear_sky_day);
                    else
                        mImageViewWeather.setImageResource(R.drawable.ic_clear_sky_night);
                } else if (pWeather.toLowerCase().contains(MyConstants.WEATHER.TYPE_BROKEN_CLOUDS) ||
                        pWeather.contains(MyConstants.WEATHER.TYPE_SCATTERED_CLOUDS)) {
                    mImageViewWeather.setImageResource(R.drawable.ic_scattered_clouds);
                } else if (pWeather.toLowerCase().contains(MyConstants.WEATHER.TYPE_FEW_CLOUDS)) {
                    Calendar cal = Calendar.getInstance();
                    if (cal.get(Calendar.HOUR_OF_DAY) < 19)
                        mImageViewWeather.setImageResource(R.drawable.ic_few_clouds_day);
                    else
                        mImageViewWeather.setImageResource(R.drawable.ic_few_clouds_night);
                } else if (pWeather.toLowerCase().contains(MyConstants.WEATHER.TYPE_SHOWER_RAIN)) {
                    mImageViewWeather.setImageResource(R.drawable.ic_shower_rain);
                } else if (pWeather.toLowerCase().contains(MyConstants.WEATHER.TYPE_THUNDERSTORM)) {
                    mImageViewWeather.setImageResource(R.drawable.ic_thunderstorm);
                } else if (pWeather.toLowerCase().contains(MyConstants.WEATHER.TYPE_RAIN)) {
                    mImageViewWeather.setImageResource(R.drawable.ic_rain);
                } else {
                    //// TODO: 6/23/2016 unknown weather type
                }
                break;
        }
    }


    @Override
    public void onLoadingView(int type) {
        switch (type) {
            case CountryWidgetData.COMPONENT_CALENDAR:
                break;
            case CountryWidgetData.COMPONENT_FOREX:
                break;
            case CountryWidgetData.COMPONENT_WEATHER:
                break;
        }
//        Toast.makeText(getContext(), String.format("Loading on %d", type),
//                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onHideLoadingView(int type) {
        switch (type) {
            case CountryWidgetData.COMPONENT_CALENDAR:
                break;
            case CountryWidgetData.COMPONENT_FOREX:
                break;
            case CountryWidgetData.COMPONENT_WEATHER:
                break;
        }
    }

    @Override
    public void onErrorView(int type, String error) {
        // call to appropriate callbacks
        switch (type) {
            case CountryWidgetData.COMPONENT_CALENDAR:
                break;
            case CountryWidgetData.COMPONENT_FOREX:
                break;
            case CountryWidgetData.COMPONENT_WEATHER:
                break;
        }
//        Toast.makeText(getContext(), String.format("Error on %d, %s", type, error),
//                Toast.LENGTH_LONG).show();
    }

    @Override
    public void showLoadingView() {
        // ignore this callback
    }

    @Override
    public void hideLoadingView() {
        // ignore this callback
    }

    @Override
    public void showErrorView(String pErrorMessage) {
        // ignore this callback
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetPresenter.destroy();
    }
}
