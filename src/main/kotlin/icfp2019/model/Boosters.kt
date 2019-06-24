package icfp2019.model

sealed class Booster {
    companion object {
        private val mapping = mapOf(
            'B' to ExtraArm,
            'F' to FastWheels,
            'L' to Drill,
            'X' to CloningLocation,
            'C' to CloneToken,
            'R' to Teleporter
        )

        val parseChars = mapping.keys.flatMap { listOf(it.toUpperCase(), it.toLowerCase()) }.toSet()

        fun fromChar(code: Char): Booster = when (code) {
            in parseChars -> {
                println("mapping: ${mapping}")
                println("$code and ${code.toUpperCase()}")
                mapping[code.toUpperCase()]!!
                // mapping.getValue(code.toUpperCase())
            }
            else -> throw IllegalArgumentException("Unknown booster code: '$code'")
        }
    }

    object ExtraArm : Booster()
    object FastWheels : Booster()
    object Drill : Booster()
    object Teleporter : Booster()

    object CloningLocation : Booster()
    object CloneToken : Booster()
}
