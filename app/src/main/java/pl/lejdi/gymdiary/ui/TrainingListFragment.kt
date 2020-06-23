package pl.lejdi.gymdiary.ui

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Slide
import pl.lejdi.gymdiary.R
import pl.lejdi.gymdiary.adapter.TrainingListAdapter
import pl.lejdi.gymdiary.database.model.Training
import pl.lejdi.gymdiary.databinding.TrainingsListFragmentBinding
import pl.lejdi.gymdiary.viewmodel.TrainingListViewModel


class TrainingListFragment : Fragment(), TrainingListAdapter.OnListFragmentInteractionListener {
    private lateinit var viewModel : TrainingListViewModel

    private lateinit var binding: TrainingsListFragmentBinding
    private lateinit var adapter : TrainingListAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = TrainingsListFragmentBinding.inflate(inflater, container, false)
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
            binding.addTrainingDate.setText(it)
        })
        viewModel.descriptionIsEmpty.observe(this, Observer {
            if(it){
                binding.addTrainingDescription.setBackgroundResource(R.drawable.training_text_background_warning)
            }
            else{
                binding.addTrainingDescription.setBackgroundResource(R.drawable.training_text_background)
            }
        })
        viewModel.dateIsEmpty.observe(this, Observer {
            if(it){
                binding.addTrainingDate.setBackgroundResource(R.drawable.training_text_background_warning)
            }
            else{
                binding.addTrainingDate.setBackgroundResource(R.drawable.training_text_background)
            }
        })
    }

    private var isAddViewShown = false

    private fun setFabClickListener() {
        binding.upperFABTrainingList.setOnClickListener {
            if(isAddViewShown){
                if(!viewModel.saveNewTraining(binding.addTrainingDate.text.toString(), binding.addTrainingDescription.text.toString())){
                    Toast.makeText(activity,getString(R.string.Fill_all_fiels), Toast.LENGTH_SHORT).show()
                }
                else{
                    if(viewModel.trainings.value != null){
                        binding.trainingRecyclerview.layoutManager?.scrollToPosition(viewModel.trainings.value?.size!! - 1)
                    }
                    binding.addTrainingContainer.layoutParams =
                        LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            0
                        )
                    isAddViewShown = false
                }
            }
            else{
                val params = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                val marginInDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10f, resources
                        .displayMetrics
                ).toInt()
                params.setMargins(marginInDp)
                binding.addTrainingContainer.layoutParams = params
                isAddViewShown = true
            }

        }
        binding.lowerFABTrainingList.setOnClickListener {

        }
    }

    private fun initRecyclerView() {
        adapter = TrainingListAdapter( viewModel, this)
        binding.trainingRecyclerview.adapter = adapter
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView( binding.trainingRecyclerview)
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        binding.trainingRecyclerview.layoutManager = layoutManager
    }

    override fun onListFragmentClickInteraction(training: Training, position: Int) {
        val trainingDetailsFragment = TrainingDetailsFragment()
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