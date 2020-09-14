package pl.lejdi.gymdiary.dependency

import dagger.Subcomponent
import pl.lejdi.gymdiary.database.repository.GymRepository
import pl.lejdi.gymdiary.dependency.annotation.ApplicationScope

@ApplicationScope
@Subcomponent(modules = [RepositoryModule::class])
interface RepositoryComponent {
    fun inject(gymRepository: GymRepository)
}