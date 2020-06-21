package pl.lejdi.gymdiary.ui

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
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
        setFabClickListener()
        viewModel.retrieveTrainings()
        initRecyclerView()
    }

    private fun setFabClickListener()
    {
        binding.fabAddTraining.setOnClickListener {
            val addTrainingFragment = AddTrainingFragment()
            addTrainingFragment.enterTransition= Slide(Gravity.START)

            activity?.supportFragmentManager!!.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.container, addTrainingFragment)
                .commit()
        }
    }

    private fun initRecyclerView()
    {
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