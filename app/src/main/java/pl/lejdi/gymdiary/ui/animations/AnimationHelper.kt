package pl.lejdi.gymdiary.ui.animations

import pl.lejdi.gymdiary.util.Fragments

class AnimationHelper {
    companion object{
        var previousFragment = Fragments.TRAINING_LIST
        var chosenTrainingPosition = 0
        var chosenSetPosition = 0
        var chosenExercisePosition = 0
        var exDetailsFromExList_isNew = false
    }
}