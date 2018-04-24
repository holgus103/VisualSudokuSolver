package holgus103.visualsudokusolver

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import holgus103.visualsudokusolver.db.dao.SudokuEntry
import kotlinx.android.synthetic.main.sudoku.*

class SolvedActivity : SudokuGridActivityBase() {

    override fun prepareSudokuGrid() =

        this.forEachField({cell, i, j, current ->
            cell.inputType = InputType.TYPE_NULL;
        });

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solved)
    }

    fun save(v: View){
        val sudoku = SudokuEntry(sudoku = this.rawSudoku)
        SudokuApp.instance.dao.add(sudoku);
        //TODO: read preference and automatically save
        //TODO: store unsolved sudoku
        val i = Intent(this, MainActivity::class.java);
        this.startActivity(i);
    }
}
