package pl.lejdi.gymdiaryserverversion.dependency

import dagger.Subcomponent
import pl.lejdi.gymdiaryserverversion.dependency.annotation.ApplicationScope
import pl.lejdi.gymdiaryserverversion.viewmodel.MainViewModel

@ApplicationScope
@Subcomponent(modules = [ViewModelModule::class])
interface ViewModelComponent {
    fun inject(mainViewModel: MainViewModel)
}