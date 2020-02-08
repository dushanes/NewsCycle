package com.newscycle

import io.reactivex.Single

interface NewsApiUtil{
    fun getTopHeadlines(): Single<Results>
}