package cz.mendelu.pef.xsvobo.projekt.ui.screens.setList

import android.net.Uri


interface SetListScreenActions {
    fun createSet()
    fun deleteSet(setId:Long)
    fun updateIcon(uri: Uri, setId: Long)
}