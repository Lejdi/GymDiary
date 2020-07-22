package pl.lejdi.gymdiaryserverversion.ui

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
import pl.lejdi.gymdiaryserverversion.R
import pl.lejdi.gymdiaryserverversion.adapter.ExerciseListAdapter
import pl.lejdi.gymdiaryserverversion.database.model.Exercise
import pl.lejdi.gymdiaryserverversion.databinding.FragmentExerciseListBinding
import pl.lejdi.gymdiaryserverversion.ui.animations.MotionProgressListener
import pl.lejdi.gymdiaryserverversion.ui.animations.animateFABColorChange
import pl.lejdi.gymdiaryserverversion.ui.animations.AnimationHelper
import pl.lejdi.gymdiaryserverversion.util.Constants
import pl.lejdi.gymdiaryserverversion.util.Fragments
import pl.lejdi.gymdiaryserverversion.viewmodel.ExerciseListViewModel

class ExerciseListFragment : Fragment(), ExerciseListAdapter.OnListFragmentInteractionListener {
    private lateinit var viewModel : ExerciseListViewModel

    private lateinit var binding: FragmentExerciseListBinding
    private lateinit var adapter : ExerciseListAdapter

    private var exercise : Exercise? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentExerciseListBinding.inflate(inflater, container, false)
        //handling progress of FAB animation
        binding.motionAddexerciseFab.setTransitionListener(MotionProgressListener { progress: Float ->
            //on the end of animation replace fragment
            if(progress == 1f){
                val editExerciseFragment = EditExerciseFragment()

                activity?.supportFragmentManager!!.beginTransaction()
                    .setCustomAnimations(R.anim.fragment_fade_in, R.anim.fragment_fade_out, R.anim.fragment_fade_in, R.anim.fragment_fade_out)
                    .addToBackStack(null)
                    .replace(R.id.container, editExerciseFragment)
                    .commit()
            }
        })
        //handling click list item animation
        binding.motionExerciselistItem.setTransitionListener(MotionProgressListener { progress: Float ->
            //on start fake list item should be hidden
            if(progress == 0f){
                binding.viewFakeListitem.width=0
                binding.viewFakeListitem.height=0
            }
            //at the end of animation change fragment
            if(progress == 1f){
                val editExerciseFragment = EditExerciseFragment()

                //exercise name is needed by the next fragment - if it's not null it means exercise should be edited (prefilling data)
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

    //init viewmodel
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

    private fun setFabClickListener() {
        //initial background color
        binding.btnListAdd.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorPrimaryDark
            )
        )
        //on FAB click
        binding.btnListAdd.setOnClickListener {
            AnimationHelper.exDetailsFromExList_isNew = true //application state for animation purposes - going back animation depends on it
            //depending on Android version do it different ways, but what this code do is hiding '+' drawable so it doesn't look strange during animation
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

            //animate changing color
            animateFABColorChange(
                requireContext(),
                binding.btnListAdd,
                R.color.colorPrimaryDark,
                R.color.fragmentsBackground,
            500L)
            binding.motionAddexerciseFab.transitionToEnd()
        }
    }

    private fun initRecyclerView() {
        //adapter
        adapter = ExerciseListAdapter( viewModel, this)
        binding.recyclerviewExerciselist.adapter = adapter
        //handling swiping items
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView( binding.recyclerviewExerciselist)
        val layoutManager = LinearLayoutManager(activity)
        binding.recyclerviewExerciselist.layoutManager = layoutManager
        //when the recyclerview is ready
        binding.recyclerviewExerciselist.viewTreeObserver
            .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    //if it's displaying after going back from editing exercise
                    if(AnimationHelper.previousFragment == Fragments.EXERCISE_EDIT){
                        //if edit exercise was reached by add button - reverse FAB animation
                        if(AnimationHelper.exDetailsFromExList_isNew){
                            binding.motionAddexerciseFab.progress = 0.99f
                            binding.motionAddexerciseFab.transitionToStart()
                        }
                        //else - reverse fakelistitem animation
                        else{
                            val selectedView = binding.recyclerviewExerciselist.getChildAt(AnimationHelper.chosenExercisePosition)
                            binding.motionExerciselistItem.progress = 0f
                            //get previously chosen item properties
                            if(selectedView != null){
                                binding.viewFakeListitem.width = selectedView.width
                                binding.viewFakeListitem.height = selectedView.height
                                binding.viewFakeListitem.x = selectedView.x
                                binding.viewFakeListitem.y = selectedView.y
                            }
                            //if its not visible animate as if this item was below list
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
                    //update app state
                    AnimationHelper.previousFragment = Fragments.EXERCISE_LIST
                    //cleanup
                    binding.recyclerviewExerciselist.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })
    }

    override fun onListFragmentClickInteraction(exercise: Exercise, position: Int) {
        AnimationHelper.exDetailsFromExList_isNew = false //properly update state
        this.exercise = exercise
        AnimationHelper.chosenExercisePosition = position
        //get item properties and animate its growing
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

        //delete item on swipe
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            //dialog R U SURE
            val builder = AlertDialog.Builder(activity!!)
            builder.setTitle(getString(R.string.R_U_sure))
            builder.setMessage(getString(R.string.U_will_lose_data))

            //delete on yes
            builder.setPositiveButton(getString(R.string.Yes)) { _, _ ->
                viewModel.deleteExercise(viewModel.exercises.value?.get(viewHolder.adapterPosition)!!)
            }

            //item is back when no
            builder.setNegativeButton(getString(R.string.No)) { _, _ -> adapter.notifyItemChanged(viewHolder.adapterPosition)}

            builder.show()
        }
    }
}