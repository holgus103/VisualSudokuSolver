package holgus103.visualsudokusolver

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class SudokuEditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sudoku_edit)
    }

    fun solve(v: View){
        val i = Intent(this, SolvedActivity::class.java)
        this.startActivity(i);
    }
}
