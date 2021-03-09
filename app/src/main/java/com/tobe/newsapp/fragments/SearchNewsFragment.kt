package com.tobe.newsapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tobe.newsapp.R
import com.tobe.newsapp.adapters.NewsAdapter
import com.tobe.newsapp.ui.NewsActivity
import com.tobe.newsapp.ui.viewmodel.NewsViewModel
import com.tobe.newsapp.util.Resource

import kotlinx.android.synthetic.main.fragment_breaking_news.paginationProgressBar
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {

    private lateinit var newsAdapter: NewsAdapter
    private lateinit var viewModel: NewsViewModel
    val TAG = "the random error"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setUpRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article ", it)
            }
            val action = SearchNewsFragmentDirections.actionSearchNewsFragmentToArticleFragment(it)
            findNavController().navigate(action)
            /*  findNavController().navigate(
                  R.id.action_searchNewsFragment_to_articleFragment,
                  bundle
                  )
             **/
        }


        var job: Job? = null
        search_edit.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(5000)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.searchNews(editable.toString())

                    }
                }
            }

        }

        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "An error Occurred : $message")

                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }

            }
        })
    }


    private fun hideProgressBar() {
        searchProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        searchProgressBar.visibility = View.VISIBLE
    }


    private fun setUpRecyclerView() {
        newsAdapter = NewsAdapter()
        rvSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}