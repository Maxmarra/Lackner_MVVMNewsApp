package com.example.newsapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.ui.NewsActivity
import com.example.newsapp.ui.NewsViewModel
import com.example.newsapp.util.Constants.Companion.QUERY_PAGE_SIZE
import com.example.newsapp.util.Resource
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import kotlinx.android.synthetic.main.item_error_message.*

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    val TAG = "BreakingNewsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )
        }

        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    hideErrorMessage()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())

                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.breakingNewsPage == totalPages

                        if(isLastPage){
                                //это из-за свойств android:clipToPadding="false" в xml файле
                                //она рисетит паддинг, чтобы у прогрессбара было свое пространство
                                //чтобы не залазить на RecyclerView
                                rvBreakingNews.setPadding(0, 0, 0, 0)

                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        //Log.e(TAG, "An error occured: $message")
                        Toast.makeText(activity, getString(R.string.resource_error_with_message, message), Toast.LENGTH_LONG).show()
                        showErrorMessage(message)
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

        btnRetry.setOnClickListener {
            viewModel.getBreakingNews()
        }
    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideErrorMessage() {
        itemErrorMessage.visibility = View.INVISIBLE
        isError = false
    }

    private fun showErrorMessage(message: String) {
        itemErrorMessage.visibility = View.VISIBLE
        tvErrorMessage.text = message
        isError = true
    }

    var isError = false
    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            //это чтобы точно показать последнюю страницу!
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            //чекаем прокрутили ли мы хоть чуть чуть
            val isNotAtBeginning = firstVisibleItemPosition >= 0

            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            //только если указанные выше булены true - мы делаем пагинацию

            val shouldPaginate = isNotLoadingAndNotLastPage
                        && isAtLastItem
                        && isNotAtBeginning
                        && isTotalMoreThanVisible
                        && isScrolling
            if(shouldPaginate) {
                viewModel.getBreakingNews()
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            //если дал такое же название как и у свойства самого RecyclerView
            //то можно обратиться к этому названию таким образом
            addOnScrollListener(this@BreakingNewsFragment.scrollListener)
            //либо поменять название
            //addOnScrollListener(myScrollListener)
        }
    }
}