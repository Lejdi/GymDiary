package pl.lejdi.gymdiary.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import pl.lejdi.gymdiary.R
import pl.lejdi.gymdiary.database.model.Training
import pl.lejdi.gymdiary.databinding.TrainingsListItemBinding
import pl.lejdi.gymdiary.ui.TrainingListFragment
import pl.lejdi.gymdiary.viewmodel.TrainingListViewModel

class TrainingListAdapter constructor(private val viewModel: TrainingListViewModel,
                                      private val mListener: OnListFragmentInteractionListener)
    : RecyclerView.Adapter<TrainingListAdapter.ViewHolder>() {

    private val mValues = MutableLiveData<List<Training>>()
    private lateinit var binding: TrainingsListItemBinding

    init{
        viewModel.trainings.observe(mListener as TrainingListFragment, Observer {
            mValues.value=it
            notifyDataSetChanged()
        } )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = TrainingsListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues.value!![position]
        holder.mItem = item
        holder.date.text = item.date
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

    inner class ViewHolder(val binding: TrainingsListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val date = this.binding.trainingListitemDate
        val description = this.binding.trainingListitemDescription
        var mItem: Training? = null
    }

    interface OnListFragmentInteractionListener {
        fun onListFragmentClickInteraction(training: Training, position: Int)
    }
}