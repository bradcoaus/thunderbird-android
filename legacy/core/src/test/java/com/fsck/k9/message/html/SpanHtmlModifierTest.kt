package com.fsck.k9.message.html

import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.text.style.URLSpan
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.fsck.k9.K9RobolectricTest
import org.junit.Test

class SpanHtmlModifierTest : K9RobolectricTest() {

    @Test
    fun `convert text with bold span to html`() {
        val text = SpannableString("Hello World").apply {
            setSpan(StyleSpan(Typeface.BOLD), 0, 5, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        val html = HtmlConverter.textToHtmlFragment(text)

        assertThat(html).isEqualTo("<div dir=\"auto\"><b>Hello</b>\u00A0World</div>")
    }

    @Test
    fun `convert text with italic span to html`() {
        val text = SpannableString("Hello World").apply {
            setSpan(StyleSpan(Typeface.ITALIC), 6, 11, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        val html = HtmlConverter.textToHtmlFragment(text)

        assertThat(html).isEqualTo("<div dir=\"auto\">Hello <i>World</i></div>")
    }

    @Test
    fun `convert text with underline span to html`() {
        val text = SpannableString("Hello World").apply {
            setSpan(UnderlineSpan(), 0, 5, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        val html = HtmlConverter.textToHtmlFragment(text)

        assertThat(html).isEqualTo("<div dir=\"auto\"><u>Hello</u>\u00A0World</div>")
    }

    @Test
    fun `convert text with url span to html`() {
        val text = SpannableString("Click here").apply {
            setSpan(URLSpan("https://example.com"), 6, 10, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        val html = HtmlConverter.textToHtmlFragment(text)

        assertThat(html).isEqualTo("<div dir=\"auto\">Click <a href=\"https://example.com\">here</a></div>")
    }
}
