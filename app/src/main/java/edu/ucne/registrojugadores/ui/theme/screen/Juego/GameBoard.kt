package edu.ucne.registrojugadores.ui.theme.screen.Juego

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun GameBoard(
    board: Array<Player?>,
    onCellClicked: (Int) -> Unit,
    modifier: Modifier = Modifier,
    colorX: Color = Color.Red,
    colorO: Color = Color.Blue
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {

        BoardBase(modifier = Modifier.fillMaxSize())

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            for (row in 0 until 3) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (col in 0 until 3) {
                        val index = row * 3 + col
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize()
                                .clickable { onCellClicked(index) },
                            contentAlignment = Alignment.Center
                        ) {
                            when (board[index]) {
                                Player.X -> Cross(color = colorX)
                                Player.O -> Circle(color = colorO)
                                null -> {}
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Cross(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val padding = size.minDimension * 0.2f
        val stroke = size.minDimension * 0.08f

        drawLine(
            color = color,
            start = Offset(padding, padding),
            end = Offset(size.width - padding, size.height - padding),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width - padding, padding),
            end = Offset(padding, size.height - padding),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun Circle(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val padding = size.minDimension * 0.2f
        val stroke = size.minDimension * 0.08f
        drawCircle(
            color = color,
            radius = (size.minDimension / 2) - padding,
            style = Stroke(width = stroke)
        )
    }
}

@Composable
fun BoardBase(modifier: Modifier = Modifier, sizeDp: Dp = 300.dp) {
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp),
    ) {
        val thirdWidth = size.width / 3
        val thirdHeight = size.height / 3
        val lineWidth = size.width * 0.02f // más proporcional al tamaño

        // Líneas verticales
        drawLine(
            color = Color.Gray,
            strokeWidth = lineWidth,
            cap = StrokeCap.Round,
            start = Offset(x = thirdWidth, y = 0f),
            end = Offset(x = thirdWidth, y = size.height)
        )
        drawLine(
            color = Color.Gray,
            strokeWidth = lineWidth,
            cap = StrokeCap.Round,
            start = Offset(x = 2 * thirdWidth, y = 0f),
            end = Offset(x = 2 * thirdWidth, y = size.height)
        )

        // Líneas horizontales
        drawLine(
            color = Color.Gray,
            strokeWidth = lineWidth,
            cap = StrokeCap.Round,
            start = Offset(x = 0f, y = thirdHeight),
            end = Offset(x = size.width, y = thirdHeight)
        )
        drawLine(
            color = Color.Gray,
            strokeWidth = lineWidth,
            cap = StrokeCap.Round,
            start = Offset(x = 0f, y = 2 * thirdHeight),
            end = Offset(x = size.width, y = 2 * thirdHeight)
        )
    }
}
