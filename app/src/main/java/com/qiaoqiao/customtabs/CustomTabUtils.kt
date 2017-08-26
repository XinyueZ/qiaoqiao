import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.util.Log

object CustomTabUtils {
    private val helper = CustomTabActivityHelper()

    fun warmUp(cxt: Context, vararg urls: Uri) {
        helper.bindCustomTabsService(cxt)

        urls.forEach {
            helper.mayLaunchUrl(it, null, null)
        }
    }

    fun clean(cxt: Context) {
        try {
            helper.unbindCustomTabsService(cxt)
        } catch (e: IllegalArgumentException) {
            Log.e(CustomTabUtils::class.qualifiedName, "Failed to unbind custom tab service", e)
        }
    }

    fun openWeb(activity: Activity, url: Uri, builder: CustomTabsIntent.Builder, fallback: CustomTabFallback = DefaultFallback) {
        CustomTabActivityHelper.openCustomTab(activity, builder.build(), url, fallback)
    }
}

/**
 * A Fallback that opens a browser when Custom Tabs is not available.
 *
 * NOTE that if there is no activity to handle the given uri nothing will happen.
 */
internal object DefaultFallback : CustomTabFallback {
    override fun openUrl(activity: Activity, uri: Uri) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = uri
            activity.startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            Log.e(DefaultFallback::class.qualifiedName, "Unable to start activity intent for uri: $uri", ex)
        }
    }
}