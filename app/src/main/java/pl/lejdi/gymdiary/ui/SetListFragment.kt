package pl.lejdi.gymdiary.ui

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import kotlinx.coroutines.*
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
        binding.motionAddsetFab.setTransitionListener(MotionProgressListener { progress: Float ->
            if(progress == 1f){
                val addSetFragment = AddSetFragment()

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
        binding.motionSetlistItem.setTransitionListener(MotionProgressListener { progress: Float ->
            if(progress == 0f){
                binding.viewFakeListitem.width=0
                binding.viewFakeListitem.height=0
            }
            if(progress == 1f){
                val editExerciseFragment = EditExerciseFragment()

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this).get(SetListViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        if (arguments!= null)
        {
            trainingId = arguments?.getInt(Constants.KEY_TRAINING_ID)!!
        }
        viewModel.retrieveSets(trainingId)

        setFabClickListener()
        initRecyclerView()
    }

    private fun setFabClickListener() {
        binding.btnListAdd.backgroundTintList= ColorStateList.valueOf(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorPrimaryDark
            )
        )
        binding.btnListAdd.setOnClickListener {
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
            binding.motionAddsetFab.transitionToEnd()
        }
    }


    private fun initRecyclerView() {
        adapter = SetListAdapter( viewModel, this@SetListFragment)
        binding.recyclerviewSetlist.adapter = adapter
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.recyclerviewSetlist)
        binding.recyclerviewSetlist.layoutManager = LinearLayoutManager(activity)
        binding.recyclerviewSetlist.viewTreeObserver
            .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    if(AnimationHelper.previousFragment == Fragments.EXERCISE_EDIT){
                        val selectedView = binding.recyclerviewSetlist.getChildAt(AnimationHelper.chosenSetPosition)
                        binding.motionSetlistItem.progress = 0f
                        if(selectedView != null){
                            binding.viewFakeListitem.width = selectedView.width
                            binding.viewFakeListitem.height = selectedView.height
                            binding.viewFakeListitem.x = selectedView.x
                            binding.viewFakeListitem.y = selectedView.y
                        }
                        else{
                            binding.viewFakeListitem.width = binding.recyclerviewSetlist.width
                            binding.viewFakeListitem.height = 0
                            binding.viewFakeListitem.x = 0f
                            binding.viewFakeListitem.y = binding.recyclerviewSetlist.bottom.toFloat()
                        }

                        binding.motionSetlistItem.progress = 0.99f
                        binding.motionSetlistItem.transitionToStart()
                    }
                    AnimationHelper.previousFragment = Fragments.SET_LIST
                    binding.recyclerviewSetlist.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })
    }

    override fun onListFragmentClickInteraction(set: Set, position: Int) {
        this.set = set
        AnimationHelper.chosenSetPosition = position
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