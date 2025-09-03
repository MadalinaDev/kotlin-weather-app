import retrofit2.http.GET
import retrofit2.http.Query

data class WeatherResponse(
    val forecast: Forecast
)

data class Forecast(
    val forecastday: List<ForecastDay>
)

data class ForecastDay(
    val day: Day,
    val hour: List<Hour>
)

data class Day(
    val mintemp_c: Double,
    val maxtemp_c: Double,
    val avghumidity: Int
)

data class Hour(
    val wind_kph: Double,
    val wind_dir: String
)

data class WeatherData(
    val city: String,
    val minTemp: Double,
    val maxTemp: Double,
    val humidity: Int,
    val windSpeed: Double,
    val windDir: String
)

interface WeatherApiService {
    @GET("forecast.json")
    suspend fun getForecast(
        @Query("q") city: String,
        @Query("days") days: Int = 1,
        @Query("dt") date: String,
        @Query("key") apiKey: String
    ): WeatherResponse
}