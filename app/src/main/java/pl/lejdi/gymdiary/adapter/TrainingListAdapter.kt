package pl.lejdi.gymdiary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import pl.lejdi.gymdiary.R
import pl.lejdi.gymdiary.database.model.Training
import pl.lejdi.gymdiary.ui.TrainingListFragment
import pl.lejdi.gymdiary.viewmodel.TrainingListViewModel

class TrainingListAdapter constructor(private val viewModel: TrainingListViewModel,
                                      private val mListener: OnListFragmentInteractionListener)
    : RecyclerView.Adapter<TrainingListAdapter.ViewHolder>() {

    private val mValues = MutableLiveData<List<Training>>()

    init{
        viewModel.trainings.observe(mListener as TrainingListFragment, Observer {
            mValues.value=it
            notifyDataSetChanged()
        } )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.trainings_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues.value!![position]
        holder.mItem = item
        holder.date.text = item.date
        holder.description.text = item.description
        holder.mView.setOnClickListener {
            mListener.onListFragmentClickInteraction(holder.mItem!!, position)
        }
    }

    override fun getItemCount(): Int {
        if(mValues.value?.size == null)
            return 0
        return mValues.value?.size!!
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val date : TextView = mView.findViewById(R.id.training_listitem_date)
        val description : TextView = mView.findViewById(R.id.training_listitem_description)
        var mItem: Training? = null
    }

    interface OnListFragmentInteractionListener {
        fun onListFragmentClickInteraction(training: Training, position: Int)
    }
}