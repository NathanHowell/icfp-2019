package icfp2019.model

sealed class Action(val ordinal: Int) : Comparable<Action> {
    fun toSolutionString(): String = when (this) {
        MoveUp -> "W"
        MoveDown -> "S"
        MoveLeft -> "A"
        MoveRight -> "D"
        DoNothing -> "Z"
        TurnClockwise -> "E"
        TurnCounterClockwise -> "Q"
        AttachFastWheels -> "F"
        StartDrill -> "L"
        PlantTeleportResetPoint -> "R"
        CloneRobot -> "C"
        is AttachManipulator -> "A(${this.point.x},${this.point.y})"
        is TeleportBack -> "T(${this.targetResetPoint.x},${this.targetResetPoint.y})"
    }

    override fun compareTo(other: Action): Int = ordinal.compareTo(other.ordinal)

    object MoveUp : Action(0)
    object MoveRight : Action(1)
    object MoveDown : Action(2)
    object MoveLeft : Action(3)
    object TurnClockwise : Action(4)
    object TurnCounterClockwise : Action(5)
    data class AttachManipulator(val point: Point) : Action(6)
    object AttachFastWheels : Action(7)
    object StartDrill : Action(8)
    object PlantTeleportResetPoint : Action(9)
    data class TeleportBack(val targetResetPoint: Point) : Action(10)
    object CloneRobot : Action(11)
    object DoNothing : Action(12)
}
