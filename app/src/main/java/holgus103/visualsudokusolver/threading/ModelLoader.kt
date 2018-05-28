package holgus103.visualsudokusolver.threading

import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import android.os.PowerManager
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import holgus103.visualsudokusolver.MainActivity
import holgus103.visualsudokusolver.R
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL


class ModelLoader(ctx: MainActivity, progress: TextView) : AsyncTask<String, Long, Boolean>() {

    private val ctx = ctx
    private val progress = progress

    override fun doInBackground(vararg params: String): Boolean {
        var input: InputStream? = null
        var output: OutputStream? = null
        var connection: HttpURLConnection? = null
        try {
            val url = URL(params[0])
            connection = url.openConnection() as HttpURLConnection
            connection!!.connect()

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection!!.getResponseCode() !== HttpURLConnection.HTTP_OK) {
                Log.d("ERROR", "Server returned HTTP " + connection!!.getResponseCode()
                        + " " + connection!!.getResponseMessage())
            }

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            val fileLength = connection!!.getContentLength()

            // download the file
            input = connection!!.getInputStream()
            output = FileOutputStream(params[1])

            val data = ByteArray(4096)
            var total: Long = 0
            var count: Int
            count = input!!.read(data)

            while (count  != -1) {
                // allow canceling with back button
                if (isCancelled) {
                    input!!.close()
                    return false;
                }
                total += count.toLong()
                // publishing the progress....
                // only if total length is known
                publishProgress(total)
                output!!.write(data, 0, count)
                count = input!!.read(data)
            }
        } catch (e: Exception) {
            Log.e("EXCEPTION", e.message)
            return false
        } finally {
            try {
                if (output != null)
                    output!!.close()
                if (input != null)
                    input!!.close()
            } catch (ignored: IOException) {
            }

            if (connection != null)
                connection!!.disconnect()
        }
        return true;
    }

    private lateinit var mWakeLock: PowerManager.WakeLock;

    override fun onPreExecute() {
        super.onPreExecute()
        // take CPU lock to prevent CPU from going off if the user
        // presses the power button during download
        val pm = this.ctx.getSystemService(Context.POWER_SERVICE) as PowerManager
        this.mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, javaClass.name)
        mWakeLock.acquire()
    }

    protected override fun onProgressUpdate(vararg values: Long?) {
        super.onProgressUpdate(values[0])
        // if we get here, length is known, now set indeterminate to false
        val p = values[0];
        if(p == null){
            return;
        }
        progress.text = p.toString() + " Bytes"
    }

    override fun onPostExecute(result: Boolean?) {
        super.onPostExecute(result)
        ctx.restoreInterface();
    }

}