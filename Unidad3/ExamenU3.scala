//1.- Importar libreria para sesiones spark
import org.apache.spark.sql.SparkSession

//2.- Utilizar lalinea de codigo para reducir los mensajes oreportes de errores
import org.apache.log4j._
Logger.getLogger("org").setLevel(Level.ERROR)

//3.- Crear sesion de spark
val spark = SparkSession.builder().getOrCreate()

//4.- Importar libreria para algoritmo Kmeans
import org.apache.spark.ml.clustering.KMeans

//5.- Cargar el dataset de wholesale customers data
val dataset = spark.read.option("header","true").option("inferSchema","true").csv("Wholesale customers data.csv")

//6.- seleccionar las columnas para el conjunto de entrenamiento
val feature_data = dataset.select($"Fresh", $"Milk", $"Grocery", $"Frozen", $"Detergents_Paper", $"Delicassen")

//7.- Importar vectorasembler y vector
import org.apache.spark.ml.feature.{VectorAssembler,StringIndexer,VectorIndexer,OneHotEncoder}
import org.apache.spark.ml.linalg.Vectors

//8.- Crear un objeto vectorasembler para las columnas recordando que no hay etiquetas
val assembler = new VectorAssembler().setInputCols(Array("Fresh", "Milk", "Grocery", "Frozen", "Detergents_Paper", "Delicassen")).setOutputCol("features")

//9.- Utilizar un objeto assembler para transformar los features 
val training_data = assembler.transform(feature_data)

//10.- crear el modelo kmeans
val kmeans = new KMeans().setK(3).setSeed(1L)
val model = kmeans.fit(training_data)

//11.- evaluar los grupos utilizando WSSSE
val WSSSE = model.computeCost(training_data)
println(s"Within Set Sum of Squared Errors = $WSSSE")

//12.- Mostrar los resultados obtenidos
println("Cluster Centers: ")
model.clusterCenters.foreach(println)
