package com.example.lab1

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.lab1.ui.theme.AppTheme

class HomeActivity : LogActivity() {

    override val TAG: String = "HomeActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            AppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreen(
                        modifier = Modifier.padding(innerPadding),
                        userName = intent.getStringExtra("name")
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    userName: String? = null,
) {
    var query by remember { mutableStateOf(userName ?: "") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        stickyHeader {
            SearchField(
                modifier = Modifier.fillMaxWidth(),
                query = query,
                onQueryChange = { query = it },
                onClear = { query = "" },
            )
        }
        repeat(4) {
            item {
                SpecialCard(
                    image = R.drawable.boba_fett,
                    label = "Boba Fett",
                    description = "Опасный наемник, служит тому у кого больше денег",
                )
            }
        }
    }
}

@Composable
private fun SpecialCard(
    @DrawableRes image: Int,
    label: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp)
    ) {
        Image(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Crop,
            painter = painterResource(image),
            contentDescription = null,
        )
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
private fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        label = { Text("Поиск по имени") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
        ),
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = onClear) {
                    Icon(Icons.Outlined.Clear, contentDescription = null)
                }
            }
        },
        modifier = modifier
    )
}
