package pl.lejdi.gymdiary.dependency

import dagger.Subcomponent
import pl.lejdi.gymdiary.viewmodel.MainViewModel

@Subcomponent(modules = [ViewModelModule::class])
interface ViewModelComponent {
    fun inject(mainViewModel: MainViewModel)
}