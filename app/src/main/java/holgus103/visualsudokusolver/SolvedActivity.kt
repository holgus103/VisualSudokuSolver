package holgus103.visualsudokusolver

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.InputType
import android.view.View
import holgus103.visualsudokusolver.db.dao.SudokuEntry

class SolvedActivity : SudokuGridActivityBase() {

    override fun prepareSudokuGrid() =

        this.forEachField({cell, i, j, current ->
            cell.inputType = InputType.TYPE_NULL;
        });

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solved)

        val m = PreferenceManager.getDefaultSharedPreferences(this);
        val autosave = m.getBoolean(getString(R.string.autosave_key), false);
        if(autosave){
            val sudoku = SudokuEntry(sudoku = this.rawSudoku)
            SudokuApp.instance.dao.add(sudoku);
        }

    }

    fun save(v: View){

        val sudoku = SudokuEntry(sudoku = this.rawSudoku)
        SudokuApp.instance.dao.add(sudoku);
        val i = Intent(this, MainActivity::class.java);
        this.startActivity(i);
    }
}
