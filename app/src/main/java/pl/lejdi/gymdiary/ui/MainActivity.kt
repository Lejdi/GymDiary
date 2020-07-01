package pl.lejdi.gymdiary.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import androidx.transition.Slide
import pl.lejdi.gymdiary.R
import pl.lejdi.gymdiary.databinding.ActivityMainBinding
import pl.lejdi.gymdiary.ui.animations.AnimationHelper
import pl.lejdi.gymdiary.util.Fragments

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val trainingListFragment = TrainingListFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, trainingListFragment)
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.exercise_list_menu_button -> {
                if(AnimationHelper.previousFragment != Fragments.EXERCISE_EDIT){
                    val exerciseListFragment = ExerciseListFragment()
                    val slide = Slide(Gravity.TOP)
                    slide.duration = 500
                    exerciseListFragment.enterTransition = slide

                    supportFragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.container, exerciseListFragment)
                        .commit()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
