package com.qiaoqiao.backend;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.api.services.vision.v1.model.ImageSource;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.qiaoqiao.keymanager.Key;
import com.qiaoqiao.utils.LL;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;
import retrofit2.http.Query;

public final class Google {
	private static final int MAX_DIMENSION = 1200;
	private static final String ANDROID_CERT_HEADER = "X-Android-Cert";
	private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";
	private @NonNull final Key mKey;
	private @NonNull final Context mContext;

	Google(@NonNull Context context, @NonNull Key key) {
		mContext = context;
		mKey = key;
	}

	public void getAnnotateImageResponse(@NonNull AbstractImageBuilder builder, @NonNull Consumer<BatchAnnotateImagesResponse> consumer) {
		callAnnotateImageResponse(builder, consumer);
	}

	private static abstract class AbstractImageBuilder {
		abstract @NonNull
		Image build();
	}

	public static final class Base64EncodedImageBuilder extends AbstractImageBuilder {
		private @NonNull final byte[] bytes;

		private Base64EncodedImageBuilder(@NonNull byte[] bytes) {
			this.bytes = bytes;
		}

		public static AbstractImageBuilder newBuilder(@NonNull byte[] bytes) {
			return new Base64EncodedImageBuilder(bytes);
		}

		@NonNull
		@Override
		Image build() {
			// Add the image
			Image base64EncodedImage = new Image();
			byte[] imageBytes = convertBytes(bytes);
			// Base64 encode the JPEG
			base64EncodedImage.encodeContent(imageBytes);
			return base64EncodedImage;
		}
	}

	public static final class UriImageBuilder extends AbstractImageBuilder {
		private @NonNull final Uri uri;

		private UriImageBuilder(@NonNull Uri uri) {
			this.uri = uri;
		}

		public static AbstractImageBuilder newBuilder(@NonNull Uri uri) {
			return new UriImageBuilder(uri);
		}

		@NonNull
		@Override
		Image build() {
			// Add the image
			Image imageFromUri = new Image();
			imageFromUri.setSource(new ImageSource().setImageUri(uri.toString()));
			return imageFromUri;
		}
	}


	private void callAnnotateImageResponse(@NonNull AbstractImageBuilder builder, @NonNull Consumer<BatchAnnotateImagesResponse> consumer) {
		Observable.just(builder)
		          .map(new Function<AbstractImageBuilder, BatchAnnotateImagesResponse>() {
			          @Override
			          public BatchAnnotateImagesResponse apply(final @NonNull AbstractImageBuilder imageBuilder) throws Exception {
				          try {
					          HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
					          JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

					          VisionRequestInitializer requestInitializer = new VisionRequestInitializer(mKey.toString()) {
						          @Override
						          protected void initializeVisionRequest(VisionRequest<?> visionRequest) throws IOException {
							          super.initializeVisionRequest(visionRequest);

							          String packageName = mContext.getPackageName();
							          visionRequest.getRequestHeaders()
							                       .set(ANDROID_PACKAGE_HEADER, packageName);

							          String sig = BackendUtils.getSignature(mContext.getPackageManager(), packageName);
							          visionRequest.getRequestHeaders()
							                       .set(ANDROID_CERT_HEADER, sig);
						          }
					          };

					          Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
					          builder.setVisionRequestInitializer(requestInitializer);

					          Vision vision = builder.build();

					          BatchAnnotateImagesRequest batchAnnotateImagesRequest = new BatchAnnotateImagesRequest();
					          batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
						          AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();
						          annotateImageRequest.setImage(imageBuilder.build());
						          // add the features we want
						          annotateImageRequest.setFeatures(new ArrayList<Feature>() {{
							          add(new Feature().setType("WEB_DETECTION")
							                           .setMaxResults(5));
							          add(new Feature().setType("LANDMARK_DETECTION")
							                           .setMaxResults(5));
						          }});
						          // Add the list of one thing to the request
						          add(annotateImageRequest);
					          }});

					          Vision.Images.Annotate annotateRequest = vision.images()
					                                                         .annotate(batchAnnotateImagesRequest);
					          // Due to a bug: requests to Vision API containing large images fail when GZipped.
					          annotateRequest.setDisableGZipContent(true);
					          LL.i("created Cloud Vision request object, sending request");

					          return annotateRequest.execute();
				          } catch (GoogleJsonResponseException e) {
					          LL.e("failed to make API request because " + e.getContent());
				          } catch (IOException e) {
					          LL.e("failed to make API request because of other IOException " + e.getMessage());
				          }
				          return null;
			          }
		          })
		          .subscribeOn(Schedulers.newThread())
		          .observeOn(AndroidSchedulers.mainThread())
		          .subscribe(consumer);
	}


	private static byte[] convertBytes(@NonNull byte[] bytes) {
		Bitmap bitmap = scaleBitmapDown(BitmapFactory.decodeByteArray(bytes, 0, bytes.length), MAX_DIMENSION);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
		return byteArrayOutputStream.toByteArray();
	}

	private static Bitmap scaleBitmapDown(@NonNull Bitmap bitmap, int maxDimension) {

		int originalWidth = bitmap.getWidth();
		int originalHeight = bitmap.getHeight();
		int resizedWidth = maxDimension;
		int resizedHeight = maxDimension;

		if (originalHeight > originalWidth) {
			resizedHeight = maxDimension;
			resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
		} else if (originalWidth > originalHeight) {
			resizedWidth = maxDimension;
			resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
		} else if (originalHeight == originalWidth) {
			resizedHeight = maxDimension;
			resizedWidth = maxDimension;
		}
		return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
	}


	public interface TranslateService {
		@POST("v2/")
		Observable<com.qiaoqiao.backend.model.translate.Response> translate(@Query("q") String q, @Query("target") String target, @Query("format") String format, @Query("key") String key);
	}

	public TranslateService getTranslateService() {

		Retrofit r = new Retrofit.Builder().baseUrl("https://translation.googleapis.com/language/translate/")
		                                   .addConverterFactory(GsonConverterFactory.create())
		                                   .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
		                                   .build();
		return r.create(TranslateService.class);
	}

}
