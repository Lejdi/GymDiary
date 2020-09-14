package pl.lejdi.gymdiary.ui

import android.content.Context
import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import pl.lejdi.gymdiary.R
import pl.lejdi.gymdiary.databinding.FragmentExerciseEditBinding
import pl.lejdi.gymdiary.ui.animations.AnimationHelper
import pl.lejdi.gymdiary.util.Constants
import pl.lejdi.gymdiary.util.Fragments
import pl.lejdi.gymdiary.viewmodel.EditExerciseViewModel

class EditExerciseFragment : Fragment() {
    private lateinit var viewModel : EditExerciseViewModel
    private lateinit var binding: FragmentExerciseEditBinding

    private var exerciseExists : Boolean = true //state of this fragment - is exercise being edited or new one is created

    //inflate view
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentExerciseEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    //init viewmodel
    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this).get(EditExerciseViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        AnimationHelper.previousFragment = Fragments.EXERCISE_EDIT //update app state
        //if exercise exists - get it's data and update UI
        if(arguments != null) {
            val exerciseName : String = arguments?.getString(Constants.KEY_EXERCISE_NAME)!!
            viewModel.retrieveExercise(exerciseName)
            viewModel.exercise.observe(this, Observer {
                binding.txtExercisedetailsName.visibility = View.VISIBLE
                binding.txtExercisedetailsNameEmpty.visibility = View.GONE
                binding.txtExercisedetailsName.text = it.name
                binding.txtExercisedetailsDescription.setText(it.description)
                binding.checkboxExercisedetailsRm.isChecked =it.isRMAuto == 1
                binding.txtExercisedetailsRm.setText(it.RM.toString())
            })
        }
        //else, display other views
        else {
            binding.txtExercisedetailsName.visibility = View.GONE
            binding.txtExercisedetailsNameEmpty.visibility = View.VISIBLE
            exerciseExists = false
        }
        setInfoButtonListener()
        setFabClickListener()
        observeData()
    }

    //display red gradients where the data is missing
    private fun observeData() {
        viewModel.descriptionIsEmpty.observe(this, Observer {
            if (it) {
                binding.txtExercisedetailsDescription.setBackgroundResource(R.drawable.background_text_grey_warn)
            } else {
                binding.txtExercisedetailsDescription.setBackgroundResource(R.drawable.background_text_grey)
            }
        })
        viewModel.nameIsEmpty.observe(this, Observer {
            if (it) {
                binding.txtExercisedetailsName.setBackgroundResource(R.drawable.background_text_grey_warn)
                binding.txtExercisedetailsNameEmpty.setBackgroundResource(R.drawable.background_text_grey_warn)
            } else {
                binding.txtExercisedetailsNameEmpty.setBackgroundResource(R.drawable.background_text_grey)
            }
        })
        viewModel.RMisEmpty.observe(this, Observer {
            if (it) {
                binding.txtExercisedetailsRm.setBackgroundResource(R.drawable.background_text_grey_warn)
            } else {
                binding.txtExercisedetailsRm.setBackgroundResource(R.drawable.background_text_grey)
            }
        })
    }

    private fun setFabClickListener() {
        binding.btnExercisedetailsSave.setOnClickListener {
            //if exercise isn't new - update it
            if(exerciseExists) {
                if(viewModel.updateExercise(
                        binding.txtExercisedetailsName.text.toString(),
                        binding.txtExercisedetailsDescription.text.toString(),
                        binding.checkboxExercisedetailsRm.isChecked,
                        binding.txtExercisedetailsRm.text.toString()
                    )
                ) {
                    activity?.supportFragmentManager!!.popBackStack()
                }
                else{
                    Toast.makeText(activity, getString(R.string.Fill_all_fiels), Toast.LENGTH_SHORT).show()
                }
            }
            //if it's new - add new one
            else{
                if(viewModel.saveExercise(
                        binding.txtExercisedetailsNameEmpty.text.toString(),
                        binding.txtExercisedetailsDescription.text.toString(),
                        binding.checkboxExercisedetailsRm.isChecked,
                        binding.txtExercisedetailsRm.text.toString()
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

    //clicking info buttons explains what RM is
    private fun setInfoButtonListener() {
        binding.btnExercisedetailsRm.setOnClickListener {
            //display popup window
            val inflater =
                requireActivity().baseContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?

            val popupView: View = inflater!!.inflate(R.layout.popup_rm_info, null)

            val popupWindow = PopupWindow(popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
            )

            popupWindow.enterTransition = Slide(Gravity.START)

            binding.btnExercisedetailsRm.setImageResource(R.drawable.vector_info_grey_background)
            popupWindow.showAsDropDown(
                binding.btnExercisedetailsRm,
                binding.btnExercisedetailsRm.width,
                (-resources.getDimension(R.dimen.popup_vertical_offset)).toInt()
            )


            popupView.setOnClickListener {
                popupWindow.dismiss()
            }

            popupWindow.setOnDismissListener {
                binding.btnExercisedetailsRm.setImageResource(R.drawable.vector_info)
            }
        }
    }
}