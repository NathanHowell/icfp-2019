package icfp2019.strategies

import icfp2019.analyzers.FindPathsToBoostersAnalyzer
import icfp2019.core.Strategy
import icfp2019.model.Action
import icfp2019.model.Booster
import icfp2019.model.GameState
import icfp2019.model.RobotId

object SpeedBoosterStrategy : Strategy {
    override fun compute(initialState: GameState): (robotId: RobotId, state: GameState) -> List<Action> {
        val boosterAnalyzerGenerator = FindPathsToBoostersAnalyzer.analyze(initialState)
        return { robotId, state ->
            val robot = state.robot(robotId)
            if (robot.hasActiveFastWheels()) {
                listOf(Action.DoNothing)
            } else if (state.boostersAvailable(Booster.FastWheels) > 0) {
                listOf(Action.AttachFastWheels)
            } else {
                val robotPoint = state.robot(robotId).currentPosition
                val boosterAnalyzer = boosterAnalyzerGenerator(robotId, state)
                val speedBoosterAnalyzer = boosterAnalyzer(Booster.FastWheels)
                if (speedBoosterAnalyzer.size == 0) {
                    listOf(Action.DoNothing)
                } else {
                    speedBoosterAnalyzer
                        .minBy { it.length }
                        .let { listOf(robotPoint.actionToGetToNeighbor(it!!.vertexList[1].point)) }
                }
            }
        }
    }
}
