package cz.mendelu.pef.xsvobo.projekt.utils

import java.util.Locale

object LanguageUtils {

    private val CZECH = "cs"
    private val ENGLISH = "eng"

    fun isLanguageCloseToCzech(): Boolean {
        val language = Locale.getDefault().language
        return language.equals(CZECH) || language.equals(ENGLISH)
    }



}