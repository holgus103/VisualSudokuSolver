package holgus103.visualsudokusolver

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

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

    fun solve(v: View){
        this.parseGrid();
        val i = Intent(this, SolvedActivity::class.java)
        val unsolved = this.rawSudoku.clone();
        this.rawSudoku = this.solve(this.rawSudoku);
        i.putExtra(getString(R.string.sudoku), this.rawSudoku);
        i.putExtra(getString(R.string.raw_sudoku), unsolved)
        this.startActivity(i);
    }

}
