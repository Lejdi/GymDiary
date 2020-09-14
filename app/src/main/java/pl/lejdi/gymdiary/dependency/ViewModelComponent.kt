package pl.lejdi.gymdiary.dependency

import dagger.Subcomponent
import pl.lejdi.gymdiary.dependency.annotation.ApplicationScope
import pl.lejdi.gymdiary.viewmodel.MainViewModel

@ApplicationScope
@Subcomponent(modules = [ViewModelModule::class])
interface ViewModelComponent {
    fun inject(mainViewModel: MainViewModel)
}