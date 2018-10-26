//Se importan las librerias para la utilizacion de diferentes funciones
import org.apache.spark.sql.SparkSession
import spark.implicits
import org.apache.spark.sql.Column
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.feature.StringIndexer
import org.apache.spark.ml.feature.VectorIndexer
import org.apache.spark.ml.classification.MultilayerPerceptronClassifier
import org.apache.spark.ml.feature.IndexToString
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
// se crea una sesion spark
val spar = SparkSession.builder().getOrCreate()
// se carga el archivo Iris.csv 
val df = spark.read.option("header", "true").option("inferSchema","true")csv("Iris.csv")
// se crea el dataframe con sus respectivos nombres de cada columna
val newNames = Seq("SepalLength", "SepalWidth", "PetalLength", "PetalWidth", "Label")
val dfRenamed = df.toDF(newNames: *)
// se imprime el esquema
dfRenamed.printSchema
//se junta los datos
val assembler = new VectorAssembler().setInputCols(Array("SepalLength", "SepalWidth", "PetalLength", "PetalWidth")).setOutputCol("features")
//se transforman los datos
val features = assembler.transform(dfRenamed)
features.show(5)

// Indexar los labels, añadir metadata a la columna label.
// que esten en todo el dataset para incluir todos los labels en el index.
val labelIndexer = new StringIndexer().setInputCol("Label").setOutputCol("indexedLabel").fit(features)
println(s"Found labels: ${labelIndexer.labels.mkString("[", ", ", "]")}")

// Automaticamente  identifica categoricamente los features, y los indexa.
// añade  maxCategories para que las features cont > 4 distintos valores  sean tratadoscomo continuo.
val featureIndexer = new VectorIndexer().setInputCol("features").setOutputCol("indexedFeatures").setMaxCategories(4).fit(features)

//Variables de entrenamiento , test y los porcentajes al azar
val splits = features.randomSplit(Array(0.6, 0.4))
val trainingData = splits(0)
val testData = splits(1)

// la arquitecturas de las capas para la red neuronal:
// la capa de entrada con tamaño 4 (features), dos intermediarios tamaño 5 y 4
//  y de salida tañamo 3 (por las clases)
val layers = Array[Int](4, 5, 5, 3)

// crea el entrenador y se pones los parametros
val trainer = new MultilayerPerceptronClassifier().setLayers(layers).setLabelCol("indexedLabel").setFeaturesCol("indexedFeatures")
.setBlockSize(128).setSeed(System.currentTimeMillis).setMaxIter(200)

//  Convierte los labels indexados devuelta a los labels originales
val labelConverter = new IndexToString().setInputCol("prediction").setOutputCol("predictedLabel").setLabels(labelIndexer.labels)

// Encadena los indexados y la  MultilayerPerceptronClassifier en una  Pipeline.
//se usa para que se procese el flujo de trabajo, aprende la prediccion del modelo
//usando los features de los vectores o labels
val pipeline = new Pipeline().setStages(Array(labelIndexer, featureIndexer, trainer, labelConverter))

// entrena el modelo tmabien corre los indexados.
val model = pipeline.fit(trainingData)

//para hacer las predicciones
val predictions = model.transform(testData)
predictions.show(5)

// Seleciona (prediccion, original, label) y hace el test de error
val evaluator = new MulticlassClassificationEvaluator().setLabelCol("indexedLabel").setPredictionCol("prediction").setMetricName("accuracy")
val accuracy = evaluator.evaluate(predictions)
println("Test Error = " + (1.0 - accuracy))
