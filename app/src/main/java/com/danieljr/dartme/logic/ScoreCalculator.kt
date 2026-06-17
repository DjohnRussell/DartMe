package com.danieljr.dartme.logic

/**
 * Enkel logikk for å beregne dart-poeng.
 */
object ScoreCalculator {
    
    /**
     * Beregner ny poengsum etter et kast.
     * @param currentScore Nåværende poengsum (f.eks. 501)
     * @param pointsScored Poeng oppnådd i denne runden
     * @return Ny poengsum, eller den samme hvis man "buster" (går under 0 eller ender på 1)
     */
    fun calculateNewScore(currentScore: Int, pointsScored: Int): Int {
        val remaining = currentScore - pointsScored
        
        // Vanlig dart-regel: Man må ende på nøyaktig 0.
        return if (remaining < 0) {
            currentScore // Bust - ingen endring
        } else {
            remaining
        }
    }
    
    /**
     * Sjekker om spilleren har vunnet.
     */
    fun isWin(score: Int): Boolean {
        return score == 0
    }
}
