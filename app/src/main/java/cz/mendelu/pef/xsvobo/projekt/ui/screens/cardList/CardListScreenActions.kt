package cz.mendelu.pef.xsvobo.projekt.ui.screens.cardList

import android.net.Uri

interface CardListScreenActions {
    fun addCard(id: Long)
    fun setTextChanged(text: String)
    fun saveSetName()

    fun updateIcon(uri: Uri)
    fun deleteCard(id:Long)
}