package icfp2019.strategies

import icfp2019.analyzers.GraphAnalyzer
import icfp2019.analyzers.ShortestPathUsingDijkstra
import icfp2019.core.Strategy
import icfp2019.model.Action
import icfp2019.model.GameState
import icfp2019.model.Node
import icfp2019.model.RobotId
import org.jgrapht.GraphPath
import org.jgrapht.graph.AsSubgraph
import org.jgrapht.graph.DefaultEdge

object BFSStrategy : Strategy {
    override fun compute(initialState: GameState): (robotId: RobotId, state: GameState) -> List<Action> {
        val graph = GraphAnalyzer.analyze(initialState).invoke(RobotId(0), initialState)

        return { robotId, gameState ->
            val currentPoint = gameState.robotState.values.first().currentPosition
            val currentNode = graph.vertexSet().filter { currentPoint == it.point }[0]

            val unwrappedGraph =
                AsSubgraph(graph, graph.vertexSet().filter { gameState.get(it.point).isWrapped.not() }.plus(currentNode).toSet())

            val neighbors = currentNode.point.neighbors
                .value
                .filter { gameState.isInBoard(it) }
                .map { gameState.get(it) }
                .filter { !it.isWrapped && !it.isObstacle }
                .map { currentPoint.actionToGetToNeighbor(it.point) }
            if (neighbors.any()) {
                neighbors
            } else {
                val analyze = ShortestPathUsingDijkstra.analyze(gameState)
                val shortestPathAlgorithm = analyze(robotId, gameState)

                val pathToClosestNode: GraphPath<Node, DefaultEdge> = unwrappedGraph.vertexSet()
                    .filter { it.point != currentNode.point }
                    .filter { it.isWrapped.not() }
                    .map { shortestPathAlgorithm.getPath(gameState.get(currentPoint), it) }
                    .minBy { it.length }!!

                // pathToClosestNode.vertexList[0] is `currentNode`
                val nextNode = pathToClosestNode.vertexList[1]
                listOf(currentPoint.actionToGetToNeighbor(nextNode.point))
            }
        }
    }
}
