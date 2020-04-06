package pl.lejdi.gymdiary.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import pl.lejdi.gymdiary.R
import pl.lejdi.gymdiary.database.model.Exercise
import pl.lejdi.gymdiary.viewmodel.EditExerciseViewModel

class EditExerciseFragment : Fragment() {
    private lateinit var viewModel : EditExerciseViewModel

    private lateinit var exerciseNameNotExists : EditText
    private lateinit var exerciseNameExists : TextView
    private lateinit var exerciseDescription : EditText
    private lateinit var autoRMCheckbox : CheckBox
    private lateinit var RM : EditText
    private lateinit var whatIsRM : ImageButton
    private lateinit var saveButton: FloatingActionButton

    private var exerciseExists : Boolean = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val fragmentView = inflater.inflate(R.layout.exercise_details_fragment, container, false)
        exerciseNameExists = fragmentView.findViewById(R.id.exercise_details_name_exists)
        exerciseNameNotExists = fragmentView.findViewById(R.id.exercise_details_name_notexists)
        exerciseDescription = fragmentView.findViewById(R.id.exercise_details_description)
        autoRMCheckbox = fragmentView.findViewById(R.id.RM_checkbox)
        RM = fragmentView.findViewById(R.id.RM_edittext)
        whatIsRM = fragmentView.findViewById(R.id.what_is_RM)
        saveButton = fragmentView.findViewById(R.id.exercise_details_fab)
        return fragmentView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this).get(EditExerciseViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()

        if(arguments != null)
        {
            val exercise : Exercise = arguments?.getParcelable("exerciseData")!!
            exerciseNameExists.visibility = View.VISIBLE
            exerciseNameNotExists.visibility = View.GONE
            exerciseNameExists.text = exercise.name
            exerciseDescription.setText(exercise.description)
            autoRMCheckbox.isChecked = exercise.isRMAuto == 1
            RM.setText(exercise.RM.toString())
        }
        else
        {
            exerciseNameExists.visibility = View.GONE
            exerciseNameNotExists.visibility = View.VISIBLE
            exerciseExists = false
        }
        setInfoButtonListener()
        setFabClickListener()
    }

    private fun setFabClickListener()
    {
        saveButton.setOnClickListener {
            if(exerciseExists)
            {
                if(viewModel.updateExercise(exerciseNameExists.text.toString(), exerciseDescription.text.toString(), autoRMCheckbox.isChecked, RM.text.toString() ))
                {
                    Toast.makeText(activity,"Success", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(activity,"Please, fill all the required fields", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                if(viewModel.saveExercise(exerciseNameNotExists.text.toString(), exerciseDescription.text.toString(), autoRMCheckbox.isChecked, RM.text.toString() ))
                {
                    Toast.makeText(activity,"Success", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(activity,"Please, fill all the required fields", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setInfoButtonListener()
    {
        whatIsRM.setOnClickListener {
            Toast.makeText(activity,"1RM is the maximum weight that theoretically you can lift with this exercise in one set and one repetition. It can be calculated based on your repetitions or checked experimentally.", Toast.LENGTH_SHORT).show()
        }
    }
}