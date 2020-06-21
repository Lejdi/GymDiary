package pl.lejdi.gymdiary.ui

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.transition.Slide
import pl.lejdi.gymdiary.databinding.NewTrainingFragmentBinding
import pl.lejdi.gymdiary.viewmodel.AddTrainingViewModel

class AddTrainingFragment : Fragment() {
    private lateinit var viewModel : AddTrainingViewModel

    private lateinit var binding: NewTrainingFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = NewTrainingFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this).get(AddTrainingViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        setDefaultDate()
        setFabClickListener()
    }

    private fun setDefaultDate()
    {
        viewModel.getCurrentDate().observe(this, Observer {
            binding.newTrainingDate.setText(it)
        })
    }

    private fun setFabClickListener()
    {
        binding.newTrainingFab.setOnClickListener {
            if(viewModel.saveNewTraining(binding.newTrainingDate.text.toString(), binding.newTrainingDescription.text.toString())){
                val trainingListFragment = TrainingListFragment()
                trainingListFragment.enterTransition= Slide(Gravity.START)

                activity?.supportFragmentManager!!.popBackStack()
            }
            else Toast.makeText(activity,"Please, fill all the required fields", Toast.LENGTH_SHORT).show()
        }
    }
}