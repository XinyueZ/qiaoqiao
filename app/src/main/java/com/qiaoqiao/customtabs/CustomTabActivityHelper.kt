import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsClient
import android.support.customtabs.CustomTabsIntent
import android.support.customtabs.CustomTabsServiceConnection
import android.support.customtabs.CustomTabsSession

internal class CustomTabActivityHelper : ServiceConnectionCallback {
    private var customTabsSession: CustomTabsSession? = null
    private var client: CustomTabsClient? = null
    private var connection: CustomTabsServiceConnection? = null

    companion object {

        internal fun openCustomTab(activity: Activity, customTabsIntent: CustomTabsIntent, uri: Uri, fallback: CustomTabFallback?) {
            val packageName = CustomTabsHelper.getPackageNameToUse(activity)

            if (packageName == null) {
                fallback?.openUrl(activity, uri)
            } else {
                customTabsIntent.intent.`package` = packageName
                customTabsIntent.launchUrl(activity, uri)
            }
        }
    }

    /**
     * Unbinds the Activity from the Custom Tabs Service.
     *
     * @param cxt that is connected to the service.
     */
    fun unbindCustomTabsService(cxt: Context) {
        val conn = this.connection ?: return

        cxt.unbindService(conn)
        client = null
        customTabsSession = null
        connection = null
    }

    /**
     * Binds the Activity to the Custom Tabs Service.
     *
     * NOTE that service will be bound only once no matter how
     * often this method gets called.
     *
     * @param context to be bound to the service.
     *
     * @return  [CustomTabActivityHelper]
     */
    fun bindCustomTabsService(context: Context): CustomTabActivityHelper {
        if (client != null) {
            return this
        }

        val packageName = CustomTabsHelper.getPackageNameToUse(context) ?: return this

        connection = ServiceConnection(this)
        CustomTabsClient.bindCustomTabsService(context, packageName, connection)
        return this
    }


    /**
     * @see {@link CustomTabsSession.mayLaunchUrl
     * @return true if call to mayLaunchUrl was accepted.
     */
    fun mayLaunchUrl(uri: Uri, extras: Bundle?, otherLikelyBundles: List<Bundle>?): Boolean {
        if (client == null) {
            return false
        }

        val session = session
        return session != null && session.mayLaunchUrl(uri, extras, otherLikelyBundles)

    }

    /**
     * Creates or retrieves an exiting CustomTabsSession.
     *
     * @return a CustomTabsSession.
     */
    private val session: CustomTabsSession?
        get() {
            val client = this.client

            if (client == null) {
                customTabsSession = null
            } else if (customTabsSession == null) {
                customTabsSession = client.newSession(null)
            }
            return customTabsSession
        }

    override fun onServiceConnected(client: CustomTabsClient) {
        this.client = client
        client.warmup(0L)
    }

    override fun onServiceDisconnected() {
        client = null
        customTabsSession = null
    }
}

interface CustomTabFallback {
    fun openUrl(activity: Activity, uri: Uri)
}
