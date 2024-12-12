package com.sargaev.aiedu

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.anychart.AnyChartView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import smile.base.cart.SplitRule
import smile.base.mlp.Layer
import smile.base.mlp.LayerBuilder
import smile.base.mlp.OutputFunction
import smile.classification.DecisionTree
import smile.classification.LogisticRegression
import smile.classification.LogisticRegression.Binomial
import smile.classification.RandomForest
import smile.classification.mlp
import smile.data.formula.Formula

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var topAppBar: MaterialToolbar
    private lateinit var navigationView: NavigationView
    private var currentFragmentTag: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        drawerLayout = findViewById(R.id.drawerLayout)
        topAppBar = findViewById(R.id.topAppBar)
        navigationView = findViewById(R.id.navigationView)
        if (savedInstanceState == null) {
            // Load the default fragment on first creation
            val hellofragment = HelloFragment()
            replaceFragment(hellofragment, HelloFragment::class.java.simpleName)
        } else {
            // Restore the current fragment tag
            currentFragmentTag = savedInstanceState.getString("CURRENT_FRAGMENT_TAG")
        }
        topAppBar.setOnClickListener{
            drawerLayout.open()
        }
        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            val selectedFragment: Fragment = when (menuItem.itemId) {
                R.id.lm_item -> LinearModel()
                R.id.dt_item -> DecisionTreeCode()
                R.id.ai_item -> AiModel()
                else -> return@setNavigationItemSelectedListener false
            }
            replaceFragment(selectedFragment, selectedFragment::class.java.simpleName)
            drawerLayout.close()
            true
        }
        val dataContainer = DataContainer.initialize(this)





//        val anyChartView = findViewById<AnyChartView>(R.id.any_chart_view)
//        val csvReader = CSVReader(this)
//        val (features, labels) = csvReader.readCsvData(R.raw.xor_data)
//        val (trainData, testData) = DataPrepare.trainTestSplit(features, labels, 0.2f)
//        val lr = LogisticRegression.fit(trainData.first, trainData.second)
//        val yPredTrain = lr.predict(trainData.first)
//        val yPredTest = lr.predict(testData.first)
//        MetricsPrint.evaluateModel(trainData.second, yPredTrain, testData.second, yPredTest)
//        val coefficients = (lr as Binomial).coefficients()
//        ChartTool.drawLineChart(anyChartView, features, labels, coefficients)

//        val df = DataPrepare.csv2df(features, labels)
//        val formula = Formula.lhs("Class")
//
//        // Обучаем модель Decision Tree
//        val decisionTree = DecisionTree.fit(
//            formula,
//            df,
//            SplitRule.GINI,
//            20,
//            100000,
//            5
//        )

//        var predictions = decisionTree.predict(df)
//        MetricsPrint.evaluateModel(predictions, labels)
//
//        val randomforest =
//            RandomForest.fit(
//                formula,
//                df,
//                1000,
//                0,
//                SplitRule.GINI,
//                3,
//                100000,
//                10,
//                1.0
//            )
//        predictions = randomforest.predict(df)
//        MetricsPrint.evaluateModel(predictions, labels)
        //val layers = arrayOf(Layer.input(2),
//            Layer.rectifier(10),
//            Layer.mle(1, OutputFunction.SOFTMAX)
//        )
//        val model = mlp(features,
//            labels,
//            layers,
//            100,
//            learningRate: TimeFunction = TimeFunction.linear(0.01, 10000.0, 0.001),
//            momentum: TimeFunction = TimeFunction.constant(0.0),
//            weightDecay: Double = 0.0,
//            rho: Double = 0.0,
//            epsilon: Double = 1E-7
//        )
//        predictions = model.predict(features)
//        MetricsPrint.evaluateModel(labels, predictions)
    }
    private fun replaceFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment, tag)
            .commit()
        currentFragmentTag = tag
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("CURRENT_FRAGMENT_TAG", currentFragmentTag)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentFragmentTag = savedInstanceState.getString("CURRENT_FRAGMENT_TAG")
        currentFragmentTag?.let { tag ->
            val fragment = supportFragmentManager.findFragmentByTag(tag)
            if (fragment != null) {
                replaceFragment(fragment, tag)
            }
        }
    }
}