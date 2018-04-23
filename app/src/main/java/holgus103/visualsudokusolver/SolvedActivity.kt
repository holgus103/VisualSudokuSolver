package holgus103.visualsudokusolver

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import holgus103.visualsudokusolver.db.dao.SudokuEntry
import kotlinx.android.synthetic.main.sudoku.*

class SolvedActivity : SudokuActivityBase() {

    override fun prepareSudokuGrid() {
        for (i in 0..(sudoku_grid.childCount-1)) {
            val row = sudoku_grid.getChildAt(i) as ViewGroup;
            for(j in 0..(row.childCount-1)){
                val cell = row.getChildAt(j) as EditText;
                // make field not editable
                cell.inputType = InputType.TYPE_NULL;
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solved)
    }

    fun save(v: View){
        val sudoku = SudokuEntry(sudoku = this.rawSudoku)
        SudokuApp.instance.dao.add(sudoku);
        val i = Intent(this, MainActivity::class.java);
        this.startActivity(i);
    }
}
