package com.pawlowski.acceptance

import ai.koog.agents.core.dsl.builder.AIAgentSubgraphBuilderBase

const val ACCEPTANCE = "ACCEPT"

val ACCEPTANCE_SYSTEM_PROMPT = "Please answer with either only 'ACCEPT' or instructions on what is missing to ACCEPT."

inline fun <reified Input> AIAgentSubgraphBuilderBase<*, *>.AccpetanceNode(userAcceptance: IAcceptance) =
    node<Input, String>(name = "AcceptanceNode") {
        llm.writeSession {
            val response: String
            val acceptance: String
            val userAcceptanceVerdict: Boolean

            response = requestLLMWithoutTools().content
            acceptance = userAcceptance.requestUserClarification(llmResult = response+ACCEPTANCE_SYSTEM_PROMPT)
            userAcceptanceVerdict = (response.uppercase() in ACCEPTANCE)
            
            if (!userAcceptanceVerdict) {
                updatePrompt {
                    user("Improve: $acceptance")
                }
            }

            AcceptanceResult(response = response, accepted = userAcceptanceVerdict)
        }
    }