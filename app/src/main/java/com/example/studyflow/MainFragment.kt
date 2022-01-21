package com.example.studyflow

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainFragment : Fragment() {
    private lateinit var viewModel: MainViewModel
    private lateinit var tv0: TextView
    private lateinit var tv: TextView
    private lateinit var tv2: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val appContainer = (requireActivity().applicationContext as Application) as AppContainer

        val viewModelFactory = ViewModelFactory<SomeRepository>(appContainer.someRepository)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        lifecycleScope.launchWhenStarted {

            launch {
                viewModel.getFlow().collect {
                    tv0.text = "\n На рассылку этого текста вы подписались через Flow. Счетчик: " + it.toString()
                }
            }
            launch {
                viewModel.textStateFlow.collect {
                    tv.text = it
                }
            }
            launch {
                viewModel.textSharedFlow.collect {
                    tv2.text = "\n На рассылку этого текста вы подписались через SharedFlow. Счетчик: " + it.toString()
                }
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        tv0 = view.findViewById(R.id.tv0)
        tv = view.findViewById(R.id.tv)
        tv2 = view.findViewById(R.id.tv2)

        return view
    }
}