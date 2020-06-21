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
import pl.lejdi.gymdiary.adapter.ExerciseListAdapter
import pl.lejdi.gymdiary.database.model.Exercise
import pl.lejdi.gymdiary.databinding.ExerciseListFragmentBinding
import pl.lejdi.gymdiary.viewmodel.ExerciseListViewModel

class ExerciseListFragment : Fragment(), ExerciseListAdapter.OnListFragmentInteractionListener {
    private lateinit var viewModel : ExerciseListViewModel

    private lateinit var binding: ExerciseListFragmentBinding
    private lateinit var adapter : ExerciseListAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ExerciseListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this).get(ExerciseListViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        setFabClickListener()
        viewModel.retrieveExercises()
        initRecyclerView()
    }

    private fun setFabClickListener()
    {
        binding.fabAddExercise.setOnClickListener {
            val editExerciseFragment = EditExerciseFragment()
            editExerciseFragment.enterTransition= Slide(Gravity.START)

            activity?.supportFragmentManager!!.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.container, editExerciseFragment)
                .commit()
        }
    }

    private fun initRecyclerView()
    {
        adapter = ExerciseListAdapter( viewModel, this)
        binding.exerciseRecyclerview.adapter = adapter
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView( binding.exerciseRecyclerview)
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        binding.exerciseRecyclerview.layoutManager = layoutManager
    }

    override fun onListFragmentClickInteraction(exercise: Exercise, position: Int) {
        val editExerciseFragment = EditExerciseFragment()
        editExerciseFragment.enterTransition= Slide(Gravity.START)

        val bundle = Bundle()
        bundle.putString(getString(R.string.KEY_EXERCISE_NAME), exercise.name)
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
            val builder = AlertDialog.Builder(activity!!)
            builder.setTitle(getString(R.string.R_U_sure))
            builder.setMessage(getString(R.string.U_will_lose_data))

            builder.setPositiveButton(getString(R.string.Yes)) { _, _ ->
                viewModel.deleteExercise(viewModel.exercises.value?.get(viewHolder.adapterPosition)!!)
            }

            builder.setNegativeButton(getString(R.string.No)) { _, _ -> adapter.notifyItemChanged(viewHolder.adapterPosition)}

            builder.show()
        }
    }
}