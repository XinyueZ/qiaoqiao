package com.qiaoqiao.views;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.qiaoqiao.R;
import com.qiaoqiao.databinding.LayoutWeatherBinding;
import com.qiaoqiao.views.model.weather.Weather;
import com.qiaoqiao.views.model.weather.WeatherDetail;

import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


public final class WeatherLayout extends FrameLayout {
	private static final String UNITS = "metric";
	private static final String APPID = "0246a11ec3af0dd4fd5f5146da8666de";
	private static final String ICON_URL = "http://openweathermap.org/img/w/%s.png";
	private static final String BASE_URL = "http://api.openweathermap.org/";
	private LayoutWeatherBinding mLayoutWeatherBinding;
	private Service mService;

	public WeatherLayout(Context context) {
		super(context);
		init(context);
	}

	public WeatherLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public WeatherLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(@NonNull Context cxt) {
		mService = provideService();
		LayoutInflater inflater = LayoutInflater.from(cxt);
		mLayoutWeatherBinding = LayoutWeatherBinding.inflate(inflater, this, true);
		setVisibility(GONE);
	}

	public WeatherLayout setWeather(double latitude, double longitude) {
		mService.getWeather(latitude,
		                    longitude,
		                    Locale.getDefault()
		                          .getLanguage(), UNITS, APPID)
		        .subscribeOn(Schedulers.io())
		        .observeOn(AndroidSchedulers.mainThread())
		        .subscribe(new Consumer<Weather>() {
			        @Override
			        public void accept(@NonNull Weather w) {
				        setVisibility(View.VISIBLE);
				        List<WeatherDetail> details = w.getDetails();
				        if (details != null && details.size() > 0) {
					        WeatherDetail weatherDetail = details.get(0);
					        if (weatherDetail != null) {
						        String weatherDesc = String.format("%s", getTemp(w));
						        if (!TextUtils.isEmpty(weatherDesc)) {
							        mLayoutWeatherBinding.weatherTv.setText(weatherDesc);
						        }
						        String url = !TextUtils.isEmpty(weatherDetail.getIcon()) ?
						                     getWeatherIconUrl(weatherDetail.getIcon()) :
						                     getWeatherIconUrl("50d");
						        Glide.with(getContext())
						             .load(url)
						             .into(mLayoutWeatherBinding.weatherIconIv);
					        }
				        }
			        }
		        });


		return this;
	}

	private String getTemp(@NonNull Weather w) {
		return w.getTemperature() != null ?
		       getContext().getString(R.string.temp_degree,
		                              w.getTemperature()
		                               .getValue()) :
		       getContext().getString(R.string.temp_degree, 0f);
	}

	private String getWeatherIconUrl(String name) {
		return String.format(ICON_URL, name);
	}


	public WeatherLayout setWeather(@NonNull LatLng latLng) {
		return setWeather(latLng.latitude, latLng.longitude);
	}

	public WeatherLayout setWeather(@NonNull Location l) {
		return setWeather(l.getLatitude(), l.getLongitude());
	}

	private Service provideService() {
		Retrofit r = new Retrofit.Builder().baseUrl(BASE_URL)
		                                   .addConverterFactory(GsonConverterFactory.create())
		                                   .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
		                                   .build();
		return r.create(Service.class);
	}
}


interface Service {
	@GET("data/2.5/weather")
	Observable<Weather> getWeather(@Query("lat") double lat, @Query("lon") double lon, @Query("lang") String language, @Query("units") String units, @Query("APPID") String APPID);


}
