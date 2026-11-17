package com.example.projectforwebdevelopmentmasagoesmuhammadsounika

import kotlinx.coroutines.delay

object SchoolQuotes {
    private val quotes = listOf(
        "\"The best way to predict your academic future is to create it.\" — Campus Life",
        "\"Deadlines are just milestones on the road to a finished app.\" — ISEP Dev Team",
        "\"Code, test, debug, repeat. That's the engineer's way.\" — CS Student Proverb",
        "\"A little progress each day adds up to big results.\" — Study Motivation",
        "\"Collaboration is the key to building great Android apps.\" — UniEvents Motto",
        "\"The university is a place where exploration meets purpose.\" — Campus Life",
        "\"Every lecture, late-night study session, and event builds the foundation of your journey.\" — Student Philosophy",
        "\"Balance your books, your activities, and your rest—growth happens in the spaces between.\" — Campus Wellness",
        "\"The best memories aren't just made in the lecture hall, but in the community we build together.\" — Student Experience",
        "\"Curiosity is the compass that guides every great undergraduate adventure.\" — Academic Inquiry"
    )

    suspend fun fetchRandomQuote(): String {
        // Simulating an asynchronous text fetch operation (non-Retrofit)
        delay(150)
        return quotes.random()
    }
}
