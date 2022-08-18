package com.oganbelema.hellocomposeiii

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.oganbelema.hellocomposeiii.components.InputField
import com.oganbelema.hellocomposeiii.ui.theme.HelloComposeIIITheme
import com.oganbelema.hellocomposeiii.widgets.RoundIconButton

@ExperimentalComposeUiApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelloComposeIIITheme {
                MyApp {
                    MainContent()
                }
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    // A surface container using the 'background' color from the theme
    Surface(
       color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {
        content()
    }
}

@Composable
fun TopHeader(totalPerPerson: Double = 0.0) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(all = 16.dp)
            .clip(shape = RoundedCornerShape(corner = CornerSize(12.dp))),
        color = Color(0xFF64B5F6)
    ) {
        val total = "%.2f".format(totalPerPerson)

        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Total Per Person", style = MaterialTheme.typography.h5)
            Text(
                text = "€$total", style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }

}

@Preview
@ExperimentalComposeUiApi
@Composable
fun MainContent() {

    Column(modifier = Modifier.padding(all = 12.dp)) {
        BillForm() { billAmount ->
            Log.d("Callback", billAmount)
        }
    }

}

@ExperimentalComposeUiApi
@Composable
fun BillForm(modifier: Modifier = Modifier,
onValChange: (String) -> Unit = {}){

    val totalBillState = remember {
        mutableStateOf("")
    }

    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    val sliderPositionState = remember {
        mutableStateOf(0f)
    }

    val tipAmountState = remember {
        mutableStateOf(0.0)
    }

    val tipPercentage = (sliderPositionState.value * 100).toInt()

    val splitRange = IntRange(start = 1, endInclusive = 10)

    TopHeader()

    Surface(
        modifier = modifier
            .padding(2.dp)
            .fillMaxWidth(),
        shape = CircleShape.copy(all = CornerSize(8.dp)),
        border = BorderStroke(width = 1.dp, color = Color.LightGray)
    ) {
        Column(modifier = modifier.padding(6.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start) {
            InputField(valueState = totalBillState,
                labelId = "Enter Bill",
                enabled = true,
                isSingleLine = true,
                onAction = KeyboardActions {
                    if (!validState) return@KeyboardActions

                    onValChange(totalBillState.value.trim())

                    keyboardController?.hide()
                })

            if (validState) {
                val splitNumber = remember {
                    mutableStateOf(1)
                }

                Row(modifier = modifier.padding(3.dp),
                horizontalArrangement = Arrangement.Start) {
                    Text(text = "Split",
                        modifier = modifier.align(
                            alignment = Alignment.CenterVertically
                        ))
                    Spacer(modifier = modifier.width(120.dp))
                    Row(modifier = modifier.padding(horizontal = 3.dp),
                    horizontalArrangement = Arrangement.End) {
                        RoundIconButton(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Remove Icon",
                            onClick = {
                                if (splitNumber.value > splitRange.first){
                                    splitNumber.value -= 1
                                }
                            })
                        
                        Text(text = splitNumber.value.toString(), modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 9.dp, end = 9.dp))

                        RoundIconButton(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Icon",
                            onClick = {
                                if (splitNumber.value < splitRange.last) {
                                    splitNumber.value += 1
                                }
                            })
                    }
                }

                //Tip Row
                Row(modifier = Modifier.padding(horizontal = 3.dp, vertical = 12.dp)) {
                    Text(text = "Tip",
                    modifier = Modifier.align(alignment = Alignment.CenterVertically))
                    
                    Spacer(modifier = Modifier.width(200.dp))
                    
                    Text(text = "€${tipAmountState.value}",
                        modifier = Modifier.align(alignment = Alignment.CenterVertically))
                }
                
                Column(verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "$tipPercentage%")
                    
                    Spacer(modifier = Modifier.height(14.dp))
                    
                    //Slider
                    Slider(modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                        value = sliderPositionState.value , onValueChange = { newValue ->
                            sliderPositionState.value = newValue
                            tipAmountState.value = calculateTotalTip(totalBill = totalBillState.value.toDouble(),
                                tipPercentage = tipPercentage)
                    },
                    steps = 5)
                }
            } else {
                Box() {}
            }
        }
    }
}

fun calculateTotalTip(totalBill: Double, tipPercentage: Int): Double {
    return if (totalBill.toString().isNotEmpty() && totalBill > 1)
        (totalBill * tipPercentage) / 100
    else
        0.0
}


@Preview(showBackground = true)
@ExperimentalComposeUiApi
@Composable
fun DefaultPreview() {
    HelloComposeIIITheme {
        MyApp {
            MainContent()
        }
    }
}