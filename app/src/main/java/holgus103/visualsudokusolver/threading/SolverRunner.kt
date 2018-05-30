package holgus103.visualsudokusolver.threading

import android.content.Intent
import android.os.AsyncTask
import android.provider.Settings.Global.getString
import holgus103.visualsudokusolver.R
import holgus103.visualsudokusolver.SolvedActivity
import holgus103.visualsudokusolver.SudokuBaseActivity
import holgus103.visualsudokusolver.SudokuEditActivity

class SolverRunner(activity: SudokuBaseActivity, unsolved: IntArray) : AsyncTask<Void, Void, Int>() {
    val act = activity
    val unsolved = unsolved
    override fun doInBackground(vararg params: Void?): Int {
        act.rawSudoku = act.solve(act.rawSudoku)
        return 0;
    }

    override fun onPostExecute(result: Int) {
        super.onPostExecute(result)
        val i = Intent(act, SolvedActivity::class.java)
        i.putExtra(act.getString(R.string.sudoku), act.rawSudoku);
        i.putExtra(act.getString(R.string.raw_sudoku), unsolved);
        act.startActivity(i);
    }
}