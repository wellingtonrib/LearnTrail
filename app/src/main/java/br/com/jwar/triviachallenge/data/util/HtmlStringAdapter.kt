package br.com.jwar.triviachallenge.data.util

import android.text.Html
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class HtmlStringAdapter {
    @FromJson
    fun fromJson(jsonString: String): String {
        return Html.fromHtml(jsonString, Html.FROM_HTML_MODE_LEGACY).toString()
    }

    @ToJson
    fun toJson(htmlString: String): String {
        return htmlString
    }
}