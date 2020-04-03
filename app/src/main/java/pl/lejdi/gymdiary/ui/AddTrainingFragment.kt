package pl.lejdi.gymdiary.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import pl.lejdi.gymdiary.R
import pl.lejdi.gymdiary.viewmodel.AddTrainingViewModel

class AddTrainingFragment : Fragment() {
    private lateinit var viewModel : AddTrainingViewModel

    private lateinit var dateField : EditText
    private lateinit var descriptionField : EditText
    private lateinit var saveButton: FloatingActionButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val fragmentView = inflater.inflate(R.layout.new_training_fragment, container, false)
        dateField = fragmentView.findViewById(R.id.new_training_date)
        descriptionField = fragmentView.findViewById(R.id.new_training_description)
        saveButton = fragmentView.findViewById(R.id.new_training_fab)
        return fragmentView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProviders.of(this).get(AddTrainingViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        setDefaultDate()
        setFabClickListener()
    }

    private fun setDefaultDate()
    {
        viewModel.getCurrentDate().observe(this, Observer {
            dateField.setText(it)
        })
    }

    private fun setFabClickListener()
    {
        saveButton.setOnClickListener {
            if(viewModel.saveNewTraining(dateField.text.toString(), descriptionField.text.toString())){
                Toast.makeText(activity,"Success", Toast.LENGTH_SHORT).show()
                TODO("NAVIGATE TO TRAININGS LIST")
            }
            else {
                Toast.makeText(activity,"Enter date and description", Toast.LENGTH_SHORT).show()
                TODO("DIALOG INSTEAD OF TOAST")
            }
        }
    }
}