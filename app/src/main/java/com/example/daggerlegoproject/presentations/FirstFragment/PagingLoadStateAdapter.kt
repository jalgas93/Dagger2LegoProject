package com.example.daggerlegoproject.presentations.FirstFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.daggerlegoproject.databinding.ItemLoadingStateBinding

class PagingLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<PagingLoadStateAdapter.PagingLoadStateViewHolder>() {

    class PagingLoadStateViewHolder(
        private val binding: ItemLoadingStateBinding,
        retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.retryButton.also {
                it.setOnClickListener {
                    retry.invoke()
                }
            }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.errorMsg.text = loadState.error.localizedMessage
            }

            binding.retryButton.isVisible = loadState is LoadState.Loading
            binding.errorMsg.isVisible = loadState !is LoadState.Loading
            binding.progressBar.isVisible = loadState !is LoadState.Loading
        }

        companion object {
            fun create(parent: ViewGroup, retry: () -> Unit): PagingLoadStateViewHolder {
//            val view = LayoutInflater.from(parent.context)
//                .inflate(R.layout.loading_error_state_paging, parent, false)
                val view = ItemLoadingStateBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                //val binding = LoadingErrorStatePagingBinding.bind(view)
                return PagingLoadStateViewHolder(view, retry)
            }
        }

    }



    override fun onBindViewHolder(holder: PagingLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): PagingLoadStateViewHolder {
        return PagingLoadStateViewHolder.create(parent,retry)

    }



}