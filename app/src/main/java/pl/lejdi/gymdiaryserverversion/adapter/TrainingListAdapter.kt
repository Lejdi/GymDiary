package pl.lejdi.gymdiaryserverversion.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import pl.lejdi.gymdiaryserverversion.database.model.Training
import pl.lejdi.gymdiaryserverversion.databinding.ViewTrainingsListItemBinding
import pl.lejdi.gymdiaryserverversion.ui.TrainingListFragment
import pl.lejdi.gymdiaryserverversion.viewmodel.TrainingListViewModel

class TrainingListAdapter constructor(private val viewModel: TrainingListViewModel,
                                      private val mListener: OnListFragmentInteractionListener)
    : RecyclerView.Adapter<TrainingListAdapter.ViewHolder>() {

    private val mValues = MutableLiveData<List<Training>>()
    private lateinit var binding: ViewTrainingsListItemBinding

    init{
        viewModel.trainings.observe(mListener as TrainingListFragment, Observer {
            mValues.value=it
            notifyDataSetChanged()
        } )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ViewTrainingsListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    inner class ViewHolder(val binding: ViewTrainingsListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val date = this.binding.txtTrainingslistDate
        val description = this.binding.txtTrainingslistDescription
        var mItem: Training? = null
    }

    interface OnListFragmentInteractionListener {
        fun onListFragmentClickInteraction(training: Training, position: Int)
    }
}