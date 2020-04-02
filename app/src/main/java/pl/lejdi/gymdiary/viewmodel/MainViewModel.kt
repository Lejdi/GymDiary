package pl.lejdi.gymdiary.viewmodel

import android.content.Context
import pl.lejdi.gymdiary.database.repository.GymRepository

class MainViewModel(context: Context) {
    init {
        val repo = GymRepository(context)
        repo.getAllExercises()
    }
}