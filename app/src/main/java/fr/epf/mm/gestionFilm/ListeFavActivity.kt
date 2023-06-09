package fr.epf.mm.gestionFilm

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.epf.mm.gestionFilm.model.listeFilm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListeFavActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    var adapter : FilmAdapter? = null
    //var call1 = null
    val ACCESS_TOKEN = " Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxZGVhMDM2ODIxYWUzMWMwN2YxOGE0MDE0ZmRiYWY1NiIsInN1YiI6IjY0N2YwZWRhY2Y0YjhiMDBhODc5MWQwMCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.782_AKlIv5ZYkuixffIheQZBmGZGcmPj8FfDKNj66w8"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liste_fav)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerView = findViewById<RecyclerView>(R.id.list_film_fav_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

        recupFilmFav()


    }
    //bouton retour
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun recupFilmFav() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var service = retrofit.create(FilmService::class.java)
            val call1 = service.getFavoriteFilm(ACCESS_TOKEN)
            call1.enqueue(object : Callback<listeFilm> {
                override fun onResponse(call: Call<listeFilm>, response: Response<listeFilm>) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        adapter = result?.results?.let { FilmAdapter(it, this@ListeFavActivity) }
                        Log.d("APIII", result.toString())
                        recyclerView.setAdapter(adapter)
                    }
                }

                override fun onFailure(call: Call<listeFilm>, t: Throwable) {
                    Toast.makeText(this@ListeFavActivity, "Erreur serveur", Toast.LENGTH_LONG)
                        .show()
                }
            })
    }
}