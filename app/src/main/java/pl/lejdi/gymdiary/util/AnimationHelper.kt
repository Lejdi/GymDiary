package pl.lejdi.gymdiary.util

import androidx.fragment.app.Fragment
import pl.lejdi.gymdiary.ui.TrainingListFragment
import kotlin.reflect.KClass

class AnimationHelper {
    companion object{
        var previousFragment = Fragments.TRAINING_LIST
        var chosenTrainingPosition = 0
        var chosenSetPosition = 0
        var chosenExercisePosition = 0
    }
}