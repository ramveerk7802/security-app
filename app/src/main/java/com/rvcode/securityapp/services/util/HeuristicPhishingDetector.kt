package com.rvcode.securityapp.services.util

import android.net.Uri
import android.util.Patterns
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HeuristicPhishingDetector @Inject constructor() {

    private val suspiciousKeywords = setOf(
        "login","verify","account","secure","update","banking","password","confirm"
    )
    private val urlShorteners = setOf(
        "bit.ly","tinyurl.com","t.co","goo.gl","is.gd","soo.gd","short.io"
    )

    fun findAndCheckUrl(text: String): String? {
        val matcher = Patterns.WEB_URL.matcher(text)
        while (matcher.find()) {
            val url = matcher.group()
            if (isPhishing(url)) return url
        }
        return null
    }

    private fun isPhishing(url: String): Boolean {
        var score = 0
        val u = url.lowercase(Locale.getDefault())

        if (usesUrlShortener(u)) return true

        if (hasSuspiciousKeyword(u)) score += 1
        if (usesUrlShortener(u)) score += 1
        if (isIpAddressBased(u)) score += 2
        if (hasExcessiveSpecialChars(u)) score += 1

        return score >0
    }

    private fun getHost(url: String): String? = try {
        Uri.parse(url).host
    } catch (_: Exception) {
        url.substringBefore("/")
    }

    private fun hasSuspiciousKeyword(url: String): Boolean {
        val host = getHost(url) ?: return false
        return suspiciousKeywords.any { host.contains(it) }
    }

    private fun usesUrlShortener(url: String): Boolean {
        val host = getHost(url) ?: return false
        return urlShorteners.any { host.contains(it) }
    }

    private fun isIpAddressBased(url: String): Boolean {
        val ip = Regex("^(https|http)://(\\d{1,3}\\.){3}\\d{1,3}(:\\d+)?(/.*)?$")
        return ip.matches(url)
    }

    private fun hasExcessiveSpecialChars(url: String): Boolean {
        val host = getHost(url) ?: return false
        return host.count { it == '-' } > 3 || host.count { it == '.' } > 4
    }
}
