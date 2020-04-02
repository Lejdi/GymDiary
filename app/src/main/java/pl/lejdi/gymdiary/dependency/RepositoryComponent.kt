package pl.lejdi.gymdiary.dependency

import dagger.Subcomponent
import pl.lejdi.gymdiary.database.repository.GymRepository

@Subcomponent(modules = [RepositoryModule::class])
interface RepositoryComponent {
    fun inject(gymRepository: GymRepository)
}