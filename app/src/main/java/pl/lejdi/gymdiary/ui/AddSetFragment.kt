package pl.lejdi.gymdiary.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.transition.Slide
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

    private var trainingID : Int = 0

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

        if(arguments != null)
        {
            trainingID = arguments?.getInt("trainingID")!!
        }

        setFabClickListener()
        getExercises()
        setSpinner()
        viewModel.suggestedWeight.observe(this, Observer {
            weightField.setText(it.toString())
        })
        viewModel.suggestedReps.observe(this, Observer {
            repetitionsField.setText(it.toString())
        })
    }

    private fun getExercises()
    {
        viewModel.retrieveExercises()
        exerciseNameField.setOnItemClickListener { _, _, _, _ ->
            run {
            setDescription()
            calculateSuggestedWeights()
            insertSuggestedReps()
            }
        exerciseNameField.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                exerciseDescriptionField.text = ""
            }

        })
        }
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
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (exerciseDescriptionField.text.isNotEmpty())
                {
                    calculateSuggestedWeights()
                    insertSuggestedReps()
                }
            }

        }
        chooseTypeField.onItemSelectedListener = itemSelectedListener

    }

    private fun calculateSuggestedWeights()
    {
        viewModel.calculateSuggestedWeight(chooseTypeField.selectedItem.toString(), exerciseNameField.text.toString()).toString()

    }

    private fun insertSuggestedReps()
    {
        viewModel.suggestedReps(chooseTypeField.selectedItem.toString()).toString()
    }

    private fun setFabClickListener()
    {
        saveButton.setOnClickListener {
            if(exerciseDescriptionField.text.isEmpty())
                Toast.makeText(activity,"Please, fill all the required fields", Toast.LENGTH_SHORT).show()
            else{
                if(viewModel.saveSet(trainingID,exerciseNameField.text.toString(),weightField.text.toString(),repetitionsField.text.toString(), chooseTypeField.selectedItem.toString()))
                {
                    val trainingDetailsFragment = TrainingDetailsFragment()
                    trainingDetailsFragment.enterTransition= Slide(Gravity.START)

                    val bundle = Bundle()
                    bundle.putInt("trainingID", trainingID)
                    trainingDetailsFragment.arguments=bundle

                    activity?.supportFragmentManager!!.popBackStack()
                }
                else{
                    Toast.makeText(activity,"Please, fill all the required fields", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}