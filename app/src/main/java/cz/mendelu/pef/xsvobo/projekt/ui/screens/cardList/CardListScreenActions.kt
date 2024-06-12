package cz.mendelu.pef.xsvobo.projekt.ui.screens.cardList

interface CardListScreenActions {
    fun addCard(id: Long)
    fun setTextChanged(text: String)
    fun saveSetName()
}