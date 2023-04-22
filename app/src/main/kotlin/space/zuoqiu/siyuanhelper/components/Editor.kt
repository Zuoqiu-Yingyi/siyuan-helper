package space.zuoqiu.siyuanhelper.components


import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.meetup.twain.MarkdownEditor

@Composable
fun Editor(init: String = "", modifier: Modifier = Modifier) {
    val markdown = rememberSaveable(
        stateSaver = TextFieldValue.Saver
    ) {
        mutableStateOf(TextFieldValue(init))
    }
    Card(
        shape = RectangleShape,
    ) {
        MarkdownEditor(
            value = markdown.value,
            onValueChange = { value -> markdown.value = value.copy(text = value.text) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun EditorPreview() {
    Editor("**粗体** *斜体* `行内代码` ~~删除线~~")
}
