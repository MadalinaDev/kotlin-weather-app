import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate

fun computeTomorrowDate(): LocalDate = LocalDate.now().plusDays(1)

suspend fun fetchWeatherData(apiService: WeatherApiService, city: String, date: LocalDate, apiKey: String): WeatherData? {
    return try {
        val response = apiService.getForecast(city, 1, date.toString(), apiKey)
        val day = response.forecast.forecastday[0].day
        val hour12 = response.forecast.forecastday[0].hour[12]

        WeatherData(
            city = city,
            minTemp = day.mintemp_c,
            maxTemp = day.maxtemp_c,
            humidity = day.avghumidity,
            windSpeed = hour12.wind_kph,
            windDir = hour12.wind_dir
        )
    } catch (e: Exception) {
        println("Error fetching data for $city: ${e.message}")
        null
    }
}

fun main() = runBlocking {
    val apiKey = "e142634501294007b0190901252808"
    val cities = arrayOf("Chisinau", "Madrid", "Kyiv", "Amsterdam")
    val date = computeTomorrowDate()

    val okHttpClient = okhttp3.OkHttpClient.Builder().build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.weatherapi.com/v1/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(WeatherApiService::class.java)

    println("                    Weather Forecast for Tomorrow, $date:")
    println("_____________________________________________________________________________________")
    println("| City       | Min Temp (C) | Max Temp (C) | Humidity (%)  | Wind (kph)  | Wind Dir |")
    println("_____________________________________________________________________________________")

    for (city in cities) {
        val weatherData = fetchWeatherData(apiService, city, date, apiKey)

        if (weatherData != null) {
            println("| %-10s | %12s | %12s | %13s | %11s | %-8s |".format(
                weatherData.city,
                weatherData.minTemp,
                weatherData.maxTemp,
                weatherData.humidity,
                weatherData.windSpeed,
                weatherData.windDir
            ))
        } else {
            println("| %-10s | %12s | %12s | %13s | %11s | %-8s |".format(city, "-", "-", "-", "-", "-"))
        }
    }
    println("_____________________________________________________________________________________")

    okHttpClient.dispatcher.executorService.shutdown()
    okHttpClient.connectionPool.evictAll()
}