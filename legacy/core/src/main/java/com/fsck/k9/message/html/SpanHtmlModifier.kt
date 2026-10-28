package com.fsck.k9.message.html

import android.graphics.Typeface
import android.text.Spanned
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.text.style.URLSpan

internal object SpanHtmlModifier : TextToHtml.HtmlModifier {
    override fun findModifications(text: CharSequence): List<HtmlModification> {
        if (text !is Spanned) return emptyList()

        val modifications = mutableListOf<HtmlModification>()

        // Find StyleSpans (Bold, Italic)
        val styleSpans = text.getSpans(0, text.length, StyleSpan::class.java)
        for (span in styleSpans) {
            val start = text.getSpanStart(span)
            val end = text.getSpanEnd(span)
            if (start >= 0 && end > start) {
                when (span.style) {
                    Typeface.BOLD -> modifications.add(BoldWrap(start, end))
                    Typeface.ITALIC -> modifications.add(ItalicWrap(start, end))
                    Typeface.BOLD_ITALIC -> {
                        modifications.add(BoldWrap(start, end))
                        modifications.add(ItalicWrap(start, end))
                    }
                }
            }
        }

        // Find UnderlineSpans
        val underlineSpans = text.getSpans(0, text.length, UnderlineSpan::class.java)
        for (span in underlineSpans) {
            val start = text.getSpanStart(span)
            val end = text.getSpanEnd(span)
            if (start >= 0 && end > start) {
                modifications.add(UnderlineWrap(start, end))
            }
        }

        // Find URLSpans
        val urlSpans = text.getSpans(0, text.length, URLSpan::class.java)
        for (span in urlSpans) {
            val start = text.getSpanStart(span)
            val end = text.getSpanEnd(span)
            val url = span.url
            if (start >= 0 && end > start && !url.isNullOrEmpty()) {
                modifications.add(UrlWrap(start, end, url))
            }
        }

        return modifications
    }

    class BoldWrap(startIndex: Int, endIndex: Int) : HtmlModification.Wrap(startIndex, endIndex) {
        override fun appendPrefix(textToHtml: TextToHtml) {
            textToHtml.appendHtml("<b>")
        }
        override fun appendSuffix(textToHtml: TextToHtml) {
            textToHtml.appendHtml("</b>")
        }
    }

    class ItalicWrap(startIndex: Int, endIndex: Int) : HtmlModification.Wrap(startIndex, endIndex) {
        override fun appendPrefix(textToHtml: TextToHtml) {
            textToHtml.appendHtml("<i>")
        }
        override fun appendSuffix(textToHtml: TextToHtml) {
            textToHtml.appendHtml("</i>")
        }
    }

    class UnderlineWrap(startIndex: Int, endIndex: Int) : HtmlModification.Wrap(startIndex, endIndex) {
        override fun appendPrefix(textToHtml: TextToHtml) {
            textToHtml.appendHtml("<u>")
        }
        override fun appendSuffix(textToHtml: TextToHtml) {
            textToHtml.appendHtml("</u>")
        }
    }

    class UrlWrap(startIndex: Int, endIndex: Int, val url: String) : HtmlModification.Wrap(startIndex, endIndex) {
        override fun appendPrefix(textToHtml: TextToHtml) {
            textToHtml.appendHtml("<a href=\"")
            textToHtml.appendHtmlAttributeEncoded(url)
            textToHtml.appendHtml("\">")
        }
        override fun appendSuffix(textToHtml: TextToHtml) {
            textToHtml.appendHtml("</a>")
        }
    }
}
