package cz.mendelu.pef.xsvobo.projekt.ui.screens.appInfo

import android.content.Context
import android.content.pm.PackageInfo

interface AppInfoScreenActions {
    fun getAppInfo(context: Context): PackageInfo?

}