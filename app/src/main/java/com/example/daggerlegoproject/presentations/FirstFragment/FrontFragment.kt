package com.example.daggerlegoproject.presentations.FirstFragment

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.daggerlegoproject.databinding.FragmentFrontBinding
import com.example.daggerlegoproject.di.Injectable
import com.example.daggerlegoproject.retrofit.IN_QUALIFIER
import com.example.daggerlegoproject.retrofit.RetrofitService
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


class FrontFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val frontViewModel: FrontViewModel by viewModels { viewModelFactory }
    private var _binding: FragmentFrontBinding? = null
    private val mBinding get() = _binding!!
    private var searchJob: Job? = null
    var pagingAdapter = PagingAdapter()


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
        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: DEFUALT_QUERY
        Search(query)
        setupRecyclerView()
        initSearch(query)
         //mBinding.retryButton.setOnClickListener { pagingAdapter.retry() }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_SEARCH_QUERY, mBinding.etSearch.text?.trim().toString())
    }

    private fun initSearch(query: String) {
        mBinding.etSearch.setText(query)
        mBinding.etSearch.setOnEditorActionListener { textView, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                updateRepoListFromInput()
                true
            } else {
                false
            }
        }
        mBinding.etSearch.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateRepoListFromInput()
                true
            } else {
                false
            }
        }

        lifecycleScope.launch {
            pagingAdapter.loadStateFlow
                .distinctUntilChangedBy {
                    it.refresh
                }
                .filter {
                    it.refresh is LoadState.NotLoading
                }
                .collect {
                    mBinding.rvMainFragment.scrollToPosition(0)
                }
        }
    }

    private fun updateRepoListFromInput() {
        mBinding.etSearch.text!!.trim().let {
            if (it.isNotEmpty()) {
                mBinding.rvMainFragment.scrollToPosition(0)
                Search(it.toString())
            } else {

                Search("")
            }
        }
    }


    private fun Search(query: String) {
        mBinding.rvMainFragment.apply {
            this.adapter = pagingAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            frontViewModel.searchRepo(query).collectLatest {
                pagingAdapter.submitData(it)


            }
        }

        pagingAdapter.setItemClick {
            var a = it.imageUrl
            var w = it.lastModifiedDate
            var e = it.name
            var r = it.numParts
            var t = it.themeId
            var y = it.url
            var u = it.year

            var action = FrontFragmentDirections.actionFrontFragmentToDetailFragment(it)
            view?.let { it1 -> Navigation.findNavController(it1).navigate(action) }
        }
    }

    private fun setupRecyclerView() {

//        pagingAdapter.addLoadStateListener {
//            mBinding.rvMainFragment.isVisible = it.source.refresh is LoadState.NotLoading
//            mBinding.progressBar.isVisible = it.source.refresh is LoadState.Loading
//            mBinding.retryButton.isVisible = it.source.refresh is LoadState.Error
//
//
//            val errorState = it.source.append as? LoadState.Error
//                ?: it.source.prepend as? LoadState.Error
//                ?: it.append as? LoadState.Error
//                ?: it.prepend as? LoadState.Error
//            errorState?.let {
//                Toast.makeText(requireContext(),"LoadState", Toast.LENGTH_SHORT).show()
//            }
//
//        }
    }

    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
        private const val DEFUALT_QUERY = " "
    }
}