package com.example.sareevastra.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BannerCarousel() {

    val banners = listOf(

        "https://images.unsplash.com/photo-1529139574466-a303027c1d8b",

        "https://images.unsplash.com/photo-1496747611176-843222e1e57c",

        "https://images.unsplash.com/photo-1483985988355-763728e1935b"
    )

    val pagerState = rememberPagerState(
        pageCount = {
            banners.size
        }
    )

    LaunchedEffect(Unit) {

        while (true) {

            delay(3000)

            val nextPage =
                (pagerState.currentPage + 1) %
                        banners.size

            pagerState.animateScrollToPage(
                nextPage
            )
        }
    }

    HorizontalPager(

        state = pagerState,

        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
    ) { page ->

        AsyncImage(

            model = banners[page],

            contentDescription = null,

            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .clip(
                    RoundedCornerShape(20.dp)
                ),

            contentScale = ContentScale.Crop
        )
    }
}