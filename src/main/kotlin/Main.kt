import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL

fun main () {
    val apiKey = "e142634501294007b0190901252808"
    val cities = arrayOf("Chisinau", "Madrid", "Kyiv", "Amsterdam")

    for (city in cities) {
        val apiUrl = "https://api.weatherapi.com/v1/forecast.json?q=$city&days=1&dt=2025-08-29&key=$apiKey"
        println("Weather data for $city:")
        try {
            val url: URL = URI.create(apiUrl).toURL()
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"

            val responseCode: Int = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                var line: String?
                val response = StringBuilder()
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                reader.close()
                println("Received Weather Data: $response")
            } else {
                println("An unexpected error occurred while fetching data from the WeatherAPI")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}