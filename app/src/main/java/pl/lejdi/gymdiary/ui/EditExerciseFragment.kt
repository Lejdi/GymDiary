package pl.lejdi.gymdiary.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import pl.lejdi.gymdiary.databinding.ExerciseDetailsFragmentBinding
import pl.lejdi.gymdiary.viewmodel.EditExerciseViewModel

class EditExerciseFragment : Fragment() {
    private lateinit var viewModel : EditExerciseViewModel
    private lateinit var binding: ExerciseDetailsFragmentBinding

    private var exerciseExists : Boolean = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ExerciseDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this).get(EditExerciseViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()

        if(arguments != null)
        {
            val exerciseName : String = arguments?.getString("exerciseName")!!
            viewModel.retrieveExercise(exerciseName)
            viewModel.exercise.observe(this, Observer {
                binding.exerciseDetailsNameExists.visibility = View.VISIBLE
                binding.exerciseDetailsNameNotexists.visibility = View.GONE
                binding.exerciseDetailsNameExists.text = it.name
                binding.exerciseDetailsDescription.setText(it.description)
                binding.RMCheckbox.isChecked =it.isRMAuto == 1
                binding.RMEdittext.setText(it.RM.toString())
            })
        }
        else
        {
            binding.exerciseDetailsNameExists.visibility = View.GONE
            binding.exerciseDetailsNameNotexists.visibility = View.VISIBLE
            exerciseExists = false
        }
        setInfoButtonListener()
        setFabClickListener()
    }

    private fun setFabClickListener()
    {
        binding.exerciseDetailsFab.setOnClickListener {
            if(exerciseExists)
            {
                if(viewModel.updateExercise(
                        binding.exerciseDetailsNameExists.text.toString(),
                        binding.exerciseDetailsDescription.text.toString(),
                        binding.RMCheckbox.isChecked,
                        binding.RMEdittext.text.toString() ))
                {
                    activity?.supportFragmentManager!!.popBackStack()
                }
                else{
                    Toast.makeText(activity,"Please, fill all the required fields", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                if(viewModel.saveExercise(
                        binding.exerciseDetailsNameNotexists.text.toString(),
                        binding.exerciseDetailsDescription.text.toString(),
                        binding.RMCheckbox.isChecked,
                        binding.RMEdittext.text.toString() ))
                {
                    activity?.supportFragmentManager!!.popBackStack()
                }
                else{
                    Toast.makeText(activity,"Please, fill all the required fields", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setInfoButtonListener()
    {
        binding.whatIsRM.setOnClickListener {
            Toast.makeText(activity,"1RM is the maximum weight that theoretically you can lift with this exercise in one set and one repetition. It can be calculated based on your repetitions or checked experimentally.", Toast.LENGTH_SHORT).show()
        }
    }
}