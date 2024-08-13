package com.example.petapp.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.petapp.R
import com.example.petapp.domain.model.Animal
import com.example.petapp.ui.components.ErrorScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToAnimalDetail: (animalId: Int) -> Unit,

    ) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(title = { Text(text = stringResource(id = R.string.home_title)) })
        }
    ) { contentPadding ->
        val state by viewModel.homeUiState.collectAsState()

        when (state) {
            is HomeUiState.Loading -> LoadingScreen()
            is HomeUiState.Loaded -> LoadedScreen(
                Modifier.padding(contentPadding),
                animalList = (state as HomeUiState.Loaded).animalList,
                onNavigateToAnimalDetail = onNavigateToAnimalDetail
            )

            is HomeUiState.Error -> {
                ErrorScreen(
                    modifier = Modifier
                        .padding(contentPadding)
                        .fillMaxSize(),
                    onRetry = { viewModel.getAnimals() },
                    error = stringResource(id = R.string.error_text)
                )
            }
        }
    }

}

@Composable
fun LoadedScreen(
    modifier: Modifier = Modifier,
    animalList: List<Animal>,
    onNavigateToAnimalDetail: (animalId: Int) -> Unit,
) {

    if (animalList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = R.string.empty_screen),
                style = MaterialTheme.typography.bodyMedium
            )
        }

    } else {
        LazyColumn(
            modifier = modifier.fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items = animalList, key = { it.id }) {
                AnimalItem(
                    animal = it,
                    onNavigateToAnimalDetail = onNavigateToAnimalDetail
                )
            }
        }
    }
}

@Composable
fun AnimalItem(
    modifier: Modifier = Modifier,
    animal: Animal,
    onNavigateToAnimalDetail: (animal: Int) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onNavigateToAnimalDetail(animal.id) },
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        AsyncImage(
            model = animal.photo.firstOrNull()?.small,
            contentDescription = "Image of the animal",
            modifier = Modifier
                .size(88.dp)
                .clip(MaterialTheme.shapes.small),
            placeholder = painterResource(id = R.drawable.pet_placeholder),
            error = painterResource(id = R.drawable.pet_placeholder),
            contentScale = ContentScale.Crop,
        )

        Column {
            Text(
                text = animal.name,
                style = MaterialTheme.typography.titleMedium,
            )

            Text(
                text = animal.breeds.primary,
                style = MaterialTheme.typography.bodyMedium,
            )

            Text(
                text = animal.gender,
                style = MaterialTheme.typography.bodyMedium,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                animal.tags.forEach { tag ->
                    if (tag.length < 15) {
                        Tag(text = tag)
                    }
                }
            }
        }
    }
}

@Composable
fun Tag(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.small
            )
            .padding(4.dp)
    )
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(MaterialTheme.colorScheme.onPrimary, MaterialTheme.colorScheme.primary)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.secondaryContainer
        )
    }

}