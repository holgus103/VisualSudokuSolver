package holgus103.visualsudokusolver.threading

import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import android.os.PowerManager
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import holgus103.visualsudokusolver.MainActivity
import holgus103.visualsudokusolver.R
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL


class ModelLoader(ctx: MainActivity, bar: ProgressBar) : AsyncTask<String, Int, Boolean>() {

    private val ctx = ctx
    private val bar = bar

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
                if (fileLength > 0)
                // only if total length is known
                    publishProgress((total * 100 / fileLength) as Int)
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

    protected override fun onProgressUpdate(vararg values: Int?) {
        super.onProgressUpdate(values[0])
        // if we get here, length is known, now set indeterminate to false
        bar.isIndeterminate = false
        bar.max = 100
        val p = values[0] ?: return;
        bar.progress = p;
    }

    protected fun onPostExecute(result: String?) {
        mWakeLock.release()
        ctx.restoreInterface();
    }


}