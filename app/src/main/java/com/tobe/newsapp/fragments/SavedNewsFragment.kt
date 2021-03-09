package com.tobe.newsapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tobe.newsapp.R
import com.tobe.newsapp.adapters.NewsAdapter
import com.tobe.newsapp.ui.NewsActivity
import com.tobe.newsapp.ui.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.fragment_breaking_news.*


class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {

    private lateinit var newsAdapter: NewsAdapter
    private lateinit var viewModel: NewsViewModel


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