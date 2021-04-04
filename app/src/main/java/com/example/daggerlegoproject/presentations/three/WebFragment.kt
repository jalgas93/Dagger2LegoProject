package com.example.daggerlegoproject.presentations.three

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.example.daggerlegoproject.R
import com.example.daggerlegoproject.databinding.FragmentWebBinding


class WebFragment : Fragment() {

    private var _binding:FragmentWebBinding? = null
      private val mBinding get() = _binding!!
    private val args:WebFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWebBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.webView.apply {
            webViewClient = WebViewClient()
            Log.i("jalgas4",args.url.toString())
            args.url?.let { loadUrl(it) }
        }
    }


}