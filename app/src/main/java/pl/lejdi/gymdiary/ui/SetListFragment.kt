package pl.lejdi.gymdiary.ui

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.lejdi.gymdiary.R
import pl.lejdi.gymdiary.adapter.SetListAdapter
import pl.lejdi.gymdiary.database.model.Set
import pl.lejdi.gymdiary.databinding.FragmentSetListBinding
import pl.lejdi.gymdiary.ui.animations.MotionProgressListener
import pl.lejdi.gymdiary.ui.animations.animateFABColorChange
import pl.lejdi.gymdiary.ui.animations.AnimationHelper
import pl.lejdi.gymdiary.util.Constants
import pl.lejdi.gymdiary.util.Fragments
import pl.lejdi.gymdiary.viewmodel.SetListViewModel
import java.util.*


class SetListFragment : Fragment(), SetListAdapter.OnListFragmentInteractionListener {
    private lateinit var viewModel : SetListViewModel
    private lateinit var binding : FragmentSetListBinding

    private lateinit var adapter : SetListAdapter

    private var set : Set? = null

    var trainingId = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSetListBinding.inflate(inflater, container, false)
        //handle animation of FAB
        binding.motionAddsetFab.setTransitionListener(MotionProgressListener { progress: Float ->
            //replace fragment on end of animation
            if(progress == 1f){
                val addSetFragment = AddSetFragment()

                //foreign key of new Set
                val bundle = Bundle()
                bundle.putInt(Constants.KEY_TRAINING_ID, trainingId)
                addSetFragment.arguments=bundle

                activity?.supportFragmentManager!!.beginTransaction()
                    .setCustomAnimations(R.anim.fragment_fade_in, R.anim.fragment_fade_out, R.anim.fragment_fade_in, R.anim.fragment_fade_out)
                    .addToBackStack(null)
                    .replace(R.id.container, addSetFragment)
                    .commit()
            }
        })
        //handle listitem clicks animation
        binding.motionSetlistItem.setTransitionListener(MotionProgressListener { progress: Float ->
            //on the beginning fake item must be hidden
            if(progress == 0f){
                binding.viewFakeListitem.width=0
                binding.viewFakeListitem.height=0
            }
            //at the end replace fragment
            if(progress == 1f){
                val editExerciseFragment = EditExerciseFragment()

                //put exercise name in bundle - prefilling data in edit mode
                val bundle = Bundle()
                bundle.putString(Constants.KEY_EXERCISE_NAME, set?.exerciseName)
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
        viewModel = ViewModelProvider(this).get(SetListViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        if (arguments!= null)
        {
            trainingId = arguments?.getInt(Constants.KEY_TRAINING_ID)!! //get sets foreign key
        }
        viewModel.retrieveSets(trainingId) //retrieve sets with the same foreign key

        setFabClickListener()
        initRecyclerView()
    }

    private fun setFabClickListener() {
        //initial FAB background
        binding.btnListAdd.backgroundTintList= ColorStateList.valueOf(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorPrimaryDark
            )
        )
        //handle clicks
        binding.btnListAdd.setOnClickListener {
            //hiding '+' sign so it woun't be seen during animation
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
            //change bg color
            animateFABColorChange(
                requireContext(),
                binding.btnListAdd,
                R.color.colorPrimaryDark,
                R.color.fragmentsBackground,
                500L)
            binding.motionAddsetFab.transitionToEnd()
        }
    }

    private fun initRecyclerView() {
        //adapter
        adapter = SetListAdapter( viewModel, this)
        binding.recyclerviewSetlist.adapter = adapter
        //swiping and reorganizing items
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.recyclerviewSetlist)
        binding.recyclerviewSetlist.layoutManager = LinearLayoutManager(activity)
        //when recyclerview is ready
        binding.recyclerviewSetlist.viewTreeObserver
            .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    //if previously you were editing exercise - animate going back to list item
                    if(AnimationHelper.previousFragment == Fragments.EXERCISE_EDIT){
                        val selectedView = binding.recyclerviewSetlist.getChildAt(AnimationHelper.chosenSetPosition)
                        binding.motionSetlistItem.progress = 0f
                        //if it's visible on the list - get properties
                        if(selectedView != null){
                            binding.viewFakeListitem.width = selectedView.width
                            binding.viewFakeListitem.height = selectedView.height
                            binding.viewFakeListitem.x = selectedView.x
                            binding.viewFakeListitem.y = selectedView.y
                        }
                        //else animate below the list
                        else{
                            binding.viewFakeListitem.width = binding.recyclerviewSetlist.width
                            binding.viewFakeListitem.height = 0
                            binding.viewFakeListitem.x = 0f
                            binding.viewFakeListitem.y = binding.recyclerviewSetlist.bottom.toFloat()
                        }

                        binding.motionSetlistItem.progress = 0.99f
                        binding.motionSetlistItem.transitionToStart()
                    }
                    //if previously you were adding set - animate back FAB
                    if(AnimationHelper.previousFragment == Fragments.SET_ADD){
                        binding.motionAddsetFab.progress = 0.99f
                        binding.motionAddsetFab.transitionToStart()
                    }
                    AnimationHelper.previousFragment = Fragments.SET_LIST //update app state
                    binding.recyclerviewSetlist.viewTreeObserver.removeOnGlobalLayoutListener(this) //cleanup
                }
            })
    }

    override fun onListFragmentClickInteraction(set: Set, position: Int) {
        this.set = set //update set for prefilling data
        AnimationHelper.chosenSetPosition = position //update app state
        //get clicked item's properties and animate fake item
        val layoutManager = binding.recyclerviewSetlist.layoutManager as LinearLayoutManager
        val selectedView = binding.recyclerviewSetlist.getChildAt(position - layoutManager.findFirstVisibleItemPosition())
        binding.viewFakeListitem.width = selectedView.width
        binding.viewFakeListitem.height = selectedView.height
        binding.viewFakeListitem.x = selectedView.x
        binding.viewFakeListitem.y = selectedView.y
        binding.motionSetlistItem.transitionToEnd()
    }

    private val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.DOWN or ItemTouchHelper.UP, ItemTouchHelper.RIGHT){

        //reorganizing items
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            //tell adapter to update rv_positions of items
            adapter.notifyItemMoved(viewHolder.adapterPosition, target.adapterPosition)
            //swap items on the list
            Collections.swap(viewModel.sets.value, viewHolder.adapterPosition, target.adapterPosition)
            viewModel.notifyOrderChanged(viewHolder as SetListAdapter.ViewHolder, target as SetListAdapter.ViewHolder)
            return true
        }

        //on swipe delete item
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            //RU sure dialog
            val builder = AlertDialog.Builder(activity!!)
            builder.setTitle(getString(R.string.R_U_sure))
            builder.setMessage(getString(R.string.U_will_lose_data))

            //yes - delete
            builder.setPositiveButton(getString(R.string.Yes)) { _, _ ->
                viewModel.deleteSet(viewModel.sets.value?.get(viewHolder.adapterPosition)!!)
            }
            //no - give item back
            builder.setNegativeButton(getString(R.string.No)) { _, _ -> adapter.notifyItemChanged(viewHolder.adapterPosition)}

            builder.show()
        }
    }
}