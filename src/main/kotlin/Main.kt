import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URI
import java.time.LocalDate
import org.json.JSONObject

fun computeTomorrowDate(): LocalDate = LocalDate.now().plusDays(1)

fun main() {
    val apiKey = "e142634501294007b0190901252808"
    val cities = arrayOf("Chisinau", "Madrid", "Kyiv", "Amsterdam")
    val date = computeTomorrowDate()

    println("                    Weather Forecast for Tomorrow, $date:")
    println("_____________________________________________________________________________________")
    println("| City       | Min Temp (C) | Max Temp (C) | Humidity (%)  | Wind (kph)  | Wind Dir |")
    println("_____________________________________________________________________________________")

    for (city in cities) {
        val apiUrl = "https://api.weatherapi.com/v1/forecast.json?q=$city&days=1&dt=$date&key=$apiKey"
        try {
            val connection = URI.create(apiUrl).toURL().openConnection() as HttpURLConnection
            connection.requestMethod = "GET"

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val response = BufferedReader(InputStreamReader(connection.inputStream)).use { it.readText() }
                val json = JSONObject(response)
                val day = json.getJSONObject("forecast")
                    .getJSONArray("forecastday")
                    .getJSONObject(0)
                    .getJSONObject("day")
                val hour12 = json.getJSONObject("forecast")
                    .getJSONArray("forecastday")
                    .getJSONObject(0)
                    .getJSONArray("hour")
                    .getJSONObject(12)

                val minTemp = day.getDouble("mintemp_c")
                val maxTemp = day.getDouble("maxtemp_c")
                val humidity = day.getInt("avghumidity")
                val windSpeed = hour12.getDouble("wind_kph")
                val windDir = hour12.getString("wind_dir")

                println("| %-10s | %12s | %12s | %13s | %11s | %-8s |".format(
                    city, minTemp, maxTemp, humidity, windSpeed, windDir
                ))
            } else {
                println("| %-10s | %12s | %12s | %13s | %11s | %-8s |".format(city, "-", "-", "-", "-", "-"))
            }
        } catch (_: Exception) {
            println("| %-10s | %12s | %12s | %13s | %11s | %-8s |".format(city, "-", "-", "-", "-", "-"))
        }
    }
    println("_____________________________________________________________________________________")
}