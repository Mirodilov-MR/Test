package com.example.myapplication

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL



//Google short link va Yandex short linkdan kordinata olish, faqat yandex ishlamayapti


//fun expandShortMapLink(link: String): Pair<Double, Double>? {
//    var url = URL(link)
//    var connection: HttpURLConnection
//    do {
//        connection = url.openConnection() as HttpURLConnection
//        connection.requestMethod = "GET"
//        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3")
//        connection.connect()
//
//        val responseCode = connection.responseCode
//        if (responseCode == 200) {
//            val reader = BufferedReader(InputStreamReader(connection.inputStream))
//            val response = reader.use { it.readText() }
//
//            // Google Maps
//            val googleRegex = """https?:\/\/www\.google\.com\/maps\/.*?\/@(-?\d+(\.\d+)?),(-?\d+(\.\d+)?)""".toRegex()
//            val googleMatch = googleRegex.find(response)
//            if (googleMatch!= null) {
//                val lat = googleMatch.groupValues[1].toDouble()
//                val lon = googleMatch.groupValues[3].toDouble()
//                return Pair(lat, lon)
//            }
//
//            // Yandex Maps
//            val yandexRegex = """https?:\/\/yandex\.(?:by|ru|com)\/maps\/-\/[^"]+""".toRegex()
//            val yandexMatch = yandexRegex.find(link)
//            if (yandexMatch!= null) {
//                val yandexUrl = "https://yandex.by/maps/?utm_source=m&utm_medium=mapframe&utm_campaign=None&z=12&ll=$link"
//                val yandexConnection = URL(yandexUrl).openConnection() as HttpURLConnection
//                yandexConnection.requestMethod = "GET"
//                yandexConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3")
//                yandexConnection.connect()
//
//                val yandexResponseCode = yandexConnection.responseCode
//                if (yandexResponseCode == 200) {
//                    val yandexReader = BufferedReader(InputStreamReader(yandexConnection.inputStream))
//                    val yandexResponse = yandexReader.use { it.readText() }
//
//                    if (yandexResponse.contains("captcha")) {
//                        println("Captcha detected. Cannot get coordinates.")
//                        return null
//                    }
//
//                    val yandexLatRegex = """ll=([^,]+),""".toRegex()
//                    val yandexLonRegex = """,([^"]+)""".toRegex()
//                    val yandexLatMatch = yandexLatRegex.find(yandexResponse)
//                    val yandexLonMatch = yandexLonRegex.find(yandexResponse)
//                    if (yandexLatMatch!= null && yandexLonMatch!= null) {
//                        val lat = yandexLatMatch.groupValues[1].toDouble()
//                        val lon = yandexLonMatch.groupValues[1].toDouble()
//                        return Pair(lat,lon)
//                    }
//                }
//            }
//        } else if (responseCode == 301 || responseCode == 302) {
//            val newLocation = connection.getHeaderField("Location")
//            url = URL(newLocation)
//        } else {
//            return null
//        }
//    } while (true)
//}
//
//fun main() {
//    val googleShortLink = "https://maps.app.goo.gl/yrx8fmSFawSnENm8A"
//    val yandexShortLink = "https://yandex.by/maps/-/CDVuqY~4"
//
//    val googleCoordinates = expandShortMapLink(googleShortLink)
//    println("Google Coordinates: $googleCoordinates")
//
//    val yandexCoordinates = expandShortMapLink(yandexShortLink)
//    println("Yandex Coordinates: $yandexCoordinates")
//}







//--------------------------------------------------------------------
//Google map short link uchun kordinata olish

fun expandShortGoogleMapsLink(link: String): Pair<Double, Double>? {
    var url = URL(link)
    var connection: HttpURLConnection
    do {
        connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connect()

        val responseCode = connection.responseCode
        if (responseCode == 200) {
            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            val response = reader.use { it.readText() }

            val regex = """https?:\/\/www\.google\.com\/maps\/.*?\/@(-?\d+(\.\d+)?),(-?\d+(\.\d+)?)""".toRegex()
            val match = regex.find(response)
            if (match != null) {
                val lat = match.groupValues[1].toDouble()
                val lon = match.groupValues[3].toDouble()
                return Pair(lat, lon)
            }
        } else if (responseCode == 301 || responseCode == 302) {
            val newLocation = connection.getHeaderField("Location")
            url = URL(newLocation)
        } else {
            return null
        }
    } while (true)
}

fun main() {
    val shortLink = "https://maps.app.goo.gl/sxSRwMxL9ALqZmxa6"
    val coordinates = expandShortGoogleMapsLink(shortLink)
    println("Coordinates: $coordinates")
}

//-----------------------------------------------------------------------

// Uzun linkdan kordinata oladi Google va Yandex dan

//import java.net.URLDecoder
//fun extractCoordinatesFromGoogleMapsLink(link: String): Pair<Double, Double>? {
//    val regex = """https?:\/\/www\.google\.com\/maps\/place\/.*?\/@(-?\d+(\.\d+)?),(-?\d+(\.\d+)?)""".toRegex()
//    val match = regex.find(link)
//    if (match!= null) {
//        val lat = match.groupValues[1].toDouble()
//        val lon = match.groupValues[3].toDouble()
//        return Pair(lat, lon)
//    }
//    return null
//}
//
//fun extractCoordinatesFromYandexMapsLink(link: String): Pair<Double, Double>? {
//    val regex = """ll=([^&]+)""".toRegex()
//    val match = regex.find(link)
//    if (match != null) {
//        val coordsStr = URLDecoder.decode(match.groupValues[1], "UTF-8")
//        val coords = coordsStr.split(",")
//        if (coords.size == 2) {
//            val lon = coords[0].toDouble()
//            val lat = coords[1].toDouble()
//            return Pair(lat, lon)
//        }
//    }
//    return null
//}
//
//fun main() {
//    val googleLink = "https://www.google.com/maps/place/Istanbul+Aydin+University/@41.3239304,69.2735667,16z/data=!4m6!3m5!1s0x38ae8b005faedb77:0x21fdf98e1bdd2afb!8m2!3d41.327505!4d69.2812991!16s%2Fg%2F11v_0cp525?entry=tts&g_ep=EgoyMDI0MDYwMi4wKgBIAVAD"
//    val yandexLink = "https://yandex.by/maps/10335/tashkent/?ll=69.249053%2C41.281081&mode=poi&poi%5Bpoint%5D=69.245563%2C41.284257&poi%5Buri%5D=ymapsbm1%3A%2F%2Forg%3Foid%3D56283017360&utm_source=share&z=15"
//
//    val googleCoords = extractCoordinatesFromGoogleMapsLink(googleLink)
//    val yandexCoords = extractCoordinatesFromYandexMapsLink(yandexLink)
//
//    println("Google Maps Coordinates: $googleCoords")
//    println("Yandex Maps Coordinates: $yandexCoords")
//}
