package pl.lejdi.gymdiary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import pl.lejdi.gymdiary.database.model.Set
import pl.lejdi.gymdiary.databinding.ViewSetListItemBinding
import pl.lejdi.gymdiary.ui.SetListFragment
import pl.lejdi.gymdiary.viewmodel.SetListViewModel

class SetListAdapter constructor(private val viewModel: SetListViewModel,
                                 private val mListener: OnListFragmentInteractionListener?)
    : RecyclerView.Adapter<SetListAdapter.ViewHolder>() {

    private val mValues = MutableLiveData<List<Set>>()

    private lateinit var binding: ViewSetListItemBinding

    init{
        viewModel.sets.observe(mListener as SetListFragment, Observer {
            mValues.value=it
            notifyDataSetChanged()
        } )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ViewSetListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues.value!![position]
        holder.mItem = item
        holder.exerciseName.text = item.exerciseName
        holder.weight.text = item.weight.toString()
        holder.repetitions.text = item.repetitions.toString()
        holder.binding.root.setOnClickListener {
            mListener?.onListFragmentClickInteraction(holder.mItem!!, position)
        }
    }

    override fun getItemCount(): Int {
        if(mValues.value?.size == null)
            return 0
        return mValues.value?.size!!
    }

    inner class ViewHolder(val binding: ViewSetListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val exerciseName = this.binding.txtSetlistitemName
        val weight = this.binding.txtSetlistitemWeight
        val repetitions = this.binding.txtSetlistitemReps
        var mItem: Set? = null
    }

    interface OnListFragmentInteractionListener {
        fun onListFragmentClickInteraction(set: Set, position: Int)
    }
}