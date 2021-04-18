package com.newscycle.data

import android.util.Log
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.newscycle.BuildConfig
import com.newscycle.Feed
import com.newscycle.api.ApiUtilities
import com.newscycle.data.models.ArticleModel
import io.reactivex.rxjava3.core.Single
import retrofit2.HttpException
import java.io.IOException

class ArticlePagingSource(private val query: String, private val sortBy: String, private val fromDate: String, private val FEED_TAG: Feed): RxPagingSource<Int, ArticleModel>() {
    private val apiUtilities: ApiUtilities by lazy { ApiUtilities }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, ArticleModel>> {
        Log.d("loading list", "page: ${params.key}")
        //on first load key is null, subsequent loads next key in load result is used
        val position: Int = params.key?.toInt() ?: 1

        return when(FEED_TAG){
            Feed.POP_FEED -> apiUtilities.getTopHeadlines(BuildConfig.NEWS_KEY, page = position, )
            Feed.SEARCH_FEED -> apiUtilities.searchTopic(BuildConfig.NEWS_KEY, q = query, page = position, sortBy = sortBy, fromDate = fromDate)
            Feed.TOPIC_FEED -> apiUtilities.getCategoryHeadlines(BuildConfig.NEWS_KEY, page = position, category = query)
            Feed.MY_FEED -> apiUtilities.getTopHeadlines(BuildConfig.NEWS_KEY, page = position)
        }.map<LoadResult<Int, ArticleModel>> { result ->
            LoadResult.Page(
                data = result.articles,
                prevKey = if (position == 1) null else position-1,
                nextKey = position  +1
            )
        }.onErrorReturn { e ->
            when (e) {
                // Retrofit calls that return the body type throw either IOException for
                // network failures, or HttpException for any non-2xx HTTP status codes.
                // This code reports all errors to the UI, but you can inspect/wrap the
                // exceptions to provide more context.
                is IOException -> LoadResult.Error(e)
                is HttpException -> LoadResult.Error(e)
                else -> throw e
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ArticleModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}