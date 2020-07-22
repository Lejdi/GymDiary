package pl.lejdi.gymdiaryserverversion.viewmodel

import androidx.lifecycle.ViewModel
import pl.lejdi.gymdiaryserverversion.GymDiaryApplication
import pl.lejdi.gymdiaryserverversion.database.repository.GymRepository
import pl.lejdi.gymdiaryserverversion.dependency.ViewModelModule
import javax.inject.Inject

open class MainViewModel : ViewModel() {
    @Inject
    lateinit var repo : GymRepository //MainViewModel's only purpose is providing repo to it's children
    init {
        GymDiaryApplication.component.viewModelComponent(ViewModelModule()).inject(this)
    }
}