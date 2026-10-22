package ru.klimtrofimov.vkeducationstarter.ui.apps

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import ru.klimtrofimov.vkeducationstarter.data.SampleApps

@Composable
fun Showcase(onAppClick: () -> Unit, modifier: Modifier = Modifier) {
    val pages = SampleApps.showcasePages
    val pagerState = rememberPagerState(pageCount = { pages.size })
    Column(modifier) {
        Text(
            text = "Витрина",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Spacer(Modifier.height(8.dp))
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("showcase_pager")
                .semantics {
                    stateDescription = "Страница ${pagerState.currentPage + 1} из ${pages.size}"
                },
        ) { page ->
            Column {
                pages[page].forEach { app ->
                    AppListItem(app = app, onClick = onAppClick)
                }
            }
        }
    }
}
