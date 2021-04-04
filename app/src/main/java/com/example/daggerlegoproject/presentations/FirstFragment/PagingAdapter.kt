package com.example.daggerlegoproject.presentations.FirstFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.daggerlegoproject.databinding.ItemFrontFragmentBinding
import com.example.daggerlegoproject.domain.modelRetrofit.Result
import com.example.daggerlegoproject.domain.modelRetrofit.RetrofitModel


class PagingAdapter :
    PagingDataAdapter<Result, PagingAdapter.PagingViewHolder>(REPO_COMPARATOR) {


    private lateinit var ItemClickPaging: (Result) -> Unit
    fun setItemClick(itemClick: (retrofitModel: Result) -> Unit) {
        this.ItemClickPaging = itemClick
    }


    inner class PagingViewHolder(var binding: ItemFrontFragmentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var title = binding.tvTitle
        var rating = binding.tvRating

        fun bind(retrofitModel: Result) {
            title.text = retrofitModel.name
            rating.text = retrofitModel.year.toString()

            Glide.with(itemView).load(retrofitModel.imageUrl
            ).into(binding.ivItemMain)

            itemView.setOnClickListener {
                ItemClickPaging.invoke(retrofitModel)
            }

        }
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Result>() {
            override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: Result,
                newItem: Result
            ): Boolean =
                oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: PagingViewHolder, position: Int) {
        var repoItem = getItem(position)
        if (repoItem != null) {
            holder.bind(repoItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagingViewHolder {
        var view =
            ItemFrontFragmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PagingViewHolder(view)
    }
}