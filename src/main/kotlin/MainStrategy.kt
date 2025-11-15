package com.pawlowski

import ai.koog.agents.core.dsl.builder.strategy
import com.pawlowski.clarification.IClarificationUseCase
import com.pawlowski.useCase.UseCaseDiagramInput
import com.pawlowski.useCase.UseCaseDiagramOutput
import com.pawlowski.useCase.useCaseDiagramSubgraph

fun mainStrategy(clarificationUseCase: IClarificationUseCase) =
    strategy<UseCaseDiagramInput, UseCaseDiagramOutput>("MAS-workflow") {
        val useCaseDiagramSubgraph by useCaseDiagramSubgraph(clarificationUseCase = clarificationUseCase)

        nodeStart then useCaseDiagramSubgraph then nodeFinish
    }
