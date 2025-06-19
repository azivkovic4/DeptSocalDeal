package com.example.deptsocaldeal.domain

import androidx.core.text.HtmlCompat
import javax.inject.Inject

class HtmlMapperUseCase @Inject constructor() {

    operator fun invoke(htmlStringCode: String) =
        HtmlCompat.fromHtml(htmlStringCode, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
}