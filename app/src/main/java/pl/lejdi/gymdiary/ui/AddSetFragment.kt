package pl.lejdi.gymdiary.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import pl.lejdi.gymdiary.R
import pl.lejdi.gymdiary.viewmodel.AddSetViewModel


class AddSetFragment : Fragment() {
    private lateinit var viewModel : AddSetViewModel

    private lateinit var exerciseNameField : AutoCompleteTextView
    private lateinit var exerciseDescriptionField : TextView
    private lateinit var chooseTypeField : Spinner
    private lateinit var weightField : EditText
    private lateinit var repetitionsField : EditText
    private lateinit var saveButton: FloatingActionButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val fragmentView = inflater.inflate(R.layout.add_set_fragment, container, false)
        exerciseNameField = fragmentView.findViewById(R.id.add_set_exercise_name)
        exerciseDescriptionField = fragmentView.findViewById(R.id.add_set_exercise_description)
        chooseTypeField = fragmentView.findViewById(R.id.add_set_exercise_type)
        weightField = fragmentView.findViewById(R.id.add_set_weight)
        repetitionsField = fragmentView.findViewById(R.id.add_set_repetitions)
        saveButton = fragmentView.findViewById(R.id.add_set_fab)
        return fragmentView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this).get(AddSetViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        setFabClickListener()
        getExercises()
        setSpinner()
        calculateSuggestedWeights()
        insertSuggestedReps()
    }

    private fun getExercises()
    {
        viewModel.retrieveExercises()
        exerciseNameField.setOnItemClickListener { _, _, _, _ -> setDescription() }
        exerciseNameField.threshold=1
        viewModel.exercises.observe(this, Observer {
            val names = viewModel.getExercisesNames()
            val adapter = ArrayAdapter(requireContext(),
                android.R.layout.simple_list_item_1, names)
            exerciseNameField.setAdapter(adapter)
        })
    }

    private fun setDescription()
    {
        viewModel.getExerciseDescription(exerciseNameField.text.toString())
        viewModel.description.observe(this, Observer {
            exerciseDescriptionField.text = it
        })
    }

    private fun setSpinner()
    {
        val types = arrayOf("Strength", "Hypertrophy", "Endurance")
        val adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_item, types)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        chooseTypeField.adapter = adapter


        val itemSelectedListener : OnItemSelectedListener = object: OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                calculateSuggestedWeights()
                insertSuggestedReps()
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                calculateSuggestedWeights()
                insertSuggestedReps()
            }

        }
        chooseTypeField.onItemSelectedListener = itemSelectedListener

    }

    private fun calculateSuggestedWeights()
    {
        viewModel.calculateSuggestedWeight(chooseTypeField.selectedItem.toString()).toString()
        viewModel.suggestedWeight.observe(this, Observer {
            weightField.setText(it.toString())
        })
    }

    private fun insertSuggestedReps()
    {
        viewModel.suggestedReps(chooseTypeField.selectedItem.toString()).toString()
        viewModel.suggestedReps.observe(this, Observer {
            repetitionsField.setText(it.toString())
        })
    }

    private fun setFabClickListener()
    {
        saveButton.setOnClickListener {
                Toast.makeText(activity,"Clicked", Toast.LENGTH_SHORT).show()
        }
    }
}