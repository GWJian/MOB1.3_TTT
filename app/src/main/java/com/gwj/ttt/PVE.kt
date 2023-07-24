package com.gwj.ttt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.gwj.ttt.databinding.ActivityPveBinding

class PVE : AppCompatActivity() {

    enum class Turn {
        NOUGHT,
        CROSS
    }

    //player names
    private var playerOne: String = "Player One"

    //computer name
    private var computer: String = "Computer"

    //turn
    private var firstTurn = Turn.CROSS
    private var curretTurn = Turn.CROSS

    //player score
    private var crossScore = 0
    private var noughtScore = 0

    //board
    private var boardList = mutableListOf<Button>()
    lateinit var binding: ActivityPveBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPveBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getPlayerNames()
        initBoard()

        binding.btnBack2.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
    //=================================================================
    //=================================================================
    //=================================================================
    //=================================================================

    //==================== getPlayerNames Start ====================
    private fun getPlayerNames() {
        playerOne = intent.getStringExtra("PLAYER_ONE") ?: ""

        setTurnLable()
    }
    //==================== getPlayerNames End ====================

    //==================== initBoard Start ====================
    private fun initBoard() {
        boardList.add(binding.a1)
        boardList.add(binding.a2)
        boardList.add(binding.a3)
        boardList.add(binding.b1)
        boardList.add(binding.b2)
        boardList.add(binding.b3)
        boardList.add(binding.c1)
        boardList.add(binding.c2)
        boardList.add(binding.c3)
    }
    //==================== initBoard End ====================

    //===================== boardTapped Start =====================
    fun boardTapped(view: View) {
        val button = view as Button
        var player = ""

        if (curretTurn == Turn.CROSS) {
            player = "X"
            curretTurn = Turn.NOUGHT
        } else if (curretTurn == Turn.NOUGHT) {
            player = "O"
            curretTurn = Turn.CROSS
        }

        button.text = player
        button.isEnabled = false

        // Call checkForVictory() to check if there is a winner
        if (checkForVictory(player)) {
            if (player == "X") {
                crossScore++
                result("$playerOne Wins!")
            } else {
                noughtScore++
                result("$computer Wins!")
            }
        } else if (isBoardFull()) {
            result("It's a Draw!")
        } else {
            setTurnLable()
        }
    }
    //===================== boardTapped End =====================


    //==================== checkForVictory Start ====================
    private fun checkForVictory(s: String): Boolean {
        // Horizontal check for victory
        if (match(binding.a1, s) && match(binding.a2, s) && match(binding.a3, s)) return true
        if (match(binding.b1, s) && match(binding.b2, s) && match(binding.b3, s)) return true
        if (match(binding.c1, s) && match(binding.c2, s) && match(binding.c3, s)) return true

        //vertical check for victory
        if (match(binding.a1, s) && match(binding.b1, s) && match(binding.c1, s)) return true
        if (match(binding.a2, s) && match(binding.b2, s) && match(binding.c2, s)) return true
        if (match(binding.a3, s) && match(binding.b3, s) && match(binding.c3, s)) return true

        //diagonal check for victory
        if (match(binding.a1, s) && match(binding.b2, s) && match(binding.c3, s)) return true
        if (match(binding.a3, s) && match(binding.b2, s) && match(binding.c1, s)) return true

        return false
    }

    private fun match(button: Button, symbol: String) = button.text == symbol
    //==================== checkForVictory End ====================

    //===================== result Start =====================
    private fun result(title: String) {
        val message = "$title\n\n$playerOne: $crossScore\n$computer: $noughtScore"
        AlertDialog.Builder(this).setTitle(title).setMessage(message)
            .setPositiveButton("reset")
            // Call resetBoard() when the user taps the positive button
            { _, _ ->
                resetBoard()
            }.setCancelable(false).show()
    }
    //===================== result End =====================

    //===================== resetBoard Start =====================
    private fun resetBoard() {
        for (button in boardList) {
            button.text = ""
            button.isEnabled = true
        }
        curretTurn = firstTurn
        setTurnLable()

        //If the computer is first, call computerMove()
        if (firstTurn == Turn.NOUGHT) {
            computerMove()
        }
    }
    //===================== resetBoard End =====================

    //===================== isBoardFull Start =====================
    private fun isBoardFull(): Boolean {
        for (button in boardList) {
            if (button.text == "")
                return false
        }
        return true
    }
    //===================== isBoardFull End =====================


    //===================== addToBoard Start =====================
    private fun addToBoard(button: Button) {
        if (button.text != "")
            return

        if (curretTurn == Turn.NOUGHT) {
            button.text = NOUGHT
            curretTurn = Turn.CROSS
        } else if (curretTurn == Turn.CROSS) {
            button.text = CROSS
            curretTurn = Turn.NOUGHT
        }
        setTurnLable()
    }

    companion object {
        const val NOUGHT = "O"
        const val CROSS = "X"
    }
    //===================== addToBoard End =====================


    //===================== setTurnLable Start =====================
    private fun setTurnLable() {
        var turnText = ""
        if (curretTurn == Turn.CROSS) {
            turnText = "$playerOne's turn"
        } else if (curretTurn == Turn.NOUGHT) {
            turnText = "$computer's turn"
            computerMove()
        }
        binding.turnX.text = turnText
    }
    //===================== setTurnLable End =====================

    //===================== computerMove Start =====================
    private fun computerMove() {
        val bestMove = findBestMove()
        if (bestMove != -1) {
            boardList[bestMove].performClick()
        }
    }
    //===================== computerMove End =====================


    //===================== findBestMove Start =====================
    private fun findBestMove(): Int {
        var bestVal = Integer.MIN_VALUE
        var bestMove = -1

        // Traverse all cells, evaluate minimax function for all empty cells.
        for (i in 0 until boardList.size) {
            if (boardList[i].text == "") {
                // Make the move
                boardList[i].text = CROSS
                val moveVal = minimax(0, false)
                // Undo the move
                boardList[i].text = ""

                if (moveVal > bestVal) {
                    bestVal = moveVal
                    bestMove = i
                }
            }
        }
        return bestMove
    }
    //===================== findBestMove End =====================

    //===================== minimax Start =====================
    private fun minimax(depth: Int, isMaximizing: Boolean): Int {
        val score = evaluateBoard()

        // If Maximizer has won the game, return their evaluated score
        if (score == 10) {
            return score - depth
        }

        // If Minimizer has won the game, return their evaluated score
        if (score == -10) {
            return score + depth
        }

        // If there are no more moves and the game is a draw
        if (isBoardFull()) {
            return 0
        }

        if (isMaximizing) {
            var best = Integer.MIN_VALUE
            for (i in 0 until boardList.size) {
                if (boardList[i].text == "") {
                    boardList[i].text = CROSS
                    best = maxOf(best, minimax(depth + 1, !isMaximizing))
                    boardList[i].text = ""
                }
            }
            return best
        } else {
            var best = Integer.MAX_VALUE
            for (i in 0 until boardList.size) {
                if (boardList[i].text == "") {
                    boardList[i].text = NOUGHT
                    best = minOf(best, minimax(depth + 1, !isMaximizing))
                    boardList[i].text = ""
                }
            }
            return best
        }
    }
    //===================== minimax End =====================

    //===================== evaluateBoard Start =====================
    private fun evaluateBoard(): Int {
        // Check for victory conditions for both players
        if (checkForVictory(CROSS)) {
            return 10
        } else if (checkForVictory(NOUGHT)) {
            return -10
        }

        // If no one wins, return 0
        return 0
    }
    //===================== evaluateBoard End =====================


}



