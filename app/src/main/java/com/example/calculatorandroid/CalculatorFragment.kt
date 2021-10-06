package com.example.calculatorandroid

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.util.*

private const val TEXT_KEY = "textKey"

class CalculatorFragment : Fragment(), View.OnClickListener {

    private lateinit var viewCalculation: TextView
    private lateinit var zero: Button
    private lateinit var one: Button
    private lateinit var two: Button
    private lateinit var three: Button
    private lateinit var four: Button
    private lateinit var five: Button
    private lateinit var six: Button
    private lateinit var seven: Button
    private lateinit var eight: Button
    private lateinit var nine: Button
    private lateinit var add: Button
    private lateinit var sub: Button
    private lateinit var mult: Button
    private lateinit var div: Button
    private lateinit var point: Button
    private lateinit var clear: Button
    private var anew: Boolean = true;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        savedInstanceState?.let { data ->
//            val passedData = data.getString(TEXT_KEY)
//            viewCalculation.text = passedData
//        }

        return inflater.inflate(R.layout.fragment_calculator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.run {

            viewCalculation = findViewById(R.id.text_calculation)
            zero = findViewById(R.id.button_0)
            one = findViewById(R.id.button_1)
            two = findViewById(R.id.button_2)
            three = findViewById(R.id.button_3)
            four = findViewById(R.id.button_4)
            five = findViewById(R.id.button_5)
            six = findViewById(R.id.button_6)
            seven = findViewById(R.id.button_7)
            eight = findViewById(R.id.button_8)
            nine = findViewById(R.id.button_9)
            add = findViewById(R.id.button_add)
            sub = findViewById(R.id.button_sub)
            mult = findViewById(R.id.button_mul)
            div = findViewById(R.id.button_div)
            point = findViewById(R.id.button_point)
            clear = findViewById(R.id.button_clear)

            savedInstanceState?.let { data ->
                val passedData = data.getString(TEXT_KEY)
                viewCalculation.text = passedData
            }

            zero.setOnClickListener(this@CalculatorFragment)
            one.setOnClickListener(this@CalculatorFragment)
            two.setOnClickListener(this@CalculatorFragment)
            three.setOnClickListener(this@CalculatorFragment)
            four.setOnClickListener(this@CalculatorFragment)
            five.setOnClickListener(this@CalculatorFragment)
            six.setOnClickListener(this@CalculatorFragment)
            seven.setOnClickListener(this@CalculatorFragment)
            eight.setOnClickListener(this@CalculatorFragment)
            nine.setOnClickListener(this@CalculatorFragment)
            add.setOnClickListener(this@CalculatorFragment)
            sub.setOnClickListener(this@CalculatorFragment)
            mult.setOnClickListener(this@CalculatorFragment)
            div.setOnClickListener(this@CalculatorFragment)
            point.setOnClickListener(this@CalculatorFragment)
            clear.setOnClickListener(this@CalculatorFragment)

        }
    }

   override fun onConfigurationChanged(newConfig: Configuration) {
       super.onConfigurationChanged(newConfig)
       if(newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE){
           Toast.makeText(context, "landscape", Toast.LENGTH_SHORT).show()
       }else if (newConfig.orientation === Configuration.ORIENTATION_PORTRAIT) {
           Toast.makeText(context, "portrait", Toast.LENGTH_SHORT).show()
       }
//        super.onConfigurationChanged(newConfig)
   }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(TEXT_KEY, viewCalculation.text?.toString())
        outState.get(TEXT_KEY)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.button_0 -> {
                addDigit("0")
            }
            R.id.button_1 -> {
                addDigit("1")
            }
            R.id.button_2 -> {
                addDigit("2")
            }
            R.id.button_3 -> {
                addDigit("3")
            }
            R.id.button_4 -> {
                addDigit("4")
            }
            R.id.button_5 -> {
                addDigit("5")
            }
            R.id.button_6 -> {
                addDigit("6")
            }
            R.id.button_7 -> {
                addDigit("7")
            }
            R.id.button_8 -> {
                addDigit("8")
            }
            R.id.button_9 -> {
                addDigit("9")
            }
            R.id.button_add -> {
                addSymbol('+')
            }
            R.id.button_sub -> {
                addSymbol('-')
            }
            R.id.button_mul -> {
                addSymbol('x')
            }
            R.id.button_div -> {
                addSymbol('/')
            }
            R.id.button_point -> {
                viewCalculation.append(".")
            }
            R.id.button_clear -> {
                val result = calculate(viewCalculation.getText())
                viewCalculation.setText(result)
                anew = true;
                //empty

//                val a = Toast.makeText(context, viewCalculation.getText(), Toast.LENGTH_SHORT).show();
                //     viewCalculation.setText(null)
            }
        }


    }

    private fun addDigit(digit: String) {
        if(anew){
            viewCalculation.setText(null)
            anew = false;
        }
        viewCalculation.append(digit)
    }

    private fun addSymbol(symbol: Char) {
        val lastChar = viewCalculation?.getText().toString().last()
        if (lastChar == '+' || lastChar == '/' || lastChar == 'x' || lastChar == '-') {
            val replaced: String = viewCalculation.getText().toString()?.replace(lastChar, symbol)
            viewCalculation.text = replaced
        } else {
            viewCalculation.append(symbol.toString())
        }
    }

    private fun calculate(text: CharSequence?): String{

        val listOfDigitsAndOperators = makeListOfDigitsAndOperators(text)
        if(listOfDigitsAndOperators.isEmpty()) return ""

        val multOrDivResult = timesDivisionCalculate(listOfDigitsAndOperators)
        if (multOrDivResult.isEmpty()) return ""
        else if(multOrDivResult.first()=="NaN") return "NaN"

        val result = addOrSubCalculation(multOrDivResult)
        return result
    }

    private fun timesDivisionCalculate(listOfDigitsAndOperators: MutableList<Any>): MutableList<Any> {
        var list = listOfDigitsAndOperators
        while (list.contains('x') || list.contains('/')) {
            list = multOrDivCalculation(list)
        }
        return list

    }

    private fun makeListOfDigitsAndOperators(text: CharSequence?): MutableList<Any> {
        val list = mutableListOf<Any>() //val?
        var digit = ""

        if (text != null) {
            for (char in text) {
                if (char.isDigit() || char == '.') {
                    digit += char
                } else {
                    list.add(digit.toFloat())
                    digit = ""
                    list.add(char)
                }
            }
            if(digit!="") {
                list.add(digit.toFloat())
            }
        }
        return list
    }

    private fun addOrSubCalculation(multOrDivResult: MutableList<Any>): String {
        var result = multOrDivResult[0] as Float

        for (i in multOrDivResult.indices) {
            if (multOrDivResult[i] is Char && i != multOrDivResult.lastIndex) {
                val operator = multOrDivResult[i]
                val nextDigit = multOrDivResult[i + 1] as Float
                when (operator) {
                    '+' -> {
                        result += nextDigit
                    }
                    '-' -> {
                        result -= nextDigit
                    }
                }
            }
        }
        return result.toString()
    }



    private fun multOrDivCalculation(list: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()
        var index: Int = list.size
        //while (list.contains('x') || list.contains('/')) {
            //var index = newList.size
            for (i in list.indices) {
                if (list[i] is Char && i != list.lastIndex && i<index) {
                    val operator = list[i]
                    val prevDigit = list[i - 1] as Float
                    val nextDigit = list[i + 1] as Float
                    if(nextDigit== 0.toFloat()){
                        newList.add("NaN")
                        return newList
                    }
                    when (operator) {
                        'x' -> {
                            newList.add(prevDigit * nextDigit)
                            index = i + 1
                        }
                        '/' -> {
                            newList.add(prevDigit / nextDigit)
                            index = i + 1
                        }
                        else -> {
                            newList.add(prevDigit)
                            newList.add(operator)
                        }
                    }
                }
                if (i > index) {
                    newList.add(list[i])
                }
            }
        //}
        return newList
            // viewCalculation.append(result)
    }


}
