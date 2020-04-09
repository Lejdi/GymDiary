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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Slide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import pl.lejdi.gymdiary.R
import pl.lejdi.gymdiary.adapter.TrainingListAdapter
import pl.lejdi.gymdiary.database.model.Training
import pl.lejdi.gymdiary.viewmodel.TrainingListViewModel

class TrainingListFragment : Fragment(), TrainingListAdapter.OnListFragmentInteractionListener {
    private lateinit var viewModel : TrainingListViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter : TrainingListAdapter
    private lateinit var addButton: FloatingActionButton


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val fragmentView = inflater.inflate(R.layout.trainings_list_fragment, container, false)
        recyclerView = fragmentView.findViewById(R.id.training_recyclerview)
        addButton = fragmentView.findViewById(R.id.fab_add_training)
        return fragmentView
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
        addButton.setOnClickListener {
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
        recyclerView.adapter = adapter
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recyclerView)
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        recyclerView.layoutManager = layoutManager
    }

    override fun onListFragmentClickInteraction(training: Training, position: Int) {
        val trainingDetailsFragment = TrainingDetailsFragment()
        trainingDetailsFragment.enterTransition= Slide(Gravity.START)

        val bundle = Bundle()
        bundle.putInt("trainingID", training.id)
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
            builder.setTitle("Are you sure?")
            builder.setMessage("You will lose the data forever...")

            builder.setPositiveButton(android.R.string.yes) { _, _ ->
                viewModel.deleteTraining(viewModel.trainings.value?.get(viewHolder.adapterPosition)!!)
            }

            builder.setNegativeButton(android.R.string.no) { _, _ -> adapter.notifyItemChanged(viewHolder.adapterPosition)}

            builder.show()
        }

    }
}