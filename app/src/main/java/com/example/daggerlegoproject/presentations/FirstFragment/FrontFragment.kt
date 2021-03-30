package com.example.daggerlegoproject.presentations.FirstFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.daggerlegoproject.R
import com.example.daggerlegoproject.databinding.FragmentFrontBinding
import com.example.daggerlegoproject.di.Injectable
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


class FrontFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val frontViewModel: FrontViewModel by viewModels { viewModelFactory }

    private var _binding: FragmentFrontBinding? = null
    private val mBinding get() = _binding!!

    private var searchJob: Job? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFrontBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //frontViewModel.legoSet(2,"")
        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: DEFUALT_QUERY
        setupRecyclerView()
        search("Pizza")

    }

    private fun search(query: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            frontViewModel.searchRepo(query).collectLatest {
                Log.i("jalgas1", it.toString())
            }
        }


    }

    private fun setupRecyclerView() {
//        frontViewModel.liveData.observe(requireActivity(), Observer {
//            Log.i("jalgas",it.results.toString())
//        })
    }

    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
        private const val DEFUALT_QUERY = ""
    }


}