package cz.mendelu.pef.xsvobo.projekt.ui.screens.addCard

interface AddCardScreenActions {

    fun cardQuestionChanged(text: String)
    fun cardAnswerChanged(text: String)
    fun loadCard(id: Long?)
    fun saveCard()
}