package cz.pef.va2_2024_petstore.ui.screens.list_of_pets

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.pef.va2_2024_petstore.R
import cz.pef.va2_2024_petstore.communication.Pet
import cz.pef.va2_2024_petstore.navigation.INavigationRouter
import cz.pef.va2_2024_petstore.ui.elements.BaseScreen

@Composable
fun ListOfPetsScreen(
    navigation: INavigationRouter
){

    val viewModel = hiltViewModel<ListOfPetsViewModel>()

    val pets = remember {
        mutableListOf<Pet>()
    }
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    state.value.let {
        when(it){
            is ListOfPetsScreenUIState.DataLoaded -> {
                pets.clear()
                pets.addAll(it.pets)
            }
            is ListOfPetsScreenUIState.Error -> {

            }
            ListOfPetsScreenUIState.Loading -> {

            }
        }
    }




    BaseScreen(
        topBarText = stringResource(R.string.list_of_pets),
    ) {
        ListOfPetsScreenContent(
            paddingValues = it,
            navigation = navigation,
            pets=pets)
    }
}

@Composable
fun ListOfPetsScreenContent(
    paddingValues: PaddingValues,
    navigation: INavigationRouter,
    pets:List<Pet>
){
LazyColumn(modifier = Modifier.padding(paddingValues)) {
    items(pets){
        Row {
            Text(text=it.name.toString())
        }
    }

}
}