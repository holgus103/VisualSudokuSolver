package holgus103.visualsudokusolver

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

abstract class SudokuActivityBase : AppCompatActivity() {

    abstract fun prepareSudokuGrid();

    override fun onResume() {
        super.onResume();
        this.prepareSudokuGrid();
    }

}