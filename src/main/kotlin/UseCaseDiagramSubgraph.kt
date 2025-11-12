package com.pawlowski

import ai.koog.agents.core.agent.entity.ToolSelectionStrategy
import ai.koog.agents.core.dsl.builder.AIAgentGraphStrategyBuilder
import ai.koog.agents.core.dsl.builder.AIAgentSubgraphBuilderBase
import ai.koog.agents.core.dsl.builder.AIAgentSubgraphDelegate
import ai.koog.agents.core.dsl.builder.forwardTo
import com.pawlowski.clarification.IClarificationUseCase

data class UseCaseDiagramInput(
    val plainTextUseCaseDesciption: String,
)

data class UseCaseDiagramOutput(
    val plantUmlText: String,
)

fun AIAgentGraphStrategyBuilder<*, *>.useCaseDiagramSubgraph(
    clarificationUseCase: IClarificationUseCase,
): AIAgentSubgraphDelegate<UseCaseDiagramInput, UseCaseDiagramOutput> =
    subgraph<UseCaseDiagramInput, UseCaseDiagramOutput>(
        name = "UseCaseDiagramSubgraph",
        toolSelectionStrategy = ToolSelectionStrategy.NONE,
    ) {
        val changePromptToModeler by modelerPromptNode()
        val clarificationNode by clarificableNode<UseCaseDiagramInput>(clarificationUseCase)

        edge(nodeStart forwardTo changePromptToModeler)
        edge(changePromptToModeler forwardTo clarificationNode)
        edge(
            clarificationNode forwardTo nodeFinish transformed { input ->
                UseCaseDiagramOutput(plantUmlText = input)
            },
        )
    }

private fun AIAgentSubgraphBuilderBase<*, *>.modelerPromptNode() =
    node<UseCaseDiagramInput, UseCaseDiagramInput>("manager_plan") { input ->
        llm.writeSession {
            updatePrompt {
                system(
                    """
                    You are the Use Case Diagram Modeler in a Multi-Agent System (MAS) for software modeling.
                    Your goal is to create accurate and comprehensive use case diagrams based on user requirements.
                    Diagams should be in PlantUML format and only Use Case Diagrams!
                    """.trimIndent(),
                )

                user("Use case description: $input")
            }
            input
        }
    }
