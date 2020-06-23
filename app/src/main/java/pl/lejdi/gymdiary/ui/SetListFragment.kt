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
import pl.lejdi.gymdiary.adapter.SetListAdapter
import pl.lejdi.gymdiary.database.model.Set
import pl.lejdi.gymdiary.databinding.FragmentSetListBinding
import pl.lejdi.gymdiary.viewmodel.SetListViewModel
import java.util.*


class SetListFragment : Fragment(), SetListAdapter.OnListFragmentInteractionListener {
    private lateinit var viewModel : SetListViewModel
    private lateinit var binding : FragmentSetListBinding

    private lateinit var adapter : SetListAdapter

    var trainingId = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSetListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this).get(SetListViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        if (arguments!= null)
        {
            trainingId = arguments?.getInt(getString(R.string.KEY_TRAINING_ID))!!
        }
        viewModel.retrieveSets(trainingId)

        setFabClickListener()
        initRecyclerView()
    }

    private fun setFabClickListener() {
        binding.btnSetlistAdd.setOnClickListener {
            val addSetFragment = AddSetFragment()
            addSetFragment.enterTransition= Slide(Gravity.START)

            val bundle = Bundle()
            bundle.putInt(getString(R.string.KEY_TRAINING_ID), trainingId)
            addSetFragment.arguments=bundle

            activity?.supportFragmentManager!!.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.container, addSetFragment)
                .commit()
        }
    }

    private fun initRecyclerView() {
        adapter = SetListAdapter( viewModel, this)
        binding.recyclerviewSetlist.adapter = adapter
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.recyclerviewSetlist)
        binding.recyclerviewSetlist.layoutManager = LinearLayoutManager(activity)
    }

    override fun onListFragmentClickInteraction(set: Set, position: Int) {
        val editExerciseFragment = EditExerciseFragment()
        editExerciseFragment.enterTransition= Slide(Gravity.START)

        val bundle = Bundle()
        bundle.putString(getString(R.string.KEY_EXERCISE_NAME), set.exerciseName)
        editExerciseFragment.arguments=bundle

        activity?.supportFragmentManager!!.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.container, editExerciseFragment)
            .commit()
    }

    private val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.DOWN or ItemTouchHelper.UP, ItemTouchHelper.RIGHT){

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            adapter.notifyItemMoved(viewHolder.adapterPosition, target.adapterPosition)
            Collections.swap(viewModel.sets.value, viewHolder.adapterPosition, target.adapterPosition)
            viewModel.notifyOrderChanged()
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val builder = AlertDialog.Builder(activity!!)
            builder.setTitle(getString(R.string.R_U_sure))
            builder.setMessage(getString(R.string.U_will_lose_data))

            builder.setPositiveButton(getString(R.string.Yes)) { _, _ ->
                viewModel.deleteSet(viewModel.sets.value?.get(viewHolder.adapterPosition)!!)
            }

            builder.setNegativeButton(getString(R.string.No)) { _, _ -> adapter.notifyItemChanged(viewHolder.adapterPosition)}

            builder.show()
        }
    }
}