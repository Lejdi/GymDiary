package pl.lejdi.gymdiary.ui

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Slide
import pl.lejdi.gymdiary.R
import pl.lejdi.gymdiary.adapter.ExerciseListAdapter
import pl.lejdi.gymdiary.database.model.Exercise
import pl.lejdi.gymdiary.databinding.FragmentExerciseListBinding
import pl.lejdi.gymdiary.ui.animations.MotionProgressListener
import pl.lejdi.gymdiary.ui.animations.animateFABColorChange
import pl.lejdi.gymdiary.util.AnimationHelper
import pl.lejdi.gymdiary.util.Constants
import pl.lejdi.gymdiary.util.Fragments
import pl.lejdi.gymdiary.viewmodel.ExerciseListViewModel

class ExerciseListFragment : Fragment(), ExerciseListAdapter.OnListFragmentInteractionListener {
    private lateinit var viewModel : ExerciseListViewModel

    private lateinit var binding: FragmentExerciseListBinding
    private lateinit var adapter : ExerciseListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentExerciseListBinding.inflate(inflater, container, false)
        binding.motionAddexerciseFab.setTransitionListener(MotionProgressListener { progress: Float ->
            if(progress == 1f){
                val editExerciseFragment = EditExerciseFragment()

                activity?.supportFragmentManager!!.beginTransaction()
                    .setCustomAnimations(R.anim.fragment_fade_in, R.anim.fragment_fade_out, R.anim.fragment_fade_in, R.anim.fragment_fade_out)
                    .addToBackStack(null)
                    .replace(R.id.container, editExerciseFragment)
                    .commit()
            }
        })
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AnimationHelper.previousFragment = Fragments.EXERCISE_LIST
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
        binding.btnExerciselistAdd.backgroundTintList= ColorStateList.valueOf(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorPrimaryDark
            )
        )
        binding.btnExerciselistAdd.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                binding.btnExerciselistAdd.drawable.colorFilter =
                    BlendModeColorFilter(
                        ContextCompat.getColor(requireContext(), R.color.invisible),
                        BlendMode.CLEAR)
            } else {
                binding.btnExerciselistAdd.drawable.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.invisible),
                    PorterDuff.Mode.CLEAR)
            }

            animateFABColorChange(
                requireContext(),
                binding.btnExerciselistAdd,
                R.color.colorPrimaryDark,
                R.color.fragmentsBackground,
            500L)
            binding.motionAddexerciseFab.transitionToEnd()
        }
    }

    private fun initRecyclerView()
    {
        adapter = ExerciseListAdapter( viewModel, this)
        binding.recyclerviewExerciselist.adapter = adapter
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView( binding.recyclerviewExerciselist)
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        binding.recyclerviewExerciselist.layoutManager = layoutManager
    }

    override fun onListFragmentClickInteraction(exercise: Exercise, position: Int) {
        val editExerciseFragment = EditExerciseFragment()
        editExerciseFragment.enterTransition= Slide(Gravity.START)

        val bundle = Bundle()
        bundle.putString(Constants.KEY_EXERCISE_NAME, exercise.name)
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