package cz.pef.va2_2024_petstore.ui.screens.pet_detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
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
fun PetDetailScreen(
    navigation: INavigationRouter,
    id: Long?
){
    val viewModel = hiltViewModel<PetDetailViewModel>()

    val state = viewModel.uiState.collectAsStateWithLifecycle()


    val pet = remember {
        mutableStateOf<Pet?>(null)
    }

    state.value.let {
        when(it){
            is PetDetailScreenUIState.DataLoaded -> {
                pet.value=it.pet
            }
            is PetDetailScreenUIState.Error -> {

            }
            PetDetailScreenUIState.Loading -> {
                if (id != null) {
                    viewModel.loadDetail(id)
                }
            }
        }
    }

    BaseScreen(
        topBarText = stringResource(R.string.pet_detail),
        onBackClick = {
            navigation.returnBack()
        }
    ) {
        PetDetailScreenContent(
            paddingValues = it,
            navigation = navigation,
            pet =pet.value)
    }
}

@Composable
fun PetDetailScreenContent(
    paddingValues: PaddingValues,
    navigation: INavigationRouter,
    pet: Pet?
) {
    if (pet != null) {
        Column(modifier = Modifier.padding(paddingValues)) {
            Text(text = pet.id.toString())
            Text(text = pet.name!!)
            Text(text = pet.status!!)

            LazyColumn(modifier = Modifier.padding(paddingValues)) {
                pet.tags?.let {
                    items(it.count()){//Type mismatch.Required:List<TypeVariable(T)>Found:List<Tag>?
                        Row {
                            Text(text=it.toString())
                        }
                    }
                }
            }
        }
    }
}