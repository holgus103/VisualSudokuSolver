package holgus103.visualsudokusolver.threading

import android.content.Intent
import android.os.AsyncTask
import android.os.Environment
import android.util.Log
import android.widget.Toast
import holgus103.visualsudokusolver.MainActivity
import holgus103.visualsudokusolver.R
import holgus103.visualsudokusolver.SudokuBaseActivity
import holgus103.visualsudokusolver.SudokuEditActivity
import java.io.File

class RecognitionRunner : AsyncTask<MainActivity, Void, MainActivity?>() {


    override fun doInBackground(vararg params: MainActivity?): MainActivity? {
        val activity = params[0];
        val f = File(MainActivity.imagePath);
        if(!f.exists()){
            Log.e("ERROR", "File not found");
            return null;
        }
        Log.d("INFO", "Loading from: " + MainActivity.imagePath)
        val storageDir = activity!!.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        // TEST PATH FOR EMULATOR USE
//        val path = "/sdcard/Download/20180518_040815.jpg";
        val path = MainActivity.imagePath!!;
        activity!!.rawSudoku = activity!!.runRecognition(path,
                storageDir.absolutePath + '/' + SudokuBaseActivity.MODEL_PATH);
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