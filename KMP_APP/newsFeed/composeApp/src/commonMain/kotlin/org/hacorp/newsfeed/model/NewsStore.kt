package org.hacorp.newsfeed.model

import org.hacorp.newsfeed.data.NewsArticle
import org.hacorp.newsfeed.store_lib.InMemoryStore

class NewsStore : InMemoryStore<NewsIntent, NewsState>(
    initialState = NewsState.Loading
) {
    override fun handleIntent(intent: NewsIntent): NewsState {
        return when (intent) {
            NewsIntent.LoadNews -> {
                // In a real app, this would make an API call
                // For demo purposes, we'll return mock data
                NewsState.Success(getMockNews())
            }
            NewsIntent.RefreshNews -> {
                // Simulate refresh
                NewsState.Success(getMockNews())
            }
            is NewsIntent.LoadNewsDetail -> {
                val article = getMockNews().find { it.id == intent.articleId }
                if (article != null) {
                    NewsState.DetailLoaded(article)
                } else {
                    NewsState.Error("Article not found")
                }
            }
            NewsIntent.ClearError -> NewsState.Loading
        }
    }

    private fun getMockNews(): List<NewsArticle> {
        return listOf(
            NewsArticle(
                id = "1",
                title = "Breaking: Major Technology Breakthrough Announced",
                description = "Scientists have made a significant discovery that could revolutionize the tech industry.",
                content = "In a groundbreaking announcement today, researchers at leading technology institutes revealed a major breakthrough that promises to transform how we interact with digital devices. The discovery, which has been years in the making, represents a significant leap forward in computational efficiency and user experience.\n\nThe research team, led by Dr. Sarah Johnson, explained that this innovation could lead to devices that are not only faster but also more intuitive to use. 'We're looking at a future where technology truly understands and adapts to human needs,' Dr. Johnson stated during the press conference.\n\nEarly testing has shown promising results, with prototype devices demonstrating up to 300% improvement in processing speed while consuming 50% less energy. This combination of enhanced performance and improved efficiency could have far-reaching implications for everything from smartphones to data centers.\n\nMajor technology companies have already expressed interest in licensing this breakthrough, with several announcing plans to integrate the technology into their upcoming product lines. Industry analysts predict that consumer devices incorporating this innovation could be available as early as next year.\n\nThe research has been published in the latest issue of the Journal of Advanced Computing, and the team is scheduled to present their findings at the upcoming International Technology Conference.",
                imageUrl = "https://via.placeholder.com/400x200/0066CC/FFFFFF?text=Tech+Breakthrough",
                author = "Tech Reporter",
                publishedAt = "2024-01-15T10:30:00Z",
                source = "Tech News Daily",
                category = "Technology"
            ),
            NewsArticle(
                id = "2",
                title = "Global Climate Summit Reaches Historic Agreement",
                description = "World leaders unite on ambitious climate action plan with concrete targets.",
                content = "After days of intense negotiations, world leaders at the Global Climate Summit have reached a historic agreement that sets ambitious targets for reducing greenhouse gas emissions over the next decade. The agreement, signed by representatives from 195 countries, marks a significant step forward in the global fight against climate change.\n\nThe accord includes commitments to reduce global emissions by 45% by 2030, compared to 2019 levels, and achieve net-zero emissions by 2050. Additionally, developed nations have pledged $100 billion annually to support developing countries in their transition to clean energy.\n\n'This agreement represents hope for future generations,' said UN Climate Chief Maria Santos. 'We've moved beyond promises to concrete, measurable actions that will make a real difference.'\n\nKey provisions of the agreement include accelerated deployment of renewable energy technologies, protection of forests and biodiversity, and support for communities most vulnerable to climate impacts. The deal also establishes a new monitoring system to track progress and ensure accountability.\n\nEnvironmental groups have cautiously welcomed the agreement, though some argue that the targets, while ambitious, may still not be sufficient to limit global warming to 1.5°C above pre-industrial levels. However, most agree that this represents the most comprehensive climate action plan to date.",
                imageUrl = "https://via.placeholder.com/400x200/228B22/FFFFFF?text=Climate+Summit",
                author = "Environmental Correspondent",
                publishedAt = "2024-01-14T14:15:00Z",
                source = "Global News Network",
                category = "Environment"
            ),
            NewsArticle(
                id = "3",
                title = "Revolutionary Medical Treatment Shows Promise in Clinical Trials",
                description = "New therapy demonstrates remarkable success rates in treating previously incurable conditions.",
                content = "A revolutionary medical treatment currently in Phase III clinical trials has shown unprecedented success rates in treating conditions that were previously considered incurable. The innovative therapy, developed by a team of international researchers, represents a major breakthrough in personalized medicine.\n\nThe treatment uses advanced gene therapy techniques combined with artificial intelligence to target specific cellular mechanisms responsible for disease progression. Initial results from the clinical trials show a 85% success rate in patients who had not responded to conventional treatments.\n\n'We're witnessing a paradigm shift in how we approach treatment,' explained Dr. Michael Chen, lead researcher on the project. 'This therapy doesn't just treat symptoms – it addresses the root cause at the cellular level.'\n\nThe trials, conducted across 15 medical centers worldwide, have enrolled over 2,000 patients with various conditions. The treatment has shown particular promise in addressing autoimmune disorders, certain types of cancer, and rare genetic diseases.\n\nRegulatory agencies in several countries are fast-tracking the approval process, with the therapy potentially becoming available to patients within the next 18 months. The research team is also working on expanding the treatment's applications to other conditions.\n\nPatient advocacy groups have expressed cautious optimism about the results, emphasizing the importance of continued rigorous testing to ensure both safety and efficacy.",
                imageUrl = "https://via.placeholder.com/400x200/DC143C/FFFFFF?text=Medical+Breakthrough",
                author = "Medical Reporter",
                publishedAt = "2024-01-13T09:45:00Z",
                source = "Health Science Today",
                category = "Health"
            )
        )
    }
}

sealed interface NewsIntent {
    object LoadNews : NewsIntent
    object RefreshNews : NewsIntent
    data class LoadNewsDetail(val articleId: String) : NewsIntent
    object ClearError : NewsIntent
}

sealed interface NewsState {
    object Loading : NewsState
    data class Success(val articles: List<NewsArticle>) : NewsState
    data class DetailLoaded(val article: NewsArticle) : NewsState
    data class Error(val message: String) : NewsState
}
