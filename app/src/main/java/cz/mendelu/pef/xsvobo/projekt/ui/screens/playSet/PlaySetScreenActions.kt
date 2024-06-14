package cz.mendelu.pef.xsvobo.projekt.ui.screens.playSet

interface PlaySetScreenActions {
    fun cardAnswerChanged(text: String)
    fun incrementCorrectCount()
    fun loadSet(id: Long)
    fun nextCard()

}