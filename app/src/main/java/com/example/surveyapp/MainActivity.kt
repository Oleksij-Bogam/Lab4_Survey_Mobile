package com.example.surveyapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.surveyapp.ui.theme.SurveyAppTheme
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SurveyAppTheme {
                SurveyScreen()
            }
        }
    }
}

@Composable
fun SurveyScreen() {
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var studyAnswer by remember { mutableStateOf("") }
    var appsAnswer by remember { mutableStateOf("") }
    var comment by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Форма опитування",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Будь ласка, дайте відповіді на запитання нижче.",
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            SurveyTextField(
                label = "1. Ваше ім’я",
                value = name,
                onValueChange = { name = it }
            )

            SurveyTextField(
                label = "2. Ваш вік",
                value = age,
                onValueChange = { age = it },
                keyboardType = KeyboardType.Number
            )

            SurveyTextField(
                label = "3. Чи подобається вам навчання в університеті?",
                value = studyAnswer,
                onValueChange = { studyAnswer = it },
                minLines = 2
            )

            SurveyTextField(
                label = "4. Якими застосунками ви користуєтеся найчастіше?",
                value = appsAnswer,
                onValueChange = { appsAnswer = it },
                minLines = 2
            )

            SurveyTextField(
                label = "5. Ваші побажання або коментарі",
                value = comment,
                onValueChange = { comment = it },
                minLines = 3
            )

            Button(
                onClick = {
                    if (
                        name.isBlank() ||
                        age.isBlank() ||
                        studyAnswer.isBlank() ||
                        appsAnswer.isBlank()
                    ) {
                        Toast.makeText(
                            context,
                            "Заповніть основні поля форми",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val currentDate = SimpleDateFormat(
                            "dd.MM.yyyy HH:mm:ss",
                            Locale.getDefault()
                        ).format(Date())

                        val surveyText = """
                            Дата заповнення: $currentDate

                            1. Ім’я:
                            $name

                            2. Вік:
                            $age

                            3. Чи подобається вам навчання в університеті?
                            $studyAnswer

                            4. Якими застосунками користуєтеся найчастіше?
                            $appsAnswer

                            5. Побажання або коментарі:
                            $comment

                            ----------------------------------------

                        """.trimIndent()

                        try {
                            val file = File(context.filesDir, "survey_answers.txt")
                            file.appendText(surveyText, Charsets.UTF_8)

                            resultText = "Відповіді збережено у файл:\n${file.absolutePath}"

                            Toast.makeText(
                                context,
                                "Відповіді успішно збережено",
                                Toast.LENGTH_SHORT
                            ).show()

                            name = ""
                            age = ""
                            studyAnswer = ""
                            appsAnswer = ""
                            comment = ""

                        } catch (e: Exception) {
                            resultText = "Помилка збереження: ${e.message}"

                            Toast.makeText(
                                context,
                                "Помилка збереження файлу",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Text(text = "Зберегти відповіді")
            }

            if (resultText.isNotEmpty()) {
                Text(
                    text = resultText,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}

@Composable
fun SurveyTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    minLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(text = label)
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        minLines = minLines,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 14.dp)
    )
}