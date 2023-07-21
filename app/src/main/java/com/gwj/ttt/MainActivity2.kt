package com.gwj.ttt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gwj.ttt.databinding.ActivityMain2Binding

//https://www.youtube.com/watch?v=POFvcoRo3Vw&ab_channel=CodeWithCal
class MainActivity2 : AppCompatActivity() {

    lateinit var binding: ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        //this will get the player name from the edit text and send it to the next activity using intent
        binding.btnSubmit.setOnClickListener {
            val playerOne = binding.editTextTextPersonName.text.toString()
            val playerTwo = binding.editTextTextPersonName2.text.toString()

            val intent = Intent(this, PVP::class.java)
            intent.putExtra("PLAYER_ONE", playerOne)
            intent.putExtra("PLAYER_TWO", playerTwo)
            startActivity(intent)
        }

        binding.btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}