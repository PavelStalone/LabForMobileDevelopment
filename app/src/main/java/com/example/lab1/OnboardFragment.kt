package com.example.lab1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.compose.content
import com.example.lab1.ui.icon.CloneTrooper
import com.example.lab1.ui.icon.CustomIcons
import com.example.lab1.ui.icon.DarthVader
import com.example.lab1.ui.icon.Empire
import com.example.lab1.ui.icon.Mando
import com.example.lab1.ui.theme.AppTheme

class OnboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = content {
        AppTheme {
            Scaffold { innerPadding ->
                OnboardScreen(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(horizontal = 24.dp),
                    onSignIn = {
                        MainActivity.navigateToFragment(parentFragmentManager, SignInFragment())
                    },
                )
            }
        }
    }
}

@Composable
private fun OnboardScreen(
    modifier: Modifier = Modifier,
    onSignIn: () -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Card(shape = CircleShape) {
            Icon(
                modifier = Modifier
                    .padding(16.dp)
                    .sizeIn(maxWidth = 120.dp, maxHeight = 120.dp)
                    .aspectRatio(1f),
                imageVector = CustomIcons.DarthVader,
                contentDescription = null,
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Добро пожаловать",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "на темную сторону силы",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Card {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = CustomIcons.Empire,
                        contentDescription = null,
                    )
                    Text(
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.End,
                        text = "Просмтаривай базу данных Имерии и будь в курсе всех событий",
                    )
                }
            }
            Card {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Start,
                        text = "Изучай противника и обстановку перед навязыванием боя",
                    )
                    Icon(
                        imageVector = CustomIcons.Mando,
                        contentDescription = null,
                    )
                }
            }
            Card {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = CustomIcons.CloneTrooper,
                        contentDescription = null,
                    )
                    Text(
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.End,
                        text = "Узнавай разыскиваемых приступников и членов сопротивления",
                    )
                }
            }
        }
        Spacer(Modifier.weight(1f))
        Button(
            onClick = onSignIn,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Войти", style = MaterialTheme.typography.labelLarge)
        }
    }
}
