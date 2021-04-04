package com.example.daggerlegoproject.presentations.secontFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.daggerlegoproject.R
import com.example.daggerlegoproject.databinding.FragmentDetailBinding
import com.example.daggerlegoproject.presentations.FirstFragment.FrontFragmentDirections
import kotlinx.android.synthetic.main.fragment_detail.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val mBinding get() = _binding!!
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
        toolbar()
    }

    private fun toolbar() {
        mBinding.toolbar.title = args.modelResult?.name
        mBinding.ivToolbar.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setup() {

        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val date =
            dateFormat.parse(args.modelResult?.lastModifiedDate)
        val formatter =
            SimpleDateFormat("yyyy-MM-dd") //If you need time just put specific format for time like ‘HH:mm:ss’
        val dateStr = formatter.format(date)

        mBinding.tvData.text = dateStr
        mBinding.tvDetailName.text = args.modelResult?.name
        Glide.with(requireContext()).load(args.modelResult?.imageUrl).into(mBinding.image)
        mBinding.tvCountDetail.append(args.modelResult?.id)
        mBinding.image.setOnClickListener {
            var action = DetailFragmentDirections.actionDetailFragmentToWebFragment(args.modelResult?.url)
            view?.let { it1 -> Navigation.findNavController(it1).navigate(action) }
        }
    }


}