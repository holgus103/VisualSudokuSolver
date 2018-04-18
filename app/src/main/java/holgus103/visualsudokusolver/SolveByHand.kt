package holgus103.visualsudokusolver

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import kotlinx.android.synthetic.main.sudoku.*

class SolveByHand : SudokuActivityBase() {

    override fun prepareSudokuGrid() {
        for(i in 1..sudoku_grid.childCount){
            val row = sudoku_grid.getChildAt(i) as ViewGroup;
            for(j in 1..row.childCount){
                val cell = row.getChildAt(j) as EditText;
                if(cell.text.toString() == ""){
                    cell.inputType = InputType.TYPE_NULL;
                }
            }
        }
    }

    fun solve(v: View){
        val i = Intent(this, SolvedActivity::class.java)
    }

    fun check(v: View){
        //
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solve_by_hand)
    }
}
