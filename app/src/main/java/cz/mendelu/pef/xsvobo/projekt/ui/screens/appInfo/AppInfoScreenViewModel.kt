package cz.mendelu.pef.xsvobo.projekt.ui.screens.appInfo


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.pef.xsvobo.projekt.database.card.ILocalCardsRepository
import cz.mendelu.pef.xsvobo.projekt.model.Card
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.text.format.DateFormat
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.LocalContext
import java.io.File
import java.util.Date

@HiltViewModel
class AppInfoScreenViewModel @Inject constructor(
) : ViewModel(), AppInfoScreenActions {


    override fun getAppInfo(context: Context): PackageInfo? {
        return try {
            val packageManager = context.packageManager
            val packageName = context.packageName
            packageManager.getPackageInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }
    }
    @RequiresApi(Build.VERSION_CODES.P)
    fun getAppSize(packageInfo: PackageInfo): Long {
        val apkPath = packageInfo.applicationInfo.sourceDir
        val file = File(apkPath)
        return file.length()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun getFormattedInstallTime(context: Context, packageInfo: PackageInfo): String {
        val installTimeMillis = packageInfo.firstInstallTime
        val dateFormat = DateFormat.getDateFormat(context)

        val installDate = Date(installTimeMillis)
        return dateFormat.format(installDate)
    }

}
