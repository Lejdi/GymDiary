package pl.lejdi.gymdiary.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import pl.lejdi.gymdiary.R
import pl.lejdi.gymdiary.database.model.Training

class TrainingListAdapter constructor(private var mValues: List<Training>,
                                      private val mListener: OnListFragmentInteractionListener)
    : RecyclerView.Adapter<TrainingListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.trainings_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mItem = item
        holder.date.setText(item.date)
        holder.description.setText(item.description)
        holder.mView.setOnClickListener {
            mListener.onListFragmentClickInteraction(holder.mItem!!, position)
        }
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val date : EditText = mView.findViewById(R.id.training_listitem_date)
        val description : EditText = mView.findViewById(R.id.training_listitem_description)
        var mItem: Training? = null
    }

    interface OnListFragmentInteractionListener {
        fun onListFragmentClickInteraction(training: Training, position: Int)
    }
}