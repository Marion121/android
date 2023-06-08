package fr.epf.mm.gestionclient

import fr.epf.mm.gestionclient.model.Client
import fr.epf.mm.gestionclient.model.Gender

fun Client.getImage() = when (this.gender) {
    Gender.MAN -> R.drawable.man
    Gender.WOMAN -> R.drawable.woman
}

val Client.fullName
    get() = "${firstName} ${lastName}"


