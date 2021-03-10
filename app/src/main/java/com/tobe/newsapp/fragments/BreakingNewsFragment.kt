package com.tobe.newsapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tobe.newsapp.R
import com.tobe.newsapp.adapters.NewsAdapter
import com.tobe.newsapp.ui.NewsActivity
import com.tobe.newsapp.ui.viewmodel.NewsViewModel
import com.tobe.newsapp.util.Resource
import kotlinx.android.synthetic.main.fragment_breaking_news.*

class BreakingNewsFragment: Fragment(R.layout.fragment_breaking_news) {
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var viewModel : NewsViewModel
    val TAG = "the random error"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setUpRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article ", it)
            }
            val action = BreakingNewsFragmentDirections.actionBreakingNewsFragmentToArticleFragment(it)
            findNavController().navigate(action)
          /*  findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
                )
           **/
        }



        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success ->{
                    hideProgressBar()
                    response.data?.let { newsResponse->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }

                is Resource.Error ->{
                    hideProgressBar()
                    response.message?.let { message->
                      Toast.makeText(activity,"An error Occured : $message", Toast.LENGTH_SHORT).show()

                    }
                }
                is Resource.Loading ->{
                    showProgressBar()
                }

            }
        })


    }

    private fun hideProgressBar(){
        paginationProgressBar.visibility = View.INVISIBLE
    }
    private fun showProgressBar(){
        paginationProgressBar.visibility = View.VISIBLE
    }


    private fun setUpRecyclerView(){
        newsAdapter = NewsAdapter()
        rvBreakingNews.apply {
            adapter =  newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}