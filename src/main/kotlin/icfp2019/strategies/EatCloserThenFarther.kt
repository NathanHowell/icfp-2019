package icfp2019.strategies

import icfp2019.analyzers.DistanceToWalls
import icfp2019.core.Strategy
import icfp2019.core.applyAction
import icfp2019.model.Action
import icfp2019.model.GameState
import icfp2019.model.RobotId

object EatCloserThenFarther : Strategy {
    override fun compute(initialState: GameState): (robotId: RobotId, state: GameState) -> Action {
        val distanceToWallsAnalyzer = DistanceToWalls().analyze(initialState)
        return { robotId, state ->
            // val currentDistance = distanceToWallsAnalyzer(0, state)

            val allMoves = listOf(
                    0 to (Action.MoveUp to state.robotState.getValue(robotId).currentPosition.up()),
                    1 to (Action.MoveRight to state.robotState.getValue(robotId).currentPosition.right()),
                    2 to (Action.MoveDown to state.robotState.getValue(robotId).currentPosition.down()),
                    3 to (Action.MoveLeft to state.robotState.getValue(robotId).currentPosition.left()))

            // [Index, GameState]
            val movesWithinGameboard = allMoves
                .filter { state.mapSize.pointInMap(it.second.second) }
                .map { it.first to applyAction(state, robotId, it.second.first) }

            val movesAvoidingObstacles = movesWithinGameboard
                .map { it.first to distanceToWallsAnalyzer(robotId, it.second) }
                .filter { it.second != DistanceToWalls.obstacleIdentifier }

            // Deal with wrapped vs unwrapped. If all wrapped, go for the largest.
            // Else, go for the smallest.
            val allWrapped = movesAvoidingObstacles
                .map { it.first }
                .none { !state.get(allMoves[it].second.second).isWrapped }

            val result = if (allWrapped) {
                movesAvoidingObstacles.maxBy { it.second }
            } else {
                movesAvoidingObstacles
                    .filter { !state.get(allMoves[it.first].second.second).isWrapped }
                    .minBy { it.second }
            }

            if (result != null) {
                allMoves[result.first].second.first
            } else {
                Action.DoNothing
            }
        }
    }
}
