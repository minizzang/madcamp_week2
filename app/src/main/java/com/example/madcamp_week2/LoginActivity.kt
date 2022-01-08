package com.example.madcamp_week2

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.commit
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class NaverLogin {
    fun get(apiUrl: String, requestHeaders: Map<String, String>): String? {
        val con: HttpURLConnection = connect(apiUrl)
        return try {
            con.setRequestMethod("GET")
            for ((key, value) in requestHeaders) {
                con.setRequestProperty(key, value)
            }
            val responseCode: Int = con.getResponseCode()
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                readBody(con.getInputStream())
            } else { // 에러 발생
                readBody(con.getErrorStream())
            }
        } catch (e: IOException) {
            throw RuntimeException("API 요청과 응답 실패", e)
        } finally {
            con.disconnect()
        }
    }

    fun connect(apiUrl: String): HttpURLConnection {
        return try {
            val url: URL = URL(apiUrl)
            url.openConnection() as HttpURLConnection
        } catch (e: MalformedURLException) {
            throw RuntimeException("API URL이 잘못되었습니다. : $apiUrl", e)
        } catch (e: IOException) {
            throw RuntimeException("연결이 실패했습니다. : $apiUrl", e)
        }
    }

    fun readBody(body: InputStream): String? {
        val streamReader = InputStreamReader(body)
        try {
            BufferedReader(streamReader).use { lineReader ->
                val responseBody = StringBuilder()
                var line: String?
                while (lineReader.readLine().also { line = it } != null) {
                    responseBody.append(line)
                }
                return responseBody.toString()
            }
        } catch (e: IOException) {
            throw RuntimeException("API 응답을 읽는데 실패했습니다.", e)
        }
    }
}

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val naver_client_id = "Z1VZ6dT56XDYLSohRZRN"
        val naver_client_secret = "Oxdzed_RYw"
        val naver_client_name = "빌리지"

        naverLogin()




        // 서버-앱 연결 테스트용 버튼
//        val btn : Button = findViewById(R.id.button_test)
//        btn.setOnClickListener {
//            Toast.makeText(this, "hi", Toast.LENGTH_LONG).show()
//            VolleyService.testVolley(this) { testSuccess ->
//                if (testSuccess) {
//                    Toast.makeText(this, "통신 성공!", Toast.LENGTH_LONG).show()
//                } else {
//                    Toast.makeText(this, "통신 실패ㅠ", Toast.LENGTH_LONG).show()
//                }
//            }
//
//        }
    }

    fun naverLogin() {
        var mOAuthLoginButton: OAuthLoginButton? = null
        mOAuthLoginButton = findViewById(R.id.btn_naverLogin)
        var mOAuthLoginInstance = OAuthLogin.getInstance()

        mOAuthLoginInstance.init(this, "Z1VZ6dT56XDYLSohRZRN", "Oxdzed_RYw", "빌리지")

        val mOAuthLoginHandler: OAuthLoginHandler = object : OAuthLoginHandler() {
            override fun run(success: Boolean) {
                if (success) {
                    val accessToken = mOAuthLoginInstance.getAccessToken(applicationContext)

                    Thread {
                        val header = "Bearer $accessToken"
                        val requestHeaders: MutableMap<String, String> = HashMap()
                        requestHeaders["Authorization"] = header
                        val apiURL = "https://openapi.naver.com/v1/nid/me"
                        val responseBody: String = NaverLogin().get(apiURL, requestHeaders).toString()
                        Log.d(TAG, responseBody)
                    naverLoginParser(responseBody)
                    }.start()
                    Log.d("result", "happy")
                } else {
                    val errorCode = mOAuthLoginInstance.getLastErrorCode(applicationContext).code
                    val errorDesc = mOAuthLoginInstance.getLastErrorDesc(applicationContext)

                    Toast.makeText(
                        baseContext, "errorCode:" + errorCode + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        mOAuthLoginButton!!.setOAuthLoginHandler(mOAuthLoginHandler)

    }




    fun naverLoginParser(msg: String) {
        Log.d("msg parsing", "$msg")

        val jObject = JSONObject(msg)

        val resultCode = jObject.get("resultcode").toString()
        val message = jObject.get("message").toString()
        val resultJson = jObject.get("response") as JSONObject

        if (resultCode == "00") {
            if (message == "success") {
                val id = resultJson.get("id").toString()
                val email = resultJson.get("email").toString()
                val mobile = resultJson.get("mobile").toString()
                val name = resultJson.get("name").toString()  //이름 타입?

//                val stringRequest = object : StringRequest(
//                    Request.Method.POST,
//                )

                //DB에 회원정보가 없으면 (회원가입의 경우) 닉네임, 지역 입력 fragment 띄우기
                val nickname_intent = Intent(this, NickNameActivity::class.java)
                nickname_intent.putExtra("user_name", name)
                startActivity(nickname_intent)

                //DB에 회원정보가 있으면 액티비티 종료 후 main activity 실행
//                Toast.makeText(applicationContext, "네이버 로그인에 성공했습니다.", Toast.LENGTH_SHORT).show()
//                val intent = Intent(this, MainActivity::class.java)
//                intent.putExtra("user_name", name)
//                startActivity(intent)
//                finish()
            } else {
                Toast.makeText(applicationContext, "네이버 로그인에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(applicationContext,"네이버 로그인에 실패했습니다.",Toast.LENGTH_SHORT).show()
        }
    }


}