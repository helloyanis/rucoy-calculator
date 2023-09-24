import android.content.Context
import android.os.AsyncTask
import android.widget.Toast
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.UnknownHostException

class GitHubReleaseChecker(private val context: Context, private val repositoryUrl: String) :
    AsyncTask<Void, Void, String>() {

    override fun doInBackground(vararg params: Void?): String {
        try {
            val apiUrl = "$repositoryUrl/releases/latest"
            val url = URL(apiUrl)
            val connection = url.openConnection() as HttpURLConnection

            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            val stringBuilder = StringBuilder()
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }

            reader.close()
            return stringBuilder.toString()
        } catch (e: UnknownHostException) {
            return "Network Error"
        } catch (e: Exception) {
            return "Error"
        }
    }

    override fun onPostExecute(result: String) {
        super.onPostExecute(result)

        when {
            result == "Network Error" -> return //showToast("Network error occurred.")
            result == "Error" -> return //showToast("An error occurred.")
            result == "" -> return
            else -> {
                try {
                    val jsonObject = JSONObject(result)
                    val tagName = jsonObject.getString("tag_name")
                    val latestVersion = tagName.replace("v", "").trim()

                    if (isUpdateAvailable(latestVersion)) {
                        showToast("Update available: $latestVersion")
                        //showToast("Go on GitHub to update!")
                    } else {
                        //showToast("You are using the latest version.")
                    }
                } catch (e: Exception) {
                    showToast("Error checking for updates.")
                }
            }
        }
    }

    private fun isUpdateAvailable(latestVersion: String): Boolean {
        // Replace this with the current installed version of your app.
        val currentVersion = "4.1"

        // Split the version strings into individual parts.
        val currentVersionParts = currentVersion.split(".")
        val latestVersionParts = latestVersion.split(".")

        // Compare each part of the version numbers.
        for (i in 0 until minOf(currentVersionParts.size, latestVersionParts.size)) {
            val currentPart = currentVersionParts[i].toInt()
            val latestPart = latestVersionParts[i].toInt()

            if (currentPart < latestPart) {
                return true
            } else if (currentPart > latestPart) {
                return false
            }
        }

        // If all parts are equal, consider it up-to-date.
        return false
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}
