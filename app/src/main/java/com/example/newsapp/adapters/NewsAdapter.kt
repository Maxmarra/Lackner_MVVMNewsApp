package com.example.newsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.databinding.ItemArticlePreviewBinding
import com.example.newsapp.models.Article
import kotlinx.android.synthetic.main.item_article_preview.view.*

class NewsAdapter(val clickListener: ArticleListener)
    : ListAdapter<Article, NewsAdapter.ArticleViewHolder>(DiffCallback) {

    class ArticleViewHolder(private val binding:ItemArticlePreviewBinding)
        : RecyclerView.ViewHolder(binding.root){
            fun bind(clickListener: ArticleListener, article: Article){
                binding.article = article
                binding.clickListener = clickListener
                binding.executePendingBindings()
            }
        }

    //при ListAdapter differ будет не нужен! или нет? посмотри SavedNewsFragment
    //val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            ItemArticlePreviewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)
        )
    }



    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = getItem(position)
        holder.bind(clickListener, article)

//        holder.itemView.apply {
//           // Glide.with(this).load(article?.urlToImage ?: R.drawable.ic_breaking_news).into(ivArticleImage)
//            //tvSource.text = article.source?.name ?: "неизвестный источник"
//            //tvTitle.text = article.title
//            //tvDescription.text = article.description ?: "нет описания"
//            //tvPublishedAt.text = article.publishedAt
////            setOnClickListener {
////                onItemClickListener?.let { it(article) }
////            }
//        }
    }

//    private var onItemClickListener: ((Article) -> Unit)? = null
//
//    fun setOnItemClickListener(listener: (Article) -> Unit) {
//        onItemClickListener = listener
//    }


    companion object DiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
}

class ArticleListener(val clickListener: (article:Article) -> Unit) {
    fun onClick(article:Article) = clickListener(article)
}