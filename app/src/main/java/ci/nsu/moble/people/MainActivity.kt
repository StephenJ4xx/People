package ci.nsu.moble.people

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ci.nsu.moble.people.ui.theme.PeopleTheme
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
val availableColors = listOf(
    ColorInfo("Красный", "Red", Color.Red),
    ColorInfo("Зеленый", "Green", Color.Green),
    ColorInfo("Желтый", "Yellow", Color.Yellow),
    ColorInfo("Синий", "Blue", Color.Blue),
    ColorInfo("Белый", "White", Color.White),
    ColorInfo("Черный", "Black", Color.Black)
)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PeopleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ColorChecker(
                        modifier = Modifier.padding(innerPadding)
                    )

                }
            }
        }
    }
}

// Модель данных для цветов
data class ColorInfo(
    val russianName: String,
    val englishName: String,
    val color: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorChecker(modifier: Modifier = Modifier) {
    // Список доступных цветов с русскими и английскими названиями


    var inputText by remember { mutableStateOf("") }
    var buttonColor by remember { mutableStateOf(Color.Blue) }
    var showColorTable by remember { mutableStateOf(false) } // Показывать ли таблицу цветов
    var hasUserInteracted by remember { mutableStateOf(false) } // Был ли уже ввод

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Поле для ввода текста
        TextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Введите цвет (на английском)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Кнопка для проверки
        Button(
            onClick = {
                hasUserInteracted = true

                // Проверяем, есть ли текст в списке
                if (inputText.isNotBlank()) {
                    val normalizedInput = inputText.trim()
                    val foundColor = availableColors.find {
                        it.englishName.equals(normalizedInput, ignoreCase = true)
                    }

                    if (foundColor != null) {
                        Log.d("ColorChecker", "Цвет '$normalizedInput' найден в списке!")
                        // Меняем цвет кнопки на введенный цвет
                        buttonColor = foundColor.color
                        // Показываем таблицу цветов
                        showColorTable = true
                    } else {
                        Log.d("ColorChecker", "Цвет '$normalizedInput' отсутствует в списке.")
                        // Возвращаем стандартный цвет
                        buttonColor = Color.Blue
                    }
                } else {
                    Log.d("ColorChecker", "Пожалуйста, введите цвет для проверки.")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
        ) {
            Text(
                "Применить цвет",
                color = when (buttonColor) {
                    Color.Black, Color.Blue -> Color.White
                    else -> Color.Black
                }
            )
        }
        ColorTable(availableColors)

        if (hasUserInteracted) {
        }
    }
}

@Composable
fun ColorTable(colors: List<ColorInfo>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Text(
            "Доступные цвета:",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Заголовок таблицы
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Цвет", fontWeight = FontWeight.Bold)
            Text("Название", fontWeight = FontWeight.Bold)
            Text("Введите", fontWeight = FontWeight.Bold)
        }

        // Элементы таблицы
        colors.forEach { colorInfo ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Показать цвет
                Spacer(
                    modifier = Modifier
                        .size(24.dp)
                        .background(colorInfo.color, RoundedCornerShape(4.dp))
                        .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                )

                // Русское название
                Text(colorInfo.russianName)

                // Английское название для ввода
                Text(colorInfo.englishName, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ColorCheckerPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ColorChecker()
    }
}