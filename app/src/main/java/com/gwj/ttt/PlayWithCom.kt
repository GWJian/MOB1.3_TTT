package com.gwj.ttt

import android.content.Intent
import android.icu.text.CaseMap.Title
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.gwj.ttt.databinding.ActivityPveBinding
import com.gwj.ttt.utils.GameLogic
import com.gwj.ttt.utils.GameLogic.board
import com.gwj.ttt.utils.GameLogic.flag
import com.gwj.ttt.utils.GameLogic.getBestMove
import com.gwj.ttt.utils.GameLogic.getStatus
import com.gwj.ttt.utils.GameLogic.isGameFinished
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayWithCom : AppCompatActivity() {
    private lateinit var binding: ActivityPveBinding
    private lateinit var buttons: Array<Array<Button>>
    private var isRunning = false
    private var currentTurn = Turn.NOUGHT

    override fun onCreate(saveInstanceState: Bundle?) {
        super.onCreate(saveInstanceState)

        binding = ActivityPveBinding.inflate(layoutInflater)
        setContentView(binding.root)
        start()

        binding.btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun start() {
        binding.run {
            buttons = arrayOf(
                arrayOf(a1, a2, a3),
                arrayOf(b1, b2, b3),
                arrayOf(c1, c2, c3)
            )

            //Logic for user Turn Start
            for (i in 0..2) {
                for (j in 0..2) {
                    buttons[i][j].setOnClickListener {
                        if (isRunning && currentTurn == Turn.NOUGHT) {
                            if (GameLogic.board[i][j] == '?') {
                                buttons[i][j].text = "O"
                                GameLogic.board[i][j] = 'o'
                                nextTurn()
                            }
                        }
                    }
                }
            }
            //Logic for user Turn End
            isRunning = true
        }
    }

    fun nextTurn() {
        checkStatus()
        if (currentTurn == Turn.NOUGHT) {
            currentTurn = Turn.CROSS
            computerMove()
        } else {
            currentTurn = Turn.NOUGHT
        }
        setTurnLable()
    }

    //computer Logic Turn Start
    fun computerMove() {
        lifecycleScope.launch {
            delay(1000)
            if (isRunning) {
                val (x, y) = GameLogic.getBestMove(GameLogic.board)
                GameLogic.board[x][y] = 'x'
                buttons[x][y].text = "X"
                nextTurn()
            }
        }
    }
    //computer Logic Turn End


    fun checkStatus() {
        val status = GameLogic.getStatus(GameLogic.board)
        if (status == -1) {
            result("You Win")
//            Log.d("debugging", "You Win")
            isRunning = false
            return
        }

        if (status == 1) {
            result("You Lose")
//            Log.d("debugging", "Comp Win")
            isRunning = false
            return
        }
        if (GameLogic.isGameFinished()) {
            result("Draw")
//            Log.d("debugging", "Draw")
            isRunning = false
        }
    }

    private fun result(title: String) {
        val message = "Do you want to play again?"
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Reset")
            // Call resetBoard() when the user taps the positive button
            { _, _ ->
                resetBoard()
            }
            .setCancelable(false)
            .show()
    }

    private fun resetBoard() {
        GameLogic.resetBoard()
        for (row in buttons) {
            for (button in row) {
                button.text = ""
            }
        }

        currentTurn = Turn.NOUGHT
        isRunning = true
        nextTurn()
    }

    fun setTurnLable() {
        var turnText = ""
        if (currentTurn == Turn.NOUGHT) {
            turnText = "Your Turn"
        } else {
            turnText = "Computer Turn"
        }
        binding.turnX.text = turnText
    }

    enum class Turn {
        NOUGHT, CROSS
    }

}