package pl.lejdi.gymdiaryserverversion.dependency

import dagger.Subcomponent
import pl.lejdi.gymdiaryserverversion.database.repository.GymRepository
import pl.lejdi.gymdiaryserverversion.dependency.annotation.ApplicationScope

@ApplicationScope
@Subcomponent(modules = [RepositoryModule::class])
interface RepositoryComponent {
    fun inject(gymRepository: GymRepository)
}