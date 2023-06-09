package fr.epf.mm.gestionclient

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Camera
import android.hardware.camera2.CameraManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.ViewGroup
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.BarcodeFormat
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import me.dm7.barcodescanner.zxing.ZXingScannerView
import com.google.zxing.Result



//private const val TAG = "AddClientActivity"

class ScannerActivity: AppCompatActivity(), SurfaceHolder.Callback, ZXingScannerView.ResultHandler {
    private lateinit var scannerView: ZXingScannerView
    private lateinit var surfaceView: SurfaceView

    private val CAMERA_PERMISSION_REQUEST_CODE = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sanner)
        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraIds = cameraManager.cameraIdList
        val numberOfCameras = cameraIds.size
        Log.d("cam", "${numberOfCameras}")
        surfaceView = findViewById(R.id.surface_view)
        scannerView = ZXingScannerView(this)
        scannerView.setAutoFocus(true)
        scannerView.setFormats(listOf(BarcodeFormat.QR_CODE))
        surfaceView.holder.addCallback(this)
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
            startCamera()
        } else {
            startCamera()
        }
    }

    override fun onPause() {
        super.onPause()
        scannerView.stopCamera()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
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

    override fun handleResult(rawResult: Result?) {
        // Le résultat du scan de code QR est disponible ici
        val resultText : String? = rawResult?.text
        // Faites quelque chose avec le résultat (par exemple, affichage dans un TextView)
        // textView.text = resultText
        // Redémarrez le scanner pour continuer à scanner d'autres codes
        scannerView.resumeCameraPreview(this)
    }

    private fun startCamera() {
        scannerView.setResultHandler(this)
        scannerView.startCamera()
        val holder = surfaceView.holder
        holder.addCallback(this)
    }

}
       /* Log.d(TAG, "onCreate() called with: savedInstanceState = $savedInstanceState")
        val addButton = findViewById<Button>(R.id.add_client_add_button)


        val genderRadioGroup = findViewById<RadioGroup>(R.id.add_client_gender_radiogroup)
        genderRadioGroup.check(R.id.client_gender_woman_radiobutton)
        val ageTextview = findViewById<TextView>(R.id.add_client_age_textview)
        val ageSeekbar = findViewById<SeekBar>(R.id.add_client_age_seekbar)

        ageSeekbar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
               ageTextview.text = progress.toString()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) = Unit
            override fun onStopTrackingTouch(p0: SeekBar?) = Unit

        })


        addButton.setOnClickListener{
            val gender =
                if(genderRadioGroup.checkedRadioButtonId == R.id.client_gender_woman_radiobutton) "Femme" else "Homme"
            Log.d("EPF", "Genre : ${gender}")
            Toast.makeText(this, "Client ajouté", Toast.LENGTH_SHORT).show()


            finish()
        }*/

