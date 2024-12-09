package com.example.anuk_guerraizquierdo_uf1_act17

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private var isPlayerOneTurn = true
    private val board = Array(3) { CharArray(3) { ' ' } }
    private lateinit var turnIndicator: TextView
    private lateinit var resultText: TextView
    private lateinit var restartButton: Button
    private lateinit var buttons: Array<Array<Button>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        turnIndicator = findViewById(R.id.turnIndicator)
        resultText = findViewById(R.id.resultText)
        restartButton = findViewById(R.id.restartButton)

        buttons = arrayOf(
            arrayOf(findViewById(R.id.button1), findViewById(R.id.button2), findViewById(R.id.button3)),
            arrayOf(findViewById(R.id.button4), findViewById(R.id.button5), findViewById(R.id.button6)),
            arrayOf(findViewById(R.id.button7), findViewById(R.id.button8), findViewById(R.id.button9))
        )

        for (row in 0..2) {
            for (col in 0..2) {
                buttons[row][col].setOnClickListener { handleButtonClick(buttons[row][col], row, col) }
            }
        }

        restartButton.setOnClickListener { resetGame() }
        resetGame()
    }

    private fun handleButtonClick(button: Button, row: Int, col: Int) {
        if (button.text.isNotEmpty()) return

        button.text = if (isPlayerOneTurn) "X" else "O"
        board[row][col] = if (isPlayerOneTurn) 'X' else 'O'

        if (checkWin(row, col)) {
            showWinner(if (isPlayerOneTurn) "Winner: Player X" else "Winner: Player O")
        } else if (isDraw()) {
            showWinner("Draw")
        } else {
            isPlayerOneTurn = !isPlayerOneTurn
            turnIndicator.text = if (isPlayerOneTurn) "Player X's Turn" else "Player O's Turn"
        }
    }

    private fun checkWin(row: Int, col: Int): Boolean {
        val symbol = if (isPlayerOneTurn) 'X' else 'O'
        return board[row].all { it == symbol } ||
                (0..2).all { board[it][col] == symbol } ||
                (row == col && (0..2).all { board[it][it] == symbol }) ||
                (row + col == 2 && (0..2).all { board[it][2 - it] == symbol })
    }

    private fun isDraw() = board.all { row -> row.all { it != ' ' } }

    private fun showWinner(winner: String) {
        resultText.text = winner
        resultText.visibility = View.VISIBLE
        restartButton.visibility = View.VISIBLE
        turnIndicator.visibility = View.GONE
        buttons.forEach { row -> row.forEach { it.isEnabled = false } }
    }

    private fun resetGame() {
        board.forEach { it.fill(' ') }
        buttons.forEach { row ->
            row.forEach {
                it.text = ""
                it.isEnabled = true
            }
        }
        isPlayerOneTurn = true
        turnIndicator.text = "Player X's Turn"
        turnIndicator.visibility = View.VISIBLE
        resultText.visibility = View.GONE
        restartButton.visibility = View.GONE
    }
}
