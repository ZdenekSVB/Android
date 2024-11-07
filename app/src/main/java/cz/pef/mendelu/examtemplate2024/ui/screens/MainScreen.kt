package cz.pef.mendelu.examtemplate2024.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import cz.pef.mendelu.examtemplate2024.communication.User
import cz.pef.mendelu.examtemplate2024.navigation.INavigationRouter

@Composable
fun MainScreen(
    navigation: INavigationRouter
) {
    val viewModel = hiltViewModel<MainScreenViewModel>()
    val user by viewModel.currentUser.collectAsState()

    // Zavolání funkce pro generování uživatele při inicializaci
    LaunchedEffect(Unit) {
        viewModel.generateNewRandomUser() // Vygeneruje uživatele při načtení obrazovky
    }

    MainScreenContent(
        paddingValues = PaddingValues(16.dp),
        navigation = navigation,
        user = user,
        viewModel = viewModel,
        onRandomUserClick = { viewModel.generateNewRandomUser() }
    )
}


@Composable
fun MainScreenContent(
    paddingValues: PaddingValues,
    navigation: INavigationRouter,
    user: User?,
    viewModel: MainScreenViewModel,
    onRandomUserClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        user?.let {
            Text(
                text = "First Name: ${user.results[0].name.first}",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Text(
                text = "Last Name: ${user.results[0].name.last}",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Text(
                text = "Location:",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Text(
                text = "Street: ${user.results[0].location.street.name} ${user.results[0].location.street.number}",
                modifier = Modifier.padding(vertical = 2.dp)
            )
            Text(
                text = "Country: ${user.results[0].location.country}",
                modifier = Modifier.padding(vertical = 2.dp)
            )
            Text(
                text = "Currency: ${viewModel.getCurrencyForCountry(user.results[0].location.country)}",
                modifier = Modifier.padding(vertical = 2.dp)
            )
            Text(
                text = "Capital: ${viewModel.getCapitalForCountry(user.results[0].location.country)}",
                modifier = Modifier.padding(vertical = 2.dp)
            )
            Text(
                text = "Email: ${user.results[0].email}",
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Text(
                text = "Age: ${user.results[0].dob.age}",
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Image(
                painter = rememberAsyncImagePainter(user.results[0].picture.large),
                contentDescription = null,
                modifier = Modifier
                    .size(128.dp)
                    .padding(vertical = 8.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onRandomUserClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Select Random User")
        }
    }
}
