package com.pawlowski.useCase

import ai.koog.agents.core.agent.entity.ToolSelectionStrategy
import ai.koog.agents.core.dsl.builder.AIAgentGraphStrategyBuilder
import ai.koog.agents.core.dsl.builder.AIAgentSubgraphBuilderBase
import ai.koog.agents.core.dsl.builder.AIAgentSubgraphDelegate
import ai.koog.agents.core.dsl.builder.forwardTo
import ai.koog.prompt.dsl.prompt
import com.pawlowski.clarification.IClarificationUseCase
import com.pawlowski.clarification.clarificableNode
import com.pawlowski.plantUml.diagramErrorCorrectorNode
import com.pawlowski.plantUml.generateUmlImage

data class UseCaseDiagramInput(
    val plainTextUseCaseDescription: String,
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
        val setPromptNode by setUseCasePromptNode()
        val clarificationNode by clarificableNode<UseCaseDiagramInput>(clarificationUseCase)
        val generateDiagramNode by generateDiagramNode()
        val diagramCorrectorNode by diagramErrorCorrectorNode()

        edge(nodeStart forwardTo setPromptNode)
        edge(setPromptNode forwardTo clarificationNode)
        edge(clarificationNode forwardTo generateDiagramNode)
        edge(
            generateDiagramNode forwardTo diagramCorrectorNode onCondition { result ->
                result.isFailure
            },
        )
        edge(diagramCorrectorNode forwardTo generateDiagramNode)
        edge(
            generateDiagramNode forwardTo nodeFinish onCondition { result ->
                result.isSuccess
            } transformed { input ->
                UseCaseDiagramOutput(plantUmlText = input.getOrThrow())
            },
        )
    }

private fun AIAgentSubgraphBuilderBase<*, *>.setUseCasePromptNode() =
    node<UseCaseDiagramInput, UseCaseDiagramInput>("change_prompt") { input ->
        llm.writeSession {
            rewritePrompt {
                prompt(id = "Use case diagram subhraph prompt") {
                    system(
                        """
                        You are the Use Case Diagram Modeler in a Multi-Agent System (MAS) for software modeling.
                        Your goal is to create accurate and comprehensive use case diagrams based on user requirements.
                        Diagams should be in PlantUML format and only Use Case Diagrams!
                        """.trimIndent(),
                    )

                    user("Use case description: $input")
                }
            }
            input
        }
    }

private fun AIAgentSubgraphBuilderBase<*, *>.generateDiagramNode() =
    node<String, Result<String>>("use_case_diagram_generator") { input ->
        println("Generating diagram...")
        runCatching {
            generateUmlImage(
                umlSource = input,
                outputPath = "use_case_diagram.png",
            )
            println("Use case diagram generated and saved to use_case_diagram.png")
            input
        }
    }
