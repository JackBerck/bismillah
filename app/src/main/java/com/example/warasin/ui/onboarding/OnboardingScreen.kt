package com.example.warasin.ui.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.warasin.ui.component.ButtonWithoutIcon
import com.example.warasin.ui.theme.WarasInTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onFinishClick: () -> Unit
) {
    val pages = listOf(
        OnboardingPage.FirstPages,
        OnboardingPage.SecondPages,
        OnboardingPage.ThirdPages
    )
    val pagerState = rememberPagerState(pageCount = { pages.size })

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(48.dp)
        ) {
            this@Column.AnimatedVisibility(
                visible = pagerState.currentPage < pagerState.pageCount - 1,
                modifier = Modifier.align(Alignment.CenterEnd)) {
                TextButton(
                    onClick = onFinishClick,
                ) {
                    Text("Skip", color = MaterialTheme.colorScheme.primary)
                }
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f) // Pager akan mengisi ruang yang tersedia
        ) { pageIndex ->
            PagerContent(onboardingPage = pages[pageIndex])
        }
        
        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 20.dp)
        )

        BottomButtons(
            pagerState = pagerState,
            onGetStartedClick = onFinishClick,
            modifier = Modifier.padding(horizontal = 40.dp, vertical = 20.dp)
        )
    }
}

@Composable
fun PagerContent(onboardingPage: OnboardingPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = onboardingPage.image),
            contentDescription = onboardingPage.title,
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = onboardingPage.title,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = onboardingPage.description,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BottomButtons(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    onGetStartedClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (pagerState.currentPage < pagerState.pageCount - 1) {
            ButtonWithoutIcon(
                text = "Lanjut",
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            ButtonWithoutIcon(
                text = "Mulai Sekarang",
                onClick = onGetStartedClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalPagerIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    activeColor: Color = MaterialTheme.colorScheme.primary,
    inactiveColor: Color = activeColor.copy(alpha = 0.5f),
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
    ) {
        repeat(pagerState.pageCount) { iteration ->
            val color = if (pagerState.currentPage == iteration) activeColor else inactiveColor
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(12.dp)
            )
        }
    }
}

@Preview(name = "Halaman 1", showBackground = true)
@Composable
fun OnboardingPage1Preview() {
    WarasInTheme {
        PagerContent(onboardingPage = OnboardingPage.FirstPages)
    }
}

@Preview(name = "Halaman 2", showBackground = true)
@Composable
fun OnboardingPage2Preview() {
    WarasInTheme {
        PagerContent(onboardingPage = OnboardingPage.SecondPages)
    }
}

@Preview(name = "Halaman 3", showBackground = true)
@Composable
fun OnboardingPage3Preview() {
    WarasInTheme {
        PagerContent(onboardingPage = OnboardingPage.ThirdPages)
    }
}

// (Opsional) Anda juga bisa tetap memiliki preview untuk layar interaktifnya
@Preview(name = "Layar Interaktif Penuh", showBackground = true)
@Composable
fun OnboardingScreenFullPreview() {
    WarasInTheme {
        OnboardingScreen(onFinishClick = {})
    }
}
