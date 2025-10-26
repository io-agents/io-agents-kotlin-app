package com.pawlowski

import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.core.agent.config.AIAgentConfig
import ai.koog.agents.core.dsl.builder.forwardTo
import ai.koog.agents.core.dsl.builder.strategy
import ai.koog.prompt.dsl.Prompt
import ai.koog.prompt.executor.clients.google.GoogleModels
import ai.koog.prompt.executor.llms.all.simpleGoogleAIExecutor
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

suspend fun main() {
    val apiKey = System.getenv("GOOGLE_API_KEY") ?: error("Set GOOGLE_API_KEY environment variable")

    val promptExecutor = simpleGoogleAIExecutor(apiKey)

    val masStrategy =
        strategy<String, String>("MAS-workflow") {
            val managerPlanNode by node<String, String>("manager_plan") { input ->
                llm.writeSession {
                    updatePrompt {
                        system(
                            """
                            You are the ManagerAgent in a Multi-Agent System (MAS) for software modeling.
                            Your goal is to create a detailed workflow plan for the given task description.
                            Include which agents should be involved and in what order.
                            If the task description is unclear, ask the user for clarification before planning (if so, please include word "clarify" in your response.
                            Be sure you fully understand the task before proceeding (e.g. is it mobile app or backend).
                            """.trimIndent(),
                        )

                        user("Task description: $input")
                    }

                    var response = requestLLMWithoutTools()

                    if ("clarify" in response.content.lowercase()) {
                        println("🤖 ManagerAgent: ${response.content}")
                        println("🧑 Please clarify: ")
                        // clarificationRequestChannel.send(Unit)
                        val clarification = readLine() // clarificationChannel.receive()

                        updatePrompt {
                            user("Clarification: $clarification")
                        }
                        response = requestLLMWithoutTools()
                    }

                    response.content
                }
            }

            edge(nodeStart forwardTo managerPlanNode)
            edge(managerPlanNode forwardTo nodeFinish)
        }

    val agentConfig =
        AIAgentConfig(
            prompt = Prompt.build("mas-io-workflow") {},
            model = GoogleModels.Gemini2_5Flash,
            maxAgentIterations = 10,
        )

    val masAgent =
        AIAgent(
            promptExecutor = promptExecutor,
            strategy = masStrategy,
            agentConfig = agentConfig,
        )

    coroutineScope {
        val result = masAgent.run("Stwórz diagram aktywności dla procesu rejestracji użytkownika.")
        println("masAgent run ended with result:")
        println(result)
    }
}
