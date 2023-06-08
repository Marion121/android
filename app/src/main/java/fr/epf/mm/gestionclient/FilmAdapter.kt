package fr.epf.mm.gestionclient

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.squareup.picasso.Picasso
import fr.epf.mm.gestionclient.model.Film

class FilmViewHolder(val view: View) : ViewHolder(view)


class FilmAdapter(val listFilmRecup: List<Film>, val context: Context) : RecyclerView.Adapter<FilmViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.film_view, parent, false)
        return FilmViewHolder(view)
    }


    override fun getItemCount() = listFilmRecup.size

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val film : Film = listFilmRecup[position]
        val imageFilm = film.poster_path
        val view = holder.itemView

        val titreView = view.findViewById<TextView>(R.id.film_view_titre_textview)
        titreView.text = film.original_title


        val imageView = view.findViewById<ImageView>(R.id.film_view_imageview)
        val posterUrl = "https://image.tmdb.org/t/p/original" + imageFilm
        Picasso.get().load(posterUrl).into(imageView);
        //imageView.setImageResource(R.drawable.imageFilm)

        val cardView = view.findViewById<CardView>(R.id.film_view_cardview)
        cardView.setOnClickListener {
            val intent = Intent(context, DetailsFilmActivity::class.java)
            intent.putExtra("film_id", position)
            intent.putExtra("film", film)
            context.startActivity(intent)
        }
    }

    override fun toString(): String {
        return "FilmAdapter(context=$context, listFilmRecup=$listFilmRecup, )"
    }


}
