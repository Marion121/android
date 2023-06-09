package fr.epf.mm.gestionFilm

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import me.dm7.barcodescanner.zxing.ZXingScannerView
import com.google.zxing.Result
import fr.epf.mm.gestionFilm.model.Film
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//private const val TAG = "AddClientActivity"

class ScannerActivity: AppCompatActivity(), SurfaceHolder.Callback, ZXingScannerView.ResultHandler {
    private lateinit var scannerView: ZXingScannerView
    private val CAMERA_PERMISSION_REQUEST_CODE = 123
    val ACCESS_TOKEN = " Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxZGVhMDM2ODIxYWUzMWMwN2YxOGE0MDE0ZmRiYWY1NiIsInN1YiI6IjY0N2YwZWRhY2Y0YjhiMDBhODc5MWQwMCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.782_AKlIv5ZYkuixffIheQZBmGZGcmPj8FfDKNj66w8"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sanner)
        scannerView = ZXingScannerView(this)
        setContentView(scannerView)
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            startCamera()
        }
    }

    override fun onPause() {
        super.onPause()
        scannerView.stopCamera()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            } else {
                Toast.makeText(
                    this,
                    "La permission de la caméra est nécessaire pour utiliser le scanner QR",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

override fun surfaceCreated(holder: SurfaceHolder) {
        scannerView.startCamera()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        scannerView.stopCamera()
    }

    override fun handleResult(rawResult: Result) {
        // Le résultat du scan de code QR est disponible ici
        val resultText : String = rawResult.text
        val QrCode : Int = resultText.toInt()
        Log.d("Qresponse", "${resultText}")
        Log.d("QR code", "${QrCode}")

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var service = retrofit.create(FilmService::class.java)

        val call = service.getFilmDetails(ACCESS_TOKEN, QrCode)
        call.enqueue( object : Callback<Film> {
            override fun onResponse(call: Call<Film>, response: Response<Film>) {

                if (response.isSuccessful) {
                    val result = response.body()
                    Log.d("APIII", result.toString())
                    val intent = Intent(this@ScannerActivity, DetailsFilmActivity::class.java)
                    intent.putExtra("film_id", result?.id)
                    intent.putExtra("film", result)
                    this@ScannerActivity.startActivity(intent)
                }
            }

            override fun onFailure(call: Call<Film>, t: Throwable) {
                Log.d("requete rate", "${t.message}")
                Toast.makeText(this@ScannerActivity, "Erreur serveur", Toast.LENGTH_LONG).show()
            }
        })

        scannerView.resumeCameraPreview(this)
    }

        private fun startCamera() {
            scannerView.setResultHandler(this)
            scannerView.startCamera()
        }
}


