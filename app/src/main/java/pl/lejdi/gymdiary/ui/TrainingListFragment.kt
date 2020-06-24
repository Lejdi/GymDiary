package pl.lejdi.gymdiary.ui

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Slide
import kotlinx.android.synthetic.main.fragment_trainings_list.*
import pl.lejdi.gymdiary.R
import pl.lejdi.gymdiary.adapter.TrainingListAdapter
import pl.lejdi.gymdiary.database.model.Training
import pl.lejdi.gymdiary.databinding.FragmentTrainingsListBinding
import pl.lejdi.gymdiary.viewmodel.TrainingListViewModel


class TrainingListFragment : Fragment(), TrainingListAdapter.OnListFragmentInteractionListener {
    private lateinit var viewModel : TrainingListViewModel

    private lateinit var binding: FragmentTrainingsListBinding
    private lateinit var adapter : TrainingListAdapter

    class MotionProgressListener(private val progressListener: (Float) -> Unit) :
        MotionLayout.TransitionListener {
        override fun onTransitionTrigger(layout: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) {}
        override fun onTransitionStarted(layout: MotionLayout?, startId: Int, endId: Int) {}
        override fun onTransitionChange(layout: MotionLayout?, startId: Int, endId: Int, progress: Float) {
            progressListener.invoke(progress)
        }
        override fun onTransitionCompleted(layout: MotionLayout?, currentId: Int) {}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTrainingsListBinding.inflate(inflater, container, false)
        binding.motion.setTransitionListener(MotionProgressListener { progress: Float ->
            binding.txtAddtrainingDate.isVisible = (progress >= 0.4f)
            binding.txtAddtrainingDescription.isVisible = (progress >= 0.8f)
        })
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this).get(TrainingListViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        observeData()
        setFabClickListener()
        viewModel.retrieveTrainings()
        initRecyclerView()
    }

    private fun observeData() {
        viewModel.getCurrentDate().observe(this, Observer {
            binding.txtAddtrainingDate.setText(it)
        })
        viewModel.descriptionIsEmpty.observe(this, Observer {
            if(it){
                binding.txtAddtrainingDescription.setBackgroundResource(R.drawable.background_text_yellow_warn)
            }
            else{
                binding.txtAddtrainingDescription.setBackgroundResource(R.drawable.background_text_yellow)
            }
        })
        viewModel.dateIsEmpty.observe(this, Observer {
            if(it){
                binding.txtAddtrainingDate.setBackgroundResource(R.drawable.background_text_yellow_warn)
            }
            else{
                binding.txtAddtrainingDate.setBackgroundResource(R.drawable.background_text_yellow)
            }
        })
    }

    private var isAddViewShown = false

    private fun setFabClickListener() {
        binding.btnTraininglistAddOrSave.setOnClickListener {
            if(isAddViewShown){
                if(!viewModel.saveNewTraining(binding.txtAddtrainingDate.text.toString(), binding.txtAddtrainingDescription.text.toString())){
                    Toast.makeText(activity,getString(R.string.Fill_all_fiels), Toast.LENGTH_SHORT).show()
                }
                else{
                    if(viewModel.trainings.value != null){
                        binding.recyclerviewTraininglist.layoutManager?.scrollToPosition(viewModel.trainings.value?.size!! - 1)
                    }
                    isAddViewShown = false
                }
            }
            else{
                binding.motion.visibility = View.VISIBLE.also{
                    binding.motion.transitionToEnd()
                }

                isAddViewShown = true
            }

        }
        binding.btnTraininglistDiscard.setOnClickListener {

        }
    }

    private fun initRecyclerView() {
        adapter = TrainingListAdapter( viewModel, this)
        binding.recyclerviewTraininglist.adapter = adapter
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView( binding.recyclerviewTraininglist)
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        binding.recyclerviewTraininglist.layoutManager = layoutManager
    }

    override fun onListFragmentClickInteraction(training: Training, position: Int) {
        val trainingDetailsFragment = SetListFragment()
        trainingDetailsFragment.enterTransition= Slide(Gravity.START)

        val bundle = Bundle()
        bundle.putInt(getString(R.string.KEY_TRAINING_ID), training.id)
        trainingDetailsFragment.arguments=bundle

        activity?.supportFragmentManager!!.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.container, trainingDetailsFragment)
            .commit()
    }

    private val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val builder = AlertDialog.Builder(activity!!)
            builder.setTitle(getString(R.string.R_U_sure))
            builder.setMessage(getString(R.string.U_will_lose_data))

            builder.setPositiveButton(getString(R.string.Yes)) { _, _ ->
                viewModel.deleteTraining(viewModel.trainings.value?.get(viewHolder.adapterPosition)!!)
            }

            builder.setNegativeButton(getString(R.string.No)) { _, _ -> adapter.notifyItemChanged(viewHolder.adapterPosition)}

            builder.show()
        }
    }
}