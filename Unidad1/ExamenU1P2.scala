// se importa la libreria de sql para sesiones spark
import org.apache.spark.sql.SparkSession
// 1.- comienza una simple sesion spark 
val ss = SparkSession.builder().getOrCreate()
// 2.- Cargue el archivo Netflix Stock CSV, haga que spark infiera los tipos de datos
val data = spark.read.option("header", "true").option("inferSchema","true")csv("Netflix_2011_2016.csv")
data.show() // se muestran los datos
// 3.- ¿ cuales son los nombres de las columnas?
data.columns
// 4.- ¿como es el esquema?
data.printSchema()
// 5.- Imprime las primeras 5 columnas
data.select($"Date",$"Open",$"High",$"Low",$"Close").show()
// 6.- Usa describe() para aprender sobre el DataFrame
data.describe().show()
/* 7.- crea un nuevo dataframe con una columna llamada HV Ratio
que es la relacion entre el precio alto frente al volumen de acciones negociadas
por un dia*/
val dataM = data.withColumn("HV Ratio",data("High")+data("Close"))
// 8.- ¿que dia tuvo Peak High en Price?
data.orderBy($"High".desc).show(1)
// 9.- ¿cual es el significado de la columna Cerrar(Close)?
data.select(mean("Close")).show()
// 10.- ¿cual es el maximo y minimo de la columna volumen?
data.select(max("Volume")).show()
data.select(min("Volume")).show()

// 11.- con sintaxis Scala/Spark $ conteste lo siguiente: 

  // a ¿cuantos dias fue el cierre inferior a $600?
  data.filter($"Close"<600).count()
  // b ¿que porcentaje del tiempo fue el alto mayor de $500?
  (data.filter($"High" > 500).count() * 1.0/ data.count())*100
  // c ¿cual es la correlacion de Pearson entre alto y volumen?
  data.select(corr("High","Volume")).show()
  // d ¿cual es el maximo alto por año?
  val datay = data.withColumn("Year",year(data("Date")))
  val dataymax = datay.select($"Year",$"High").groupBy("Year").max()
  val res = dataymax.select($"Year",$"max(High)")
  res.orderBy("Year").show()
  // e.- ¿cual es el promedio de cierre para cada mes del calendario?
  val datam = data.withColumn("Month",month(data("Date")))
  val dataAVG = datam.select($"Month",$"Close").groupBy("Month").mean()
  dataAVG.select($"Month",$"avg(Close)").orderBy("Month").show()
