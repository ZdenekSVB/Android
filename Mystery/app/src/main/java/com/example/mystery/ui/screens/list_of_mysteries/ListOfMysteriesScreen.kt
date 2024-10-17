package com.example.mystery.ui.screens.list_of_pets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mystery.R
import com.example.mystery.communication.MysteryItem
import com.example.mystery.navigation.INavigationRouter
import com.example.mystery.ui.elements.BaseScreen

@Composable
fun ListOfPetsScreen(
    navigation: INavigationRouter
){

    val viewModel = hiltViewModel<ListOfMysteriesViewModel>()

    val mystery = remember {
        mutableStateOf<MysteryItem?>(null)
    }
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    state.value.let {
        when(it){
            is ListOfMysteriesScreenUIState.DataLoaded -> {
                mystery.value=it.mysteries
            }
            is ListOfMysteriesScreenUIState.Error -> {

            }
            ListOfMysteriesScreenUIState.Loading -> {

            }
        }
    }




    BaseScreen(
        topBarText = stringResource(R.string.list_of_mysteries),
        showLoading = state.value is ListOfMysteriesScreenUIState.Loading
    ) {
        ListOfPetsScreenContent(
            paddingValues = it,
            navigation = navigation,
            mystery =mystery)
    }
}

@Composable
fun ListOfPetsScreenContent(
    paddingValues: PaddingValues,
    navigation: INavigationRouter,
    mystery: MutableState<MysteryItem?>
) {
    LazyColumn(modifier = Modifier.padding(paddingValues)) {
        val itemsList = mystery.value?.data ?: emptyList()

        items(itemsList) { item ->
            Text(text = item.date.toString())
        }
    }
}

