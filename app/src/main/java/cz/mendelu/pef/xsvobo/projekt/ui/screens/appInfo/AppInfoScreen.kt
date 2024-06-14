package cz.mendelu.pef.xsvobo.projekt.ui.screens.appInfo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cz.mendelu.pef.xsvobo.projekt.database.SetsDatabase
import cz.mendelu.pef.xsvobo.projekt.model.Card
import cz.mendelu.pef.xsvobo.projekt.navigation.INavigationRouter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.content.Context
import android.content.pm.PackageInfo
import android.os.Build
import android.text.format.DateFormat
import androidx.annotation.RequiresApi
import androidx.compose.ui.res.stringResource
import cz.mendelu.pef.xsvobo.projekt.R
import java.util.Date

@RequiresApi(Build.VERSION_CODES.P)
fun getFormattedInstallTime(context: Context, packageInfo: PackageInfo): String {
    val installTimeMillis = packageInfo.firstInstallTime
    val dateFormat = DateFormat.getDateFormat(context)

    val installDate = Date(installTimeMillis)
    return dateFormat.format(installDate)
}

@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppInfoScreen(navigationRouter: INavigationRouter) {
    val viewModel: AppInfoScreenViewModel = hiltViewModel()
    val version = remember { mutableStateOf(0) }


    val context = LocalContext.current
    val packageInfo: PackageInfo? = viewModel.getAppInfo(context)


    // Launch a coroutine to fetch the database version
    LaunchedEffect(key1 = true) {
        version.value = withContext(Dispatchers.IO) {
            SetsDatabase.getDatabaseVersion(context)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(navigationIcon = {
                IconButton(onClick = {
                    navigationRouter.navigateToMenuScreen(null)
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                }
            }, title = {
                Text(text = "App Info")
            }

            )
        },
    ) {
        ResultsScreenContent(
            paddingValues = it,
            DBversion = version.value,
            packageInfo = packageInfo,
            context = context,
            viewModel = viewModel
        )
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun ResultsScreenContent(
    paddingValues: PaddingValues,
    DBversion: Int,
    packageInfo: PackageInfo?,
    viewModel:AppInfoScreenViewModel,
    context: Context
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray) // nastaví barvu pozadí na šedou
            .padding(paddingValues),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.database_version)+": $DBversion")

        packageInfo?.let {
            val appName = it.applicationInfo.loadLabel(context.packageManager).toString()
            val versionName = it.versionName
            val minSdkVersion = packageInfo.applicationInfo?.minSdkVersion ?: 0
            val targetSdkVersion = packageInfo.applicationInfo?.targetSdkVersion ?: 0

            val installTime = viewModel.getFormattedInstallTime(context, it)
            val appSize = viewModel.getAppSize(it)

            // Zobrazíme informace v Compose UI
            Text(text = stringResource(id = R.string.app_name)+"App Name: $appName")
            Text(text = stringResource(id = R.string.version_name)+": $versionName")
            Text(text = stringResource(id = R.string.minsdk_version)+": $minSdkVersion")
            Text(text = stringResource(id = R.string.targetsdk_version)+": $targetSdkVersion")
            Text(text = stringResource(id = R.string.install_time)+": $installTime")
            Text(text = stringResource(id = R.string.app_size)+": $appSize bytes")
        } ?: run {
            Text(text = stringResource(id = R.string.app_info_not_available))
        }
    }
}
