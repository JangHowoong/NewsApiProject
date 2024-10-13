package com.test.newsapiproject.ui

import android.content.Intent
import android.os.Bundle
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebViewActivity: AppCompatActivity() {

    companion object {
        private val TAG = WebViewActivity::class.java.simpleName

        const val GET_URL = "GET_URL"
    }

    private var getUrl: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getUrl = intent.getStringExtra(GET_URL) ?: ""

        onBackPressedDispatcher.addCallback {
            val intent = Intent()
            intent.putExtra("VISITED_URL", getUrl)
            setResult(RESULT_CANCELED, intent)
            finish()
        }

        setContent {
            MaterialTheme {
                WebViewContent(getUrl)
            }
        }
    }



    @Composable
    fun WebViewContent(url: String) {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    webViewClient = object : WebViewClient() {
                        override fun onReceivedError(
                            view: WebView?,
                            request: WebResourceRequest?,
                            error: WebResourceError?
                        ) {
                            super.onReceivedError(view, request, error)

                        }

                        override fun onReceivedHttpError(
                            view: WebView?,
                            request: WebResourceRequest?,
                            errorResponse: WebResourceResponse?
                        ) {
                            super.onReceivedHttpError(view, request, errorResponse)

                        }
                    }
                }
            },
            update = { webView ->
                webView.loadUrl(url)
            },
        )
    }
}