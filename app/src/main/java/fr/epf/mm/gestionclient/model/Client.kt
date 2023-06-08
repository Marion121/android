package fr.epf.mm.gestionclient.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


enum class Gender {
    MAN, WOMAN
}

/**
 *
 * Doc: https://developer.android.com/kotlin/parcelize?hl=fr
 *
 */
@Parcelize
data class Client(
    val lastName: String,
    val firstName: String,
    val gender: Gender
) : Parcelable {
    companion object {

        fun generate(size: Int = 20) = (1..size).map {
            val gender = if (it % 3 == 0) Gender.MAN else Gender.WOMAN
            Client("Nom${it}", "Prenom${it}", gender)
        }
    }
}
