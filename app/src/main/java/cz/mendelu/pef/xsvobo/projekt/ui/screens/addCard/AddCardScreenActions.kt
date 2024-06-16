package cz.mendelu.pef.xsvobo.projekt.ui.screens.addCard

interface AddCardScreenActions {

    fun cardQuestionChanged(text: String)
    fun cardRightAnswerChanged(text: String)
    fun loadCard(id: Long?)
    fun saveCard()
    fun cardTextChanged(text: String)
}