package com.pawlowski

import ai.koog.agents.core.dsl.builder.AIAgentSubgraphBuilderBase
import ai.koog.agents.core.dsl.extension.nodeUpdatePrompt
import com.pawlowski.clarification.IClarificationUseCase

const val CLARIFICATION_PREFIX = "<<CLARIFICATION>>"

inline fun <reified Input> AIAgentSubgraphBuilderBase<*, *>.clarificableNode(clarificationUseCase: IClarificationUseCase) =
    node<Input, String>(name = "ClarificableNode") {
        llm.writeSession {
            updatePrompt {
                system(
                    """
                    Begin by thoroughly analyzing the task description provided by the user.
                        If the task description is unclear, ask the user for clarification before planning (if so, 
                        please start your response with $CLARIFICATION_PREFIX and then continue with your questions).
                        Be sure you fully understand the requirements before proceeding.
                    """.trimIndent(),
                )
            }

            var response: String
            while (true) {
                response = requestLLMWithoutTools().content

                if (CLARIFICATION_PREFIX in response.uppercase()) {
                    val clarification = clarificationUseCase.requestUserClarification(llmQuestions = response)

                    updatePrompt {
                        user("Clarification: $clarification")
                    }
                } else {
                    break
                }
            }
            response
        }
    }
