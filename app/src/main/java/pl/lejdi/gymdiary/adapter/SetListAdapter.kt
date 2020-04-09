package pl.lejdi.gymdiary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import pl.lejdi.gymdiary.R
import pl.lejdi.gymdiary.database.model.Set
import pl.lejdi.gymdiary.ui.TrainingDetailsFragment
import pl.lejdi.gymdiary.viewmodel.TrainingDetailsViewModel

class SetListAdapter constructor(private val viewModel: TrainingDetailsViewModel,
                                 private val mListener: OnListFragmentInteractionListener?)
    : RecyclerView.Adapter<SetListAdapter.ViewHolder>() {

    private val mValues = MutableLiveData<List<Set>>()

    init{
        viewModel.sets.observe(mListener as TrainingDetailsFragment, Observer {
            mValues.value=it
            notifyDataSetChanged()
        } )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.set_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues.value!![position]
        holder.mItem = item
        holder.exerciseName.text = item.exerciseName
        holder.weight.text = item.weight.toString()
        holder.repetitions.text = item.repetitions.toString()
        holder.mView.setOnClickListener {
            mListener?.onListFragmentClickInteraction(holder.mItem!!, position)
        }
    }

    override fun getItemCount(): Int {
        if(mValues.value?.size == null)
            return 0
        return mValues.value?.size!!
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val exerciseName : TextView = mView.findViewById(R.id.exercise_listitem_name)
        val weight : TextView = mView.findViewById(R.id.exercise_listitem_weight_text)
        val repetitions : TextView = mView.findViewById(R.id.exercise_listitem_repetitions_text)
        var mItem: Set? = null
    }

    interface OnListFragmentInteractionListener {
        fun onListFragmentClickInteraction(set: Set, position: Int)
    }
}