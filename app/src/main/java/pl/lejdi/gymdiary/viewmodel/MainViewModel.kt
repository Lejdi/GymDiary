package pl.lejdi.gymdiary.viewmodel

import pl.lejdi.gymdiary.GymDiaryApplication
import pl.lejdi.gymdiary.database.repository.GymRepository
import javax.inject.Inject

class MainViewModel {
    @Inject
    lateinit var repo : GymRepository
    init {
        GymDiaryApplication.component.inject(this)
        repo.getAllExercises()
    }
}