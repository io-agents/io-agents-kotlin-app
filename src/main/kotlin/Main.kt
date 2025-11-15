package com.pawlowski

import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.core.agent.config.AIAgentConfig
import ai.koog.agents.core.dsl.builder.forwardTo
import ai.koog.agents.core.dsl.builder.strategy
import ai.koog.prompt.dsl.Prompt
import ai.koog.prompt.executor.clients.google.GoogleModels
import ai.koog.prompt.executor.llms.all.simpleGoogleAIExecutor
import com.pawlowski.clarification.ClarificationUseCase
import com.pawlowski.plantUml.generateUmlImage
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

suspend fun main() {
    val apiKey = System.getenv("GOOGLE_API_KEY") ?: error("Set GOOGLE_API_KEY environment variable")

    val promptExecutor = simpleGoogleAIExecutor(apiKey)

    val clarificationUseCase = ClarificationUseCase()

    val masStrategy = mainStrategy(clarificationUseCase = clarificationUseCase)

    val agentConfig =
        AIAgentConfig(
            prompt = Prompt.build("mas-io-workflow") {},
            model = GoogleModels.Gemini2_5Flash,
            maxAgentIterations = 30,
        )

    val masAgent =
        AIAgent(
            promptExecutor = promptExecutor,
            strategy = masStrategy,
            agentConfig = agentConfig,
        )

    coroutineScope {
        val result =
            async {
                masAgent.run(ExamplePrompts.promptNotEnough)
            }
        val clarificationJob =
            launch {
                clarificationUseCase.observeClarificationRequests().collect {
                    println("Clarification requested by MAS agent:")
                    println(it)
                    println("Please provide clarification:")
                    val userInput = readLine() ?: ""
                    clarificationUseCase.handleUserClarification(userInput)
                }
            }
        result.join()
        println("masAgent run ended with result: ${result.await()}")
        clarificationJob.cancel()
    }
}
