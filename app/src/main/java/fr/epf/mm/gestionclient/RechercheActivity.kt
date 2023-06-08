package fr.epf.mm.gestionclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.epf.mm.gestionclient.model.*
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RechercheActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView

    lateinit var nomFilmEdittext: EditText
    var adapter : FilmAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recherche)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val lancerRechercheButton = findViewById<Button>(R.id.lancer_recherche_button)
        recyclerView = findViewById<RecyclerView>(R.id.list_film_recyclerview)
        nomFilmEdittext = findViewById<EditText>(R.id.recherche_nom_film_edittext)

        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

        lancerRechercheButton.setOnClickListener {
            recupFilm(nomFilmEdittext.text)
        }

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

    fun recupFilm(nom: Editable) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/search/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var service = retrofit.create(FilmService::class.java)

            val call = service.getFilm(nom)
            call.enqueue( object : Callback<listeFilm> {
                override fun onResponse(call: Call<listeFilm>, response: Response<listeFilm>) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        adapter = result?.results?.let { FilmAdapter(it, this@RechercheActivity) }
                        Log.d("APIII", result.toString())
                    }
                }

                override fun onFailure(call: Call<listeFilm>, t: Throwable) {
                    Toast.makeText(this@RechercheActivity, "Erreur serveur", Toast.LENGTH_LONG).show()
                }
            })
        recyclerView.setAdapter(adapter)
    }
}


// val films = response.body()
//Log.d("API1", films)
// }
/*
val listeFilm = service.getFilm(nom).results.map {
    Log.d("APIII", "${it.original_title}")
//val listeFilm = FilmService.getFilm(nomFilmEdittext.text).results.map {
    Film(
        it.original_title,
        it.overview
    )
}*/

// recyclerView.adapter = FilmAdapter(listeFilm, this@RechercheActivity)