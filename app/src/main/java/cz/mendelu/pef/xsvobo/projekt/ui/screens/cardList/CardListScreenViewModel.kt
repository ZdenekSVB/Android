package cz.mendelu.pef.xsvobo.projekt.ui.screens.cardList

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.pef.xsvobo.projekt.database.card.ILocalCardsRepository
import cz.mendelu.pef.xsvobo.projekt.database.set.ILocalSetsRepository
import cz.mendelu.pef.xsvobo.projekt.datastore.SetPreferences
import cz.mendelu.pef.xsvobo.projekt.model.Card
import cz.mendelu.pef.xsvobo.projekt.ui.screens.setList.SetListScreenData
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CardListScreenViewModel @Inject constructor(
    private val repositoryCards: ILocalCardsRepository,
    private val repositorySets: ILocalSetsRepository,
    private val setPreferences: SetPreferences,
    @ApplicationContext private val context: Context
) : ViewModel(), CardListScreenActions {

    var cards: MutableList<Card> = mutableListOf()
    private var cardData: CardListScreenData = CardListScreenData()
    private var setData: SetListScreenData = SetListScreenData()

    private val _setIconUrl: MutableStateFlow<String?> = MutableStateFlow(null)
    val setIconUrl: StateFlow<String?> = _setIconUrl

    private val _cardListScreenUIState: MutableStateFlow<CardListScreenUIState> =
        MutableStateFlow(CardListScreenUIState.Loading())
    val cardListScreenUIState = _cardListScreenUIState.asStateFlow()

    private var setId: Long = 0L
    init {
        viewModelScope.launch {
            setId = setPreferences.setId.first() ?: 0L
            if (setId != 0L) {
                val set = repositorySets.getSet(setId)
                _setIconUrl.value = set.icon
            }
        }
    }

    override fun addCard(id: Long) {
        viewModelScope.launch {
            Log.d("CardViewModel", "addCard called")
            if (cardData.card.id == null) {
                cardData.card.setsId = id
                cardData.card.name = "Card"
                Log.d(
                    "CardViewModel", "Inserting card: ${cardData.card.setsId} + ${cardData.card.id}"
                )

                repositoryCards.insert(cardData.card)

                setData.set.cardsCount += 1
                repositorySets.update(setData.set)

                _cardListScreenUIState.value =
                    CardListScreenUIState.Success(cards)  // Update the state to trigger recomposition
            }
        }
    }

    override fun setTextChanged(text: String) {
        setData.set.name = text
        _cardListScreenUIState.value = CardListScreenUIState.SetNameChanged(setData)
        viewModelScope.launch {
            _cardListScreenUIState.value = CardListScreenUIState.Success(cards)
        }
    }

    override fun saveSetName() {
        if (setData.set.name.isNotEmpty()) {
            viewModelScope.launch {
                if (setData.set.id != null) {
                    repositorySets.update(setData.set)
                } else {
                    throw Exception("ID NULL")
                }
            }
        } else {
            throw Exception("Cannot Be Empty")
        }
    }

    override fun updateIcon(uri: Uri) {
        viewModelScope.launch {
            val fileName = saveImageToInternalStorage(uri)
            repositorySets.updateSetIcon(setId, fileName)
            _setIconUrl.value = fileName
            setPreferences.saveIconUrl(setId, fileName)
        }
    }

    private fun saveImageToInternalStorage(uri: Uri): String {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val fileName = "JPEG_${timeStamp}_.jpg"
        val file = File(context.filesDir, fileName)

        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            file.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }

        return fileName
    }


    fun loadSet(id: Long) {
        Log.d("loadSet", "ID: $id")
        viewModelScope.launch {
            setData.set = repositorySets.getSet(id)
            _cardListScreenUIState.value = CardListScreenUIState.SetNameChanged(setData)
        }
        viewModelScope.launch {
            repositoryCards.getCardsBySetId(id).collect {
                cards = it.toMutableList()
                _cardListScreenUIState.value = CardListScreenUIState.Success(cards)
            }
        }
    }

    override fun deleteCard(id: Long) {
        viewModelScope.launch {
            val numberOfDeleted = repositoryCards.delete(repositoryCards.getCard(id))
            if (numberOfDeleted > 0) {
                _cardListScreenUIState.value = CardListScreenUIState.CardDeleted()
            }
        }
    }

}
