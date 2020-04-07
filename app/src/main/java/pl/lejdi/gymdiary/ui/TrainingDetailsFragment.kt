package pl.lejdi.gymdiary.ui

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Slide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import pl.lejdi.gymdiary.R
import pl.lejdi.gymdiary.adapter.SetListAdapter
import pl.lejdi.gymdiary.database.model.Exercise
import pl.lejdi.gymdiary.database.model.Set
import pl.lejdi.gymdiary.viewmodel.TrainingDetailsViewModel

class TrainingDetailsFragment : Fragment(), SetListAdapter.OnListFragmentInteractionListener {
    private lateinit var viewModel : TrainingDetailsViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter : SetListAdapter
    private lateinit var addButton: FloatingActionButton

    var trainingId = 0
    private lateinit var exercise : Exercise

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val fragmentView = inflater.inflate(R.layout.set_list_fragment, container, false)
        recyclerView = fragmentView.findViewById(R.id.set_recyclerview)
        addButton = fragmentView.findViewById(R.id.fab_add_set)
        return fragmentView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this).get(TrainingDetailsViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        if (arguments!= null)
        {
            trainingId = arguments?.getInt("trainingID")!!
        }
        viewModel.retrieveSets(trainingId)

        setFabClickListener()
        initRecyclerView()
    }

    private fun setFabClickListener()
    {
        addButton.setOnClickListener {
            val addSetFragment = AddSetFragment()
            addSetFragment.enterTransition= Slide(Gravity.START)

            val bundle = Bundle()
            bundle.putInt("trainingID", trainingId)
            addSetFragment.arguments=bundle

            activity?.supportFragmentManager!!.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.container, addSetFragment)
                .commit()
        }
    }

    private fun initRecyclerView()
    {
        adapter = SetListAdapter( viewModel, this)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)
    }

    override fun onListFragmentClickInteraction(set: Set, position: Int) {
        val editExerciseFragment = EditExerciseFragment()
        editExerciseFragment.enterTransition= Slide(Gravity.START)

        val bundle = Bundle()
        bundle.putString("exerciseName", set.exerciseName)
        editExerciseFragment.arguments=bundle

        activity?.supportFragmentManager!!.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.container, editExerciseFragment)
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
            viewModel.deleteSet(viewModel.sets.value?.get(viewHolder.adapterPosition)!!)
        }

    }
}