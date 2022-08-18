package com.oganbelema.hellocomposeiii.util

fun calculateTotalTip(totalBill: Double, tipPercentage: Int): Double {
    return if (totalBill.toString().isNotEmpty() && totalBill > 1)
        (totalBill * tipPercentage) / 100
    else
        0.0
}

fun calculateTotalPerPerson(totalBill: Double, tipPercentage: Int, splitNumber: Int): Double {

    val bill = calculateTotalTip(totalBill = totalBill, tipPercentage = tipPercentage) + totalBill

    return (bill / splitNumber)
}