package com.mobolajialabi.tlvparser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.mobolajialabi.tlvparser.R
import com.mobolajialabi.tlvparser.viewmodel.MainViewModel
import com.mobolajialabi.tlvparser.adapter.TlvRecyclerAdapter
import com.mobolajialabi.tlvparser.databinding.ActivityMainBinding
import com.mobolajialabi.tlvparser.utils.hideKeyboard
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val vm: MainViewModel by viewModels()
    private lateinit var adapter: TlvRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerview()
        setupObservers()
        setupClickListener()
    }


    private fun setupObservers() {
        /**adds an observer for the tlvList variable in the viewmodel
         *and then updates the recyclerview adapter's list of items
         */
        lifecycleScope.launch {
            vm.tlvList.observe(this@MainActivity) {
                if (it == null) return@observe
                adapter.updateItems(it)
            }
        }

        /**adds an observer for the errorString variable in the viewmodel
         *and then updates the error field of the tlv textInputLayout
         */
        lifecycleScope.launch {
            vm.errorString.observe(this@MainActivity) {
                binding.tlvLyt.error = it
            }
        }
    }

    /**sets up the recycler view to show the list of parsed tlv data*/
    private fun setupRecyclerview() {
        adapter = TlvRecyclerAdapter(arrayListOf(), this)
        binding.tlvRecycler.adapter = adapter
    }

    /**
     * adds an onClickListener to the parseBtn
     * checks that the input field is not blank
     * checks that the inputted string has an even length
     */
    private fun setupClickListener() {
        binding.parseBtn.setOnClickListener {
            val tlvData = binding.tlvInput.text.toString()
            if (tlvData.isNotBlank()) {
                if (tlvData.length % 2 == 0) {
                    hideKeyboard(currentFocus ?: binding.root)
                    binding.tlvLyt.error = null
                    vm.parseTlv(binding.tlvInput.text.toString())
                } else binding.tlvLyt.error = getString(R.string.error_uneven_length)
            } else binding.tlvLyt.error = getString(R.string.error_blank_data)
        }
    }
}