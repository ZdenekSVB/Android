package cz.mendelu.pef.xsvobo.projekt.ui.screens.playSet

import cz.mendelu.pef.xsvobo.projekt.model.Card

class PlaySetScreenData {
    var card: Card = Card("Card")
    var index:Int = 0
    var correctCount:Int = 0
    var cardTextError: String? = null
}