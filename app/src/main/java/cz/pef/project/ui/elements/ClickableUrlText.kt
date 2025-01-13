package cz.pef.project.ui.elements

import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight

@Composable
fun ClickableUrlText(description: String) {
    // Předpokládám, že description obsahuje URL, např. "https://example.com"
    val annotatedString = buildAnnotatedString {
        // Předpokládejme, že URL je v textu, a vytvořte odkaz
        val url = description // Nebo použijte regulární výraz pro extrakci URL
        append(description)
        addStyle(
            style = SpanStyle(color = Color.Blue, fontWeight = FontWeight.Bold),
            start = description.indexOf(url),
            end = description.length
        )
        addStringAnnotation("URL", url, start = description.indexOf(url), end = description.length)
    }

    ClickableText(
        text = annotatedString,
        onClick = { offset ->
            val annotation = annotatedString.getStringAnnotations(offset, offset).firstOrNull()
            annotation?.let {
                // Spustit akci, např. otevření odkazu
                val url = it.item
                // Tady je možné spustit záměr pro otevření URL
            }
        }
    )
}
