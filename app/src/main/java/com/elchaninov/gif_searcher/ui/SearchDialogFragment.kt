package com.elchaninov.gif_searcher.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doOnTextChanged
import com.elchaninov.gif_searcher.databinding.BottomSheetDialogLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SearchDialogFragment : BottomSheetDialogFragment() {

    private var _binding: BottomSheetDialogLayoutBinding? = null
    private val binding get() = _binding!!
    private var onSearchClickListener: OnSearchClickListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onSearchClickListener = context as? OnSearchClickListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = BottomSheetDialogLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.apply {
            searchButtonTextview.isEnabled = false
            searchButtonTextview.setOnClickListener { executeSearch() }

            searchEditText.doOnTextChanged { _, _, _, _ ->
                if (searchEditText.text != null && searchEditText.text.toString().isNotEmpty()) {
                    searchButtonTextview.isEnabled = true
                    searchInputLayout.setStartIconOnClickListener { executeSearch() }
                } else {
                    searchButtonTextview.isEnabled = false
                    searchInputLayout.setStartIconOnClickListener { }
                }
            }

            searchEditText.setOnEditorActionListener { _, actionId, _ ->
                var handled = false
                if (actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_SEARCH) {
                    executeSearch()
                    handled = true
                }
                handled
            }
        }
    }

    private fun executeSearch() {
        onSearchClickListener?.onClick(binding.searchEditText.text.toString())
        dismiss()
    }

    override fun onDestroyView() {
        onSearchClickListener = null
        super.onDestroyView()
    }

    interface OnSearchClickListener {
        fun onClick(searchWord: String)
    }

    companion object {
        fun newInstance(): SearchDialogFragment = SearchDialogFragment()
    }
}