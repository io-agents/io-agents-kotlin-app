package useCase

import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.core.agent.config.AIAgentConfig
import ai.koog.agents.core.dsl.builder.strategy
import ai.koog.agents.testing.feature.withTesting
import ai.koog.agents.testing.tools.getMockExecutor
import ai.koog.agents.testing.tools.mockLLMAnswer
import ai.koog.prompt.dsl.Prompt
import com.pawlowski.clarification.CLARIFICATION_SYSTEM_PROMPT
import com.pawlowski.clarification.ClarificationUseCase
import com.pawlowski.plantUml.DiagramExamples
import com.pawlowski.useCase.UseCaseDiagramInput
import com.pawlowski.useCase.UseCaseDiagramOutput
import com.pawlowski.useCase.useCaseDiagramSubgraph
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class UseCaseSubgraphTest {
    private val testClarificationUseCase = ClarificationUseCase()

    @Test
    fun `useCaseDiagramSubgraph handles happy path`() {
        runBlocking {
            val plainTextUseCaseDescription = "request1"
            val mockLLMApi =
                getMockExecutor {
                    mockLLMAnswer(response = DiagramExamples.diagram1) onRequestContains CLARIFICATION_SYSTEM_PROMPT

                    mockLLMAnswer(response = "I don't know how to answer that.").asDefaultResponse
                }

            val config =
                AIAgentConfig(
                    prompt = Prompt.build(id = "testPrompt") {},
                    model = mockk(relaxed = true),
                    maxAgentIterations = 8,
                )

            val testStrategy =
                strategy<UseCaseDiagramInput, UseCaseDiagramOutput>(name = "useCaseDiagramTestStrategy") {
                    val useCaseDiagramSubgraph by useCaseDiagramSubgraph(clarificationUseCase = testClarificationUseCase)

                    nodeStart then useCaseDiagramSubgraph then nodeFinish
                }

            val masAgent =
                AIAgent.Companion(
                    promptExecutor = mockLLMApi,
                    strategy = testStrategy,
                    agentConfig = config,
                ) {
                    withTesting()
                }

            val result =
                masAgent.run(
                    agentInput =
                        UseCaseDiagramInput(
                            plainTextUseCaseDescription = plainTextUseCaseDescription,
                        ),
                )

            result shouldBe
                UseCaseDiagramOutput(
                    plantUmlText = DiagramExamples.diagram1,
                )
        }
    }

    @Test
    fun `useCaseDiagramSubgraph handles autocorrection for wrong diagram formats`() {
        runBlocking {
            val plainTextUseCaseDescription = "request1"
            val mockLLMApi =
                getMockExecutor {
                    mockLLMAnswer(response = DiagramExamples.incorrectDiagram1) onRequestContains CLARIFICATION_SYSTEM_PROMPT
                    mockLLMAnswer(response = DiagramExamples.diagram1) onRequestContains "Diagram generation failed"

                    mockLLMAnswer(response = "I don't know how to answer that.").asDefaultResponse
                }

            val config =
                AIAgentConfig(
                    prompt = Prompt.build(id = "testPrompt") {},
                    model = mockk(relaxed = true),
                    maxAgentIterations = 10,
                )

            val testStrategy =
                strategy<UseCaseDiagramInput, UseCaseDiagramOutput>(name = "useCaseDiagramTestStrategy") {
                    val useCaseDiagramSubgraph by useCaseDiagramSubgraph(clarificationUseCase = testClarificationUseCase)

                    nodeStart then useCaseDiagramSubgraph then nodeFinish
                }

            val masAgent =
                AIAgent.Companion(
                    promptExecutor = mockLLMApi,
                    strategy = testStrategy,
                    agentConfig = config,
                ) {
                    withTesting()
                }

            val result =
                masAgent.run(
                    agentInput =
                        UseCaseDiagramInput(
                            plainTextUseCaseDescription = plainTextUseCaseDescription,
                        ),
                )

            result shouldBe
                UseCaseDiagramOutput(
                    plantUmlText = DiagramExamples.diagram1,
                )
        }
    }
}
