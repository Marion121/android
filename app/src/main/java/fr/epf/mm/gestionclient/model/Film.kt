package fr.epf.mm.gestionclient.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

data class listeFilm(
        var page : Int,
        var results : List<Film>
    )


data class Genre(
    val id: Int,
    val name: String
)


@Parcelize
data class Film(
    var original_title : String,
    var overview : String,
    val adult: Boolean,
    // val backdrop_path: String,
    // val belongs_to_collection : Any?,
    val budget: Int,
    //val genres: List<Genre>,
    // val homepage : String,
    val id: Int,
    //  val imdb_id : String,
    val original_language: String?,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    //  val revenue : Int,
    val runtime : Int,
    // val status : String,
    // val tagline : String,
    //  val title: String,
    //  val video: Boolean,
    val vote_average: Double,
    val vote_count: Int,
    //val favorite : Boolean
    ) : Parcelable