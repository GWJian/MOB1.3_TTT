package com.gwj.ttt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.gwj.ttt.databinding.ActivityMain3Binding

class MainActivity3 : AppCompatActivity() {

    enum class Turn {
        NOUGHT,
        CROSS
    }

    //player names
    private var playerOne:String = ""
    private var playerTwo:String = ""

    //player turn
    private var firstTurn = Turn.CROSS
    private var curretTurn = Turn.CROSS

    //player score
    private var crossScore = 0
    private var noughtScore = 0

    //board
    private var boardList = mutableListOf<Button>()
    lateinit var binding: ActivityMain3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain3Binding.inflate(layoutInflater)
        setContentView(binding.root)
        initBoard()
        getPlayerNames()

        binding.btnBack2.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    //===================== getPlayerNames Start =====================
    private fun getPlayerNames() {
        //Retrieve the player names from the intent extras at MainActivity2
        playerOne = intent.getStringExtra("PLAYER_ONE") ?: ""
        playerTwo = intent.getStringExtra("PLAYER_TWO") ?: ""

        setTurnLable()
    }
    //===================== getPlayerNames End =====================

    //===================== initBoard Start =====================
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
    //===================== initBoard End =====================

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

        // Call checkForVictory() to check if the player has won
        if (checkForVictory(player)) {
            if (player == "X") {
                crossScore++
                result("$playerOne Wins!")
            } else {
                noughtScore++
                result("$playerTwo Wins!")
            }
        } else if (isBoardFull()) {
            result("It's a Draw!")
        } else {
            setTurnLable()
        }
    }
    //===================== boardTapped End =====================

    //===================== checkForVictory Start =====================
    private fun checkForVictory(s: String): Boolean {
        // Horizontal check for victory
        if (match(binding.a1, s) && match(binding.a2, s) && match(binding.a3, s))
            return true
        if (match(binding.b1, s) && match(binding.b2, s) && match(binding.b3, s))
            return true
        if (match(binding.c1, s) && match(binding.c2, s) && match(binding.c3, s))
            return true

        //vertical check for victory
        if (match(binding.a1, s) && match(binding.b1, s) && match(binding.c1, s))
            return true
        if (match(binding.a2, s) && match(binding.b2, s) && match(binding.c2, s))
            return true
        if (match(binding.a3, s) && match(binding.b3, s) && match(binding.c3, s))
            return true

        //diagonal check for victory
        if (match(binding.a1, s) && match(binding.b2, s) && match(binding.c3, s))
            return true
        if (match(binding.a3, s) && match(binding.b2, s) && match(binding.c1, s))
            return true

        return false
    }

    private fun match(button: Button, symbol: String) = button.text == symbol
    //===================== checkForVictory End =====================

    //===================== result Start =====================
    private fun result(title: String) {
        val message = "$title\n\n$playerOne: $crossScore\n$playerTwo: $noughtScore"
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Reset")
            { _, _ ->
                resetBoard()
            }
            .setCancelable(false)
            .show()
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
        if (curretTurn == Turn.CROSS)
            turnText = "Turn $playerOne ($CROSS)"
        else if (curretTurn == Turn.NOUGHT)
            turnText = "Turn $playerTwo ($NOUGHT)"

        binding.turnTV.text = turnText
    }
    //===================== setTurnLable End =====================
}
