package cz.pef.va2_2024_petstore.ui.screens.pet_detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.pef.va2_2024_petstore.R
import cz.pef.va2_2024_petstore.navigation.INavigationRouter
import cz.pef.va2_2024_petstore.ui.elements.BaseScreen
import cz.pef.va2_2024_petstore.ui.screens.list_of_pets.ListOfPetsScreenUIState
import cz.pef.va2_2024_petstore.ui.screens.list_of_pets.ListOfPetsViewModel
import cz.pef.va2_2024_petstore.ui.theme.basicMargin

@Composable
fun PetDetailScreen(
    navigation: INavigationRouter,
    id: Long?
){
    val viewModel = hiltViewModel<PetDetailViewModel>()


    BaseScreen(
        topBarText = stringResource(R.string.pet_detail),
        onBackClick = {
            navigation.returnBack()
        }
    ) {
        PetDetailScreenContent(
            paddingValues = it,
            navigation = navigation)
    }
}

@Composable
fun PetDetailScreenContent(
    paddingValues: PaddingValues,
    navigation: INavigationRouter,
){

}

