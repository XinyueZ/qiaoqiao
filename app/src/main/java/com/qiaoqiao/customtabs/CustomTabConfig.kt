package de.immowelt.mobile.livestream.core.utils.customtab

import android.support.customtabs.CustomTabsIntent

/**
 * @author Norman
 */
class CustomTabConfig(
        val builder: CustomTabsIntent.Builder = CustomTabsIntent.Builder().setShowTitle(true)
)