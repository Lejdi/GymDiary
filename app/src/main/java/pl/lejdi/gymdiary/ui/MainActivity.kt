package pl.lejdi.gymdiary.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import androidx.transition.Slide
import pl.lejdi.gymdiary.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.new_exercise -> {
                val editExerciseFragment = EditExerciseFragment()
                editExerciseFragment.enterTransition= Slide(Gravity.START)

                supportFragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.container, editExerciseFragment)
                    .commit()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
