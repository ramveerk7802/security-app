package com.rvcode.securityapp.sevices.util

import android.net.Uri
import android.util.Patterns
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class HeuristicPhishingDetector @Inject constructor() {


    // Keywords often found in phishing URLs
    private val suspiciousKeywords = setOf(
        "login", "verify", "account", "secure", "update", "banking", "password", "confirm"
    )

    // Common URL shorteners used to mask the final destination
    private val urlShorteners = setOf(
        "bit.ly", "tinyurl.com", "t.co", "goo.gl", "is.gd", "soo.gd", "short.io"
    )

    fun findAndCheckUrl(text: String) : String?{
        val matcher = Patterns.WEB_URL.matcher(text)

        while (matcher.find()){
            val url = matcher.group()
            if(isPhishing(url))
                    return url
        }
        return null
    }


    private fun isPhishing(url: String): Boolean{
        var riskScore:Int = 0
        val cleanUrl = url.lowercase(Locale.getDefault())
        // --- FIX: Re-enabled the core detection rules ---
        if (hasSuspiciousKeyword(cleanUrl))
            riskScore++
        if (usesUrlShortener(cleanUrl))
            riskScore++
        // for High risk
        if (isIpAddressBased(cleanUrl))
            riskScore += 2
        if (hasExcessiveSpecialChars(cleanUrl))
            riskScore++


        return riskScore>0
    }
    private fun getHost(url: String): String? {
        // Use try-catch for malformed URLs
        return try {
            Uri.parse(url).host
        } catch (e: Exception) {
            // Fallback for URLs without a scheme like 'www.example.com'
            url.substringBefore("/")
        }
    }

    private fun hasSuspiciousKeyword(url: String): Boolean {
        val domain = getHost(url) ?: return false // Use the correct getHost function
        return suspiciousKeywords.any { keyword -> domain.contains(keyword) }
    }

    private fun usesUrlShortener(url: String): Boolean {
        val domain = getHost(url) ?: return false // Use the correct getHost function
        return urlShorteners.any { shortener -> domain.contains(shortener) }
    }

    private fun isIpAddressBased(url: String): Boolean {
        val ipRegex = Regex("^(https|http)://(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}).*")
        return ipRegex.matches(url)
    }

    private fun hasExcessiveSpecialChars(url: String): Boolean {
        val domain = getHost(url) ?: return false // Use the correct getHost function
        // More than 3 hyphens or 4 dots in a domain is unusual.
        return domain.count { it == '-' } > 3 || domain.count { it == '.' } > 4
    }
}