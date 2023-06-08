package fr.epf.mm.gestionclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast


private const val TAG = "AddClientActivity"

class ScannerActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sanner)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        Log.d(TAG, "onCreate() called with: savedInstanceState = $savedInstanceState")
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
        }


    }
}