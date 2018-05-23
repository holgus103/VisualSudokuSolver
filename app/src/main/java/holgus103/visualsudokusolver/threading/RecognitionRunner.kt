package holgus103.visualsudokusolver.threading

import android.content.Intent
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import holgus103.visualsudokusolver.MainActivity
import holgus103.visualsudokusolver.R
import holgus103.visualsudokusolver.SudokuEditActivity
import java.io.File

class RecognitionRunner : AsyncTask<MainActivity, Void, MainActivity?>() {


    override fun doInBackground(vararg params: MainActivity?): MainActivity? {
        val activity = params[0];
//            val file = File(this.photoPath);
        val f = File(activity!!.imagePath);
        if(!f.exists()){
            Log.e("ERROR", "File not found");
            return null;
        }
        Log.d("INFO", "Loading from: " + activity.imagePath)
        activity.rawSudoku = activity.runRecognitionSafe(activity.imagePath!!);
        f.delete();
        return activity;

    }

    override fun onPostExecute(activity: MainActivity?) {
        if(null == activity){
            Toast.makeText(activity, "Recognition failed!", Toast.LENGTH_LONG).show();
            Log.e("ERROR", "Recognition failed");
        }
        val i = Intent(activity, SudokuEditActivity::class.java);
        i.putExtra(activity!!.getString(R.string.sudoku), activity!!.rawSudoku);
        activity.leftActivity = true
        activity!!.startActivity(i)
    }
}