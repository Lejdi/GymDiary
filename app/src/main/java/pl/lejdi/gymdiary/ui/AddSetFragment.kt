package pl.lejdi.gymdiary.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import pl.lejdi.gymdiary.R
import pl.lejdi.gymdiary.databinding.AddSetFragmentBinding
import pl.lejdi.gymdiary.viewmodel.AddSetViewModel


class AddSetFragment : Fragment() {
    private lateinit var viewModel : AddSetViewModel

    private lateinit var binding: AddSetFragmentBinding

    private var trainingID : Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = AddSetFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this).get(AddSetViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()

        if(arguments != null)
        {
            trainingID = arguments?.getInt(getString(R.string.KEY_TRAINING_ID))!!
        }

        setFabClickListener()
        getExercises()
        setSpinner()
        viewModel.suggestedWeight.observe(this, Observer {
            binding.addSetWeight.setText(it.toString())
        })
        viewModel.suggestedReps.observe(this, Observer {
            binding.addSetRepetitions.setText(it.toString())
        })
        viewModel.repsIsEmpty.observe(this, Observer {
            if(it){
                binding.addSetRepetitions.setBackgroundResource(R.drawable.text_background_warning)
            }
            else{
                binding.addSetRepetitions.setBackgroundResource(R.drawable.text_background)
            }
        })
        viewModel.weightIsEmpty.observe(this, Observer {
            if(it){
                binding.addSetWeight.setBackgroundResource(R.drawable.text_background_warning)
            }
            else{
                binding.addSetWeight.setBackgroundResource(R.drawable.text_background)
            }
        })
        viewModel.exerciseNameIsEmpty.observe(this, Observer {
            if(it){
                binding.addSetExerciseName.setBackgroundResource(R.drawable.text_background_warning)
            }
            else{
                binding.addSetExerciseName.setBackgroundResource(R.drawable.text_background)
            }
        })
    }

    private fun getExercises()
    {
        viewModel.retrieveExercises()
        binding.addSetExerciseName.setOnItemClickListener { _, _, _, _ ->
            run {
            setDescription()
            calculateSuggestedWeights()
            insertSuggestedReps()
            }
        binding.addSetExerciseName.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.addSetExerciseDescription.text = ""
            }

        })
        }
        binding.addSetExerciseName.threshold=1
        viewModel.exercises.observe(this, Observer {
            val names = viewModel.getExercisesNames()
            val adapter = ArrayAdapter(requireContext(),
                R.layout.dropdown_item, names)
            binding.addSetExerciseName.setAdapter(adapter)
        })
    }

    private fun setDescription()
    {
        viewModel.getExerciseDescription(binding.addSetExerciseName.text.toString())
        viewModel.description.observe(this, Observer {
            binding.addSetExerciseDescription.text = it
        })
    }

    private fun setSpinner()
    {
        val types = arrayOf(
            resources.getStringArray(R.array.types)[0],
            resources.getStringArray(R.array.types)[1],
            resources.getStringArray(R.array.types)[2])
        val adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_item, types)
        adapter.setDropDownViewResource(R.layout.dropdown_item)
        binding.addSetExerciseType.adapter = adapter


        val itemSelectedListener : OnItemSelectedListener = object: OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (binding.addSetExerciseDescription.text.isNotEmpty())
                {
                    calculateSuggestedWeights()
                    insertSuggestedReps()
                }
            }

        }
        binding.addSetExerciseType.onItemSelectedListener = itemSelectedListener

    }

    private fun calculateSuggestedWeights()
    {
        viewModel.calculateSuggestedWeight(binding.addSetExerciseType.selectedItem.toString(), binding.addSetExerciseName.text.toString()).toString()

    }

    private fun insertSuggestedReps()
    {
        viewModel.suggestedReps(binding.addSetExerciseType.selectedItem.toString()).toString()
    }

    private fun setFabClickListener()
    {
        binding.addSetFab.setOnClickListener {
            if(binding.addSetExerciseDescription.text.isEmpty()){
                Toast.makeText(activity, getString(R.string.Fill_all_fiels), Toast.LENGTH_SHORT).show()
                viewModel.exerciseNameIsEmpty.value = true
            }
            else{
                if(viewModel.saveSet(
                        trainingID,binding.addSetExerciseName.text.toString(),
                        binding.addSetWeight.text.toString(),
                        binding.addSetRepetitions.text.toString(),
                        binding.addSetExerciseType.selectedItem.toString()))
                {
                    val trainingDetailsFragment = TrainingDetailsFragment()
                    trainingDetailsFragment.enterTransition= Slide(Gravity.START)

                    val bundle = Bundle()
                    bundle.putInt(getString(R.string.KEY_TRAINING_ID), trainingID)
                    trainingDetailsFragment.arguments=bundle

                    activity?.supportFragmentManager!!.popBackStack()
                }
                else{
                    Toast.makeText(activity,getString(R.string.Fill_all_fiels), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}