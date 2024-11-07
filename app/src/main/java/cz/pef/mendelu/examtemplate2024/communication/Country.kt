package cz.pef.mendelu.examtemplate2024.communication


data class Country(
    val name: NameDetails,
    val tld: List<String>,
    val cca2: String,
    val ccn3: String,
    val cca3: String,
    val independent: Boolean,
    val status: String,
    val unMember: Boolean,
    val currencies: Map<String, Currency>,
    val idd: Idd,
    val capital: List<String>,
    val altSpellings: List<String>,
    val region: String,
    val languages: Map<String, String>,
    val translations: Map<String, Translation>
)

data class NameDetails(
    val common: String,
    val official: String,
    val nativeName: Map<String, Translation>
)

data class Currency(
    val name: String,
    val symbol: String
)

data class Idd(
    val root: String,
    val suffixes: List<String>
)

data class Translation(
    val official: String,
    val common: String
)
