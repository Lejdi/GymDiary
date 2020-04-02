package pl.lejdi.gymdiary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.lejdi.gymdiary.R
import pl.lejdi.gymdiary.database.model.Set

class SetListAdapter constructor(private var mValues: List<Set>,
                                 private val mListener: OnListFragmentInteractionListener?)
    : RecyclerView.Adapter<SetListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.training_exercises_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mItem = item
        holder.exerciseName.text = item.exerciseName
        holder.weight.text = item.weight.toString()
        holder.repetitions.text = item.weight.toString()
        holder.mView.setOnClickListener {
            mListener?.onListFragmentClickInteraction(holder.mItem!!, position)
        }
    }

    override fun getItemCount(): Int {
        return mValues.size
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