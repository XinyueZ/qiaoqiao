// Copyright 2015 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.qiaoqiao.customtabs;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.Toast;

/**
 * A Fallback that opens a browser when Custom Tabs is not available
 */
public final class BrowserFallback implements CustomTabsActivityHelper.CustomTabFallback {
	@Override
	public void openUrl(Activity activity, String title, @NonNull Uri uri) {
		try {
			Intent myIntent = new Intent(Intent.ACTION_VIEW, uri);
			activity.startActivity(myIntent);
		} catch (ActivityNotFoundException ex) {
			try {
				Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.android.chrome"));
				activity.startActivity(myIntent);
			} catch (ActivityNotFoundException exx) {
				Toast.makeText(activity, "Cannot open external-link, cannot find browser.", Toast.LENGTH_SHORT)
				     .show();
			}
		}
	}
}