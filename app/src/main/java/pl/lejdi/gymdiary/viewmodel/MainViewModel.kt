package pl.lejdi.gymdiary.viewmodel

import pl.lejdi.gymdiary.GymDiaryApplication
import pl.lejdi.gymdiary.database.repository.GymRepository
import pl.lejdi.gymdiary.dependency.ViewModelModule
import javax.inject.Inject

class MainViewModel {
    @Inject
    lateinit var repo : GymRepository
    init {
        GymDiaryApplication.component.viewModelComponent(ViewModelModule()).inject(this)
    }
}