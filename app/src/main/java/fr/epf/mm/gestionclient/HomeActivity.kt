package fr.epf.mm.gestionclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val rechercheButton = findViewById<Button>(R.id.home_recherche_button)
        val scannerButton = findViewById<Button>(R.id.home_scanner_button)
        val favorieButton = findViewById<Button>(R.id.home_favorie_button)

        rechercheButton.setOnClickListener {
            val intent = Intent(this, RechercheActivity::class.java)
            startActivity(intent)
        }

        scannerButton .setOnClickListener {
            val intent = Intent(this, ScannerActivity::class.java)
            startActivity(intent)
        }

        favorieButton.setOnClickListener {
            val intent = Intent(this, ListeFavActivity::class.java)
            startActivity(intent)
        }
    }

}