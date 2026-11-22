package ru.klimtrofimov.vkeducationstarter.presentation.apps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.klimtrofimov.vkeducationstarter.R

@Composable
fun RatingsControls(state: AppListUiState, onLoad: (RatingsMode) -> Unit) {
    Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(onClick = { onLoad(RatingsMode.Sequential) }, enabled = state.ratingsLoading == null) {
            Text(stringResource(R.string.ratings_sequential))
        }
        Button(onClick = { onLoad(RatingsMode.Parallel) }, enabled = state.ratingsLoading == null) {
            Text(stringResource(R.string.ratings_parallel))
        }
        if (state.ratingsLoading != null) {
            LinearProgressIndicator(Modifier.fillMaxWidth())
            Text(stringResource(R.string.ratings_loading))
        }
        state.sequentialMillis?.let {
            Text(stringResource(R.string.ratings_time, stringResource(R.string.ratings_sequential), it, state.apps.size))
        }
        state.parallelMillis?.let {
            Text(stringResource(R.string.ratings_time, stringResource(R.string.ratings_parallel), it, state.apps.size))
        }
        if (state.ratingsError) {
            Text(stringResource(R.string.ratings_error), color = MaterialTheme.colorScheme.error)
        }
    }
}
