package pl.lejdi.gymdiary.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import pl.lejdi.gymdiary.R
import pl.lejdi.gymdiary.viewmodel.TrainingListViewModel

class TrainingListFragment : Fragment() {
    private lateinit var viewModel : TrainingListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.trainings_list_fragment, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProviders.of(this).get(TrainingListViewModel::class.java)
    }
}