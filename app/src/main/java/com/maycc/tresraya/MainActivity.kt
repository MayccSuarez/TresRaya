package com.maycc.tresraya

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val NUM_CELLS = 9
    private val SIGN_GAMER_ONE = "X"
    private val SIGN_GAMER_TWO = "O"
    private var isGamerOne = true
    private var selectedCellsOfGamerOne = ArrayList<Int>()
    private var selectedCellsOfGamerTwo = ArrayList<Int>()
    private val combinationsWinners = arrayOf(intArrayOf(1,2,3), intArrayOf(4,5,6), intArrayOf(7,8,9),
                                             intArrayOf(1,4,7), intArrayOf(2,5,8), intArrayOf(3,6,9),
                                             intArrayOf(1,5,9), intArrayOf(3,5,7))
    private var cont = 0
    private var thereIsWinner = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        updateTxtGamer(1)

        setListenersBtns()
    }

    private fun updateTxtGamer(numGamer: Int, customText:String = "") {
        if (customText.isNotEmpty()) {
            tvGamer.text = customText
        } else {
            tvGamer.text = resources.getString(R.string.txt_gamer, numGamer)
        }
    }

    private fun setListenersBtns() {
        for (i in 1..NUM_CELLS) {
            val btn = findViewById<Button>(resources.getIdentifier("btn$i", "id", packageName))
            btn.setOnClickListener(this)
        }
    }

    override fun onClick(v: View?) {
        val id = v?.id
        selectCell(id)
        changeOfGamer()
    }

    private fun changeOfGamer() {
        isGamerOne = !isGamerOne
    }

    private fun selectCell(id: Int?) {
        when (id) {
            R.id.btn1 -> mark(btn1, 1)
            R.id.btn2 -> mark(btn2, 2)
            R.id.btn3 -> mark(btn3, 3)
            R.id.btn4 -> mark(btn4, 4)
            R.id.btn5 -> mark(btn5, 5)
            R.id.btn6 -> mark(btn6, 6)
            R.id.btn7 -> mark(btn7, 7)
            R.id.btn8 -> mark(btn8, 8)
            R.id.btn9 -> mark(btn9, 9)
        }
    }

    private fun mark(btn: Button, numCell: Int) {
        if (isGamerOne) {
            selectedCellsOfGamerOne.add(numCell)
            btn.text = SIGN_GAMER_ONE
            updateTxtGamer(2) // es turno del jugador dos
            cont++

        } else {
            selectedCellsOfGamerTwo.add(numCell)
            btn.text = SIGN_GAMER_TWO
            updateTxtGamer(1) // es turno del jugador 1
        }

        btn.isClickable = false

        if (cont >= 3) {
            determineWinner()
        }

        if (thereIsWinner) {
            endGame()
        }

        if (cont == 5 && !thereIsWinner) {
            updateTxtGamer(0, getString(R.string.txt_draw))
            endGame()
        }
    }

    private fun determineWinner() {
        for (combination in combinationsWinners) {
            val arrayListCombination = ArrayList<Int>()
            for (i in combination) {
                arrayListCombination.add(i)
            }

            if (selectedCellsOfGamerOne.containsAll(arrayListCombination)) {
                putGamerWinner(1)
                return
            }

            if (selectedCellsOfGamerTwo.containsAll(arrayListCombination)){
                putGamerWinner(2)
                return
            }
        }
    }

    private fun putGamerWinner(numGamer: Int) {
        thereIsWinner = true

        val customText = resources.getString(R.string.txt_winner) + " " +
                        resources.getString(R.string.txt_gamer, numGamer)

        updateTxtGamer(numGamer, customText)
    }

    private fun endGame() {
        val msj = getString(R.string.txt_end_game)
        showToast(msj)
        enableGridCells(false)
        enableBtnReset(View.VISIBLE)
    }

    private fun showToast(msj: String) {
        Toast.makeText(this, msj, Toast.LENGTH_SHORT).show()
    }

    private fun enableGridCells(isClickable: Boolean) {
        for (i in 1..NUM_CELLS) {
            val btn = findViewById<Button>(resources.getIdentifier("btn$i", "id", packageName))
            btn.isClickable = isClickable

            if (isClickable)
                btn.text = ""
        }
    }

    private fun enableBtnReset(visibility: Int) {
        btnReset.visibility = visibility
    }

    fun resetGame(view: View) {
        showToast(getString(R.string.txt_restart_game))
        enableGridCells(true)
        enableBtnReset(View.GONE)
        updateTxtGamer(1)
        isGamerOne = true
        selectedCellsOfGamerOne.clear()
        selectedCellsOfGamerTwo.clear()
        cont = 0
        thereIsWinner = false
    }
}
