package com.test.newsapiproject.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.test.newsapiproject.R
import com.test.newsapiproject.ui.common.ImageAsync
import com.test.newsapiproject.ui.common.ImageScale
import com.test.newsapiproject.network.data.Article
import com.test.newsapiproject.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment: Fragment() {

    companion object {
        private val TAG = NewsFragment::class.java.simpleName
    }

    private val viewmodel: NewsViewModel by viewModels()
    private var visitedUrl = mutableStateOf("")

    private val joinWebViewContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode == Activity.RESULT_CANCELED) {
            visitedUrl.value = it.data?.getStringExtra("VISITED_URL") ?: ""
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewmodel.fetchApiData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = colorResource(id = R.color.white)
                    ) {
                        val newsContentState = viewmodel.newItemLiveData.observeAsState(emptyList())

                        NewsContent(
                            visitedUrl = visitedUrl.value,
                            newsContentState = newsContentState
                        )
                    }
                }
            }
        }
    }

    private fun showWebViewPage(url: String) {
        joinWebViewContract.launch(
            Intent(context, WebViewActivity::class.java).apply {
                putExtras(
                    bundleOf(WebViewActivity.GET_URL to url)
                )
            }
        )
    }

    @Composable
    private fun NewsContent(
        visitedUrl: String,
        newsContentState: State<List<Article>>
    ) {
        val colors = remember { mutableStateMapOf<String, Color>() }

        BoxWithConstraints {
            // 가로 사이즈가 600dp 미만이면 1개의 열, 아니면 3개의 열
            val columns = if (maxWidth < 600.dp) 1 else 3

            LazyVerticalGrid(
                columns = GridCells.Fixed(columns),
                modifier = Modifier.fillMaxWidth()
            ) {
                val filteredItems = newsContentState.value.filter {
                    it.url != null && it.imageUrl != null && it.title != null &&
                            it.publishedAt != null && it.description != null
                }

                items(items = filteredItems) { item ->
                    val titleColor = colors[item.url] ?: Color.Black
                    colors[visitedUrl] = Color.Red

                    NewsItem(
                        onClick = {
                            showWebViewPage(it)
                        },
                        url = item.url ?: return@items,
                        imageUrl = item.imageUrl ?: return@items,
                        title = item.title ?: return@items,
                        titleColor = titleColor,
                        publishedAt = item.publishedAt ?: "",
                        description = item.description ?: return@items
                    )
                }
            }
        }
    }


    @Composable
    fun NewsItem(
        onClick: (String) -> Unit,
        url: String,
        imageUrl: String,
        title: String,
        titleColor: Color = Color.Black,
        publishedAt: String,
        description: String
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable {
                    onClick(url)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            ImageAsync(
                url = imageUrl,
                contentDescription = "",
                placeholder = painterResource(id = R.drawable.place_holder),
                error = painterResource(id = R.drawable.place_holder),
                imageScale = ImageScale.Crop,
                modifier = Modifier
                    .width(120.dp)
                    .height(90.dp)
                    .clip(RoundedCornerShape(5.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = titleColor,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Text(
                    text = publishedAt,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        HorizontalDivider()
    }

}