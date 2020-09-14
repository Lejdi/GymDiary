package pl.lejdi.gymdiary.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import pl.lejdi.gymdiary.R
import pl.lejdi.gymdiary.databinding.FragmentAddSetBinding
import pl.lejdi.gymdiary.ui.animations.AnimationHelper
import pl.lejdi.gymdiary.util.Constants
import pl.lejdi.gymdiary.util.Fragments
import pl.lejdi.gymdiary.viewmodel.AddSetViewModel


class AddSetFragment : Fragment() {
    private lateinit var viewModel : AddSetViewModel

    private lateinit var binding: FragmentAddSetBinding

    private var trainingID : Int = 0 //foreign key for new set

    //inflate view
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddSetBinding.inflate(inflater, container, false)
        return binding.root
    }

    //initialize viewmodel
    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this).get(AddSetViewModel::class.java)
        viewModel.init(context)
    }

    override fun onStart() {
        super.onStart()
        AnimationHelper.previousFragment = Fragments.SET_ADD //update application state
        if(arguments != null) {
            trainingID = arguments?.getInt(Constants.KEY_TRAINING_ID)!! //get foreign key from args
        }

        setFabClickListener()
        getExercises()
        setSpinner()
        observeData()
    }

    //observe changes in viewmodel
    private fun observeData() {
        //suggested weight and reps calculated from provided exercise statistics
        viewModel.suggestedWeight.observe(this, Observer {
            binding.txtAddsetWeight.setText(it.toString())
        })
        viewModel.suggestedReps.observe(this, Observer {
            binding.txtAddsetReps.setText(it.toString())
        })
        //displaying red gradients when data is missing in fields
        viewModel.repsIsEmpty.observe(this, Observer {
            if (it) {
                binding.txtAddsetReps.setBackgroundResource(R.drawable.background_text_grey_warn)
            } else {
                binding.txtAddsetReps.setBackgroundResource(R.drawable.background_text_grey)
            }
        })
        viewModel.weightIsEmpty.observe(this, Observer {
            if (it) {
                binding.txtAddsetWeight.setBackgroundResource(R.drawable.background_text_grey_warn)
            } else {
                binding.txtAddsetWeight.setBackgroundResource(R.drawable.background_text_grey)
            }
        })
        viewModel.exerciseNameIsEmpty.observe(this, Observer {
            if (it) {
                binding.txtAddsetExercisename.setBackgroundResource(R.drawable.background_text_grey_warn)
            } else {
                binding.txtAddsetExercisename.setBackgroundResource(R.drawable.background_text_grey)
            }
        })
    }

    //get a list of exercises in a dropdown
    private fun getExercises() {
        viewModel.retrieveExercises() //get all exercises
        binding.txtAddsetExercisename.setOnItemClickListener { _, _, _, _ -> //on item click update other data
            run {
            setDescription()
            calculateSuggestedWeights()
            insertSuggestedReps()
            }
        binding.txtAddsetExercisename.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.txtAddsetExercisedescription.text = "" //cleanup description if something changes in chosen exercise
            }

        })
        }
        //update suggested exercises every changed letter
        binding.txtAddsetExercisename.threshold=1
        viewModel.exercises.observe(this, Observer {
            val names = viewModel.getExercisesNames()
            val adapter = ArrayAdapter(requireContext(),
                R.layout.view_dropdown_item, names)
            binding.txtAddsetExercisename.setAdapter(adapter)
        })
    }

    //setting the description of chosen exercise
    private fun setDescription() {
        viewModel.getExerciseDescription(binding.txtAddsetExercisename.text.toString())
        viewModel.description.observe(this, Observer {
            binding.txtAddsetExercisedescription.text = it
        })
    }

    //setting items of a spinner
    private fun setSpinner() {
        val types = arrayOf(
            resources.getStringArray(R.array.types)[0],
            resources.getStringArray(R.array.types)[1],
            resources.getStringArray(R.array.types)[2])
        val adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_item, types)
        adapter.setDropDownViewResource(R.layout.view_dropdown_item) //UI of dropdown item
        binding.spinnerAddsetExercisetype.adapter = adapter


        val itemSelectedListener : OnItemSelectedListener = object: OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (binding.txtAddsetExercisedescription.text.isNotEmpty())
                {
                    calculateSuggestedWeights()
                    insertSuggestedReps()
                }
            }

        }
        binding.spinnerAddsetExercisetype.onItemSelectedListener = itemSelectedListener

    }

    private fun calculateSuggestedWeights() {
        viewModel.calculateSuggestedWeight(binding.spinnerAddsetExercisetype.selectedItem.toString(), binding.txtAddsetExercisename.text.toString()).toString()
    }

    private fun insertSuggestedReps() {
        viewModel.suggestedReps(binding.spinnerAddsetExercisetype.selectedItem.toString()).toString()
    }

    //handle clicking FAB
    private fun setFabClickListener() {
        binding.btnAddsetSave.setOnClickListener {
            //in some specific cases you can bypass checks and cause an error - additional check here
            if(binding.txtAddsetExercisedescription.text.isEmpty()){
                Toast.makeText(activity, getString(R.string.Fill_all_fiels), Toast.LENGTH_SHORT).show()
                viewModel.exerciseNameIsEmpty.value = true
            }
            else{
                //if save successful, go back to the list
                if(viewModel.saveSet(
                        trainingID,binding.txtAddsetExercisename.text.toString(),
                        binding.txtAddsetWeight.text.toString(),
                        binding.txtAddsetReps.text.toString()
                    )
                ) {
                    activity?.supportFragmentManager!!.popBackStack()
                }
                else{
                    Toast.makeText(activity,getString(R.string.Fill_all_fiels), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}