package com.example.bottomnavi.homefragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bottomnavi.BuildConfig
import com.example.bottomnavi.R
import com.example.bottomnavi.databinding.FragmentHomeBinding
import com.example.bottomnavi.retrofit.Contract
import com.example.bottomnavi.retrofit.NetWorkClient
import com.example.bottomnavi.retrofit.Snippet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: VideoAdapter
    companion object{
        var videoList: ArrayList<MyVideoItems> = ArrayList()
    }
    private val viewModel by lazy{
        ViewModelProvider(this)[HomeViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        initView()
        initViewModel()
        return binding.root
    }

    private fun initViewModel() = with(viewModel){
        searchParam.observe(viewLifecycleOwner){
            communicateNetWork(it)
        }
        searchResult.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }
    }

    private fun initView() {
        spinnerSetting()
        adapter = VideoAdapter { position, videoItem ->

        }
        binding.videoRecycler.adapter = adapter
        binding.videoRecycler.layoutManager = GridLayoutManager(context, 2)
        viewModel.setUpVideoParameter()
    }

    private fun spinnerSetting() {
        val items = resources.getStringArray(R.array.tag_array)
        val spinnerAdapter: ArrayAdapter<String> = object : ArrayAdapter<String>(
            requireContext(),
            R.layout.custom_spinner_item,
            R.id.spinner_text,
            items
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val textView = view.findViewById<TextView>(R.id.spinner_text)
                textView.gravity = Gravity.CENTER
                return view
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent)
                val textView = view.findViewById<TextView>(R.id.spinner_text)
                val image = view.findViewById<ImageView>(R.id.spinner_arrow)
                image.visibility = INVISIBLE
                textView.gravity = Gravity.CENTER
                return view
            }
        }
        binding.spinner.adapter = spinnerAdapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {

                    }

                    1 -> {

                    }

                    2 -> {

                    }

                    3 -> {

                    }

                    else -> {

                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}