package pl.lejdi.gymdiary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import pl.lejdi.gymdiary.database.model.Exercise
import pl.lejdi.gymdiary.databinding.ExerciseListItemBinding
import pl.lejdi.gymdiary.ui.ExerciseListFragment
import pl.lejdi.gymdiary.viewmodel.ExerciseListViewModel

class ExerciseListAdapter constructor(private val viewModel: ExerciseListViewModel,
                                      private val mListener: OnListFragmentInteractionListener)
    : RecyclerView.Adapter<ExerciseListAdapter.ViewHolder>() {

    private val mValues = MutableLiveData<List<Exercise>>()
    private lateinit var binding: ExerciseListItemBinding

    init{
        viewModel.exercises.observe(mListener as ExerciseListFragment, Observer {
            mValues.value=it
            notifyDataSetChanged()
        } )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ExerciseListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues.value!![position]
        holder.mItem = item
        holder.name.text = item.name
        holder.description.text = item.description
        holder.binding.root.setOnClickListener {
            mListener.onListFragmentClickInteraction(holder.mItem!!, position)
        }
    }

    override fun getItemCount(): Int {
        if(mValues.value?.size == null)
            return 0
        return mValues.value?.size!!
    }

    inner class ViewHolder(val binding: ExerciseListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val name = this.binding.exerciseListitemName
        val description = this.binding.exerciseListitemDescription
        var mItem: Exercise? = null
    }

    interface OnListFragmentInteractionListener {
        fun onListFragmentClickInteraction(exercise: Exercise, position: Int)
    }
}