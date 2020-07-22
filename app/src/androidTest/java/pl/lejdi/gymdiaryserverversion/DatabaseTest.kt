package pl.lejdi.gymdiaryserverversion

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import pl.lejdi.gymdiaryserverversion.database.GymDatabase
import pl.lejdi.gymdiaryserverversion.database.dao.ExerciseDAO
import pl.lejdi.gymdiaryserverversion.database.dao.SetDAO
import pl.lejdi.gymdiaryserverversion.database.dao.TrainingDAO
import pl.lejdi.gymdiaryserverversion.database.model.Exercise
import java.io.IOException

@RunWith(AndroidJUnit4ClassRunner::class)
class DatabaseTest
{
    private lateinit var exerciseDAO : ExerciseDAO
    private lateinit var setDAO : SetDAO
    private lateinit var trainingDAO : TrainingDAO
    private lateinit var db: GymDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, GymDatabase::class.java).build()
        exerciseDAO = db.getExerciseDAO()
        setDAO = db.getSetDAO()
        trainingDAO = db.getTrainingDAO()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun save_and_load_exercise() {
        val exerciseName = "ex1"
        val exerciseToSave = Exercise(exerciseName, "ex1", 1.0f, 0)
        exerciseDAO.insertExercise(exerciseToSave)

        val exerciseFromDB = exerciseDAO.getExercise(exerciseName)

        assertThat(exerciseFromDB.description, equalTo(exerciseToSave.description))
        assertThat(exerciseFromDB.name, equalTo(exerciseToSave.name))
        assertThat(exerciseFromDB.RM, equalTo(exerciseToSave.RM))
        assertThat(exerciseFromDB.isRMAuto, equalTo(exerciseToSave.isRMAuto))
    }

    @Test
    @Throws(Exception::class)
    fun delete_exercise() {
        val exerciseName = "ex1"
        val exercise = Exercise(exerciseName, "ex1", 1.0f, 0)
        exerciseDAO.insertExercise(exercise)

        assert(exerciseDAO.getAllExercises().contains(exercise))

        exerciseDAO.deleteExercise(exercise)

        assert(!exerciseDAO.getAllExercises().contains(exercise))
    }
}