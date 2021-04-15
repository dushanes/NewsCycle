package com.newscycle

enum class Feed{
    MY_FEED, POP_FEED, TOPIC_FEED, SEARCH_FEED;

    override fun toString(): String {
        return when(this.name){
            "MY_FEED"-> "Main Feed"
            "POP_FEED"-> "Popular Feed"
            "TOPIC_FEED"-> "Topic Feed"
            "SEARCH_FEED"-> "Search Feed"

            else -> ""
        }
    }}

object Constants {
    val NEWS_KEY = BuildConfig.NEWS_KEY
    val HOME = "HOME"
    val SORT_RELEVANT = "relevancy"
    val SORT_RECENT = "RECENT"
    val SORT_POPULAR = "popularity"
    val DATE_TODAY = "TODAY"
    val DATE_WEEK = "WEEK"
    val DATE_MONTH = "MONTH"
    val values = arrayOf(
        "BUSINESS",
        "ENTERTAINMENT",
        "GENERAL",
        "HEALTH",
        "SCIENCE",
        "SPORTS",
        "TECHNOLOGY"
    )
    val general = arrayOf(
        "ABC News",
        "ABC News (AU)",
        "Al Jazeera English",
        "Associated Press",
        "Axios",
        "BBC News",
        "Breitbart News",
        "CBC News",
        "CBS News",
        "CNN",
        "Fox News",
        "Google News",
        "Google News (Australia)",
        "Google News (Canada)",
        "Google News (India)",
        "Google News (UK)",
        "Independent",
        "MSNBC",
        "National Review",
        "NBC News",
        "News24",
        "News.com.au",
        "Newsweek",
        "New York Magazine",
        "Politico",
        "Reddit /r/all",
        "Reuters",
        "RTE",
        "The American Conservative",
        "The Globe And Mail",
        "The Hill",
        "The Hindu",
        "The Huffington Post",
        "The Irish Times",
        "The Jerusalem Post",
        "The Times of India",
        "The Washington Post",
        "The Washington Times",
        "Time",
        "USA Today",
        "Vice News"
    )
    val science = arrayOf(
        "National Geographic",
        "New Scientist",
        "Next Big Future"
    )
    val sports = arrayOf(
        "BBC Sport",
        "Bleacher Report",
        "ESPN",
        "ESPN Cric Info",
        "Football Italia",
        "FourFourTwo",
        "Fox Sports",
        "NFL News",
        "NHL News",
        "TalkSport",
        "The Sport Bible"
    )

    val business = arrayOf(
        "Australian Financial Review",
        "Bloomberg",
        "Business Insider",
        "Business Insider (UK)",
        "Financial Post",
        "Fortune",
        "The Wall Street Journal"
    )
    val entertainment = arrayOf(
        "Buzzfeed",
        "Entertainment Weekly",
        "IGN",
        "Mashable",
        "MTV News",
        "MTV News (UK)",
        "Polygon",
        "The Lad Bible"
    )
    val technology = arrayOf(
        "Ars Technica",
        "Crypto Coins News",
        "Engadget",
        "Hacker News",
        "Recode",
        "TechCrunch",
        "TechRadar",
        "The Next Web",
        "The Verge",
        "Wired"
    )
}