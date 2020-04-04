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
import pl.lejdi.gymdiary.adapter.SetListAdapter
import pl.lejdi.gymdiary.database.model.Set
import pl.lejdi.gymdiary.viewmodel.TrainingDetailsViewModel

class TrainingDetailsFragment : Fragment(), SetListAdapter.OnListFragmentInteractionListener {
    private lateinit var viewModel : TrainingDetailsViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter : SetListAdapter
    private lateinit var addButton: FloatingActionButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val fragmentView = inflater.inflate(R.layout.set_list_fragment, container, false)
        recyclerView = fragmentView.findViewById(R.id.set_recyclerview)
        addButton = fragmentView.findViewById(R.id.fab_add_set)
        return fragmentView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProviders.of(this).get(TrainingDetailsViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        setFabClickListener()
        viewModel.retrieveSets()
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
        adapter = SetListAdapter( viewModel, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
    }

    override fun onListFragmentClickInteraction(set: Set, position: Int) {
        Toast.makeText(activity,"Item $position Pressed", Toast.LENGTH_SHORT).show()
    }
}