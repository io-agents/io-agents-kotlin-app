package com.pawlowski.SAD


data class SADInput(
    val plantUmlText: String,
)

data class SADOutput(
    val scenariosText: String,
    val activitiesText: String,
)

fun AIAgentGraphStrategyBuilder<*, *>.SADSubgraph()