package holgus103.visualsudokusolver

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import kotlinx.android.synthetic.main.sudoku.*

class SolveByHand : SudokuActivityBase() {

    override fun prepareSudokuGrid() {
        for(i in 0..(sudoku_grid.childCount-1)){
            val row = sudoku_grid.getChildAt(i) as ViewGroup;
            for(j in 0..(row.childCount-1)){
                val cell = row.getChildAt(j) as EditText;
                if(cell.text.toString() != ""){
                    cell.inputType = InputType.TYPE_NULL;
                }
            }
        }
    }

    fun solve(v: View){
        val i = Intent(this, SolvedActivity::class.java)
        startActivity(i);
    }

    fun check(v: View){
//        AlertDialog.bui
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solve_by_hand)
    }
}
