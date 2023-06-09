package fr.epf.mm.gestionclient

import android.text.Editable
import com.google.gson.JsonObject
import fr.epf.mm.gestionclient.model.Film
import fr.epf.mm.gestionclient.model.listeFilm
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface FilmService {
    companion object {
        const val API_KEY = "1dea036821ae31c07f18a4014fdbaf56"
        const val ACCOUNT_ID = 19867451
        const val ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxZGVhMDM2ODIxYWUzMWMwN2YxOGE0MDE0ZmRiYWY1NiIsInN1YiI6IjY0N2YwZWRhY2Y0YjhiMDBhODc5MWQwMCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.782_AKlIv5ZYkuixffIheQZBmGZGcmPj8FfDKNj66w8"

    }

    @GET("movie/{film_id}?api_key=$API_KEY")
    fun getFilmDetails(
        @Header("Authorization") authorization: String,
        @Path("film_id") film_id: Int,
    ): Call<Film>

    @GET("movie?api_key=$API_KEY")
    fun getFilm(@Query("query") movie: Editable): Call<listeFilm>

    @GET("movie/{film_id}/recommendations?api_key=$API_KEY")
    fun getFilmRecommendation(
        @Path("film_id") filmId: Int,
    ): Call<listeFilm>

    @GET ("account/$ACCOUNT_ID/favorite/movies?language=en-US&page=1&sort_by=created_at.asc")
    fun getFavoriteFilm (
         @Header("Authorization") authorization: String
    ) : Call<listeFilm>

    @POST("account/$ACCOUNT_ID/favorite")
    fun addFilmFavorite(@Header("Authorization") authorization: String, @Body film: RequestBody)  : Call<ReponseAddFav>
 }
data class ReponseAddFav(
    val success : Boolean,
    val status_code : Int,
    val status_message : String
)
data class GetlisteFilm(val results : List<FilmRecup>)
data class FilmRecup(val original_title : String, val overview : String)

