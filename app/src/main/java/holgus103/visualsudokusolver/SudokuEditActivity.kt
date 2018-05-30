package holgus103.visualsudokusolver

import android.content.Intent
import android.os.Bundle
import android.view.View
import holgus103.visualsudokusolver.threading.SolverRunner

class SudokuEditActivity : SudokuGridActivityBase() {

    override fun prepareSudokuGrid() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sudoku_edit)

    }


    fun solveManually(v: View){
        this.parseGrid();
        val i = Intent(this, SolveByHand::class.java);
        i.putExtra(getString(R.string.sudoku), this.rawSudoku);
        i.putExtra(getString(R.string.raw_sudoku), this.rawSudoku)
        startActivity(i);
    }

    var unsolved: IntArray = IntArray(0);

    fun solve(v: View){
        this.parseGrid();
        this.unsolved = this.rawSudoku.clone();
        val checked = this.checkSudoku(this.rawSudoku);
        if(checked.all{v -> v}) {
            SolverRunner(this, this.unsolved).execute()
        }
        else{
            this.processFailedGridReview(checked, R.string.grid_error_msg)
        }
    }

}
