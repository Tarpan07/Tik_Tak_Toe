package com.example.tic_tak_toe

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var buttons: Array<Button>
    private lateinit var tvStatus: TextView
    private var player1Name: String = "Player 1"
    private var player2Name: String = "Player 2"
    private var player1Sign: String = ""
    private var player2Sign: String = ""
    private var activePlayer: Int = 0
    private var gameActive: Boolean = true
    private var filledPos = IntArray(9) { -1 }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvStatus = findViewById(R.id.textView2)

        buttons = arrayOf(
            findViewById(R.id.b0), findViewById(R.id.b1), findViewById(R.id.b2),
            findViewById(R.id.b3), findViewById(R.id.b4), findViewById(R.id.b5),
            findViewById(R.id.b6), findViewById(R.id.b7), findViewById(R.id.b8)
        )

        buttons.forEachIndexed { index, button ->
            button.setOnClickListener(this)
            button.tag = index
            button.setBackgroundColor(Color.parseColor("#c3aed6"))
        }

        getPlayerNamesAndSigns()
    }

    private fun getPlayerNamesAndSigns() {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        val input1 = EditText(this).apply {
            hint = "Enter Player 1 Name"
            inputType = InputType.TYPE_CLASS_TEXT
        }

        val chooseText = TextView(this).apply {
            text = "Choose your icon"
            setPadding(0, 16, 0, 8)
            textSize = 16f
        }

        val signGroup = RadioGroup(this).apply {
            orientation = RadioGroup.HORIZONTAL
        }

        val radioX = RadioButton(this).apply { text = "X" }
        val radioO = RadioButton(this).apply { text = "O" }
        signGroup.addView(radioX)
        signGroup.addView(radioO)

        layout.addView(input1)
        layout.addView(chooseText)
        layout.addView(signGroup)

        AlertDialog.Builder(this)
            .setTitle("Player 1 Setup")
            .setView(layout)
            .setCancelable(false)
            .setPositiveButton("Continue") { _, _ ->
                player1Name = input1.text.toString().ifBlank { "Player 1" }
                player1Sign = when {
                    radioX.isChecked -> "X"
                    radioO.isChecked -> "O"
                    else -> "X" // default if nothing selected
                }
                player2Sign = if (player1Sign == "X") "O" else "X"
                getPlayer2Name()
            }
            .show()
    }

    private fun getPlayer2Name() {
        val input2 = EditText(this).apply {
            hint = "Enter Player 2 Name"
            inputType = InputType.TYPE_CLASS_TEXT
        }

        AlertDialog.Builder(this)
            .setTitle("Player 2 Setup")
            .setView(input2)
            .setCancelable(false)
            .setPositiveButton("Start Game") { _, _ ->
                player2Name = input2.text.toString().ifBlank { "Player 2" }
                tvStatus.text = "$player1Name's Turn"
            }
            .show()
    }

    override fun onClick(v: View?) {
        if (!gameActive) return

        val btnClicked = v as Button
        val clickedTag = btnClicked.tag.toString().toInt()

        if (filledPos[clickedTag] != -1) return

        filledPos[clickedTag] = activePlayer

        if (activePlayer == 0) {
            btnClicked.text = player1Sign
            btnClicked.setTextColor(Color.BLACK)
            btnClicked.setBackgroundColor(Color.parseColor("#f7b267"))
            activePlayer = 1
            tvStatus.text = "$player2Name's Turn"
        } else {
            btnClicked.text = player2Sign
            btnClicked.setTextColor(Color.BLACK)
            btnClicked.setBackgroundColor(Color.parseColor("#70c1b3"))
            activePlayer = 0
            tvStatus.text = "$player1Name's Turn"
        }

        checkForWin()
    }

    private fun checkForWin() {
        val winPos = arrayOf(
            intArrayOf(0, 1, 2), intArrayOf(3, 4, 5), intArrayOf(6, 7, 8),
            intArrayOf(0, 3, 6), intArrayOf(1, 4, 7), intArrayOf(2, 5, 8),
            intArrayOf(0, 4, 8), intArrayOf(2, 4, 6)
        )

        for (positions in winPos) {
            val (a, b, c) = positions
            if (filledPos[a] != -1 && filledPos[a] == filledPos[b] && filledPos[b] == filledPos[c]) {
                gameActive = false
                highlightWinningButtons(positions)
                val winner = if (filledPos[a] == 0) player1Name else player2Name
                Handler().postDelayed({
                    showResultDialog("$winner Wins!")
                }, 1500)
                return
            }
        }

        if (!filledPos.contains(-1)) {
            gameActive = false
            showResultDialog("It's a Draw")
        }
    }

    private fun highlightWinningButtons(winPos: IntArray) {
        winPos.forEach {
            buttons[it].setBackgroundColor(Color.parseColor("#5fe55a"))
        }
    }

    private fun showResultDialog(message: String) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setTitle("Game Over")
            .setPositiveButton("Restart") { _, _ -> restartGame() }
            .show()
    }

    private fun restartGame() {
        filledPos = IntArray(9) { -1 }
        activePlayer = 0
        gameActive = true
        tvStatus.text = "$player1Name's Turn"

        buttons.forEach {
            it.text = ""
            it.setBackgroundColor(Color.parseColor("#c3aed6"))
        }
    }
}

