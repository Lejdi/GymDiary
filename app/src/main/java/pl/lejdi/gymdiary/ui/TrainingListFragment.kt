package pl.lejdi.gymdiary.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        viewModel = ViewModelProviders.of(this).get(TrainingListViewModel::class.java)
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
            Toast.makeText(activity,"FAB Pressed", Toast.LENGTH_SHORT).show()

        }
    }

    private fun initRecyclerView()
    {
        adapter = TrainingListAdapter( viewModel, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
    }

    override fun onListFragmentClickInteraction(training: Training, position: Int) {
        Toast.makeText(activity,"Item $position Pressed", Toast.LENGTH_SHORT).show()

    }
}