package com.oganbelema.hellocomposeiii.util

fun calculateTotalTip(totalBill: Double, tipPercentage: Int): Double {
    return if (totalBill.toString().isNotEmpty() && totalBill > 1)
        (totalBill * tipPercentage) / 100
    else
        0.0
}