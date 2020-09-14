package pl.lejdi.gymdiary.ui

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.lejdi.gymdiary.R
import pl.lejdi.gymdiary.adapter.TrainingListAdapter
import pl.lejdi.gymdiary.database.model.Training
import pl.lejdi.gymdiary.databinding.FragmentTrainingsListBinding
import pl.lejdi.gymdiary.ui.animations.MotionProgressListener
import pl.lejdi.gymdiary.ui.animations.animateFABColorChange
import pl.lejdi.gymdiary.ui.animations.AnimationHelper
import pl.lejdi.gymdiary.util.Constants
import pl.lejdi.gymdiary.util.Fragments
import pl.lejdi.gymdiary.viewmodel.TrainingListViewModel


class TrainingListFragment : Fragment(), TrainingListAdapter.OnListFragmentInteractionListener {
    private lateinit var viewModel : TrainingListViewModel

    private lateinit var binding: FragmentTrainingsListBinding
    private lateinit var adapter : TrainingListAdapter

    //state of fragment - is add view displayed or not
    private var isAddViewShown = false

    //clicked training - for providing ID to next fragment
    private var training : Training? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTrainingsListBinding.inflate(inflater, container, false)
        isAddViewShown = false  //initial state
        //control displaying edit text fields as the add view grows
        binding.motionAddtrainingContainer.setTransitionListener(MotionProgressListener { progress: Float ->
            binding.txtAddtrainingDate.isVisible = (progress >= 0.4f)
            binding.txtAddtrainingDescription.isVisible = (progress >= 0.8f)
        })
        //control animation on clicking list item
        binding.motionTraininglistItem.setTransitionListener(MotionProgressListener { progress: Float ->
            //if progress == 0, fake list item should be hidden
            if(progress == 0f){
                binding.viewFakeListitem.width=0
                binding.viewFakeListitem.height=0
            }
            //at the end of animation, replace fragment
            if(progress == 1f){
                val setListFragment = SetListFragment()

                //id  must be provided as a foreign key for set
                val bundle = Bundle()
                bundle.putInt(Constants.KEY_TRAINING_ID, training?.id!!)
                setListFragment.arguments=bundle

                activity?.supportFragmentManager!!.beginTransaction()
                    .setCustomAnimations(R.anim.fragment_fade_in, R.anim.fragment_fade_out, R.anim.fragment_fade_in, R.anim.fragment_fade_out)
                    .addToBackStack(null)
                    .replace(R.id.container, setListFragment)
                    .commit()
            }
        })
        return binding.root
    }

    //init viewmodel
    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this).get(TrainingListViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        observeData()
        setFabClickListener()
        viewModel.retrieveTrainings()
        initRecyclerView()
    }

    //set all the observers
    private fun observeData() {
        //automatically get current date for new training
        viewModel.getCurrentDate().observe(this, Observer {
            binding.txtAddtrainingDate.setText(it)
        })
        //display red gradient if description is empty
        viewModel.descriptionIsEmpty.observe(this, Observer {
            if(it){
                binding.txtAddtrainingDescription.setBackgroundResource(R.drawable.background_text_yellow_warn)
            }
            else{
                binding.txtAddtrainingDescription.setBackgroundResource(R.drawable.background_text_yellow)
            }
        })
        //display red gradient if date is empty
        viewModel.dateIsEmpty.observe(this, Observer {
            if(it){
                binding.txtAddtrainingDate.setBackgroundResource(R.drawable.background_text_yellow_warn)
            }
            else{
                binding.txtAddtrainingDate.setBackgroundResource(R.drawable.background_text_yellow)
            }
        })
    }

    private fun setFabClickListener() {
        //the FAB with '+' - initial background color
        binding.btnTraininglistAddDiscard.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorPrimaryDark
            )
        )
        //animating the FAB with '+'
        binding.btnTraininglistAddDiscard.setOnClickListener {
            isAddViewShown = if(isAddViewShown){
                animateFABColorChange(
                    requireContext(),
                    binding.btnTraininglistAddDiscard,
                    R.color.red,
                    R.color.colorPrimaryDark,
                    500L
                )
                binding.motionAddtrainingContainer.setTransitionDuration(500)
                binding.motionAddtrainingContainer.transitionToStart()
                //when progress is 1 od 0 animation doesnt work wtf?
                binding.motionAddtrainingAdddiscardbtn.progress = 0.99f
                binding.motionAddtrainingAdddiscardbtn.transitionToStart()
                false
            } else{
                if(viewModel.trainings.value != null){
                    if(viewModel.trainings.value?.size!! > 0){
                        binding.recyclerviewTraininglist.smoothScrollToPosition(viewModel.trainings.value?.size!! - 1)
                    }
                    //wake up display so anim won't lag
                    binding.recyclerviewTraininglist.scrollBy(1,0)
                }
                animateFABColorChange(
                    requireContext(),
                    binding.btnTraininglistAddDiscard,
                    R.color.colorPrimaryDark,
                    R.color.red,
                    500L
                )
                binding.motionAddtrainingContainer.setTransitionDuration(500)
                binding.motionAddtrainingContainer.transitionToEnd()
                binding.motionAddtrainingAdddiscardbtn.transitionToEnd()
                true
            }
        }

        //the saving FAB - handle saving on click
        binding.btnTraininglistSave.setOnClickListener {
            if(!viewModel.saveNewTraining(binding.txtAddtrainingDate.text.toString(), binding.txtAddtrainingDescription.text.toString())){
                Toast.makeText(activity,getString(R.string.Fill_all_fiels), Toast.LENGTH_SHORT).show()
            }
            else{
                //if success refresh recyclerview
                viewModel.retrieveTrainings()
                if(viewModel.trainings.value != null){
                    //scroll to the top
                    binding.recyclerviewTraininglist.layoutManager?.scrollToPosition(viewModel.trainings.value?.size!! - 1)
                    //animate FAB with '+' to the beginning
                    animateFABColorChange(
                        requireContext(),
                        binding.btnTraininglistAddDiscard,
                        R.color.red,
                        R.color.colorPrimaryDark,
                        500L
                    )
                    binding.motionAddtrainingAdddiscardbtn.progress = 0.99f
                    binding.motionAddtrainingAdddiscardbtn.transitionToStart()
                    binding.motionAddtrainingContainer.setTransitionDuration(1)
                    binding.motionAddtrainingContainer.transitionToStart()

                    //clear description
                    binding.txtAddtrainingDescription.setText("")
                }
                isAddViewShown = false
            }
        }
    }

    private fun initRecyclerView() {
        //adapter
        adapter = TrainingListAdapter( viewModel, this)
        binding.recyclerviewTraininglist.adapter = adapter
        //swiping items
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView( binding.recyclerviewTraininglist)
        val layoutManager = LinearLayoutManager(activity)
        //latest items on the top
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        binding.recyclerviewTraininglist.layoutManager = layoutManager
        //when the recyclerview is ready
        binding.recyclerviewTraininglist.viewTreeObserver
            .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    //if you reach training list from the set list, animate fake list item as if it was training with specific id going back
                    if(AnimationHelper.previousFragment == Fragments.SET_LIST){
                        val mLayoutManager = binding.recyclerviewTraininglist.layoutManager as LinearLayoutManager
                        val selectedView = binding.recyclerviewTraininglist
                            .getChildAt(AnimationHelper.chosenTrainingPosition - mLayoutManager.findFirstVisibleItemPosition())
                        binding.motionTraininglistItem.progress = 0f
                        //if listitem is visible on the list, take it's parameters
                        if(selectedView != null){
                            binding.viewFakeListitem.width = selectedView.width
                            binding.viewFakeListitem.height = selectedView.height
                            binding.viewFakeListitem.x = selectedView.x
                            binding.viewFakeListitem.y = selectedView.y
                        }
                        //else animate to item "below"
                        else{
                            binding.viewFakeListitem.width = binding.recyclerviewTraininglist.width
                            binding.viewFakeListitem.height = 0
                            binding.viewFakeListitem.x = 0f
                            binding.viewFakeListitem.y = binding.recyclerviewTraininglist.bottom.toFloat()
                        }

                        binding.motionTraininglistItem.progress = 0.99f
                        binding.motionTraininglistItem.transitionToStart()
                    }
                    //update helper
                    AnimationHelper.previousFragment = Fragments.TRAINING_LIST
                    //cleanup
                    binding.recyclerviewTraininglist.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })
    }

    override fun onListFragmentClickInteraction(training: Training, position: Int) {
        this.training = training //set chosen training

        AnimationHelper.chosenTrainingPosition = position //update helper
        val layoutManager = binding.recyclerviewTraininglist.layoutManager as LinearLayoutManager
        val selectedView = binding.recyclerviewTraininglist.getChildAt(position - layoutManager.findFirstVisibleItemPosition())
        //take selected listitem and animate it growing
        binding.viewFakeListitem.width = selectedView.width
        binding.viewFakeListitem.height = selectedView.height
        binding.viewFakeListitem.x = selectedView.x
        binding.viewFakeListitem.y = selectedView.y
        binding.motionTraininglistItem.transitionToEnd()
    }

    private val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        //delete on swipe
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            //R U  SURE??
            val builder = AlertDialog.Builder(activity!!)
            builder.setTitle(getString(R.string.R_U_sure))
            builder.setMessage(getString(R.string.U_will_lose_data))

            //yes - delete
            builder.setPositiveButton(getString(R.string.Yes)) { _, _ ->
                viewModel.deleteTraining(viewModel.trainings.value?.get(viewHolder.adapterPosition)!!)
            }

            //no - items goes back
            builder.setNegativeButton(getString(R.string.No)) { _, _ -> adapter.notifyItemChanged(viewHolder.adapterPosition)}

            builder.show()
        }
    }
}