package com.pawlowski

import ai.koog.agents.core.agent.AIAgent
import ai.koog.prompt.executor.clients.google.GoogleModels
import ai.koog.prompt.executor.llms.all.simpleGoogleAIExecutor

suspend fun main() {
    val apiKey = System.getenv("GOOGLE_API_KEY") ?: error("Set GOOGLE_API_KEY environment variable")

    // Create an agent
    val agent =
        AIAgent(
            promptExecutor = simpleGoogleAIExecutor(apiKey),
            llmModel = GoogleModels.Gemini2_5Pro,
        )

    // Run the agent
    val result = agent.run("Hello! How can you help me?")
    println(result)
}
