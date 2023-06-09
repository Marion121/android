package fr.epf.mm.gestionFilm

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.epf.mm.gestionFilm.model.Film
import fr.epf.mm.gestionFilm.model.listeFilm
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.MediaType
import okhttp3.RequestBody

class DetailsFilmActivity : AppCompatActivity() {
    val ACCESS_TOKEN = " Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxZGVhMDM2ODIxYWUzMWMwN2YxOGE0MDE0ZmRiYWY1NiIsInN1YiI6IjY0N2YwZWRhY2Y0YjhiMDBhODc5MWQwMCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.782_AKlIv5ZYkuixffIheQZBmGZGcmPj8FfDKNj66w8"
    lateinit var recyclerView: RecyclerView
    lateinit var filmExtra : Film
    var adapter : FilmAdapter? = null
    var favoris = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_film)


        val extras = intent.extras
        filmExtra = extras?.get("film") as Film

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val imageFilm = filmExtra.poster_path
        val imageView = findViewById<ImageView>(R.id.film_image_imageview)
        val posterUrl = "https://image.tmdb.org/t/p/original" + imageFilm
        Picasso.get().load(posterUrl).into(imageView);

        recyclerView = findViewById<RecyclerView>(R.id.list_film_reco_recyclerview)
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

        val titleTextview = findViewById<TextView>(R.id.film_titre_textview)
        val populariteTextview = findViewById<TextView>(R.id.film_popularite_info_textview)
        val dateTextview = findViewById<TextView>(R.id.film_date_info_textview)
        val descriptionTextview = findViewById<TextView>(R.id.film_description_info_textview)
        val languageTextview = findViewById<TextView>(R.id.film_language_info_textview)
        val budgetTextview = findViewById<TextView>(R.id.film_nbr_votant_info_textview)
        //val overviewxTextview = findViewById<TextView>(R.id.details_film_overview_textview)

        // lastNameTextview.text = client.lastName
        titleTextview.text = filmExtra.original_title ?: "Non renseigné"
        populariteTextview.text = filmExtra.vote_average.toString()
        dateTextview.text = filmExtra.release_date
        descriptionTextview.text = filmExtra.overview
        languageTextview.text = filmExtra.original_language
        budgetTextview.text = filmExtra.vote_count.toString()

        // mettre les textes en gras

        val titrepopulariteTextview = findViewById<TextView>(R.id.film_popularite_textview)
        val titredateTextview = findViewById<TextView>(R.id.film_date_textview)
        val titredescriptionTextview = findViewById<TextView>(R.id.film_description_textview)
        val titreruntimeTextview = findViewById<TextView>(R.id.film_language_textview)
        val titrenbrvotantTextview = findViewById<TextView>(R.id.film_nbr_votant_textview)
        val titrelisteFilmRecoTextview = findViewById<TextView>(R.id.film_liste_recommande_textview)

        setBoldText(titleTextview)
        setBoldText(titrepopulariteTextview)
        setBoldText(titredateTextview)
        setBoldText(titredescriptionTextview)
        setBoldText(titreruntimeTextview)
        setBoldText(titrenbrvotantTextview)
        setBoldText(titrelisteFilmRecoTextview)

        // mettre en gros
        setLargeText(titleTextview, 2.2f)

        // justifié la description
        descriptionTextview.gravity = Gravity.CENTER_HORIZONTAL
        recupFilmReco()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(!favoris ) {
            menuInflater.inflate(R.menu.menu_add_fav, menu)
        }else{
            menuInflater.inflate(R.menu.menu_film_fav, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    //bouton retour
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
           R.id.action_add_fav -> {
               val jsonBody = """
                        {
                              "media_type": "movie",
                              "media_id": ${filmExtra.id},
                              "favorite": true
                        }
                    """.trimIndent()
               val mediaType = MediaType.parse("application/json")
               val requestBody = RequestBody.create(mediaType, jsonBody)
               val retrofit = Retrofit.Builder()
                   .baseUrl("https://api.themoviedb.org/3/")
                   .addConverterFactory(GsonConverterFactory.create())
                   .build()

               var service = retrofit.create(FilmService::class.java)

               var call = service.addFilmFavorite(ACCESS_TOKEN, requestBody)
               call.enqueue( object : Callback<ReponseAddFav> {
                   override fun onResponse(call: Call<ReponseAddFav>, response: Response<ReponseAddFav>) {
                       if (response.isSuccessful) {
                           val result = response.body()
                           showToast(this@DetailsFilmActivity, filmExtra.original_title + "a été ajouté aux favoris !")
                           favoris = true
                           invalidateOptionsMenu()
                           Log.d("ici", "lancement requete")
                       }
                   }
                   override fun onFailure(call: Call<ReponseAddFav>, t: Throwable) {
                       Toast.makeText(this@DetailsFilmActivity, "Erreur serveur", Toast.LENGTH_LONG).show()
                   }
               })
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        when(requestCode){
            1211 -> {
                if(resultCode == RESULT_OK){
                    val extras = intent?.extras
                    val bitmap = extras?.get("data") as? Bitmap
                }else{

                }
            }
        }
        super.onActivityResult(requestCode, resultCode, intent)
    }
    fun setBoldText(textView: TextView) {
        val spannable = SpannableStringBuilder(textView.text)
        val boldSpan = StyleSpan(Typeface.BOLD)
        spannable.setSpan(boldSpan, 0, textView.text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = spannable
    }

    fun setLargeText(textView: TextView, scale: Float) {
        val spannable = SpannableStringBuilder(textView.text)
        val sizeSpan = RelativeSizeSpan(scale)
        spannable.setSpan(sizeSpan, 0, textView.text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = spannable
    }

    fun recupFilmReco() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var service = retrofit.create(FilmService::class.java)

        val call = service.getFilmRecommendation(filmExtra.id)
        call.enqueue( object : Callback<listeFilm> {
            override fun onResponse(call: Call<listeFilm>, response: Response<listeFilm>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    adapter = result?.results?.let { FilmAdapter(it, this@DetailsFilmActivity) }
                    recyclerView.setAdapter(adapter)
                }
            }
            override fun onFailure(call: Call<listeFilm>, t: Throwable) {
                Toast.makeText(this@DetailsFilmActivity, "Erreur serveur", Toast.LENGTH_LONG).show()
            }
        })
    }
}