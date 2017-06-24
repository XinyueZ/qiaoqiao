package com.qiaoqiao.core.camera.history;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.qiaoqiao.core.camera.crop.CropPresenter;
import com.qiaoqiao.core.camera.history.bus.HistoryItemClickEvent;
import com.qiaoqiao.repository.database.HistoryItem;
import com.qiaoqiao.utils.ImageUtils;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public final class HistoryPresenter2 implements HistoryContract.Presenter2 {
	private final @NonNull HistoryContract.View2 mView;
	private @Nullable RealmResults<HistoryItem> mResult;
	private @Nullable CropPresenter mCropPresenter;
	private WeakReference<Context> mContextRef;

	@Subscribe
	public void onEvent(HistoryItemClickEvent e) {
		if (mCropPresenter == null) {
			return;
		}
		final HistoryItem historyItem = e.getHistoryItem();
		if (historyItem != null) {
			if (historyItem.getByteArray() != null && historyItem.getByteArray().length != 0) {
				mCropPresenter.openCrop(historyItem.getByteArray());
			} else {
				if (mContextRef.get() == null) {
					return;
				}
				Glide.with(mContextRef.get())
				     .load(historyItem.getImageUri())
				     .asBitmap()
				     .diskCacheStrategy(DiskCacheStrategy.ALL)
				     .skipMemoryCache(false)
				     .into(new SimpleTarget<Bitmap>() {
					     @Override
					     public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
						     mCropPresenter.openCrop(ImageUtils.convertImage2Bytes(resource));
					     }
				     });
			}
		}
	}

	@Inject
	HistoryPresenter2(@NonNull Context context, @NonNull HistoryContract.View2 view) {
		mView = view;
		mContextRef = new WeakReference<>(context);
	}

	@Inject
	void onInjected() {
		mView.setPresenter(this);
	}

	private final RealmChangeListener<RealmResults<HistoryItem>> mChangeListener = new RealmChangeListener<RealmResults<HistoryItem>>() {
		@Override
		public void onChange(RealmResults<HistoryItem> historyItemList) {
			mView.updateList(historyItemList);
		}
	};

	@Override
	public void begin(@NonNull FragmentActivity hostActivity) {
		EventBus.getDefault()
		        .register(this);
		loadHistory();
	}

	@Override
	public void end(@NonNull FragmentActivity hostActivity) {
		EventBus.getDefault()
		        .unregister(this);
		if (mResult != null) {
			mResult.removeChangeListener(mChangeListener);
		}
	}

	@Override
	public void loadHistory() {
		mResult = Realm.getDefaultInstance()
		               .where(HistoryItem.class)
		               .findAllAsync();
		mView.showList(mResult);
		mResult.addChangeListener(mChangeListener);
	}

	@Override
	public void setCropPresenter(@Nullable CropPresenter cropPresenter) {
		mCropPresenter = cropPresenter;
	}
}