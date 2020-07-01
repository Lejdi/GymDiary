package pl.lejdi.gymdiary.ui

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import pl.lejdi.gymdiary.R
import pl.lejdi.gymdiary.adapter.ExerciseListAdapter
import pl.lejdi.gymdiary.database.model.Exercise
import pl.lejdi.gymdiary.databinding.FragmentExerciseListBinding
import pl.lejdi.gymdiary.ui.animations.MotionProgressListener
import pl.lejdi.gymdiary.ui.animations.animateFABColorChange
import pl.lejdi.gymdiary.ui.animations.AnimationHelper
import pl.lejdi.gymdiary.util.Constants
import pl.lejdi.gymdiary.util.Fragments
import pl.lejdi.gymdiary.viewmodel.ExerciseListViewModel
import kotlin.coroutines.CoroutineContext

class ExerciseListFragment : Fragment(), ExerciseListAdapter.OnListFragmentInteractionListener {
    private lateinit var viewModel : ExerciseListViewModel

    private lateinit var binding: FragmentExerciseListBinding
    private lateinit var adapter : ExerciseListAdapter

    private var exercise : Exercise? = null

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
        binding.motionExerciselistItem.setTransitionListener(MotionProgressListener { progress: Float ->
            if(progress == 0f){
                binding.viewFakeListitem.width=0
                binding.viewFakeListitem.height=0
            }
            if(progress == 1f){
                val editExerciseFragment = EditExerciseFragment()

                val bundle = Bundle()
                bundle.putString(Constants.KEY_EXERCISE_NAME, exercise?.name)
                editExerciseFragment.arguments=bundle

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
        binding.btnListAdd.backgroundTintList= ColorStateList.valueOf(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorPrimaryDark
            )
        )
        binding.btnListAdd.setOnClickListener {
            AnimationHelper.exDetailsFromExList_isNew = true
            AnimationHelper.exerciseSaved = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                binding.btnListAdd.drawable.colorFilter =
                    BlendModeColorFilter(
                        ContextCompat.getColor(requireContext(), R.color.invisible),
                        BlendMode.CLEAR)
            } else {
                binding.btnListAdd.drawable.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.invisible),
                    PorterDuff.Mode.CLEAR)
            }

            animateFABColorChange(
                requireContext(),
                binding.btnListAdd,
                R.color.colorPrimaryDark,
                R.color.fragmentsBackground,
            500L)
            binding.motionAddexerciseFab.transitionToEnd()
        }
    }

    private fun initRecyclerView()
    {
        GlobalScope.launch {
            withContext(Dispatchers.Main){
                if(AnimationHelper.previousFragment != Fragments.EXERCISE_EDIT){
                    delay(500)
                }
                adapter = ExerciseListAdapter( viewModel, this@ExerciseListFragment)
                binding.recyclerviewExerciselist.adapter = adapter
                ItemTouchHelper(itemTouchHelper).attachToRecyclerView( binding.recyclerviewExerciselist)
                val layoutManager = LinearLayoutManager(activity)
                binding.recyclerviewExerciselist.layoutManager = layoutManager
                binding.recyclerviewExerciselist.viewTreeObserver
                    .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            if(AnimationHelper.previousFragment == Fragments.EXERCISE_EDIT){
                                if(AnimationHelper.exDetailsFromExList_isNew && !AnimationHelper.exerciseSaved){
                                    binding.motionAddexerciseFab.progress = 0.99f
                                    binding.motionAddexerciseFab.transitionToStart()
                                }
                                else{
                                    if(AnimationHelper.exDetailsFromExList_isNew){
                                        AnimationHelper.chosenExercisePosition = viewModel.exercises.value?.size!! -1
                                    }
                                    val selectedView = binding.recyclerviewExerciselist.getChildAt(AnimationHelper.chosenExercisePosition)
                                    binding.motionExerciselistItem.progress = 0f
                                    if(selectedView != null){
                                        binding.viewFakeListitem.width = selectedView.width
                                        binding.viewFakeListitem.height = selectedView.height
                                        binding.viewFakeListitem.x = selectedView.x
                                        binding.viewFakeListitem.y = selectedView.y
                                    }
                                    else{
                                        binding.viewFakeListitem.width = binding.recyclerviewExerciselist.width
                                        binding.viewFakeListitem.height = 0
                                        binding.viewFakeListitem.x = 0f
                                        binding.viewFakeListitem.y = binding.recyclerviewExerciselist.bottom.toFloat()
                                    }

                                    binding.motionExerciselistItem.progress = 0.99f
                                    binding.motionExerciselistItem.transitionToStart()
                                }
                            }
                            binding.recyclerviewExerciselist.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        }
                    })
            }
        }
    }

    override fun onListFragmentClickInteraction(exercise: Exercise, position: Int) {
        AnimationHelper.exDetailsFromExList_isNew = false
        AnimationHelper.exerciseSaved = false
        this.exercise = exercise
        AnimationHelper.chosenExercisePosition = position
        val layoutManager = binding.recyclerviewExerciselist.layoutManager as LinearLayoutManager
        val selectedView = binding.recyclerviewExerciselist.getChildAt(position - layoutManager.findFirstVisibleItemPosition())
        binding.viewFakeListitem.width = selectedView.width
        binding.viewFakeListitem.height = selectedView.height
        binding.viewFakeListitem.x = selectedView.x
        binding.viewFakeListitem.y = selectedView.y
        binding.motionExerciselistItem.transitionToEnd()
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