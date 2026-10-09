package ru.klimtrofimov.vkeducationstarter

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.klimtrofimov.vkeducationstarter.ui.theme.VkEducationStarterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VkEducationStarterTheme {
                var input by rememberSaveable { mutableStateOf("") }
                var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = input,
                            onValueChange = {
                                input = it
                                errorMessage = null
                            },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Введите текст или номер телефона") },
                            isError = errorMessage != null,
                            supportingText = {
                                errorMessage?.let { Text(it) }
                            }
                        )

                        Button(
                            onClick = {
                                val text = input.trim()
                                if (text.isEmpty()) {
                                    errorMessage = "Введите текст"
                                } else {
                                    startActivity(
                                        Intent(this@MainActivity, SecondActivity::class.java)
                                            .putExtra(EXTRA_TEXT, text)
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Открыть вторую Activity")
                        }

                        Button(
                            onClick = {
                                val phoneNumber = input.trim()
                                if (!Patterns.PHONE.matcher(phoneNumber).matches()) {
                                    errorMessage = "Введите корректный номер телефона"
                                } else {
                                    val dialIntent = Intent(
                                        Intent.ACTION_DIAL,
                                        Uri.fromParts("tel", phoneNumber, null)
                                    )
                                    try {
                                        startActivity(dialIntent)
                                    } catch (_: ActivityNotFoundException) {
                                        errorMessage = "Приложение для звонков не найдено"
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Позвонить другу")
                        }

                        Button(
                            onClick = {
                                val text = input.trim()
                                if (text.isEmpty()) {
                                    errorMessage = "Введите текст"
                                } else {
                                    val sendIntent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_TEXT, text)
                                    }
                                    startActivity(
                                        Intent.createChooser(sendIntent, "Поделиться через…")
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Поделиться текстом")
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val EXTRA_TEXT = "extra_text"
    }
}
