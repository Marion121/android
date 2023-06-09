package fr.epf.mm.gestionFilm

import android.text.Editable
import fr.epf.mm.gestionFilm.model.Film
import fr.epf.mm.gestionFilm.model.listeFilm
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface FilmService {
    companion object {
        const val API_KEY = "1dea036821ae31c07f18a4014fdbaf56"
        const val ACCOUNT_ID = 19867451

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

